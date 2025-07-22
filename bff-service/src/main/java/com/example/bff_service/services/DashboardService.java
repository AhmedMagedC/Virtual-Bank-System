package com.example.bff_service.services;

import com.example.bff_service.dtos.*;
import com.example.bff_service.exceptions.DownstreamServiceException;
import com.example.bff_service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class DashboardService {

    private final WebClient userServiceClient;
    private final WebClient accountServiceClient;
    private final WebClient transactionServiceClient;

    public DashboardService(@Autowired WebClient.Builder webClientBuilder) {
        this.userServiceClient = webClientBuilder.baseUrl("http://localhost:8082").build();
        this.accountServiceClient = webClientBuilder.baseUrl("http://localhost:8081").build();
        this.transactionServiceClient = webClientBuilder.baseUrl("http://localhost:8083").build();
    }

    public Mono<DashboardResponse> getDashboard(UUID userId) {
        Mono<UserProfile> userProfileMono = getUserProfile(userId);

        Flux<AccountDetails> accountDetailsFlux = getUserAccounts(userId);

        Mono<List<AccountWithTransactions>> accountsWithTransactionsMono = getAccountsWithTransactions(accountDetailsFlux);

        return Mono.zip(userProfileMono, accountsWithTransactionsMono)
                .map(tuple -> {
                    UserProfile user = tuple.getT1();
                    List<AccountWithTransactions> accounts = tuple.getT2();

                    DashboardResponse response = new DashboardResponse();
                    response.setUserId(user.getUserId());
                    response.setUsername(user.getUsername());
                    response.setEmail(user.getEmail());
                    response.setFirstName(user.getFirstName());
                    response.setLastName(user.getLastName());
                    response.setAccounts(accounts);

                    return response;
                })
                .onErrorMap(ex -> {
                    if (ex instanceof ResourceNotFoundException) return ex; // Let controller handle 404
                    return new DownstreamServiceException("Failed to retrieve dashboard data due to an issue with downstream services.", ex);
                });
    }

    private Mono<UserProfile> getUserProfile(UUID userId) {
        return userServiceClient
                .get()
                .uri("/users/{userId}/profile", userId)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, client -> Mono.error(new ResourceNotFoundException("User not found")))
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, client -> Mono.error(new DownstreamServiceException("User Service failed")))
                .bodyToMono(UserProfile.class);
    }

    private Flux<AccountDetails> getUserAccounts(UUID userId) {
        return accountServiceClient
                .get()
                .uri("/users/{userId}/accounts", userId)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> Mono.empty())
                .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, client -> Mono.error(new DownstreamServiceException("Account Service failed")))
                .bodyToFlux(AccountDetails.class)
                .onErrorResume(ex -> {
                    if (ex instanceof WebClientResponseException.NotFound) {
                        return Flux.empty();
                    }
                    return Flux.error(ex);
                });
    }

    private Mono<List<AccountWithTransactions>> getAccountsWithTransactions(Flux<AccountDetails> accountDetailsFlux) {
        return accountDetailsFlux
                .flatMap(account -> transactionServiceClient
                        .get()
                        .uri("/accounts/{accountId}/transactions", account.accountId())
                        .retrieve()
                        .onStatus(HttpStatus.NOT_FOUND::equals,
                                response -> Mono.empty())
                        .onStatus(HttpStatus.INTERNAL_SERVER_ERROR::equals, client -> Mono.error(new DownstreamServiceException("Transaction Service failed")))
                        .bodyToFlux(TransactionDetails.class)
                        .filter(txn -> txn.getTransactionId() != null)
                        .collectList()
                        .onErrorResume(WebClientResponseException.NotFound.class, ex -> Mono.just(List.of()))
                        .map(transactions -> {
                            AccountWithTransactions awt = new AccountWithTransactions();
                            awt.setAccountId(account.accountId());
                            awt.setAccountNumber(account.accountNumber());
                            awt.setAccountType(account.accountType());
                            awt.setAccountStatus(account.accountStatus());
                            awt.setBalance(account.balance());
                            awt.setTransactions(transactions);
                            return awt;
                        }))
                .collectList();
    }
}

