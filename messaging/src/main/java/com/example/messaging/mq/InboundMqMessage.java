package com.example.messaging.mq;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Modèle d'entrée interne après adaptation d'un message JMS.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundMqMessage {

    private String mqMessageId;
    private String correlationId;
    private String sourceQueue;
    private String payload;
    private Instant receivedAt;
}

