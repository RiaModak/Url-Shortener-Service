// src/main/java/com/example/urlshortener/controller/UrlController.java

package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenUrlRequest;
import com.example.urlshortener.dto.ShortenUrlResponse;
// We will create this DTO in the next step. For now, we can add the import
// in preparation, or your IDE can add it for you later.
import com.example.urlshortener.dto.UrlStatsResponse;
import com.example.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class UrlController {

    private final UrlShortenerService urlShortenerService;

    public UrlController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    // --- METHOD TO BE MODIFIED ---
    @PostMapping("/api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        // --- HIGHLIGHTED CHANGE: UPDATED SERVICE CALL ---
        // We now pass both the url and the customAlias from the request DTO to the service method.
        // The record's accessor methods (request.url() and request.customAlias()) make this clean and easy.
        // If the client didn't provide a 'customAlias' in the JSON, request.customAlias() will be null.
        String shortCode = urlShortenerService.shortenUrl(request.url(), request.customAlias());

        String fullShortUrl = "http://localhost:8080/" + shortCode;
        ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ... other existing methods (redirect, getUrlStats) ...
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlShortenerService.getOriginalUrlAndIncrementClicks(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
    }

    // --- NEWLY ADDED METHOD START ---

    /**
     * This endpoint retrieves statistics for a specific short URL.
     *
     * @GetMapping("/api/v1/url/stats/{shortCode}"): Maps HTTP GET requests to this method.
     *   - The path is namespaced under our API standard /api/v1/url.
     *   - 'stats' clearly indicates the purpose of the endpoint.
     *   - {shortCode} is a path variable to specify which URL's stats to fetch.
     *
     * @param shortCode The short code captured from the URL path via @PathVariable.
     * @return A ResponseEntity containing the statistics in a UrlStatsResponse DTO.
     *         The actual implementation will be built in the following tasks.
     */
    @GetMapping("/api/v1/url/stats/{shortCode}")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) {
        // In the next tasks, we will:
        // 1. Create the UrlStatsResponse DTO.
        // 2. Add a new method to the UrlShortenerService to fetch the stats.
        // 3. Call that service method here and return its result.

        // For now, returning null is a placeholder for the logic to come.
        return null;
    }

    // --- NEWLY ADDED METHOD END ---
}