package com.example.messaging.mq;

import com.example.messaging.config.MqProperties;
import com.example.messaging.service.PaymentMessageService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Listener IBM MQ qui réceptionne les messages de paiement et les persiste.
 */
@Component
@RequiredArgsConstructor
public class PaymentMessageListener {

    private final JmsInboundMessageAdapter adapter;
    private final PaymentMessageService service;
    private final MqProperties mqProperties;


    /**
     * Traite chaque message entrant sur la file configurée.
     *
     * @param message message JMS brut
     * @throws JMSException en cas d'échec d'adaptation du message
     */
    @JmsListener(destination = "${app.mq.inbound-queue}")
    public void onMessage(Message message) throws JMSException {
        InboundMqMessage inbound = adapter.adapt(message, mqProperties.getInboundQueue());
        service.saveIncomingMessage(inbound);
    }
}

