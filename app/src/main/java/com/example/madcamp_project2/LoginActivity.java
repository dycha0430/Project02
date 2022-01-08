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

        // getHashKey();
        if(login_state_check) Log.e("Initial LOGIN: ","TRUE");
        else Log.e("Initial LOGIN: ","FALSE");

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_if_login();

                if (login_state_check) {
                    // 이미 로그인 되어 있음
                    Log.e("LOGIN", "ALREADY SIGNED IN!");
                    get_user_info(false);
                    startActivity(intent);
                } else {
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
                                startActivity(intent);
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
                                startActivity(intent);
                            }
                            return null;
                        });
                    }
                }
            }
        });
/*

        Button logout_btn = findViewById(R.id.kakao_logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(error -> {
                    if (error != null) {
                        Log.e("LOGOUT", "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                    }else{
                        Log.e("LOGOUT", "로그아웃 성공, SDK에서 토큰 삭제됨");
                        kakao_unlink();
                    }
                    return null;
                });
            }
        });

        Button status_btn = findViewById(R.id.check_status);
        status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_user_info();
            }
        });
*/
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
                    intent.putExtra("email", user.getKakaoAccount().getEmail());
                    intent.putExtra("username", user.getKakaoAccount().getProfile().getNickname());
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

    public boolean is_already_SignedUp(String email) {
        Call<CheckUserForm> get_check_user = myapi.get_check_user("Bearer "+token);
        get_check_user.enqueue(new Callback<CheckUserForm>() {
           @Override
           public void onResponse(Call<CheckUserForm> call, Response<CheckUserForm> response) {
                if(response.isSuccessful()) {
                    Log.d("IS_SIGNEDUP", "SUCCESS");
                    CheckUserForm checkUserForm = response.body();
                    Log.d("UserForm status", checkUserForm.get_status());
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

    public void SignUp(String email, int kakao_pk) {

    }

    public void Login(String email, int kakao_pk) {
        // 로그인하고, authentication을 위한 access token 리턴
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
