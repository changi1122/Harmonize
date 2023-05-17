package kr.ac.chungbuk.harmonize.model;

import com.google.gson.annotations.Expose;

import java.time.OffsetDateTime;

public class Token {
    @Expose
    private String token;
    private OffsetDateTime createdAt;

    public Token(String token) {
        this.token = token;
        createdAt = OffsetDateTime.now();
    }

    public Token(String token, String createdAt) {
        this.token = token;
        this.createdAt = OffsetDateTime.parse(createdAt);
    }

    public String getToken() {
        return token;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isExpired() {
        return createdAt.isAfter(createdAt.plusDays(25));
    }
}
