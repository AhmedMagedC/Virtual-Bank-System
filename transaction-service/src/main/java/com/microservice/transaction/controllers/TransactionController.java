package com.microservice.transaction.controllers;

import com.microservice.transaction.dtos.TransactionDetail;
import com.microservice.transaction.dtos.TransferRequestExecution;
import com.microservice.transaction.dtos.TransferRequestInitiation;
import com.microservice.transaction.dtos.TransferResponse;
import com.microservice.transaction.enums.MsgType;
import com.microservice.transaction.exceptions.BadRequestException;
import com.microservice.transaction.models.Transactions;
import com.microservice.transaction.services.LoggingService;
import com.microservice.transaction.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LoggingService loggingService;

    @RequestMapping(method = RequestMethod.POST, value = "/transactions/transfer/initiation")
    public TransferResponse initiateTransaction(
            @Valid @RequestBody TransferRequestInitiation transferReq,
            @RequestHeader(value = "APP-NAME", required = false) String appName) {
        //testing WSO2 header
        System.out.println("app name header is "+appName);

        loggingService.sendLog(transferReq, MsgType.REQUEST, LocalDateTime.now());

        TransferResponse res = transactionService.initiateTransaction(transferReq);

        loggingService.sendLog(res, MsgType.RESPONSE, LocalDateTime.now());
        return res;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/transactions/transfer/execution")
    public TransferResponse executeTransaction(
            @Valid @RequestBody TransferRequestExecution transferReq) {

        loggingService.sendLog(transferReq, MsgType.REQUEST, LocalDateTime.now());

        TransferResponse res = transactionService.executeTransaction(transferReq);

        loggingService.sendLog(res, MsgType.RESPONSE, LocalDateTime.now());
        return res;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/accounts/{accountId}/transactions")
    public List<TransactionDetail> getAccountTransactions(@PathVariable("accountId")
                                                          UUID accountId){
        Map<String,UUID> logId =Map.of("accountId", accountId);
        loggingService.sendLog(logId, MsgType.REQUEST, LocalDateTime.now());

        List<TransactionDetail> resList = transactionService.getAccountTransactions(accountId);

        loggingService.sendLog(resList, MsgType.RESPONSE, LocalDateTime.now());
        return resList;
    }


}
