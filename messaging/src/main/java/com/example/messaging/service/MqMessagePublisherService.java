package com.example.messaging.service;

import com.example.messaging.dto.MqPushMessageRequest;
import com.example.messaging.dto.MqPushMessageResponse;

/**
 * Service Layer de publication des messages de paiement vers IBM MQ.
 */
public interface MqMessagePublisherService {

    /**
     * Publie un message sur la file MQ de sortie configuree.
     *
     * @param request requete de publication
     * @return details de publication
     */
    MqPushMessageResponse pushMessage(MqPushMessageRequest request);
}

