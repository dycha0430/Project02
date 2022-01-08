package com.example.madcamp_project2.ui.home.addtrip;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.ActivityAddExtraBinding;
import com.example.madcamp_project2.ui.Country;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.HomeFragment;

import java.util.Date;

public class AddExtraActivity extends AppCompatActivity {
    Country country;
    Date startDate, endDate;
    EditText titleTripEditText;
    Button completeBtn;
    Context context;
    private ActivityAddExtraBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExtraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        getSupportActionBar().hide();

        titleTripEditText = findViewById(R.id.tripTitleEditText);
        completeBtn = findViewById(R.id.completeBtn);

        Intent intent = getIntent();
        country = (Country) intent.getSerializableExtra("place");
        startDate = new Date(intent.getLongExtra("start_date", 0));
        endDate = new Date(intent.getLongExtra("end_date", 0));

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment.tripPlanList.add(new TripPlan(titleTripEditText.getText().toString(), startDate, endDate, country));



                Intent intent = new Intent(context, HomeFragment.class);

                startActivity(intent);
            }
        });
    }
}
