package com.example.messaging.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriétés de connexion IBM MQ pour la consommation JMS.
 */
@ConfigurationProperties(prefix = "app.mq")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqProperties {

    private String queueManager;
    private String channel;
    private String connName;
    private String username;
    private String password;
    private String inboundQueue;
    private String outboundQueue;
}

