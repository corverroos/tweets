package io.springtide.tweets.domain;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Flux<User> streamAll();
    Flux<User> streamFollowedUsers(User user);
    void save(User user);
    void addFollower(User user, User follower);
    Mono<User> findByName(String name);
}
