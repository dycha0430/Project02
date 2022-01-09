package com.example.madcamp_project2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import com.example.madcamp_project2.ui.Country;
import com.example.madcamp_project2.ui.CountryEnum;
import com.example.madcamp_project2.ui.home.addtrip.AddTripPlanActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcamp_project2.databinding.ActivityMainBinding;
import com.kakao.sdk.auth.AuthApiClient;
import com.kakao.sdk.user.UserApiClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private Intent intent_login;
    private static String file_path;
    static final int COUNTRY_NUM = 14;
    public static Country[] COUNTRIES = new Country[COUNTRY_NUM];

    void initCountries() {
        COUNTRIES[0] = new Country(CountryEnum.SEOUL);
        COUNTRIES[1] = new Country(CountryEnum.INCHEON);
        COUNTRIES[2] = new Country(CountryEnum.BUSAN);
        COUNTRIES[3] = new Country(CountryEnum.JEJU);
        COUNTRIES[4] = new Country(CountryEnum.GYEONGGI);
        COUNTRIES[5] = new Country(CountryEnum.POHANG);
        COUNTRIES[6] = new Country(CountryEnum.GANGNEUNG);
        COUNTRIES[7] = new Country(CountryEnum.SOKCHO);
        COUNTRIES[8] = new Country(CountryEnum.DAEGU);
        COUNTRIES[9] = new Country(CountryEnum.GYEONGJU);
        COUNTRIES[10] = new Country(CountryEnum.YEOSU);
        COUNTRIES[11] = new Country(CountryEnum.JEONJU);
        COUNTRIES[12] = new Country(CountryEnum.CHUNCHEON);
        COUNTRIES[13] = new Country(CountryEnum.DAEJEON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        file_path = getFilesDir().getAbsolutePath().toString();
        Log.e("file_path: ", file_path);
        Intent intent = getIntent();

        if(intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            if(bundle.getString("email") != null && bundle.getString("username") != null && bundle.getString("token") != null) {
                String email = intent.getExtras().getString("email");
                String username = intent.getExtras().getString("username");
                String token = intent.getExtras().getString("token");
                getIntent().getExtras().clear();

//                Log.d("MainActivity: email", email);
//                Log.d("MainActivity: username", username);
//                Log.d("MainActivity: token", token);

                JSONObject obj = new JSONObject();
                obj.put("email", email);
                obj.put("username", username);
                obj.put("token", token);
                try {
                    FileWriter file = new FileWriter(file_path + "/userinfo.json", false);
                    file.write(obj.toJSONString());
                    file.flush();
                    file.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bundle.clear();
        }

        print_json(); //json test

        intent_login = new Intent(this, LoginActivity.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initCountries();

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTripPlanActivity.class);
                startActivity(intent);
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

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Button logout_btn = headerView.findViewById(R.id.logout);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(error -> {
                    if (error != null) {
                        Log.e("LOGOUT", "로그아웃 실패, SDK에서 토큰 삭제됨", error);
                    }else{
                        Log.d("LOGOUT", "로그아웃 성공, SDK에서 토큰 삭제됨");
                        startActivity(intent_login);
                    }
                    return null;
                });
            }
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

    public static String get_filepath() {
        return file_path;
    }

    public void print_json() {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(file_path+"/userinfo.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            reader.close();
            Log.d("Json Object:", jsonObject.toJSONString());
            Log.d("Json Object:",jsonObject.get("email").toString());
            Log.d("Json Object:",jsonObject.get("username").toString());
            Log.d("Json Object:",jsonObject.get("token").toString());
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}