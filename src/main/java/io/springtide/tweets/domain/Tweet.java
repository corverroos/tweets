package io.springtide.tweets.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class Tweet {

    private Long id;

    @NotNull
    private User user;

    @NotNull
    @Size(min=1, max = 140)
    private String message;

    public Tweet(User user, String message) {
        this.user = user;
        this.message = message;
    }
}
