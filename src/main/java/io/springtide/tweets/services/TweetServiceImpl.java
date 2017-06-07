package io.springtide.tweets.services;

import io.springtide.tweets.domain.Tweet;
import io.springtide.tweets.domain.TweetRepository;
import io.springtide.tweets.domain.User;
import io.springtide.tweets.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class TweetServiceImpl implements TweetService {

    private TweetRepository tweetRepository;
    private UserRepository userRepository;

    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Flux<Tweet> streamAllForUser(User user) {
        return userRepository
                .streamFollowedUsers(user)
                .flatMap(aUser ->
                        tweetRepository.streamByUser(aUser));
    }
}
