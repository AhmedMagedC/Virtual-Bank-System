package com.microservice.transaction.controllers;

import com.microservice.transaction.dtos.TransactionDetail;
import com.microservice.transaction.dtos.TransferRequestExecution;
import com.microservice.transaction.dtos.TransferRequestInitiation;
import com.microservice.transaction.dtos.TransferResponse;
import com.microservice.transaction.exceptions.BadRequestException;
import com.microservice.transaction.models.Transactions;
import com.microservice.transaction.services.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.POST, value = "/transactions/transfer/initiation")
    public TransferResponse initiateTransaction(@Valid @RequestBody TransferRequestInitiation transferReq) {
            return transactionService.initiateTransaction(transferReq);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/transactions/transfer/execution")
    public TransferResponse executeTransaction(@Valid @RequestBody TransferRequestExecution transferReq) {
        return transactionService.executeTransaction(transferReq);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/accounts/{accountId}/transactions")
    public List<TransactionDetail> getAccountTransactions(@PathVariable("accountId")
                                                          UUID accountId){
//        try {
//            return transactionService.getAccountTransactions(accountId);
//        }catch (RuntimeException ex)
//        {
//            ex.fillInStackTrace();
//            System.out.println(ex.getMessage());
//        }
        return transactionService.getAccountTransactions(accountId);
    }


}
