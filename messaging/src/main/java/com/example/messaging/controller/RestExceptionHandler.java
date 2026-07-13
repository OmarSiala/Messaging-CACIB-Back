package com.example.messaging.controller;

import com.example.messaging.service.MessageNotFoundException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestionnaire global d'erreurs REST pour l'API messages.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Traduit l'absence d'un message en réponse 404.
     *
     * @param exception exception métier
     * @return payload d'erreur
     */
    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(MessageNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", exception.getMessage()));
    }
}

