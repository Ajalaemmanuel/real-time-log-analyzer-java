package com.emmanuel.loganalyzer;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * A service that periodically checks analytics and triggers alerts
 * if certain thresholds are breached.
 */
@Service
public class AlertingService {

    private final AnalyticService analyticsService;

    // An alert will be triggered if the error rate is > 50% in the last minute, probably going to change this later
    private static final double ERROR_RATE_THRESHOLD = 0.50;

    //chekc if alert is active currently
    private boolean alertActive = false;

    public AlertingService(AnalyticService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * This method is executed by Spring's scheduler at a fixed rate.
     * We'll run it every 10 seconds.
     */
    @Scheduled(fixedRate = 10000) // Run every 10,000 milliseconds (10 seconds)
    public void checkForAlerts() {
        double currentErrorRate = analyticsService.getErrorRateLastMinute();
        int totalErrors = analyticsService.getTotalErrorCount();


        if (currentErrorRate > ERROR_RATE_THRESHOLD) {
            if (!alertActive) {
                System.out.printf(
                        "--- ALERT TRIGGERED! ---%n" +
                                "High error rate detected: %.2f%% is above the %.2f%% threshold.%n" +
                                "Total errors since start: %d%n" +
                                "------------------------%n",
                        currentErrorRate * 100, ERROR_RATE_THRESHOLD * 100, totalErrors
                );
                alertActive = true; // Set the state to active
            }
        } else {
            if (alertActive) {
                System.out.printf(
                        "--- ALERT RESOLVED ---%n" +
                                "Error rate fell to %.2f%%, which is below the threshold.%n" +
                                "System is now stable.%n" +
                                "----------------------%n",
                        currentErrorRate * 100
                );
                alertActive = false;
            }
        }


    }
    public boolean isAlertActive() {
        return alertActive;
    }
}