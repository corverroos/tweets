package io.springtide.tweets.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(TweetHandler tweetHandler, UserHandler userHandler, HealthHandler healthHandler) {
        return RouterFunctions
                .route(GET("/health").and(accept(TEXT_PLAIN)), healthHandler::healthCheck)
                .andRoute(GET("/tweets/{name}").and(accept(APPLICATION_STREAM_JSON)), tweetHandler::streamForUser)
                .andRoute(GET("/users").and(accept(APPLICATION_STREAM_JSON)), userHandler::streamAll);
    }
}