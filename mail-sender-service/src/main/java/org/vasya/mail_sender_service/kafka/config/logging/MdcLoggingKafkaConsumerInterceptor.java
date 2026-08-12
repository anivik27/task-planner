package org.vasya.mail_sender_service.kafka.config.logging;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.MDC;
import org.springframework.kafka.listener.RecordInterceptor;
import org.springframework.lang.Nullable;
import org.vasya.mail_sender_service.kafka.contract.EmailSendingEvent;

import java.nio.charset.StandardCharsets;

public class MdcLoggingKafkaConsumerInterceptor implements RecordInterceptor<String, EmailSendingEvent> {

    @Nullable
    @Override
    public ConsumerRecord<String, EmailSendingEvent> intercept(ConsumerRecord<String,
                                                               EmailSendingEvent> record, Consumer<String,
                                                               EmailSendingEvent> consumer) {
        Header headerTraceId = record.headers().lastHeader("traceId");
        if (headerTraceId != null) {
            String traceId = new String(headerTraceId.value(), StandardCharsets.UTF_8);
            MDC.put("traceId", traceId);
        }

        Header headerUri = record.headers().lastHeader("uri");
        if (headerTraceId != null) {
            String uri = new String(headerUri.value(), StandardCharsets.UTF_8);
            MDC.put("uri", uri);
        }

        return record;
    }

    @Override
    public void afterRecord(ConsumerRecord<String, EmailSendingEvent> record, Consumer<String, EmailSendingEvent> consumer) {
        MDC.remove("traceId");
        MDC.remove("uri");
    }
}