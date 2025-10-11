package com.emmanuel.loganalyzer;


import java.time.LocalDateTime;

/**
 * A record to hold the structured data from a single log line.
 * Records are immutable (unchangable) data carriers, perfect for this use case.
 */
public record LogEntry(
        LocalDateTime timestamp,
        String level,
        String message
) {}
