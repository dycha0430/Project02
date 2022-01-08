package com.example.madcamp_project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
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
}