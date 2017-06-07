package io.springtide.tweets.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class User implements Comparable<User>{

    @NotNull
    private String name;

    public User(String name) {
        this.name = name;
    }


    @Override
    public int compareTo(User o) {
        if (this.name == null || o.name == null) {
            throw new IllegalArgumentException("Cannot compare Users with null names, this=" +this + ", other=" + o);
        }
        return this.name.compareTo(o.getName());
    }
}
