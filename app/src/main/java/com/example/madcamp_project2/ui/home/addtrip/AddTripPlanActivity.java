package com.example.madcamp_project2.ui.home.addtrip;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.ActivityAddTripPlanBinding;

public class AddTripPlanActivity extends AppCompatActivity {
    private ActivityAddTripPlanBinding binding;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTripPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.select_place_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AddPlaceAdapter(this));
    }
}
