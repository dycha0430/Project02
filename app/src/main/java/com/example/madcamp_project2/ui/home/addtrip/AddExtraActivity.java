package com.example.madcamp_project2.ui.home.addtrip;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.ActivityAddExtraBinding;
import com.example.madcamp_project2.ui.Country;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.HomeFragment;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewTravel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                TripPlan tripPlan = new TripPlan(titleTripEditText.getText().toString(), startDate, endDate, country);
                // HomeFragment.tripPlanList.add(tripPlan);

                String token = "";
                String email = "";

                String file_path = MainActivity.get_filepath();
                JSONParser parser = new JSONParser();

                try {
                    FileReader reader = new FileReader(file_path+"/userinfo.json");
                    Object obj = parser.parse(reader);
                    JSONObject jsonObject = (JSONObject) obj;
                    reader.close();

                    token = jsonObject.get("token").toString();
                    email = jsonObject.get("email").toString();
                }
                catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

                NewTravel newTravel = new NewTravel(-1, tripPlan.getTitle(), tripPlan.getStart_date(), tripPlan.getEnd_date(), tripPlan.getDestination(), email);
                MyAPI myapi = LoginActivity.get_MyAPI();

                Call<NewTravel> post_travel = myapi.post_travel("Bearer " + token, newTravel);

                post_travel.enqueue(new Callback<NewTravel>() {
                    @Override
                    public void onResponse(Call<NewTravel> call, Response<NewTravel> response) {
                        if(response.isSuccessful()) {
                            NewTravel response_travel = response.body();
                            Log.d("NEW TRAVEL", "SUCCESS");
                            Log.d("NEW TRAVEL Id", String.valueOf(response_travel.getTravel_id()));

                        }
                        else {
                            Log.d("NEW TRAVEL", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<NewTravel> call, Throwable t) {
                        Log.d("NEW TRAVEL", "FAILED");
                    }
                });

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
