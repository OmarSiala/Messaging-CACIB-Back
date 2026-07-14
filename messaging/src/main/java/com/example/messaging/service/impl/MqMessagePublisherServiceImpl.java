package com.example.messaging.service.impl;

import com.example.messaging.config.MqProperties;
import com.example.messaging.dto.MqPushMessageRequest;
import com.example.messaging.dto.MqPushMessageResponse;
import java.time.LocalDateTime;

import com.example.messaging.exceptions.MqMessagePublishException;
import com.example.messaging.service.MqMessagePublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Implementation du Service Layer de publication de messages vers IBM MQ.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MqMessagePublisherServiceImpl implements MqMessagePublisherService {

    private static final String PUBLISHED_STATUS = "PUBLISHED";

    private final JmsOperations jmsOperations;
    private final MqProperties mqProperties;

    @Override
    @Transactional
    public MqPushMessageResponse pushMessage(MqPushMessageRequest request) {
        String destinationQueue = mqProperties.getOutboundQueue();

        try {
            jmsOperations.convertAndSend(destinationQueue, request.getPayload(), message -> {
                if (StringUtils.hasText(request.getCorrelationId())) {
                    message.setJMSCorrelationID(request.getCorrelationId());
                }
                return message;
            });
        } catch (JmsException exception) {
            log.error("Echec de publication vers IBM MQ. queue={}", destinationQueue, exception);
            throw new MqMessagePublishException(destinationQueue, exception);
        }

        log.info("Message publie sur IBM MQ. queue={} correlationId={}", destinationQueue, request.getCorrelationId());
        return MqPushMessageResponse.builder()
            .queue(destinationQueue)
            .correlationId(request.getCorrelationId())
            .sentAt(LocalDateTime.now())
            .status(PUBLISHED_STATUS)
            .build();
    }
}

