package io.springtide.tweets.web;

import io.springtide.tweets.domain.Tweet;
import io.springtide.tweets.domain.User;
import io.springtide.tweets.domain.UserRepository;
import io.springtide.tweets.services.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TweetHandler {

    private TweetService tweetService;
    private UserRepository userRepository;

    @Autowired
    public TweetHandler(TweetService tweetService, UserRepository userRepository) {
        this.tweetService = tweetService;
        this.userRepository = userRepository;
    }

    public Mono<ServerResponse> streamForUser(ServerRequest serverRequest) {
        String name = serverRequest.pathVariable("name");
        Mono<User> userMono = userRepository.findByName(name);
        return userMono.flatMap(user -> {
                Flux<Tweet> tweetFlux = tweetService.streamAllForUser(user);
                return ok().contentType(APPLICATION_STREAM_JSON).body(tweetFlux, Tweet.class);
            }).switchIfEmpty(ServerResponse.notFound().build());
    }
}
