package com.example.messaging.mq;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter qui convertit un message JMS en modèle métier interne.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JmsInboundMessageAdapter {

    private final List<MessagePayloadExtractorStrategy> extractors;

    /**
     * Transforme un message JMS reçu en objet métier normalisé.
     *
     * @param message message JMS brut
     * @param sourceQueue nom de la file d'origine
     * @return message adapté
     * @throws JMSException en cas d'erreur d'accès aux métadonnées JMS
     */
    public InboundMqMessage adapt(Message message, String sourceQueue) throws JMSException {
        Objects.requireNonNull(message, "Le message JMS ne peut pas être null");
        Objects.requireNonNull(sourceQueue, "Le nom de la file source ne peut pas être null");
        String payload = extractPayload(message);
        return InboundMqMessage.builder()
            .mqMessageId(message.getJMSMessageID())
            .correlationId(message.getJMSCorrelationID())
            .sourceQueue(sourceQueue)
            .payload(payload)
            .receivedAt(Instant.now())
            .build();
    }

    private String extractPayload(Message message) throws JMSException {
        for (MessagePayloadExtractorStrategy extractor : extractors) {
            if (extractor.supports(message)) {
                log.debug("Extraction du payload du message JMS. type={} extractor={}", message.getClass().getName(), extractor.getClass().getName());
                return extractor.extract(message);
            }
        }
        log.warn("Type de message JMS non supporte pour extraction du payload. type={}", message.getClass().getName());
        throw new JMSException("Type de message JMS non supporté: " + message.getClass().getName());
    }
}

