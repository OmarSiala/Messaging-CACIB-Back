package com.example.messaging.service;

import java.util.UUID;

/**
 * Exception métier levée lorsqu'un message n'existe pas.
 */
public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(UUID id) {
        super("Message introuvable pour l'identifiant: " + id);
    }
}

