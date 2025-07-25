package com.example.account_service.controller;

import com.example.account_service.dtos.AccountCreationRequest;
import com.example.account_service.dtos.AccountCreationResponse;
import com.example.account_service.dtos.AccountDetails;
import com.example.account_service.dtos.TransferExecutionRequest;
import com.example.account_service.enums.MsgType;
import com.example.account_service.exceptions.NotFoundException;
import com.example.account_service.models.Account;
import com.example.account_service.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService=accountService;
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountDetails> getAccountDetails(@PathVariable("accountId") UUID accountId){
        Map<String, Object> reqLog = new HashMap<>();
        reqLog.put("accountId", accountId);

        this.accountService.sendLog(reqLog, MsgType.REQUEST, LocalDateTime.now());

        Account account = this.accountService.getDetails(accountId);
        AccountDetails accountDetails = new AccountDetails(accountId, account.getAccountNumber(),
                account.getBalance(), account.getAccountType(),
                account.getStatus());

        this.accountService.sendLog(accountDetails, MsgType.RESPONSE, LocalDateTime.now());
        return ResponseEntity.ok(accountDetails);
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<AccountDetails>> getUserAccounts(@PathVariable("userId") UUID userId){
        Map<String, Object> reqLog = new HashMap<>();
        reqLog.put("userId", userId);

        this.accountService.sendLog(reqLog, MsgType.REQUEST, LocalDateTime.now());

        List<Account> accounts = accountService.getListOfAccounts(userId);

        List<AccountDetails> details = accounts.stream()
                .map(account -> new AccountDetails(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getAccountType(),
                        account.getStatus()
                ))
                .toList();

        if(details.isEmpty()){
            throw new NotFoundException("No Accounts Found for userId: " + userId);
        }

        this.accountService.sendLog(details, MsgType.RESPONSE, LocalDateTime.now());

        return ResponseEntity.ok(details);
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountCreationResponse> createAccount(@RequestBody AccountCreationRequest acc) {
        this.accountService.sendLog(acc, MsgType.REQUEST, LocalDateTime.now());

        Account account = this.accountService.createAccount(acc);

        AccountCreationResponse accountCreationResponse = new AccountCreationResponse(account.getId(),
                account.getAccountNumber(),
                "Account created successfully.");

        this.accountService.sendLog(accountCreationResponse, MsgType.RESPONSE, LocalDateTime.now());

        return ResponseEntity.ok(accountCreationResponse);
    }

    @PutMapping("/accounts/transfer")
    public ResponseEntity<Map<String, String>> transferMoney(@RequestBody TransferExecutionRequest tr){
        this.accountService.sendLog(tr, MsgType.REQUEST, LocalDateTime.now());

        this.accountService.transferMoney(tr);

        Map<String, String> response = Map.of("message", "Transfer successful!");

        this.accountService.sendLog(response, MsgType.RESPONSE, LocalDateTime.now());
        return ResponseEntity.ok(response);
    }


}
