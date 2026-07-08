package com.example.urlshortener.controller;

import com.example.urlshortener.dto.UrlStatsResponse;
import com.example.urlshortener.exception.AliasAlreadyExistsException;
import com.example.urlshortener.exception.UrlNotFoundException;
import com.example.urlshortener.service.UrlShortenerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    private final UrlShortenerService urlShortenerService;

    public PageController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @PostMapping("/shorten-web")
    public String handleShortenForm(
            @RequestParam("longUrl") String longUrl,
            @RequestParam(name = "customAlias", required = false) String customAlias,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("originalUrl", longUrl);

        try {
            String shortCode = urlShortenerService.shortenUrl(longUrl, customAlias, null);
            String fullShortUrl = "http://localhost:8080/" + shortCode;
            redirectAttributes.addFlashAttribute("shortUrlResult", fullShortUrl);
        } catch (AliasAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("aliasError", e.getMessage());
        }

        return "redirect:/";
    }

    @PostMapping("/check-stats")
    public String handleStatsCheckForm(
            @RequestParam("shortCode") String shortCode,
            RedirectAttributes redirectAttributes
    ) {
        try {
            UrlStatsResponse stats = urlShortenerService.getUrlStats(shortCode);
            redirectAttributes.addFlashAttribute("urlStats", stats);
        } catch (UrlNotFoundException e) {
            redirectAttributes.addFlashAttribute("statsError", e.getMessage());
        }
        return "redirect:/";
    }
}