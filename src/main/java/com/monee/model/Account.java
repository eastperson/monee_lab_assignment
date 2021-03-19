package com.monee.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monee.security.Jwt;
import org.mindrot.jbcrypt.BCrypt;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Account {

    private Long seq;
    private final String email;
    private final String nickname;
    private final String password;
    //private Set<Role> roles;
    private LocalDateTime createAt;

    public Account(String email, String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public static enum Role {
        USER, ADMIN
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return seq.equals(account.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return "Account{" +
                "seq=" + seq +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    public String newJwt(Jwt jwt) {
        Jwt.Claims claims = Jwt.Claims.of(this.seq,this);
        return jwt.create(claims);
    }

    public void login(String credentials) {
        if (!BCrypt.checkpw(credentials, password)) {
            throw new IllegalArgumentException("Bad credential");
        }
    }
}
