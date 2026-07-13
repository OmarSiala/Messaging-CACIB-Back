package com.example.messaging.factory;

import com.example.messaging.model.MessageStatus;
import com.example.messaging.model.PaymentMessageEntity;
import com.example.messaging.mq.InboundMqMessage;
import org.springframework.stereotype.Component;

/**
 * Factory centralisant la construction des entités message.
 */
@Component
public class PaymentMessageFactory {

    /**
     * Construit l'entité persistable à partir du message inbound.
     *
     * @param inbound message adapté depuis JMS
     * @return entité initialisée
     */
    public PaymentMessageEntity buildReceived(InboundMqMessage inbound) {
        return PaymentMessageEntity.builder()
            .mqMessageId(inbound.getMqMessageId())
            .correlationId(inbound.getCorrelationId())
            .sourceQueue(inbound.getSourceQueue())
            .payload(inbound.getPayload())
            .status(MessageStatus.RECEIVED)
            .receivedAt(inbound.getReceivedAt())
            .build();
    }
}

