// src/main/java/com/example/urlshortener/controller/PageController.java

package com.example.urlshortener.controller;

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

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @PostMapping("/shorten-web")
    public String handleShortenForm(@RequestParam("longUrl") String longUrl, Model model) {
        // 1. Delegate to the service to perform the core business logic.
        String shortCode = urlShortenerService.shortenUrl(longUrl);

        // 2. Construct the full, user-facing URL.
        String fullShortUrl = "http://localhost:8080/" + shortCode;

        // 3. Pack the results into the Model object to make them available to the view.
        model.addAttribute("originalUrl", longUrl);
        model.addAttribute("shortUrlResult", fullShortUrl);

        // --- THIS TASK'S FOCUS ---
        // 4. Return the view name. This instructs Spring MVC to re-render the 'index.html'
        //    template. Because we've added attributes to the 'model', the Thymeleaf engine
        //    will now have access to 'originalUrl' and 'shortUrlResult' when processing
        //    the template, allowing us to display the results to the user.
        return "index";
    }
}