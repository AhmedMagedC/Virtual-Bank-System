package com.example.account_service.services;

import com.example.account_service.dtos.AccountCreationRequest;
import com.example.account_service.dtos.TransferExecutionRequest;
import com.example.account_service.exceptions.BadRequest;
import com.example.account_service.exceptions.NotFoundException;
import com.example.account_service.models.Account;
import com.example.account_service.repositories.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final WebClient.Builder webClientBuilder;

    public AccountService(AccountRepository accountRepository, WebClient.Builder webClientBuilder) {
        this.accountRepository=accountRepository;
        this.webClientBuilder=webClientBuilder;
    }

    public Account getDetails(UUID id){
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account with ID: " + id + " " + "not found!"));
    }

    public Account createAccount(AccountCreationRequest acc){
        validateUserExists(acc.userId());

        if(acc.initialBalance().signum() < 0){
            throw new BadRequest("InitialBalance has to be positive");
        }

        Account newAcc = new Account();
        newAcc.setAccountNumber(this.accountRepository.getNextAccountNumberValue());
        newAcc.setAccountType(acc.accountType());
        newAcc.setBalance(acc.initialBalance());
        newAcc.setUserId(acc.userId());

        return this.accountRepository.save(newAcc);
    }

    public void transferMoney(TransferExecutionRequest tr){
        boolean fromAccountExist = this.accountRepository.findById(tr.fromAccountId()).isPresent();
        boolean toAccountExist = this.accountRepository.findById(tr.toAccountId()).isPresent();

        if(!fromAccountExist || !toAccountExist) {
            throw new NotFoundException("Invalid Account!");
        }

        Account fromAccount =  this.accountRepository.findById(tr.fromAccountId()).get();
        Account toAccount =  this.accountRepository.findById(tr.toAccountId()).get();

        if(fromAccount.getBalance().compareTo(tr.amount()) < 0) {
            throw new BadRequest("Invalid transfer request!");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(tr.amount()));
        toAccount.setBalance(toAccount.getBalance().add(tr.amount()));

        fromAccount.setUpdatedAt(LocalDateTime.now());
        toAccount.setUpdatedAt(LocalDateTime.now());

        this.accountRepository.save(fromAccount);
        this.accountRepository.save(toAccount);

    }

    public List<Account> getListOfAccounts(UUID userId){
        return this.accountRepository.findByUserId(userId);
    }

    private void validateUserExists(UUID userId) {
        try {
            webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/users/{userId}/profile", userId)
                    .retrieve()
                    .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                            Mono.error(new NotFoundException("User with ID " + userId + " not found."))
                    )
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("User-service error: " + e.getMessage(), e);
        } catch (WebClientRequestException e) {
            throw new RuntimeException("Failed to reach user-service: " + e.getMessage(), e);
        }
    }


}
