// src/main/java/com/example/urlshortener/dto/UrlStatsResponse.java

package com.example.urlshortener.dto;

import java.time.LocalDateTime;

/**
 * A Data Transfer Object (DTO) for returning statistics about a specific URL mapping.
 *
 * Using a Java 'record' is ideal for DTOs because it automatically provides:
 * - An all-arguments constructor.
 * - public accessor methods for all fields (e.g., originalUrl()).
 * - Implementations for equals(), hashCode(), and toString().
 * - Immutability (all fields are final by default).
 *
 * This record defines the public contract for our statistics API endpoint.
 *
 * @param originalUrl   The original, long URL that was shortened.
 * @param shortUrl      The full, clickable short URL (e.g., "http://localhost:8080/aB1cDe").
 *                      This is more user-friendly than just returning the short code.
 * @param creationDate  The timestamp when the short URL was originally created.
 * @param clickCount    The total number of times the short URL has been clicked (redirected).
 */
public record UrlStatsResponse(
        String originalUrl,
        String shortUrl,
        LocalDateTime creationDate,
        long clickCount
) {
}