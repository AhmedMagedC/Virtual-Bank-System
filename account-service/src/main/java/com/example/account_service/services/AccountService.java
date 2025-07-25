package com.example.account_service.services;

import com.example.account_service.constants.AppConst;
import com.example.account_service.dtos.AccountCreationRequest;
import com.example.account_service.dtos.Logs;
import com.example.account_service.dtos.TransactionDetails;
import com.example.account_service.dtos.TransferExecutionRequest;
import com.example.account_service.enums.AccountStatus;
import com.example.account_service.enums.MsgType;
import com.example.account_service.exceptions.BadRequest;
import com.example.account_service.exceptions.NotFoundException;
import com.example.account_service.models.Account;
import com.example.account_service.repositories.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String,Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AccountService(AccountRepository accountRepository,
                          WebClient.Builder webClientBuilder,
                          KafkaTemplate<String,Object> kafkaTemplate,
                          ObjectMapper objectMapper) {
        this.accountRepository = accountRepository;
        this.webClientBuilder = webClientBuilder;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public Account getDetails(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account with ID: " + id + " " + "not found!"));
    }

    public Account createAccount(AccountCreationRequest acc) {
        validateUserExists(acc.userId());

        if (acc.initialBalance().signum() < 0) {
            throw new BadRequest("InitialBalance has to be positive");
        }

        Account newAcc = new Account();
        newAcc.setAccountNumber(this.accountRepository.getNextAccountNumberValue());
        newAcc.setAccountType(acc.accountType());
        newAcc.setBalance(acc.initialBalance());
        newAcc.setUserId(acc.userId());

        return this.accountRepository.save(newAcc);
    }

    public void transferMoney(TransferExecutionRequest tr) {
        boolean fromAccountExist = this.accountRepository.findById(tr.fromAccountId()).isPresent();
        boolean toAccountExist = this.accountRepository.findById(tr.toAccountId()).isPresent();

        if (!fromAccountExist || !toAccountExist) {
            throw new NotFoundException("Invalid Account!");
        }

        Account fromAccount = this.accountRepository.findById(tr.fromAccountId()).get();
        Account toAccount = this.accountRepository.findById(tr.toAccountId()).get();

        if (fromAccount.getBalance().compareTo(tr.amount()) < 0) {
            throw new BadRequest("Invalid transfer request!");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(tr.amount()));
        toAccount.setBalance(toAccount.getBalance().add(tr.amount()));

        fromAccount.setUpdatedAt(LocalDateTime.now());
        toAccount.setUpdatedAt(LocalDateTime.now());

        this.accountRepository.save(fromAccount);
        this.accountRepository.save(toAccount);

    }

    public List<Account> getListOfAccounts(UUID userId) {
        return this.accountRepository.findByUserId(userId);
    }

    private void validateUserExists(UUID userId) {
        try {
            webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/users/{userId}/profile", userId)
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

    public void inactivateIdleAccounts() {
        List<Account> activeAccounts = accountRepository.findByStatus(AccountStatus.ACTIVE);

        for (Account account : activeAccounts) {

            List<TransactionDetails> transactions = fetchTransactions(account.getId());

            // Get latest timestamp
            Optional<LocalDateTime> latestTimestampOpt = transactions.stream()
                    .map(TransactionDetails::timestamp)
                    .max(Comparator.naturalOrder());

            if (latestTimestampOpt.isPresent()) {
                LocalDateTime latest = latestTimestampOpt.get();
                if (Duration.between(latest, LocalDateTime.now()).toHours() >= 24) {
                    account.setStatus(AccountStatus.INACTIVE);
                    accountRepository.save(account);
                }
            } else {
                // No transactions at all → also mark as INACTIVE
                account.setStatus(AccountStatus.INACTIVE);
                accountRepository.save(account);
            }


        }

    }

    private List<TransactionDetails> fetchTransactions(UUID accountId) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/accounts/{accountId}/transactions", accountId)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response -> {
                    // Return an empty list on 404 error
                    return Mono.error(new NotFoundException(""));
                })
                .bodyToFlux(TransactionDetails.class)
                .collectList()
                .onErrorResume(NotFoundException.class, e -> Mono.just(Collections.emptyList()))
                .block();
    }

    public void sendLog(Object msg , MsgType type, LocalDateTime date){
        try{
            String jsonLog = objectMapper.writeValueAsString(msg);
            Logs newLog = new Logs(jsonLog,type,date);
            kafkaTemplate.send(AppConst.LOGGING, newLog);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // or log the error
        }
    }

}
