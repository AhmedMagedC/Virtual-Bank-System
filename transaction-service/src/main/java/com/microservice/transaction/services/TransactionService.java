package com.microservice.transaction.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.transaction.constant.AppConst;
import com.microservice.transaction.dao.TransactionDao;
import com.microservice.transaction.dtos.*;
import com.microservice.transaction.enums.MsgType;
import com.microservice.transaction.enums.TransactionStatus;
import com.microservice.transaction.exceptions.BadRequestException;
import com.microservice.transaction.exceptions.NotFoundException;
import com.microservice.transaction.models.Transactions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionService {

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    public void sendLog(Object msg , MsgType type, LocalDateTime date){
        try{
            String jsonLog = objectMapper.writeValueAsString(msg);
            Logs newLog = new Logs(jsonLog,type,date);
            kafkaTemplate.send(AppConst.LOGGING, newLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // or log the error
        }
    }



    @Transactional
    public TransferResponse initiateTransaction(TransferRequestInitiation transferReq){
        Transactions newTransaction = new Transactions(transferReq.getFromAccountId(),
                transferReq.getToAccountId(), transferReq.getAmount(),
                transferReq.getDescription());

        AccountDetail fromAccount;
        //check if valid account ids
        try {
            fromAccount = restTemplate.getForObject(
                    "http://localhost:8081/accounts/" + newTransaction.getFromAccountId(),
                    AccountDetail.class);
            AccountDetail toAccount = restTemplate.getForObject(
                    "http://localhost:8081/accounts/" + newTransaction.getToAccountId(),
                    AccountDetail.class);
        }catch (HttpClientErrorException ex){
            throw new BadRequestException("Invalid 'from' or 'to' account ID.");
        }

        //check valid balance
        if (fromAccount.getBalance().compareTo(newTransaction.getAmount()) < 0){
            throw new BadRequestException("Insufficient funds.");
        }


        Transactions savedTransaction = transactionDao.saveAndFlush(newTransaction);

        return new TransferResponse(savedTransaction.getId(),
                savedTransaction.getStatus(),savedTransaction.getTimestamp());
    }

    public TransferResponse executeTransaction(TransferRequestExecution transferReq) {
        Transactions transaction = transactionDao.findById(transferReq.getTransactionId())
                .orElseThrow(() -> new BadRequestException("Transaction not found"));

        if (transaction.getStatus() != TransactionStatus.INITIATED){
            throw new BadRequestException("Transaction already Executed.");
        }

        TransactionInternalRequest transactionInternalRequest= new TransactionInternalRequest(
                transaction.getFromAccountId(), transaction.getToAccountId(),
                transaction.getAmount());

        try {
            restTemplate.put("http://localhost:8081/accounts/transfer",
                    transactionInternalRequest);

        }catch (HttpClientErrorException ex){
            transaction.setStatus(TransactionStatus.FAILED);
            transactionDao.saveAndFlush(transaction);
            throw new BadRequestException("Invalid 'from' or 'to' account ID.");
        }

        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setTimestamp(LocalDateTime.now());
        transactionDao.saveAndFlush(transaction);

        return new TransferResponse(transaction.getId(),transaction.getStatus(),
                transaction.getTimestamp());

    }

    public List<TransactionDetail> getAccountTransactions(UUID accountId) {
        //check for valid account id
        try {
            restTemplate.getForObject("http://localhost:8081/accounts/" + accountId,
                    AccountDetail.class);
        }catch (HttpClientErrorException ex){
            throw new BadRequestException("Invalid account ID.");
        }

        List<TransactionDetail> transactionsList = new ArrayList<>();


        // Outgoing (sent) transactions - amount should be negative
        List<Transactions> fromTransactions = transactionDao.findByFromAccountIdAndStatusIn(
                accountId, List.of(TransactionStatus.SUCCESS ));
        fromTransactions.forEach(transaction -> {
            transactionsList.add(new TransactionDetail(
                    transaction.getId(),
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getAmount().negate(),
                    transaction.getDescription(),
                    transaction.getTimestamp()
            ));
        });

        // Incoming (received) transactions - amount remains positive
        List<Transactions> toTransactions = transactionDao.findByToAccountIdAndStatusIn(accountId,
                List.of(TransactionStatus.SUCCESS ));
        toTransactions.forEach(transaction -> {
            transactionsList.add(new TransactionDetail(
                    transaction.getId(),
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getAmount(),
                    transaction.getDescription(),
                    transaction.getTimestamp()
            ));
        });

        if(transactionsList.isEmpty()){
            throw new NotFoundException("No transactions found for account ID "+ accountId);

        }else {
            transactionsList.sort(Comparator.comparing(TransactionDetail::getTimestamp));
            return transactionsList;
        }
    }
}