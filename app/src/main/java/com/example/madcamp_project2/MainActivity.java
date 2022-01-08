package com.example.madcamp_project2;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcamp_project2.databinding.ActivityMainBinding;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private boolean login_state_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getHashKey();

        get_user_info();

        if(login_state_check) Log.e("Initial LOGIN: ","TRUE");
        else Log.e("Initial LOGIN: ","FALSE");

        Button login_btn = findViewById(R.id.kakao_login);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                    // 카카오톡이 설치되어 있으면 카톡으로 로그인 확인 요청
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, (token, loginError) -> {
                        if (loginError != null) {
                            // 로그인 실패
                        } else {
                            // 로그인 성공
                            // 사용자 정보 요청
                            Log.e("LOGIN", "SUCCESS!");
                        }
                        return null;
                    });
                } else {
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, (token, loginError) -> {
                        if (loginError != null) {
                            // 로그인 실패
                        } else {
                            // 로그인 성공
                            // 사용자 정보 요청
                            Log.e("LOGIN", "SUCCESS!");
                        }
                        return null;
                    });
                }
            }
        });

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
    }

    public void kakao_unlink() {
        UserApiClient.getInstance().unlink(error -> {
            return null;
        });
    }

    public void get_user_info() {
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e("USERINFO", "사용자 정보 요청 실패", meError);
                login_state_check = false;
            } else {
                Log.i("USERINFO", "사용자 정보 요청 성공" +
                        "\n닉네임: "+user.getKakaoAccount().getProfile().getNickname() +
                        "\n이메일: "+user.getKakaoAccount().getEmail());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
}