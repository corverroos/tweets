package io.springtide.tweets.util;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TweetCSVGeneratorTest {
    @Test
    public void testHappyFlow() throws Exception {
        assertEquals(
                Lists.newArrayList(
                        Pair.of("one","text"),
                        Pair.of("two with space","more text"),
                        Pair.of("one","text with '>'")),
                TweetCSVGenerator.generateTweets("test_tweet.txt"));
    }

    @Test
    public void testValids() throws Exception {
        assertEquals(Pair.of(">>",">>>"),TweetCSVGenerator.parseLine(">>> >>>"));
        assertEquals(Pair.of("s","> > >"),TweetCSVGenerator.parseLine("  s  > > > >"));
        assertEquals(Pair.of("ᚠᛇᚻ\tᛒᛦᚦ","ᚠᚱᚩᚠᚢᚱ\nᚠᛁᚱᚪ"),TweetCSVGenerator.parseLine("ᚠᛇᚻ\tᛒᛦᚦ > ᚠᚱᚩᚠᚢᚱ\nᚠᛁᚱᚪ"));
    }

    @Test
    public void testInvalids() throws Exception {
        assertNull(TweetCSVGenerator.parseLine(null));
        assertNull(TweetCSVGenerator.parseLine(""));
        assertNull(TweetCSVGenerator.parseLine(" "));
        assertNull(TweetCSVGenerator.parseLine(">"));
        assertNull(TweetCSVGenerator.parseLine("> "));
        assertNull(TweetCSVGenerator.parseLine("> ttt"));
        assertNull(TweetCSVGenerator.parseLine("aa> "));
    }
}