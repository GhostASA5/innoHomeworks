package org.project.functional.endpoint;

import org.project.service.KafkaHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class KafkaRouter {

    @Bean
    public RouterFunction<ServerResponse> routes(KafkaHandler handler) {
        return route(POST("/kafka/send"), handler::sendMessages);
    }
}