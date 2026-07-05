package com.example.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A custom exception thrown when a requested short URL is not found in the database.
 *
 * We are extending RuntimeException, which means this is an "unchecked" exception.
 * In the context of a Spring REST API, it's common to use unchecked exceptions for
 * server errors (like a 404 Not Found) because they don't force the calling code
 * to use a try-catch block, leading to cleaner controller logic. The exception can
 * be caught and handled globally.
 *
 * @ResponseStatus(HttpStatus.NOT_FOUND): This is a powerful Spring annotation.
 * It tells Spring that whenever this exception is thrown and bubbles up to the
 * web layer without being caught, it should automatically be translated into an
 * HTTP response with a 404 Not Found status code. This gives us a simple and
 * effective way to handle our error case without yet needing a full @ControllerAdvice.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UrlNotFoundException extends RuntimeException {

    /**
     * Constructor for the UrlNotFoundException.
     * @param message A descriptive message explaining the reason for the exception.
     */
    public UrlNotFoundException(String message) {
        // We call the constructor of the parent class (RuntimeException)
        // to set the exception message.
        super(message);
    }
}