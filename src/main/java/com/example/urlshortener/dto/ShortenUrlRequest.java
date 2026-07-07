// src/main/java/com/example/urlshortener/dto/ShortenUrlRequest.java

package com.example.urlshortener.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

/**
 * The DTO (Data Transfer Object) for incoming URL shortening requests.
 * As a Java 'record', it's an immutable carrier for our request data.
 *
 * @param url         The original, long URL to be shortened. This is mandatory.
 *                    - @NotEmpty ensures the URL is not null and not an empty string.
 *                    - @URL ensures the string is a well-formed URL.
 * @param customAlias An OPTIONAL user-defined alias for the short URL.
 *                    If this is null or empty, the service will generate a random short code.
 *                    If it's provided, the service will attempt to use it as the short code.
 */
public record ShortenUrlRequest(
        @NotEmpty(message = "URL cannot be empty")
        @URL(message = "A valid URL format is required")
        String url,

        // --- NEWLY ADDED FIELD ---
        // We are adding the 'customAlias' field to our record.
        // By simply adding it to the record's header, Java automatically adds
        // the field, a constructor parameter, and an accessor method.
        // We are not adding any validation annotations here yet, as null is a valid
        // (and expected) value representing that no alias was provided.
        // We will add more specific validation for the alias format later.
        String customAlias
) {
}