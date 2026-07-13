package com.example.messaging.mq;

import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

/**
 * Strategy d'extraction pour les BytesMessage JMS.
 */
@Component
public class BytesMessagePayloadExtractorStrategy implements MessagePayloadExtractorStrategy {

    @Override
    public boolean supports(Message message) {
        return message instanceof BytesMessage;
    }

    @Override
    public String extract(Message message) throws JMSException {
        BytesMessage bytesMessage = (BytesMessage) message;
        byte[] content = new byte[(int) bytesMessage.getBodyLength()];
        bytesMessage.readBytes(content);
        return new String(content, StandardCharsets.UTF_8);
    }
}

