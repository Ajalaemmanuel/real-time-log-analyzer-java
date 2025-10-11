package com.emmanuel.loganalyzer;

/**
 * A record to hold the status data that will be returned by the API.
 * This will be automatically converted to JSON by Spring Boot.
 */
public record StatusResponse(
        double errorRateLastMinute,
        int totalErrors,
        String status
) {}

