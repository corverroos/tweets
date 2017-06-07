package io.springtide.tweets.util;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserCSVGeneratorTest {

    @Test
    public void testHappyFlow() throws Exception {
        Map<String,Set<String>> expected = new HashMap<>();
        expected.put("one", Sets.newTreeSet("one", "two", "three"));
        expected.put("two", Sets.newTreeSet("three"));
        assertEquals(
                expected,
                UserCSVGenerator.generateUsersWithFollowed("test_user.txt"));
    }

    @Test
    public void testValids() throws Exception {
        assertEquals(Pair.of("follows", Sets.newTreeSet("follows follows")), UserCSVGenerator.parseLine("follows follows follows follows"));
        assertEquals(Pair.of("follows", Sets.newTreeSet("follows")), UserCSVGenerator.parseLine("follows follows follows, follows"));
        assertEquals(Pair.of("follows", Sets.newTreeSet("follows")), UserCSVGenerator.parseLine("follows followsfollows"));
        assertEquals(Pair.of("follows", Sets.newTreeSet("follows")), UserCSVGenerator.parseLine("follows followsfollows,"));
        assertEquals(Pair.of("follows", Sets.newTreeSet()), UserCSVGenerator.parseLine("follows"));
        assertEquals(Pair.of("\"ᚠᛇᚻ\tᛒᛦᚦ", Sets.newTreeSet("ᚠᚱᚩᚠᚢ\nᚱ","ᚠᛁᚱᚪ")), UserCSVGenerator.parseLine("\"ᚠᛇᚻ\tᛒᛦᚦ followsᚠᚱᚩᚠᚢ\nᚱ,ᚠᛁᚱᚪ"));
    }

    @Test
    public void testInvalids() throws Exception {
        assertNull(UserCSVGenerator.parseLine(null));
        assertNull(UserCSVGenerator.parseLine(""));
        assertNull(UserCSVGenerator.parseLine(" "));
        assertNull(UserCSVGenerator.parseLine("\n"));
        assertNull(UserCSVGenerator.parseLine(" follows "));
        assertNull(UserCSVGenerator.parseLine(" follows a, b"));
        assertNull(UserCSVGenerator.parseLine(" follows a, b"));

    }
}