package com.example.urlshortener.dto;

/**
 * A Data Transfer Object (DTO) that represents the successful response from the URL shortening endpoint.
 * Like the request DTO, we use a Java Record for its conciseness and immutability.
 *
 * This record defines the JSON contract for the data our API will send back to the client.
 * When our controller returns an instance of this record, Spring Boot's web layer, with the
 * help of the Jackson library, will automatically serialize it into a JSON object.
 *
 * For example, a successful response will look like this in the HTTP body:
 * {
 *     "shortUrl": "http://localhost:8080/aB1cDe"
 * }
 *
 * @param shortUrl The complete, ready-to-use short URL that the client can share or use for redirection.
 *                 It's important that we return the full URL, not just the short code, to provide
 *                 the best user experience for the API consumer.
 */
public record ShortenUrlResponse(
        String shortUrl
) {
}