package com.example.madcamp_project2.ui.friends.Friend;

public class FriendRequest {
    private String from_user_email;
    private String to_user_email;
    private String status;

    public FriendRequest(String from_user_email, String to_user_email) {
        this.from_user_email = from_user_email;
        this.to_user_email = to_user_email;
        this.status = "True";
    }

    public String getStatus() {
        return status;
    }
}
