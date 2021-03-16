package com.monee.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter @ToString
public class Account {

    private Long seq;
    private final String email;
    private final String nickname;
    private final String password;
    //private Set<Role> roles;
    private LocalDate createAt;

    public Account(String email, String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getCreateAt(){
        return this.createAt;
    }

    public static enum Role {
        USER, ADMIN
    }
}
