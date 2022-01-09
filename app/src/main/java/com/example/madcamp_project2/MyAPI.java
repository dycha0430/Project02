package com.example.madcamp_project2;

import com.example.madcamp_project2.Account.AccountInfo;
import com.example.madcamp_project2.Account.AccountLogin;
import com.example.madcamp_project2.Account.CheckUserForm;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewTravel;
import com.example.madcamp_project2.ui.home.addtrip.Travel.userTravel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MyAPI {
    @GET("madcamp/check/")
    Call<CheckUserForm> get_check_user(@Query("params1") String email);

    @POST("madcamp/login/")
    Call<AccountLogin> post_login(@Body AccountLogin accountLogin);

    @POST("madcamp/signup/")
    Call<AccountInfo> post_account(@Body AccountInfo accountInfo);

    @POST("madcamp/travels/")
    Call<NewTravel> post_travel(@Header("Authorization") String token, @Body NewTravel newTravel);

    @GET("madcamp/travels/get/")
    Call<userTravel> get_userTravel(@Header("Authorization") String token, @Query("params1") String email);
}
