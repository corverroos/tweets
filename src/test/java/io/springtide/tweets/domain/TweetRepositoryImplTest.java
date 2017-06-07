package io.springtide.tweets.domain;

import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.*;

public class TweetRepositoryImplTest {
    private static final String MAX_MESSAGE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque interdum rutrum sodales. Nullam mattis fermentum libero, non volutpat.";

    private TweetRepositoryImpl tweetRepo;

    @Before
    public void setUp() throws Exception {
        tweetRepo = new TweetRepositoryImpl();
        tweetRepo.afterPropertiesSet();
    }

    @Test
    public void testValidTweets() throws Exception {
        tweetRepo.save(new Tweet(new User("name"), "message"));
        tweetRepo.save(new Tweet(new User("name"), MAX_MESSAGE));
        tweetRepo.save(new Tweet(new User("name"), " "));
    }

    @Test
    public void testInvalidTweets() throws Exception {
        assertInvalid(() -> tweetRepo.save(new Tweet(null, "message")));
        assertInvalid(() -> tweetRepo.save(new Tweet(new User("name"), "!" + MAX_MESSAGE)));
        assertInvalid(() -> tweetRepo.save(new Tweet(new User("name"), "")));
        assertInvalid(() -> tweetRepo.save(new Tweet(new User("name"), null)));
    }

    private static void assertInvalid(Runnable runnale){
        try {
            runnale.run();
            fail("should have thrown");
        } catch (ConstraintViolationException e){
            assertNotNull(e);
        }
    }
}