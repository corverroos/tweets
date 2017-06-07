package io.springtide.tweets.domain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.validation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class TweetRepositoryImpl implements TweetRepository, InitializingBean{

    private static Validator validator;
    private Map<User, List<Tweet>> tweetsByUser = new HashMap<>();
    private AtomicLong idGenerator = new AtomicLong();
    @Override
    public Flux<Tweet> streamByUser(User user) {
        if (!tweetsByUser.containsKey(user)) {
            return Flux.empty();
        }
        return Flux.fromStream(tweetsByUser.get(user).stream());
    }

    @Override
    public void save(Tweet tweet) {
        if (tweet == null) return;

        Set<ConstraintViolation<Tweet>> constraintViolations = validator.validate(tweet);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        tweet.setId(idGenerator.incrementAndGet());

        List<Tweet> tweets = tweetsByUser.get(tweet.getUser());

        if (tweets == null) {
            tweets = new ArrayList<>();
            tweetsByUser.put(tweet.getUser(), tweets);
        }

        tweets.add(tweet);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
}
