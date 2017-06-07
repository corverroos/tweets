package io.springtide.tweets.runners;

import io.springtide.tweets.domain.Tweet;
import io.springtide.tweets.domain.TweetRepository;
import io.springtide.tweets.domain.User;
import io.springtide.tweets.domain.UserRepository;
import io.springtide.tweets.util.TweetCSVGenerator;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
@Order(101)
public class TweetApplicationRunner implements ApplicationRunner{
    private static final Logger LOG = LoggerFactory.getLogger(TweetApplicationRunner.class);

    private TweetRepository tweetRepository;
    private UserRepository userRepository;
    private String tweetsFileName;

    @Autowired
    public TweetApplicationRunner(
            TweetRepository tweetRepository,
            UserRepository userRepository,
            @Value("${runners.tweetsFileName}") String tweetsFileName) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
        this.tweetsFileName = tweetsFileName;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (StringUtils.isEmpty(tweetsFileName)) {
            LOG.info("Skipping TweetApplicationRunner, tweetsFileName undefined");
            return;
        }
        TweetCSVGenerator.generateTweets(tweetsFileName)
                .stream()
                .map(this::getTweet)
                .filter(Objects::nonNull)
                .forEach(this::saveTweet);
    }

    private void saveTweet(Tweet tweet) {
        try {
            tweetRepository.save(tweet);
        } catch (Exception e) {
            LOG.warn("Failed saving tweet, " + tweet, e);
        }
    }

    private Tweet getTweet(Pair<String, String> pair) {
        User user = userRepository.findByName(pair.getKey()).block();
        if (user == null){
            LOG.warn("User of tweet not found, " + pair.getKey());
            return null;
        }
        return new Tweet(user, pair.getValue());
    }
}
