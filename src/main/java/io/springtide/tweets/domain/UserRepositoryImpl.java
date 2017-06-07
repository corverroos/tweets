package io.springtide.tweets.domain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component()
public class UserRepositoryImpl implements UserRepository, InitializingBean{

    private static Validator validator;
    private Map<User, Set<User>> followedUsersByUser = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public Flux<User> streamAll() {
        return Flux.fromStream(followedUsersByUser.keySet().stream().sorted());
    }

    @Override
    public Flux<User> streamFollowedUsers(User user) {
        return Flux.fromStream(followedUsersByUser.get(user).stream());
    }

    @Override
    public void save(User user) {
        if (user == null) return;
        validateUser(user);
        Set<User> followedByUsers = followedUsersByUser.get(user);

        if (followedByUsers == null) {
            followedByUsers = new HashSet<>();
            // User always follows itself
            followedByUsers.add(user);
        }

        followedUsersByUser.put(user, followedByUsers);
    }

    @Override
    public void addFollower(User user, User follower) {
        if (user == null || follower == null) return;
        validateUser(user);
        validateUser(follower);

        if (!followedUsersByUser.containsKey(user)){
            throw new IllegalArgumentException("User not found, " + user);
        } else if (!followedUsersByUser.containsKey(follower)) {
            throw new IllegalArgumentException("Follower not found, " + follower);
        }

        followedUsersByUser.get(follower).add(user);
    }

    @Override
    public Mono<User> findByName(String name) {
        return Mono.justOrEmpty(followedUsersByUser.keySet().stream().filter(user -> user.getName().equals(name)).findFirst());
    }

    private void validateUser(User user) {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}
