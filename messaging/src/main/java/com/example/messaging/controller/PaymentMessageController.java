package com.example.messaging.controller;

import com.example.messaging.dto.PaymentMessageResponse;
import com.example.messaging.service.PaymentMessageService;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST de consultation des messages persistés.
 */
@Slf4j
@Tag(name = "Messages de paiement", description = "API de consultation des messages IBM MQ persistés")
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
    @Operation(summary = "Liste paginée des messages de paiement")
    @ApiResponse(responseCode = "200", description = "Page de messages retournée avec succès")
    @GetMapping
    public ResponseEntity<Page<PaymentMessageResponse>> getMessages(@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Consultation des messages avec pagination page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(service.getMessages(pageable));
    }

    /**
     * Détail d'un message.
     *
     * @param id identifiant technique
     * @return message trouvé
     */
    @Operation(summary = "Détail d'un message de paiement par ID")
    @ApiResponse(responseCode = "200", description = "Message trouvé")
    @ApiResponse(responseCode = "404", description = "Message introuvable")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentMessageResponse> getMessageById(@Parameter(description = "UUID du message à afficher")@PathVariable UUID id) {
        log.info("Consultation du message id={}", id);
        return ResponseEntity.ok(service.getById(id));
    }
}

