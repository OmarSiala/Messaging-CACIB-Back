package com.example.messaging.config;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.util.StringUtils;

/**
 * Configuration de la couche JMS vers IBM MQ.
 */
@Configuration
@EnableJms
@EnableConfigurationProperties(MqProperties.class)
public class JmsConfiguration {

    /**
     * Initialise la connexion JMS IBM MQ en mode client.
     *
     * @param properties propriétés MQ applicatives
     * @return ConnectionFactory prête pour les listeners
     * @throws JMSException en cas de configuration invalide
     */
    @Bean
    public ConnectionFactory ibmMqConnectionFactory(MqProperties properties) throws JMSException {
        MQConnectionFactory mqConnectionFactory = new MQConnectionFactory();
        mqConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
        mqConnectionFactory.setQueueManager(properties.getQueueManager());
        mqConnectionFactory.setChannel(properties.getChannel());
        mqConnectionFactory.setConnectionNameList(properties.getConnName());
        mqConnectionFactory.setAppName("messaging-cacib");

        if (StringUtils.hasText(properties.getUsername())) {
            UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
            adapter.setTargetConnectionFactory(mqConnectionFactory);
            adapter.setUsername(properties.getUsername());
            adapter.setPassword(properties.getPassword());
            return adapter;
        }

        return mqConnectionFactory;
    }

    /**
     * Configure la factory de listener JMS avec sessions transactionnelles.
     *
     * @param connectionFactory factory IBM MQ
     * @return factory des listeners @JmsListener
     */
    @Bean(name = "jmsListenerContainerFactory")
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setSessionTransacted(true);
        factory.setErrorHandler(throwable -> {
            throw new IllegalStateException("Erreur listener IBM MQ", throwable);
        });
        return factory;
    }
}

