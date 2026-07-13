package com.example.messaging.mq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import java.util.List;
import org.junit.jupiter.api.Test;

class JmsInboundMessageAdapterTest {

    @Test
    void shouldAdaptTextMessage() throws JMSException {
        MessagePayloadExtractorStrategy extractor = new TextMessagePayloadExtractorStrategy();
        JmsInboundMessageAdapter adapter = new JmsInboundMessageAdapter(List.of(extractor));
        TextMessage message = mock(TextMessage.class);

        when(message.getText()).thenReturn("payload");
        when(message.getJMSMessageID()).thenReturn("ID:42");
        when(message.getJMSCorrelationID()).thenReturn("CORR-42");

        InboundMqMessage inbound = adapter.adapt(message, "DEV.QUEUE.1");

        assertThat(inbound.getPayload()).isEqualTo("payload");
        assertThat(inbound.getMqMessageId()).isEqualTo("ID:42");
        assertThat(inbound.getCorrelationId()).isEqualTo("CORR-42");
        assertThat(inbound.getSourceQueue()).isEqualTo("DEV.QUEUE.1");
    }

    @Test
    void shouldRejectUnsupportedMessageType() {
        JmsInboundMessageAdapter adapter = new JmsInboundMessageAdapter(List.of(new TextMessagePayloadExtractorStrategy()));
        Message unsupported = mock(Message.class);

        assertThatThrownBy(() -> adapter.adapt(unsupported, "DEV.QUEUE.1"))
            .isInstanceOf(JMSException.class)
            .hasMessageContaining("non supporte");
    }
}

