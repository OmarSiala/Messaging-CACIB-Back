package com.example.messaging.controller;

import com.example.messaging.dto.MqPushMessageRequest;
import com.example.messaging.dto.MqPushMessageResponse;
import com.example.messaging.service.MqMessagePublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controleur REST de publication d'un message vers IBM MQ.
 */
@RestController
@RequestMapping("/api/v1/mq/messages")
@RequiredArgsConstructor
public class MqMessagePublisherController {

    private final MqMessagePublisherService publisherService;

    /**
     * Publie un message sur la queue MQ de sortie.
     *
     * @param request requete contenant le payload et le correlation id
     * @return resultat de publication
     */
    @PostMapping
    public ResponseEntity<MqPushMessageResponse> pushMessage(@Valid @RequestBody MqPushMessageRequest request) {
        return ResponseEntity.accepted().body(publisherService.pushMessage(request));
    }
}

