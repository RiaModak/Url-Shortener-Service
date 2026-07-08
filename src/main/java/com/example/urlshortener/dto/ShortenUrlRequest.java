// src/main/java/com/example/urlshortener/dto/ShortenUrlRequest.java

package com.example.urlshortener.dto;

// NEW: Import the validation annotation for minimum numeric value.
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

/**
 * The DTO for incoming URL shortening requests.
 *
 * @param url           The original, long URL to be shortened. Mandatory.
 * @param customAlias   An optional user-defined alias for the short URL.
 * @param hoursToExpire An OPTIONAL time-to-live (TTL) in hours. If provided, the link
 *                      will expire after this many hours. If null, the link is permanent.
 */
public record ShortenUrlRequest(
        @NotEmpty(message = "URL cannot be empty")
        @URL(message = "A valid URL format is required")
        String url,

        String customAlias,

        // --- NEWLY ADDED FIELD START ---

        /**
         * The time-to-live for the URL in hours.
         * - We use the 'Integer' wrapper class instead of the primitive 'int' so that
         *   the value can be 'null'. A null value indicates that the user did not
         *   specify an expiration, meaning the link should be permanent.
         * - @Min(1): This validation annotation ensures that if a value IS provided,
         *   it must be a positive integer (1 or greater). This prevents non-sensical
         *   values like 0 or -5. Spring's validation mechanism will automatically
         *   check this and reject the request with a 400 Bad Request if the rule is violated.
         */
        @Min(value = 1, message = "Hours to expire must be a positive number")
        Integer hoursToExpire
        // --- NEWLY ADDED FIELD END ---
) {
}