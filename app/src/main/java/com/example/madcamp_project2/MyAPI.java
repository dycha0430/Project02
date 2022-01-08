package com.example.madcamp_project2;

import com.example.madcamp_project2.Account.AccountLogin;
import com.example.madcamp_project2.Account.CheckUserForm;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MyAPI {
    @GET("madcamp/check/")
    Call<CheckUserForm> get_check_user(@Header("Authorization") String token);

    @POST("madcamp/login/")
    Call<AccountLogin> post_login(@Body AccountLogin accountLogin);
}
