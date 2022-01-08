package com.example.madcamp_project2;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.madcamp_project2.Account.AccountInfo;
import com.example.madcamp_project2.Account.AccountLogin;
import com.example.madcamp_project2.Account.CheckUserForm;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private boolean login_state_check;
    private Intent intent;
    private String token;
    private String baseUrl = "https://2779-110-76-108-130.ngrok.io/";
    private MyAPI myapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login_btn = findViewById(R.id.login_btn);
        intent = new Intent(this, MainActivity.class);
        initMyAPI();
/*
        getHashKey();
        if(login_state_check) Log.e("Initial LOGIN: ","TRUE");
        else Log.e("Initial LOGIN: ","FALSE");
*/

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    // 카카오톡이 설치되어 있으면 카톡으로 로그인 확인 요청
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (token, loginError) -> {
                        if (loginError != null) {
                            // 로그인 실패
                            Log.e("LOGIN", "FAILED!");
                            Toast.makeText(LoginActivity.this, "로그인을 할 수 없습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            // 로그인 성공
                            Log.e("LOGIN", "SUCCESS!");
                            get_user_info(false);
                            // 회원가입 필요한 지 확인 --> 회원가입 필요 없으면 바로 activity change
                            // startActivity(intent);
                        }
                        return null;
                    });
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (token, loginError) -> {
                        if (loginError != null) {
                            // 로그인 실패
                            Log.e("LOGIN", "FAILED!");
                            Toast.makeText(LoginActivity.this, "로그인을 할 수 없습니다.", Toast.LENGTH_LONG).show();
                        } else {
                            // 로그인 성공
                            Log.e("LOGIN", "SUCCESS!");
                            get_user_info(false);
                        }
                        return null;
                    });
                }

            }
        });
    }

    public void check_if_login() {
        get_user_info(true);
    }

    public void get_user_info(boolean check) {
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e("USERINFO", "사용자 정보 요청 실패", meError);
                login_state_check = false;
            } else {
                Log.i("USERINFO", "사용자 정보 요청 성공" +
                        "\n닉네임: "+user.getKakaoAccount().getProfile().getNickname() +
                        "\n이메일: "+user.getKakaoAccount().getEmail());
                if(!check) {
                    String email = user.getKakaoAccount().getEmail();
                    String username = user.getKakaoAccount().getProfile().getNickname();
                    long pk = user.getId();

                    intent.putExtra("email", email);
                    intent.putExtra("username", username);
                    is_already_SignedUp(email, pk);
                }
                login_state_check = true;
            }
            return null;
        });
    }

    public boolean is_login() {
        if(AuthApiClient.getInstance().hasToken()) {
            Log.e("asdf","1");
            check_login_more();
            if(login_state_check) {
                Log.e("asdf","2");
                return true;
            }
            else {
                Log.e("asdf","3");
                return false;
            }
        }
        else {
            Log.e("asdf","4");
            return false;
        }
    }

    public void check_login_more() {
        UserApiClient.getInstance().accessTokenInfo((user, error) -> {
            if(error != null) {
                login_state_check = false;
            }
            else {
                login_state_check = true;
            }
            return null;
        });
    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }


    }

    public void kakao_unlink() {
        UserApiClient.getInstance().unlink(error -> {
            return null;
        });
    }

    public void goto_MainActivity() {
        startActivity(intent);
    }

    public boolean is_already_SignedUp(String email, long pk) {
        CheckUserForm checkUserForm = new CheckUserForm(email);
        Call<CheckUserForm> get_check_user = myapi.get_check_user(checkUserForm);
        get_check_user.enqueue(new Callback<CheckUserForm>() {
           @Override
           public void onResponse(Call<CheckUserForm> call, Response<CheckUserForm> response) {
                if(response.isSuccessful()) {
                    Log.d("IS_SIGNEDUP", "SUCCESS");
                    CheckUserForm checkUserForm = response.body();
                    Log.d("UserForm status", checkUserForm.get_status());
                    String status = checkUserForm.get_status();
                    if(status.equals("True")) { // 이미 회원 가입
                        Log.d("Already SingedUp", "True");
                        Login(email, pk);
                    }
                    else if(status.equals("False")) { // 회원 가입 필요
                        Log.d("Already SingedUp", "False");
                        SignUp(email, pk);
                    }
                } else {
                    Log.d("IS_SIGNEDUP", "FAILED");
                }
           }

           @Override
           public void onFailure(Call<CheckUserForm> call, Throwable t) {
               Log.d("IS_SIGNEDUP", "FAILED");
           }
        });
        return false;
    }

    public void SignUp(String email, long kakao_pk) {
        AccountInfo accountInfo = new AccountInfo(email, String.valueOf(kakao_pk));
        Call<AccountInfo> post_account = myapi.post_account(accountInfo);
        post_account.enqueue(new Callback<AccountInfo>() {
            @Override
            public void onResponse(Call<AccountInfo> call, Response<AccountInfo> response) {
                if(response.isSuccessful()) {
                    Log.d("SIGNUP", "SUCCESS");
                    Login(email, kakao_pk);
                }
                else {
                    Log.d("SIGNUP", "FAILED");
                }
            }

            @Override
            public void onFailure(Call<AccountInfo> call, Throwable t) {
                Log.d("SIGNUP", "FAILED");
            }
        });
    }

    public void Login(String email, long kakao_pk) {
        // 로그인하고, authentication을 위한 access token GET
        AccountLogin accountLogin = new AccountLogin(email, String.valueOf(kakao_pk));
        Call<AccountLogin> post_login = myapi.post_login(accountLogin);
        post_login.enqueue(new Callback<AccountLogin>() {
            @Override
            public void onResponse(Call<AccountLogin> call, Response<AccountLogin> response) {
                if(response.isSuccessful()) {
                    Log.d("LOGIN", "SUCCESS");
                    AccountLogin accountLogin = response.body();
                    Log.e("Token: ", accountLogin.getToken());
                    token = accountLogin.getToken();

                    intent.putExtra("token", token);
                    goto_MainActivity();
                }
                else {
                    Log.d("LOGIN", "FAILED");
                }
            }

            @Override
            public void onFailure(Call<AccountLogin> call, Throwable t) {
                Log.d("LOGIN", "FAILED");
            }
        });
    }

    private void initMyAPI(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myapi = retrofit.create(MyAPI.class);
    }
}
