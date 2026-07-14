package com.example.messaging.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.example.messaging.config.MqProperties;
import com.example.messaging.dto.MqPushMessageRequest;
import com.example.messaging.dto.MqPushMessageResponse;
import com.example.messaging.exceptions.MqMessagePublishException;
import com.example.messaging.service.impl.MqMessagePublisherServiceImpl;
import jakarta.jms.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessagePostProcessor;

@ExtendWith(MockitoExtension.class)
class MqMessagePublisherServiceImplTest {

    @Mock
    private JmsOperations jmsOperations;
    @Mock
    private Message jmsMessage;

    @Captor
    private ArgumentCaptor<MessagePostProcessor> postProcessorCaptor;

    private MqMessagePublisherServiceImpl service;
    private MqProperties mqProperties;

    @BeforeEach
    void setUp() {
        mqProperties = new MqProperties();
        mqProperties.setOutboundQueue("DEV.QUEUE.1");
        service = new MqMessagePublisherServiceImpl(jmsOperations, mqProperties);
    }

    @Test
    void shouldPublishWithCorrelationId() throws Exception {
        MqPushMessageRequest request = MqPushMessageRequest.builder()
            .payload("payload mq")
            .correlationId("CORR-100")
            .build();

        MqPushMessageResponse response = service.pushMessage(request);

        verify(jmsOperations).convertAndSend(eq("DEV.QUEUE.1"), eq("payload mq"), postProcessorCaptor.capture());
        Message processed = postProcessorCaptor.getValue().postProcessMessage(jmsMessage);
        verify(jmsMessage).setJMSCorrelationID("CORR-100");
        assertEquals(jmsMessage, processed);
        assertEquals("DEV.QUEUE.1", response.getQueue());
        assertEquals("CORR-100", response.getCorrelationId());
        assertEquals("PUBLISHED", response.getStatus());
        assertNotNull(response.getSentAt());
    }

    @Test
    void shouldPublishWithoutCorrelationId() throws Exception {
        MqPushMessageRequest request = MqPushMessageRequest.builder()
            .payload("payload sans corr")
            .build();

        MqPushMessageResponse response = service.pushMessage(request);

        verify(jmsOperations).convertAndSend(eq("DEV.QUEUE.1"), eq("payload sans corr"), postProcessorCaptor.capture());
        postProcessorCaptor.getValue().postProcessMessage(jmsMessage);
        verify(jmsMessage, never()).setJMSCorrelationID(org.mockito.ArgumentMatchers.anyString());
        assertEquals("DEV.QUEUE.1", response.getQueue());
        assertEquals("PUBLISHED", response.getStatus());
    }

    @Test
    void shouldThrowBusinessExceptionWhenJmsFails() {
        MqPushMessageRequest request = MqPushMessageRequest.builder()
            .payload("payload")
            .build();

        doThrow(new org.springframework.jms.UncategorizedJmsException("boom", new RuntimeException("cause")))
            .when(jmsOperations)
            .convertAndSend(eq("DEV.QUEUE.1"), eq("payload"), org.mockito.ArgumentMatchers.any(MessagePostProcessor.class));

        assertThrows(MqMessagePublishException.class, () -> service.pushMessage(request));
    }
}

