package com.emmanuel.loganalyzer;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A service to perform in-memory analysis of log entries.
 * This class is thread-safe.
 */
@Service
public class AnalyticService {

    // A thread-safe counter for total errors encountered.
    private final AtomicInteger totalErrorCount = new AtomicInteger(0);

    // A thread-safe deque to store log entries from the last 60 seconds.
    private final Deque<LogEntry> recentLogEntries = new ConcurrentLinkedDeque<>();

    private static final int TIME_WINDOW_SECONDS = 60;

    /**
     * Processes a new log entry, updating the relevant metrics.
     * @param logEntry The log entry to process.
     */
    public void processLogEntry(LogEntry logEntry) {
        // Add the new entry to our time-window deque
        recentLogEntries.addLast(logEntry);

        // If it's an error, increment the total error count
        if ("ERROR".equals(logEntry.level())) {
            totalErrorCount.incrementAndGet();
        }

        // Remove old entries that are outside our time window
        pruneOldEntries();

        // For immediate feedback, let's print the current error rate.
        // We will build a formal alerter for this later.
        System.out.printf("Current error rate (last %d s): %.2f%%\n",
                TIME_WINDOW_SECONDS, getErrorRateLastMinute() * 100);
    }

    /**
     * Calculates the error rate over the last defined time window.
     * @return The error rate as a double (e.g., 0.05 for 5%).
     */
    public double getErrorRateLastMinute() {
        pruneOldEntries(); // Ensure the data is fresh before calculating

        if (recentLogEntries.isEmpty()) {
            return 0.0;
        }

        long errorCountInWindow = recentLogEntries.stream()
                .filter(entry -> "ERROR".equals(entry.level()))
                .count();

        return (double) errorCountInWindow / recentLogEntries.size();
    }

    /**
     * Gets the total number of ERROR logs processed since the application started.
     * @return The total error count.
     */
    public int getTotalErrorCount() {
        return totalErrorCount.get();
    }

    /**
     * Helper method to remove entries from the front of the deque
     * that are older than our time window.
     */
    private void pruneOldEntries() {
        LocalDateTime windowStartTime = LocalDateTime.now().minusSeconds(TIME_WINDOW_SECONDS);

        // peekFirst() is non-blocking and returns the head of the queue without removing it.
        while (!recentLogEntries.isEmpty() && recentLogEntries.peekFirst().timestamp().isBefore(windowStartTime)) {
            // pollFirst() removes the head of the queue.
            recentLogEntries.pollFirst();
        }
    }
}
