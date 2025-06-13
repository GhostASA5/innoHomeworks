package org.project.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.model.Transaction;
import org.project.service.TransactionSecurityService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionListener {

    private final TransactionSecurityService transactionSecurityService;

    @KafkaListener(id = "${kafka.consumer.group-id}",
            topics = "${kafka.topics.transactionTopic}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload Transaction event,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) String key) {

        log.info("Transaction consumer: получено новое сообщение event - {}, topic - {}, key - {}", event, topic, key);
        try {
            transactionSecurityService.secureTransaction(event);
        } catch (Exception e) {
            log.error("Ошибка при обработке события {}: {}", event, e.getMessage(), e);
        }
        log.info("Transaction consumer: записи обработаны");
    }
}
