package com.example.messaging.mq;

import jakarta.jms.JMSException;
import jakarta.jms.Message;

/**
 * Strategy d'extraction du payload selon le type concret JMS.
 */
public interface MessagePayloadExtractorStrategy {

    boolean supports(Message message);

    String extract(Message message) throws JMSException;
}

