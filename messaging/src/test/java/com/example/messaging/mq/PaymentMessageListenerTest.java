package com.example.messaging.mq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.messaging.config.MqProperties;
import com.example.messaging.service.PaymentMessageService;
import jakarta.jms.TextMessage;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentMessageListenerTest {

    @Mock
    private PaymentMessageService service;
    @Mock
    private TextMessage message;

    @Test
    void shouldDelegateToService() throws Exception {
        MqProperties mqProperties = new MqProperties();
        mqProperties.setInboundQueue("DEV.QUEUE.1");
        JmsInboundMessageAdapter adapter = new JmsInboundMessageAdapter(List.of(new TextMessagePayloadExtractorStrategy()));
        PaymentMessageListener listener = new PaymentMessageListener(adapter, service, mqProperties);

        when(message.getText()).thenReturn("payload");
        when(message.getJMSMessageID()).thenReturn("ID:99");
        when(message.getJMSCorrelationID()).thenReturn("CORR");


        listener.onMessage(message);

        verify(service).saveIncomingMessage(org.mockito.ArgumentMatchers.argThat(in ->
            in.getMqMessageId().equals("ID:99")
                && in.getCorrelationId().equals("CORR")
                && in.getPayload().equals("payload")
                && in.getSourceQueue().equals("DEV.QUEUE.1")
        ));
    }
}

