package io.springtide.tweets.runners;

import io.springtide.tweets.domain.User;
import io.springtide.tweets.domain.UserRepositoryImpl;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UserApplicationRunnerTest {

    @SpyBean
    private UserRepositoryImpl userRepository;

    @Test
    public void happyFlow() throws Exception {
        new UserApplicationRunner(userRepository, "test_user.txt").run(null);
        verify(userRepository, times(3)).save(any());
        verify(userRepository, times(4)).addFollower(any(), any());

        assertEquals(Sets.newTreeSet("one", "two", "three"), userRepository.streamAll().map(User::getName).collect(Collectors.toSet()).block());
        assertEquals(Sets.newTreeSet("one", "two", "three"), userRepository.streamFollowedUsers(new User("one")).map(User::getName).collect(Collectors.toSet()).block());
        assertEquals(Sets.newTreeSet("two", "three"),        userRepository.streamFollowedUsers(new User("two")).map(User::getName).collect(Collectors.toSet()).block());
        assertEquals(Sets.newTreeSet("three"),               userRepository.streamFollowedUsers(new User("three")).map(User::getName).collect(Collectors.toSet()).block());

    }

    @Test
    public void skipped() throws Exception {
        new UserApplicationRunner(userRepository, null).run(null);
        verify(userRepository, never()).save(any());
    }
}