package io.springtide.tweets.domain;


import reactor.core.publisher.Flux;

public interface TweetRepository {

    Flux<Tweet> streamByUser(User user);
    void save(Tweet user);
}
