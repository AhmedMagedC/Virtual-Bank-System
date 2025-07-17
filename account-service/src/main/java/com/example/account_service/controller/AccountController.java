package com.example.account_service.controller;

import com.example.account_service.dtos.AccountDetails;
import com.example.account_service.models.Account;
import com.example.account_service.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService=accountService;
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountDetails> getAccountDetails(@PathVariable("accountId") UUID accountId){
        Account account = this.accountService.getDetails(accountId);
        AccountDetails accountDetails = new AccountDetails(accountId, account.getAccountNumber(),
                account.getBalance(), account.getAccountType(),
                account.getStatus());

        return ResponseEntity.ok(accountDetails);
    }

}
