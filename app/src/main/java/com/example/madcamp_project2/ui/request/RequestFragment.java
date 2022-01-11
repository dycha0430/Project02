package com.example.madcamp_project2.ui.request;

import static com.example.madcamp_project2.MainActivity.thisUser;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.FragmentRequestBinding;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.User;
import com.example.madcamp_project2.ui.friends.Friend.Friend;
import com.example.madcamp_project2.ui.friends.Friend.GetFriend;
import com.example.madcamp_project2.ui.home.addtrip.Travel.GetTravel;
import com.example.madcamp_project2.ui.home.addtrip.Travel.userTravel;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestFragment extends Fragment {

    private FragmentRequestBinding binding;
    RecyclerView recyclerView, travelRecyclerView;
    SwipeRefreshLayout requestSwipe, inviteSwipe;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.request_recycler_view);
        travelRecyclerView = root.findViewById(R.id.request_travel_recycler_view);
        requestSwipe = root.findViewById(R.id.requestSwipe);
        inviteSwipe = root.findViewById(R.id.inviteSwipe);

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

        get_friend_requests(email, token);
        get_travel_requests(email, token);

        String finalEmail = email;
        String finalToken = token;
        requestSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO DB에서 친구요청 목록 가져오기
                get_friend_requests(finalEmail, finalToken);
                requestSwipe.setRefreshing(false);
            }
        });

        inviteSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO DB에서 여행초대 목록 가져오기
                get_travel_requests(finalEmail, finalToken);
                inviteSwipe.setRefreshing(false);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void get_friend_requests(String email, String token) {
        MyAPI myapi = LoginActivity.get_MyAPI();
        Call<GetFriend> get_friend_requests = myapi.get_friend_requests("Bearer " + token, email);
        get_friend_requests.enqueue(new Callback<GetFriend>() {
            @Override
            public void onResponse(Call<GetFriend> call, Response<GetFriend> response) {
                if(response.isSuccessful()) {
                    Log.d("GET FRIEND REQUESTS", "SUCCESS");
                    GetFriend getFriend = response.body();

                    thisUser.getPending_requests().clear();
                    for(Friend friend : getFriend.getFriend_list()) {
//                        Log.d("GET FRIEND REQUESTS", "check here!");
//                        Log.d("GET FRIEND REQUESTS", friend.getUsername());
//                        Log.d("GET FRIEND REQUESTS", friend.getEmail());
//                        Log.d("GET FRIEND REQUESTS", friend.getPhoto());
                        thisUser.getPending_requests().add(new User(friend.getUsername(), friend.getEmail(), friend.getPhoto()));
                    }
                }
                else {
                    Log.d("GET FRIEND REQUESTS", "FAILED");
                }
            }

            @Override
            public void onFailure(Call<GetFriend> call, Throwable t) {
                Log.d("GET FRIEND REQUESTS", "FAILED");
            }
        });
    }

    public void get_travel_requests(String email, String token) {
        MyAPI myapi = LoginActivity.get_MyAPI();
        Call<userTravel> get_travel_requests = myapi.get_travel_requests("Bearer " + token, email);
        get_travel_requests.enqueue(new Callback<userTravel>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<userTravel> call, Response<userTravel> response) {
                if(response.isSuccessful()) {
                    Log.d("GET TRAVEL REQUESTS", "SUCCESS");
                    userTravel userTravel = response.body();

                    thisUser.getPending_trips().clear();
                    for(GetTravel getTravel : userTravel.getTravel_list()) {
                        thisUser.getPending_trips().add(new TripPlan(getTravel));
                    }

                    RequestAdapter requestAdapter = new RequestAdapter(getActivity(), thisUser.getPending_requests());
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(requestAdapter);

                    RequestTravelAdapter requestTravelAdapter = new RequestTravelAdapter(getActivity(), thisUser.getPending_trips());
                    travelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    travelRecyclerView.setAdapter(requestTravelAdapter);
                }
                else {
                    Log.d("GET TRAVEL REQUESTS", "FAILED");
                }
            }

            @Override
            public void onFailure(Call<userTravel> call, Throwable t) {
                Log.d("GET TRAVEL REQUESTS", "FAILED");
            }
        });
    }
}