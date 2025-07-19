package com.example.account_service.controller;

import com.example.account_service.dtos.AccountCreationRequest;
import com.example.account_service.dtos.AccountCreationResponse;
import com.example.account_service.dtos.AccountDetails;
import com.example.account_service.dtos.TransferExecutionRequest;
import com.example.account_service.exceptions.NotFoundException;
import com.example.account_service.models.Account;
import com.example.account_service.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Account account = this.accountService.getDetails(accountId);
        AccountDetails accountDetails = new AccountDetails(accountId, account.getAccountNumber(),
                account.getBalance(), account.getAccountType(),
                account.getStatus());

        return ResponseEntity.ok(accountDetails);
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<AccountDetails>> getUserAccounts(@PathVariable("userId") UUID userId){
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

        return ResponseEntity.ok(details);
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountCreationResponse> createAccount(@RequestBody AccountCreationRequest acc) {
        Account account = this.accountService.createAccount(acc);

        AccountCreationResponse accountCreationResponse = new AccountCreationResponse(account.getId(),
                account.getAccountNumber(),
                "Account created successfully.");

        return ResponseEntity.ok(accountCreationResponse);
    }

    @PutMapping("/accounts/transfer")
    public ResponseEntity<Map<String, String>> transferMoney(@RequestBody TransferExecutionRequest tr){
        this.accountService.transferMoney(tr);

        Map<String, String> response = Map.of("message", "Transfer successful!");
        return ResponseEntity.ok(response);
    }


}
