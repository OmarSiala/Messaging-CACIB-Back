package com.example.messaging.exceptions;

/**
 * Exception metier levee en cas d'echec de publication vers IBM MQ.
 */
public class MqMessagePublishException extends RuntimeException {

    /**
     * Construit une exception de publication MQ.
     *
     * @param queue queue de destination
     * @param cause cause technique
     */
    public MqMessagePublishException(String queue, Throwable cause) {
        super("Echec de publication vers la queue MQ " + queue, cause);
    }
}

