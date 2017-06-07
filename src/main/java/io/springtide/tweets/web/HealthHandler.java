package io.springtide.tweets.web;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class HealthHandler {

    public Mono<ServerResponse> healthCheck(ServerRequest serverRequest) {
        return ok().contentType(TEXT_PLAIN)
                .body(BodyInserters.fromObject("Healthy!"));
    }
}
