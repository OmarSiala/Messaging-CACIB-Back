package com.example.messaging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Requete REST de publication d'un message vers IBM MQ.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqPushMessageRequest {

    @NotBlank(message = "Le payload est obligatoire")
    private String payload;

    @Size(max = 64, message = "Le correlationId ne doit pas depasser 64 caracteres")
    private String correlationId;
}

