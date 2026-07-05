// src/main/java/com/example/urlshortener/service/UrlShortenerService.java

package com.example.urlshortener.service;

// NEW IMPORT for our custom exception
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.model.UrlMapping;
import com.example.urlshortener.repository.UrlMappingRepository;
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
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setCreationDate(LocalDateTime.now());

        UrlMapping savedEntity = urlMappingRepository.save(urlMapping);

        String shortCode = encodeBase62(savedEntity.getId());
        savedEntity.setShortCode(shortCode);

        urlMappingRepository.save(savedEntity);

        return shortCode;
    }

    /**
     * Finds the original URL for a given short code and increments its click count.
     * The @Transactional annotation ensures this is an atomic operation.
     *
     * @param shortCode The unique code representing the shortened URL.
     * @return The original, long URL to redirect to.
     * @throws UrlNotFoundException if the short code does not exist in the database.
     */
    @Transactional
    public String getOriginalUrlAndIncrementClicks(String shortCode) {
        // The .orElseThrow() method is the most elegant way to handle an Optional that
        // is expected to contain a value.
        // It attempts to get the value from the Optional. If the Optional is empty,
        // it throws the exception provided by the Supplier lambda `() -> ...`.
        // This single line replaces the entire if/else block.
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found for short code: " + shortCode));

        // This part of the code is only reached if a UrlMapping was found.
        urlMapping.setClickCount(urlMapping.getClickCount() + 1);

        // Within a @Transactional method, this save call is technically optional due to
        // dirty checking, but it makes the intent to persist the change explicit.
        urlMappingRepository.save(urlMapping);

        return urlMapping.getOriginalUrl();
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