package io.springtide.tweets;

import io.springtide.tweets.domain.Tweet;
import io.springtide.tweets.domain.User;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweetsApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void contextLoads() {}

    @Test
    public void exampleClientOutput() {
        List<User> users = Lists.newArrayList();
        webTestClient
                .get()
                .uri("/users")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON)
                .expectBodyList(User.class)
                .consumeWith(users::addAll);

        StringBuffer sb = new StringBuffer();

        users.forEach(user -> {
            sb.append(user.getName()).append("\n\n");
            List<Tweet> tweets = Lists.newArrayList();

            webTestClient
                    .get()
                    .uri("/tweets/" + user.getName())
                    .accept(MediaType.APPLICATION_STREAM_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON)
                    .expectBodyList(Tweet.class)
                    .consumeWith(tweets::addAll);

            tweets.stream()
                    .sorted((o1, o2) -> Long.compare(o1.getId(), o2.getId()))
                    .forEach(tweet -> sb.append("\t@").append(tweet.getUser().getName()).append(": ").append(tweet.getMessage()).append("\n"));
            sb.append("\n");
        });

        System.out.println("********* Example Client Output: start *****************");
        System.out.println(sb.toString());
        System.out.println("********* Example Client Output: end *****************");

        assertEquals(expectedOutput, sb.toString());
    }

    private static String expectedOutput = "Alan\n" +
            "\n" +
            "\t@Alan: If you have a procedure with 10 parameters, you probably missed some.\n" +
            "\t@Alan: Random numbers should not be generated with a method chosen at random.\n" +
            "\n" +
            "Martin\n" +
            "\n" +
            "\n" +
            "Ward\n" +
            "\n" +
            "\t@Alan: If you have a procedure with 10 parameters, you probably missed some.\n" +
            "\t@Ward: There are only two hard things in Computer Science: cache invalidation, naming things and off-by-1 errors.\n" +
            "\t@Alan: Random numbers should not be generated with a method chosen at random.\n" +
            "\n";
}
