package com.example.account_service.services;

import com.example.account_service.exceptions.AccountNotFoundException;
import com.example.account_service.models.Account;
import com.example.account_service.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository=accountRepository;
    }

    public Account getDetails(UUID id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID: " + id + " " + "not found!"));
    }
}
