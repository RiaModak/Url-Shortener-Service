// src/main/java/com/example/urlshortener/controller/UrlController.java

package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenUrlRequest;
import com.example.urlshortener.dto.ShortenUrlResponse;
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

    @PostMapping("/api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        String shortCode = urlShortenerService.shortenUrl(request.url());
        String fullShortUrl = "http://localhost:8080/" + shortCode;
        ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlShortenerService.getOriginalUrlAndIncrementClicks(shortCode);

        // --- HIGHLIGHTED REFACTOR START ---

        // This single line replaces the previous multi-line implementation.
        // It uses the ResponseEntity's fluent builder pattern for a more concise and readable result.
        //
        // 1. ResponseEntity.status(HttpStatus.FOUND): This static method starts the build process.
        //    It sets the HTTP status code to 302 Found and returns a builder object.
        //
        // 2. .location(URI.create(originalUrl)): This method is called on the builder object.
        //    It sets the 'Location' header to the provided URI. It also returns the same
        //    builder object, allowing for method chaining.
        //
        // 3. .build(): This final method completes the process. It constructs the immutable
        //    ResponseEntity<Void> object from the configured builder. Since we never called
        //    the .body() method, the response has an empty body, which is correct for a redirect.
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();

        // --- HIGHLIGHTED REFACTOR END ---
    }
}