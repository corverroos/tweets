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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TweetCSVGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(TweetCSVGenerator.class);
    private static final String SPLIT = "> ";

    public static List<Pair<String, String>> generateTweets(String classPathFileName) throws IOException {
        return Files.lines(Paths.get(new ClassPathResource(classPathFileName).getURI()), StandardCharsets.UTF_8)
                .map(TweetCSVGenerator::parseLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    //@VisibleForTesting
    static Pair<String, String> parseLine(String line) {
        if (StringUtils.isEmpty(line) || StringUtils.isEmpty(line.trim())) return null;

        String[] firstSplit = line.split(SPLIT, 2);

        if (firstSplit.length > 2 || firstSplit.length < 1) {
            LOG.error("Invalid split result (how is this possible) while parsing tweet csv line: {}", line);
            return null;
        } else if (firstSplit.length == 1) {
            LOG.warn("Skipping empty user/tweet in tweet csv line: {}", line);
            return null;
        }

        String user = firstSplit[0].trim();
        String tweet = firstSplit[1].trim();
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(tweet)) {
            LOG.warn("Skipping empty user/tweet in tweet csv line: {}", line);
            return null;
        }

        return Pair.of(user, tweet);
    }
}
