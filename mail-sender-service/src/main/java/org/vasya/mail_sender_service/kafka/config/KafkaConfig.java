package org.vasya.mail_sender_service.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import org.vasya.mail_sender_service.kafka.config.logging.MdcLoggingKafkaConsumerInterceptor;
import org.vasya.mail_sender_service.kafka.config.properties.KafkaProperties;
import org.vasya.mail_sender_service.kafka.contract.EmailSendingEvent;
import org.vasya.mail_sender_service.kafka.exceprion.NonRetryableException;
import org.vasya.mail_sender_service.kafka.exceprion.RetryableException;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private final String kafkaAddress;

    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaAddress = kafkaProperties.kafkaAddress();
    }

    @Bean
    public ConsumerFactory<String, EmailSendingEvent> consumerFactory(ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        JsonDeserializer<EmailSendingEvent> jsonDeserializer = new JsonDeserializer<>(EmailSendingEvent.class, objectMapper, false);
        jsonDeserializer.addTrustedPackages("*");


        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailSendingEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, EmailSendingEvent> consumerFactory,
            KafkaTemplate kafkaTemplate) {

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(3000, 3));
        errorHandler.addRetryableExceptions(RetryableException.class);
        errorHandler.addNotRetryableExceptions(NonRetryableException.class);

        ConcurrentKafkaListenerContainerFactory<String, EmailSendingEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(1);
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setRecordInterceptor(new MdcLoggingKafkaConsumerInterceptor());

        return factory;
    }
}