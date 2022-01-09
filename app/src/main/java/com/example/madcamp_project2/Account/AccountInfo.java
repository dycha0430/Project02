package com.example.madcamp_project2.Account;

public class AccountInfo {
    private String email;
    private String password;
    private String username;
    private String profile_image;

    public AccountInfo(String email, String password, String username, String profile_image) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.profile_image = profile_image;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getImage_profile() {
        return this.profile_image;
    }
}
