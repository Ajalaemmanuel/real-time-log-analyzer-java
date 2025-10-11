package com.emmanuel.loganalyzer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.io.RandomAccessFile;
import java.nio.file.*;

/**
 * This component tails a log file in a background thread.
 * It uses the LogParser to process new lines.
 */

@Component
public class logFileTailer implements CommandLineRunner {

    private final String filePath = "app.log";
    private final LogParser logParser;
    private final AnalyticService analyticsService;
    private long lastKnownPosition = 0;


    public logFileTailer(LogParser logParser, AnalyticService analyticsService) {
        this.logParser = logParser;
        this.analyticsService = analyticsService;
    }

    // ... (the run() and tailLogFile() methods remain exactly the same)
    @Override
    public void run(String... args) throws Exception {
        // Run the tailing logic in a new thread so it doesn't block the main application
        Thread tailerThread = new Thread(this::tailLogFile, "LogFileTailerThread");
        tailerThread.setDaemon(true); // Mark as daemon so it doesn't prevent app shutdown
        tailerThread.start();
    }

    private void tailLogFile() {
        System.out.println("Watching for changes in: " + filePath);
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(filePath).toAbsolutePath().getParent();
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);


            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.context().toString().equals(Paths.get(filePath).getFileName().toString())) {
                        readNewLines();
                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }


    private void readNewLines() {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            file.seek(lastKnownPosition);
            String line;
            while ((line = file.readLine()) != null) {
                logParser.parse(line).ifPresent(logEntry -> {
                    analyticsService.processLogEntry(logEntry);
                });
            }
            lastKnownPosition = file.getFilePointer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}