// src/main/java/com/example/urlshortener/exception/GlobalExceptionHandler.java

package com.example.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<Object> handleUrlNotFoundException(UrlNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // --- NEWLY ADDED METHOD START ---

    /**
     * This method is a dedicated handler for the AliasAlreadyExistsException.
     * It provides a consistent, structured JSON response for this specific business error.
     *
     * @ExceptionHandler(AliasAlreadyExistsException.class): This annotation registers this
     *   method as the handler for any AliasAlreadyExistsException thrown by any controller.
     *   This is more specific and provides more control than the @ResponseStatus annotation
     *   on the exception class itself.
     *
     * @param ex The actual exception object that was thrown.
     * @param request The web request during which the exception occurred.
     * @return A ResponseEntity with a 409 Conflict status and a structured JSON body
     *         that is consistent with our other API error responses.
     */
    @ExceptionHandler(AliasAlreadyExistsException.class)
    public ResponseEntity<Object> handleAliasAlreadyExistsException(AliasAlreadyExistsException ex, WebRequest request) {

        // We create the same structured body as our other handlers for consistency.
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value()); // e.g., 409
        body.put("error", "Conflict"); // The standard reason phrase for a 409 status
        body.put("message", ex.getMessage()); // The specific message from our service layer
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Return the ResponseEntity, now with the 409 Conflict status.
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // --- NEWLY ADDED METHOD END ---

}