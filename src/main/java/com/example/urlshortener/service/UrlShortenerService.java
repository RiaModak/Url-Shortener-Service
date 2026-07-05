// src/main/java/com/example/urlshortener/service/UrlShortenerService.java

package com.example.urlshortener.service;

import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
// NEW: Import the DTO we will be returning.
import com.example.urlshortener.dto.UrlStatsResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UrlShortenerService {

    private static final String BASE62_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final UrlMappingRepository urlMappingRepository;

    public UrlShortenerService(UrlMappingRepository urlMappingRepository) {
        this.urlMappingRepository = urlMappingRepository;
    }

    @Transactional
    public String shortenUrl(String originalUrl) {
        // ... (existing shortenUrl method logic)
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setCreationDate(LocalDateTime.now());

        UrlMapping savedEntity = urlMappingRepository.save(urlMapping);

        String shortCode = encodeBase62(savedEntity.getId());
        savedEntity.setShortCode(shortCode);

        urlMappingRepository.save(savedEntity);

        return shortCode;
    }

    @Transactional
    public String getOriginalUrlAndIncrementClicks(String shortCode) {
        // ... (existing getOriginalUrlAndIncrementClicks method logic)
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for short code: " + shortCode));

        urlMapping.setClickCount(urlMapping.getClickCount() + 1);
        urlMappingRepository.save(urlMapping);

        return urlMapping.getOriginalUrl();
    }

    // --- NEWLY ADDED METHOD START ---

    /**
     * Retrieves statistics for a given short code.
     * This is a read-only operation and doesn't need to be @Transactional by itself,
     * but adding it is harmless and keeps it consistent with other data-access methods.
     *
     * @param shortCode The unique code to look up.
     * @return A UrlStatsResponse DTO containing the statistics.
     * @throws UrlNotFoundException if the short code does not exist.
     */
    public UrlStatsResponse getStats(String shortCode) {
        // Step 1: Find the entity. We reuse our repository's custom find method.
        // Step 2: Validate. We reuse the .orElseThrow() pattern with our existing
        // custom exception. This ensures our API's error handling is consistent.
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("No statistics found for short code: " + shortCode));

        // Step 3: Transform (Map) the entity to the DTO.
        // We construct the full URL here for user convenience, as the DTO contract requires it.
        String fullShortUrl = "http://localhost:8080/" + urlMapping.getShortCode();

        // We create a new instance of our immutable UrlStatsResponse record,
        // populating it with data from the UrlMapping entity we just fetched.
        return new UrlStatsResponse(
                urlMapping.getOriginalUrl(),
                fullShortUrl,
                urlMapping.getCreationDate(),
                urlMapping.getClickCount()
        );
    }

    // --- NEWLY ADDED METHOD END ---

    private String encodeBase62(Long number) {
        // ... (existing encodeBase62 method logic)
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