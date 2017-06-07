package io.springtide.tweets.runners;

import io.springtide.tweets.domain.User;
import io.springtide.tweets.domain.UserRepository;
import io.springtide.tweets.util.UserCSVGenerator;
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

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Order(100)
public class UserApplicationRunner implements ApplicationRunner{
    private static final Logger LOG = LoggerFactory.getLogger(UserApplicationRunner.class);

    private UserRepository userRepository;
    private String usersFileName;

    @Autowired
    public UserApplicationRunner(
            UserRepository userRepository,
            @Value("${runners.usersFileName}") String usersFileName) {
        this.userRepository = userRepository;
        this.usersFileName = usersFileName;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        if (StringUtils.isEmpty(usersFileName)) {
            LOG.info("Skipping UserApplicationRunner, userFileName undefined");
            return;
        }

        UserCSVGenerator.generateUsersWithFollowed(usersFileName).entrySet()
                .stream()
                .map(this::mapStringsToUsers)
                .filter(Objects::nonNull)
                .forEach(pair -> pair.getValue()
                        .forEach(followedUser -> {
                            addFollower(pair.getKey(), followedUser);
                        }));
    }

    private void addFollower(User follower, User followedUser) {
        try {
            userRepository.addFollower(followedUser, follower);
        } catch (Exception e) {
            LOG.warn("Failed saving follower, user=" + follower + ", followedUser=" + followedUser, e);
        }
    }

    private Pair<User, Set<User>> mapStringsToUsers(Map.Entry<String, Set<String>> entry) {
        User user = getOrCreateUser(entry.getKey());
        if (user == null) {
            return null;
        }
        return Pair.of(user, entry.getValue()
                .stream()
                .map(this::getOrCreateUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
    }

    private User getOrCreateUser(String name) {
        User user = userRepository.findByName(name).block();
        if (user != null) return user;
        try {
            User newUser = new User(name);
            userRepository.save(newUser);
            return newUser;
        } catch (Exception e) {
            LOG.warn("Failed saving user, " + name, e);
            return null;
        }
    }
}
