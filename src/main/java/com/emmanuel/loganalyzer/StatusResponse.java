package com.emmanuel.loganalyzer;

public record StatusResponse(
        double errorRateLastMinute,
        int totalErrors,
        String status
) {}

