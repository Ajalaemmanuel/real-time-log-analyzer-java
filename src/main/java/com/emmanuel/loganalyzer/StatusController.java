package com.emmanuel.loganalyzer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller to expose the application's status metrics.
 */
@RestController
@RequestMapping("/api") // All endpoints in this class will start with /api
public class StatusController {

    private final AnalyticService analyticsService;
    private final AlertingService alertingService;

    // Spring injects the required services
    public StatusController(AnalyticService analyticsService, AlertingService alertingService) {
        this.analyticsService = analyticsService;
        this.alertingService = alertingService;
    }

    /**
     * Handles GET requests to /api/status.
     * @return a StatusResponse object with the latest metrics.
     */
    @GetMapping("/status")
    public StatusResponse getStatus() {
        double errorRate = analyticsService.getErrorRateLastMinute();
        int totalErrors = analyticsService.getTotalErrorCount();
        String status = alertingService.isAlertActive() ? "ALERT" : "OK";

        return new StatusResponse(errorRate, totalErrors, status);
    }
}