package com.example.messaging.mq;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.stereotype.Component;

/**
 * Strategy d'extraction pour les TextMessage JMS.
 */
@Component
public class TextMessagePayloadExtractorStrategy implements MessagePayloadExtractorStrategy {

    @Override
    public boolean supports(Message message) {
        return message instanceof TextMessage;
    }

    @Override
    public String extract(Message message) throws JMSException {
        return ((TextMessage) message).getText();
    }
}

