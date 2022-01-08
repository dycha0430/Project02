package com.example.madcamp_project2.Account;

public class CheckUserForm {
    private String email;
    private String status;

    public CheckUserForm (String email) {
        this.email = email;
        this.status = "";
    }
    public String get_status() {
        return status;
    }
}
