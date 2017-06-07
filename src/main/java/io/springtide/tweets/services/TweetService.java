package io.springtide.tweets.services;

import io.springtide.tweets.domain.Tweet;
import io.springtide.tweets.domain.User;
import reactor.core.publisher.Flux;

public interface TweetService {
    Flux<Tweet> streamAllForUser(User user);
}
