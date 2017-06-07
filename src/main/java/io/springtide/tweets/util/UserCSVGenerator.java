package io.springtide.tweets.util;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class UserCSVGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(UserCSVGenerator.class);
    private static final String FIRST_SPLIT = " follows";
    private static final String SECOND_SPLIT = ",";

    public static Map<String,Set<String>> generateUsersWithFollowed(String classPathFileName) throws IOException {
        return Files.lines(Paths.get(new ClassPathResource(classPathFileName).getURI()), StandardCharsets.UTF_8)
                .map(UserCSVGenerator::parseLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Pair::getKey,
                        Pair::getValue,
                        (set1, set2) -> {set1.addAll(set2); return set1;}
                ));
    }

    //@VisibleForTesting
    static Pair<String, Set<String>> parseLine(String line) {
        if (StringUtils.isEmpty(line) || StringUtils.isEmpty(line.trim())) return null;

        String[] firstSplit = line.split(FIRST_SPLIT, 2);

        if (firstSplit.length > 2 || firstSplit.length < 1) {
            LOG.error("Invalid split result (how is this possible) while parsing user csv line: {}", line);
            return null;
        }

        String user = firstSplit[0].trim();

        if (StringUtils.isEmpty(user)) {
            LOG.warn("Skipping empty user csv line: {}", line);
            return null;
        }

        Set<String> followedUserSet = new HashSet<>();

        if (firstSplit.length > 1) {
            String[] followedUsers = firstSplit[1].trim().split(SECOND_SPLIT);
            followedUserSet.addAll(
                    Arrays.stream(followedUsers)
                            .map(String::trim)
                            .filter(str -> !StringUtils.isEmpty(str))
                            .collect(Collectors.toSet()));
        }

        return Pair.of(user, followedUserSet);
    }
}
