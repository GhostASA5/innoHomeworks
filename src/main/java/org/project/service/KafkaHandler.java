package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.dto.KafkaMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaHandler {

    private final KafkaSender<String, KafkaMessage> kafkaSender;

    public Mono<ServerResponse> sendMessages(ServerRequest request) {
        int count = request.queryParam("count").map(Integer::parseInt).orElse(10);

        return kafkaSender.send(Flux.range(1, count)
                        .map(i -> {
                            KafkaMessage message = new KafkaMessage();
                            message.setId((long) i);
                            message.setMessage("Message " + i);
                            message.setSendTime(LocalDateTime.now());

                            return SenderRecord.create("my-topic", null, null, UUID.randomUUID().toString(), message, null);
                        })
                        .doOnNext(record -> System.out.println("Sending message: " + record.value())))
                .then()
                .then(Mono.defer(() -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue("Messages sent successfully")));
    }
}
