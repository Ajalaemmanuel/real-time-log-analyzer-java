package com.emmanuel.loganalyzer;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * A simple class to simulate a service writing to a log file.
 */
public class LogSimulator {
    public static void main(String[] args) throws InterruptedException, IOException {
        String logFilePath = "app.log";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        System.out.println("Log Simulator started. Writing to " + logFilePath);
        int userId = 100;

        while (true) {
            LocalDateTime now = LocalDateTime.now();
            String logLine;

            // Generate a mix of INFO and ERROR logs
            if (Math.random() > 0.2) { // 80% chance of INFO
                logLine = String.format("[%s] [INFO] User logged in successfully. userId=%d\n", now.format(formatter), userId++);
            } else { // 20% chance of ERROR
                logLine = String.format("[%s] [ERROR] Failed to connect to database. connection_timeout=5000ms\n", now.format(formatter));
            }

            // Append the log line to the file
            Files.write(Paths.get(logFilePath), logLine.getBytes(), StandardOpenOption.APPEND);
            System.out.print("Wrote: " + logLine);

            // Wait for 2 seconds before writing the next log
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
