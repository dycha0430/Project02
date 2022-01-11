package com.example.madcamp_project2.ui.home.addtrip;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.ActivityAddExtraBinding;
import com.example.madcamp_project2.ui.Country;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.User;
import com.example.madcamp_project2.ui.friends.FriendAddAdapter;
import com.example.madcamp_project2.ui.home.HomeFragment;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewTravel;
import com.example.madcamp_project2.ui.home.addtrip.Travel.TravelRequestSend;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddExtraActivity extends AppCompatActivity {
    Country country;
    Date startDate, endDate;
    EditText titleTripEditText;
    Button completeBtn, addFriendBtn;

    public static FriendAddAdapter friendAddAdapter;
    Context context;
    RecyclerView recyclerView;
    public static ArrayList<User> unSelectedFriends;
    public static ArrayList<User> selectedFriends;
    public static int selectedFriendNum = 1;
    private ActivityAddExtraBinding binding;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddExtraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        selectedFriends = new ArrayList<>();
        selectedFriendNum = 1;
        selectedFriends.add(MainActivity.thisUser);

        unSelectedFriends = new ArrayList<>();
        for (User user : MainActivity.thisUser.getFriends()) {
            unSelectedFriends.add(user);
        }

        getSupportActionBar().hide();
        titleTripEditText = findViewById(R.id.tripTitleEditText);
        completeBtn = findViewById(R.id.completeBtn);
        addFriendBtn = findViewById(R.id.addFriendBtn);
        recyclerView = findViewById(R.id.selected_friend_recycler_view);

        friendAddAdapter = new FriendAddAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(friendAddAdapter);

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFriendNum - 1 >= MainActivity.thisUser.getFriends().size()) {
                    Toast.makeText(context, "더 이상 친구를 초대할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = new User();
                user.setEmail("");
                user.setProfile("https://user-images.githubusercontent.com/68413811/148871066-6f08d53e-25b7-4f0d-bffb-098fe260213b.png");
                user.setName("선택하기");
                selectedFriends.add(user);

                selectedFriendNum++;
                friendAddAdapter.notifyDataSetChanged();
            }
        });

        Intent intent = getIntent();
        country = (Country) intent.getSerializableExtra("place");
        startDate = new Date(intent.getLongExtra("start_date", 0));
        endDate = new Date(intent.getLongExtra("end_date", 0));

        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleTripEditText.getText().toString().equals("")) {
                    titleTripEditText.setText("나의");
                }

                TripPlan tripPlan = new TripPlan(-1, titleTripEditText.getText().toString(), startDate, endDate, country);
                MainActivity.thisUser.getMyTrips().add(tripPlan);

                for (User friend : selectedFriends) {
                    if (friend.getEmail().equals("")) {
                        selectedFriends.remove(friend);
                    }
                }
                tripPlan.setParticipants(selectedFriends);

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

                String finalToken = token;
                String finalEmail = email;
                post_travel.enqueue(new Callback<NewTravel>() {
                    @Override
                    public void onResponse(Call<NewTravel> call, Response<NewTravel> response) {
                        if(response.isSuccessful()) {
                            NewTravel response_travel = response.body();
                            Log.d("NEW TRAVEL", "SUCCESS");
                            Log.d("NEW TRAVEL Id", String.valueOf(response_travel.getTravel_id()));
                            tripPlan.setTravel_id(response_travel.getTravel_id());

                            request_friends_ToJoin(tripPlan, finalToken, finalEmail);
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

    public void request_friends_ToJoin(TripPlan tripPlan, String token, String email) {
        MyAPI myapi = LoginActivity.get_MyAPI();
        TravelRequestSend travelRequestSend = new TravelRequestSend(email, tripPlan.getParticipants(), tripPlan.getTravel_id());

        Call<TravelRequestSend> post_travel_request = myapi.post_travel_request("Bearer " + token, travelRequestSend);
        post_travel_request.enqueue(new Callback<TravelRequestSend>() {
            @Override
            public void onResponse(Call<TravelRequestSend> call, Response<TravelRequestSend> response) {
                if(response.isSuccessful()) {
                    Log.d("REQUEST TO JOIN", "SUCCESS");
                }
                else {
                    Log.d("REQUEST TO JOIN", "FAILED");
                }
            }

            @Override
            public void onFailure(Call<TravelRequestSend> call, Throwable t) {
                Log.d("REQUEST TO JOIN", "FAILED");
            }
        });
    }
}
