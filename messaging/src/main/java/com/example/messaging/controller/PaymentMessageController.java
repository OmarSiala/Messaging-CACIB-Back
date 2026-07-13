package com.example.messaging.controller;

import com.example.messaging.dto.PaymentMessageResponse;
import com.example.messaging.service.PaymentMessageService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST de consultation des messages persistés.
 */
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class PaymentMessageController {

    private final PaymentMessageService service;


    /**
     * Liste paginée des messages.
     *
     * @param pageable pagination demandée
     * @return page de messages
     */
    @GetMapping
    public ResponseEntity<Page<PaymentMessageResponse>> getMessages(Pageable pageable) {
        return ResponseEntity.ok(service.getMessages(pageable));
    }

    /**
     * Détail d'un message.
     *
     * @param id identifiant technique
     * @return message trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMessageResponse> getMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }
}

