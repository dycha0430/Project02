package com.example.madcamp_project2.Account;

public class AccountLogin {
    private String email;
    private String password;
    private String token;

    public AccountLogin(String email, String password) {
        this.email = email;
        this.password = password;
        token = "";
    }

    public String getToken() {
        return this.token;
    }
}