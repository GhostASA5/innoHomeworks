package org.project.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.project.dto.KafkaMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "my-topic", groupId = "messages")
    public void listen(ConsumerRecord<String, KafkaMessage> record) {
        log.info("Received message: " + record.value());
    }
}
