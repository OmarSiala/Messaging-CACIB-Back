package com.example.messaging.service;

import com.example.messaging.dto.PaymentMessageResponse;
import com.example.messaging.mq.InboundMqMessage;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Layer d'orchestration des messages bancaires.
 */
public interface PaymentMessageService {

    /**
     * Persiste un message entrant provenant de la file MQ.
     *
     * @param inbound message adapté
     * @return message persisté et exposable
     */
    PaymentMessageResponse saveIncomingMessage(InboundMqMessage inbound);

    /**
     * Récupère un message par identifiant technique.
     *
     * @param id identifiant UUID
     * @return message trouvé
     */
    PaymentMessageResponse getById(UUID id);

    /**
     * Consulte les messages avec pagination.
     *
     * @param pageable paramètres de pagination
     * @return page de messages
     */
    Page<PaymentMessageResponse> getMessages(Pageable pageable);
}

