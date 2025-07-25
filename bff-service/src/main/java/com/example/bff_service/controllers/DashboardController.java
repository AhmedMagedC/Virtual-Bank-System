package com.example.bff_service.controllers;

import com.example.bff_service.dtos.DashboardResponse;
import com.example.bff_service.enums.MsgType;
import com.example.bff_service.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/bff")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard/{userId}")
    public Mono<ResponseEntity<DashboardResponse>> getDashboard(@PathVariable UUID userId) {
        Map<String, Object> reqLog = new HashMap<>();
        reqLog.put("userId", userId);

        this.dashboardService.sendLog(reqLog, MsgType.REQUEST, LocalDateTime.now());

        Mono<DashboardResponse> dashboardResponseMono = dashboardService.getDashboard(userId);

        this.dashboardService.sendLog(dashboardResponseMono, MsgType.RESPONSE, LocalDateTime.now());

        return dashboardResponseMono
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

