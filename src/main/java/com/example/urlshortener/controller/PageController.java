// src/main/java/com/example/urlshortener/controller/PageController.java

package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlStatsResponse; // NEW: Import the DTO
import com.example.urlshortener.exception.UrlNotFoundException; // NEW: Import the exception
import com.example.urlshortener.service.UrlShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final UrlShortenerService urlShortenerService;

    public PageController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    // ... (existing indexPage() and handleShortenForm() methods) ...
    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @PostMapping("/shorten-web")
    public String handleShortenForm(@RequestParam("longUrl") String longUrl, Model model) {
        String shortCode = urlShortenerService.shortenUrl(longUrl);
        String fullShortUrl = "http://localhost:8080/" + shortCode;
        model.addAttribute("originalUrl", longUrl);
        model.addAttribute("shortUrlResult", fullShortUrl);
        return "index";
    }

    @PostMapping("/check-stats")
    public String handleStatsCheckForm(@RequestParam("checkShortCode") String shortCode, Model model) {
        // --- HIGHLIGHTED CHANGE START ---

        try {
            // 1. Call the service to get the statistics. If successful, this returns our DTO.
            UrlStatsResponse stats = urlShortenerService.getStats(shortCode);
            // 2. Add the successfully retrieved stats object to the model.
            // We will use the key "urlStats" to reference this object in our HTML.
            model.addAttribute("urlStats", stats);
        } catch (UrlNotFoundException e) {
            // 3. If the service throws UrlNotFoundException, we catch it here.
            // This prevents the application from showing a generic error page.
            // Instead, we add a user-friendly error message to the model.
            // We will use the key "statsError" to check for this message in our HTML.
            model.addAttribute("statsError", "Statistics not found for short code: " + shortCode);
        }

        // --- HIGHLIGHTED CHANGE END ---

        // 4. Return "index" to re-render the page. The Thymeleaf template will now have
        // access to either the "urlStats" object or the "statsError" message.
        return "index";
    }
}