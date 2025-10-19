package com.emmanuel.loganalyzer;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class LogParser {

    // Regex to capture timestamp, log level, and message
    // Pattern: [timestamp] [LEVEL] message
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^\\[([\\d\\-]+T[\\d:]+)]\\s\\[(\\w+)]\\s(.*)$");

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Parses a single log line string.
     * @param logLine The raw log line.
     * @return An Optional containing the LogEntry if parsing is successful, otherwise an empty Optional.
     */
    public Optional<LogEntry> parse(String logLine) {
        if (logLine == null || logLine.trim().isEmpty()) {
            return Optional.empty();
        }

        Matcher matcher = LOG_PATTERN.matcher(logLine);

        if (matcher.find()) {
            try {
                LocalDateTime timestamp = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
                String level = matcher.group(2);
                String message = matcher.group(3);
                return Optional.of(new LogEntry(timestamp, level, message));
            } catch (Exception e) {
                // This could happen if the date format is wrong
                System.err.println("Failed to parse log line: " + logLine);
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}