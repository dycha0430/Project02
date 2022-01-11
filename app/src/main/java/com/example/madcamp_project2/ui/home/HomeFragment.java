package com.example.madcamp_project2.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.FragmentHomeBinding;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.TripState;
import com.example.madcamp_project2.ui.home.addtrip.AddTripPlanActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.madcamp_project2.ui.home.addtrip.Travel.GetTravel;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewTravel;
import com.example.madcamp_project2.ui.home.addtrip.Travel.userTravel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlanSummaryAdapter planSummaryAdapter;
    private Context context;

    @Override
    public void onResume() {
        super.onResume();
        planSummaryAdapter.setTripPlanList(MainActivity.thisUser.getMyTrips());
        planSummaryAdapter.notifyDataSetChanged();

        Log.d("######", "On Resume");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("######", "On Create");
        this.context = getActivity();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

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

        MainActivity.thisUser.setMyTrips(new ArrayList<>());
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddTripPlanActivity.class);
                startActivity(intent);
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.homeSwipe);
        String finalEmail = email;
        String finalToken = token;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO DB에서 tripPlanList 받아오기
                get_travels(finalEmail, finalToken);

                planSummaryAdapter.setTripPlanList(MainActivity.thisUser.getMyTrips());
                planSummaryAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        get_travels(email, token);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.homeRecyclerView);
        planSummaryAdapter = new PlanSummaryAdapter(context, MainActivity.thisUser.getMyTrips());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(planSummaryAdapter);

        return rootView;
    }

    public void get_travels(String email, String token) {
        MyAPI myapi = LoginActivity.get_MyAPI();
        Call<userTravel> get_userTravel = myapi.get_userTravel("Bearer " + token, email);

        get_userTravel.enqueue(new Callback<userTravel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<userTravel> call, Response<userTravel> response) {
                if(response.isSuccessful()) {
                    userTravel userTravelList = response.body();
                    ArrayList<GetTravel> travelList = userTravelList.getTravel_list();
                    for(int i=0; i<travelList.size(); i++) {
                        GetTravel travel = travelList.get(i);
                        MainActivity.thisUser.getMyTrips().add(new TripPlan(travel));
                    }
                    Log.d("GET USER TRAVEL", "SUCCESS");

                    planSummaryAdapter.setTripPlanList(MainActivity.thisUser.getMyTrips());
                    planSummaryAdapter.notifyDataSetChanged();

                }
                else {
                    Log.d("GET USER TRAVEL", "FAILED");
                }
            }

            @Override
            public void onFailure(Call<userTravel> call, Throwable t) {
                Log.d("GET USER TRAVEL", "FAILED");
            }
        });
    }
}