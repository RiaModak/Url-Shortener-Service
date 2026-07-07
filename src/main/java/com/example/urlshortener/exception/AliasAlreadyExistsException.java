// src/main/java/com/example/urlshortener/exception/AliasAlreadyExistsException.java

package com.example.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception thrown when a user tries to create a short URL with a custom alias
 * that is already in use by another URL mapping.
 *
 * Extending RuntimeException makes this an "unchecked" exception, which is a common
 * practice in Spring applications for business exceptions that should be handled globally.
 *
 * The @ResponseStatus(HttpStatus.CONFLICT) annotation is a powerful Spring feature.
 * It tells Spring that if this exception is thrown and not caught by a more specific
 * @ExceptionHandler, the framework should automatically generate an HTTP response with
 * a 409 Conflict status code. This provides a sensible default behavior.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AliasAlreadyExistsException extends RuntimeException {

    /**
     * Constructor that accepts a detailed error message.
     * @param message The message describing the conflict.
     */
    public AliasAlreadyExistsException(String message) {
        super(message);
    }
}