// src/main/java/com/example/urlshortener/service/UrlShortenerService.java

package com.example.urlshortener.service;

import com.example.urlshortener.dto.UrlStatsResponse;
// We will need this exception in the next task.
import com.example.urlshortener.exception.AliasAlreadyExistsException;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// NEW: Import the Spring StringUtils class for a convenient text check.
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {

    // ... existing fields and constructor ...
    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    @Transactional
    public String shortenUrl(String originalUrl, String customAlias) {

        // --- HIGHLIGHTED REFACTORING START ---

        // Case 1: A custom alias is provided by the user.
        if (StringUtils.hasText(customAlias)) {
            // Check if the alias is already in use.
            if (urlMappingRepository.findByShortCode(customAlias).isPresent()) {
                // If it exists, throw our new, specific exception.
                // This will halt execution and be caught by our global handler.
                throw new AliasAlreadyExistsException("Alias '" + customAlias + "' is already in use.");
            }

            // If we reach here, the alias is available. We'll use it.
            UrlMapping newUrlMapping = new UrlMapping();
            newUrlMapping.setOriginalUrl(originalUrl);
            newUrlMapping.setCreationDate(LocalDateTime.now());
            newUrlMapping.setShortCode(customAlias); // Use the user's provided alias
            urlMappingRepository.save(newUrlMapping);
            return customAlias;
        }

        // Case 2: No custom alias is provided. Fall back to the original generation logic.
        else {
            // This is the logic you've already built for generating a code.
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setOriginalUrl(originalUrl);
            urlMapping.setCreationDate(LocalDateTime.now());

            // Save first to get the unique ID from the database sequence.
            UrlMapping savedEntity = urlMappingRepository.save(urlMapping);

            // Encode the ID to create the unique short code.
            String shortCode = encodeBase62(savedEntity.getId());
            savedEntity.setShortCode(shortCode);

            // Save the entity again, this time with the generated short code.
            urlMappingRepository.save(savedEntity);

            return shortCode;
        }
    }

    // ... other service methods (getOriginalUrlAndIncrementClicks, getStats, etc.) ...
    @Transactional
    public String getOriginalUrlAndIncrementClicks(String shortCode) {
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for short code: " + shortCode));

        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlMappingRepository.save(urlMapping);

        return urlMapping.getOriginalUrl();
    }

    public UrlStatsResponse getStats(String shortCode) {
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("No statistics found for short code: " + shortCode));

        String fullShortUrl = "http://localhost:8080/" + urlMapping.getShortCode();

        return new UrlStatsResponse(
                urlMapping.getOriginalUrl(),
                fullShortUrl,
                urlMapping.getCreationDate(),
                urlMapping.getClickCount()
        );
    }

    private String encodeBase62(Long number) {
        if (number == 0) {
            return String.valueOf(BASE62_CHARS.charAt(0));
        }

        StringBuilder sb = new StringBuilder();
        long num = number;

        while (num > 0) {
            int remainder = (int) (num % 62);
            sb.append(BASE62_CHARS.charAt(remainder));
            num /= 62;
        }

        return sb.reverse().toString();
    }
}