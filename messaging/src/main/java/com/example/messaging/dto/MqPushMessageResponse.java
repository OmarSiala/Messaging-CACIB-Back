package com.example.messaging.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Reponse REST suite a une publication vers IBM MQ.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqPushMessageResponse {

    private String queue;
    private String correlationId;
    private LocalDateTime sentAt;
    private String status;
}

