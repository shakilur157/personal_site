package com.sazzadur.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    private String username;
    private String password;
    private Set<String> role;


    public SignupRequest(String name, String password, Set<String> role) {
        this.username = name;
        this.password = password;
        this.role = role;
    }
}
