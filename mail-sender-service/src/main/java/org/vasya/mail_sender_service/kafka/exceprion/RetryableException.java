package org.vasya.mail_sender_service.kafka.exceprion;

public class RetryableException extends  RuntimeException {
    public RetryableException(String message) {
        super(message);
    }

    public RetryableException(Throwable cause) {
        super(cause);
    }
}