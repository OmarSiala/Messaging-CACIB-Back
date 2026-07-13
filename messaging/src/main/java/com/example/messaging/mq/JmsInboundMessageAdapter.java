package com.example.messaging.mq;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adapter qui convertit un message JMS en modèle métier interne.
 */
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
                return extractor.extract(message);
            }
        }
        throw new JMSException("Type de message JMS non supporte: " + message.getClass().getName());
    }
}

