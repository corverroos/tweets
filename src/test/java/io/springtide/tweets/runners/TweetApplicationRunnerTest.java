package io.springtide.tweets.runners;

import io.springtide.tweets.domain.TweetRepository;
import io.springtide.tweets.domain.User;
import io.springtide.tweets.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TweetApplicationRunnerTest {

    @Mock
    TweetRepository tweetRepository;

    @Mock
    UserRepository userRepository;

    @Test
    public void happyFlow() throws Exception {
        when(userRepository.findByName(any())).thenAnswer(invocationOnMock -> Mono.just(new User(invocationOnMock.getArgument(0))));
        new TweetApplicationRunner(tweetRepository, userRepository, "test_tweet.txt").run(null);
        verify(tweetRepository, times(3)).save(any());
    }

    @Test
    public void skipped() throws Exception {
        new TweetApplicationRunner(tweetRepository, userRepository, null).run(null);
        verify(tweetRepository, never()).save(any());
    }
}