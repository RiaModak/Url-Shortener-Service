// Located in src/main/java/com/example/urlshortener/dto/ShortenUrlRequest.java
package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record ShortenUrlRequest(
        // This annotation handles the requirement: "not null or empty"
        @NotEmpty(message = "URL cannot be empty")

        // This annotation provides an even stronger check
        @URL(message = "A valid URL format is required")
        String url
) {
}