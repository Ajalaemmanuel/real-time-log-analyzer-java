package com.emmanuel.loganalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
@EnableScheduling
public class LoganalyzerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoganalyzerApplication.class, args);

        String logFilePath = "app.log";
        try {
            File logFile = new File(logFilePath);
            if (!logFile.exists()) {
                logFile.createNewFile();
                System.out.println("Created new log file: " + logFilePath);
            }

            System.out.println("Log Analyzer started. Tailing file: " + logFilePath);
            System.out.println("Run the LogSimulator to see new log entries.");

        } catch (IOException e) {
            System.err.println("An error occurred during file operations.");
            e.printStackTrace();
        }
    }
}
