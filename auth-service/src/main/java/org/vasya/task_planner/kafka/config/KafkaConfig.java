package org.vasya.task_planner.kafka.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.vasya.task_planner.config.logging.MdcLoggingInterceptor;
import org.vasya.task_planner.kafka.config.properties.KafkaProperties;
import org.vasya.task_planner.kafka.contract.EmailSendingEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    private final String kafkaAddress;

    public KafkaConfig(KafkaProperties kafkaProperties) {
        this.kafkaAddress = kafkaProperties.kafkaAddress();
    }

    @Bean
    public ProducerFactory<String, EmailSendingEvent> producerFactory(ObjectMapper objectMapper) {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        config.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, MdcLoggingInterceptor.class.getName());

        JsonSerializer<EmailSendingEvent> jsonSerializer = new JsonSerializer<>(objectMapper);
        jsonSerializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), jsonSerializer);
    }

    @Bean
    public KafkaTemplate<String, EmailSendingEvent> kafkaTemplate(ProducerFactory<String, EmailSendingEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}