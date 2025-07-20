package com.example.account_service.jobs;

import com.example.account_service.services.AccountService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AccountInactivityJob {

    private final AccountService accountService;

    public AccountInactivityJob(AccountService accountService) {
        this.accountService = accountService;
    }

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void inactivateStaleAccounts() {
        accountService.inactivateIdleAccounts();
    }

}
