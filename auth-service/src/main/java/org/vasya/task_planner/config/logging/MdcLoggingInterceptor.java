package org.vasya.task_planner.config.logging;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.MDC;
import org.vasya.task_planner.kafka.contract.EmailSendingEvent;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MdcLoggingInterceptor implements ProducerInterceptor<String, EmailSendingEvent> {
    @Override
    public ProducerRecord<String, EmailSendingEvent> onSend(ProducerRecord<String, EmailSendingEvent> record) {
        String traceId = MDC.get("traceId");
        if (traceId != null) {
            record.headers().add("traceId", traceId.getBytes(StandardCharsets.UTF_8));
        }

        String uri = MDC.get("uri");
        if (traceId != null) {
            record.headers().add("uri", uri.getBytes(StandardCharsets.UTF_8));
        }

        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}