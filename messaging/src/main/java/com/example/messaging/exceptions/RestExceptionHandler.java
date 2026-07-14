package com.example.messaging.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    /**
     * Traduit un echec de publication MQ en reponse 502.
     *
     * @param exception exception metier de publication
     * @return payload d'erreur
     */
    @ExceptionHandler(MqMessagePublishException.class)
    public ResponseEntity<Map<String, String>> handlePublishError(MqMessagePublishException exception) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(Map.of("error", exception.getMessage()));
    }

    /**
     * Retourne les erreurs de validation d'entree en 400.
     *
     * @param exception exception de validation
     * @return details de validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
            errors.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}

