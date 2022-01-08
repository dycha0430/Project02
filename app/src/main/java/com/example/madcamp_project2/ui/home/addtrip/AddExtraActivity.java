package com.example.madcamp_project2.ui.home.addtrip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.ActivityAddExtraBinding;
import com.example.madcamp_project2.ui.Country;

import java.util.Date;

public class AddExtraActivity extends AppCompatActivity {
    Country country;
    Date startDate, endDate;
    EditText titleTripEditText;
    private ActivityAddExtraBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExtraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        titleTripEditText = findViewById(R.id.tripTitleEditText);

        Intent intent = getIntent();
        country = (Country) intent.getSerializableExtra("place");
        startDate = new Date(intent.getLongExtra("start_date", 0));
        endDate = new Date(intent.getLongExtra("end_date", 0));

    }
}
