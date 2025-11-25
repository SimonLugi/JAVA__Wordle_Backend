package net.ict_campus.Wordle.security.auth;

import lombok.Getter;

@Getter
public class Jwt {
    private String token;
    private long expiresIn;
    private int score;

    //Sets the JWT Token
    public Jwt setToken(String token) {
        this.token = token;
        return this; // Enable method chaining
    }

    //Sets the espiration
    public Jwt setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this; // Enable method chaining
    }

    public Jwt setScore(int id) {
        this.score = getScore();
        return this;
    }
}
