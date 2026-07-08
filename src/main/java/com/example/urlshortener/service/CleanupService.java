// src/main/java/com/example/urlshortener/service/CleanupService.java

package com.example.urlshortener.service;

import com.example.urlshortener.repository.UrlMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
// NEW: Import the @Transactional annotation.
import org.springframework.transaction.annotation.Transactional;

// NEW: Import LocalDateTime for getting the current time.
import java.time.LocalDateTime;

@Service
public class CleanupService {

    private static final Logger logger = LoggerFactory.getLogger(CleanupService.class);
    private final UrlMappingRepository urlMappingRepository;

    public CleanupService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    // --- METHOD TO BE MODIFIED ---
    @Scheduled(cron = "0 0 1 * * ?")
    // NEW: Add the @Transactional annotation. This ensures that the entire delete
    // operation is wrapped in a single, safe database transaction.
    @Transactional
    public void cleanupExpiredUrls() {
        logger.info("Starting scheduled job: Cleaning up expired URL mappings...");

        // --- HIGHLIGHTED CHANGE START ---

        // 1. Get the current time. This will be the reference point for what is considered "expired".
        LocalDateTime now = LocalDateTime.now();

        // 2. Call our new, efficient repository method.
        // This single line executes a bulk DELETE command on the database.
        long deletedCount = urlMappingRepository.deleteByExpirationDateBefore(now);

        // 3. Log the result. This provides crucial visibility into what the automated
        // job did. In a production system, this log is essential for monitoring.
        if (deletedCount > 0) {
            logger.info("Finished scheduled job: Successfully deleted {} expired URL mappings.", deletedCount);
        } else {
            logger.info("Finished scheduled job: No expired URL mappings found to delete.");
        }

        // --- HIGHLIGHTED CHANGE END ---
    }
}