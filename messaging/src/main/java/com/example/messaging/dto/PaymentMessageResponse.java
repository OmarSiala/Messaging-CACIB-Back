package com.example.messaging.dto;

import com.example.messaging.model.MessageStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO exposé par l'API pour consulter un message.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMessageResponse {

    private UUID id;
    private String mqMessageId;
    private String correlationId;
    private String sourceQueue;
    private String payload;
    private MessageStatus status;
    private Instant receivedAt;
    private Instant createdAt;
}

