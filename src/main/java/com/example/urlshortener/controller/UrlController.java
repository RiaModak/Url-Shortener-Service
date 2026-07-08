package com.example.urlshortener.controller;

import com.example.urlshortener.dto.ShortenUrlRequest;
import com.example.urlshortener.dto.ShortenUrlResponse;
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

    // ADDED: Crucial mapping to handle short link redirection and increment clicks!
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlShortenerService.getOriginalUrlAndIncrementClicks(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @PostMapping("/api/v1/url/shorten")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@Valid @RequestBody ShortenUrlRequest request) {
        String shortCode = urlShortenerService.shortenUrl(
                request.url(),
                request.customAlias(),
                request.hoursToExpire()
        );

        String fullShortUrl = "http://localhost:8080/" + shortCode;
        ShortenUrlResponse response = new ShortenUrlResponse(fullShortUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/v1/url/stats/{shortCode}")
    public ResponseEntity<UrlStatsResponse> getUrlStats(@PathVariable String shortCode) {
        UrlStatsResponse stats = urlShortenerService.getUrlStats(shortCode);
        return ResponseEntity.ok(stats);
    }
}