package com.example.madcamp_project2.ui.request;

import static com.example.madcamp_project2.MainActivity.thisUser;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewTravel;
import com.example.madcamp_project2.ui.home.addtrip.Travel.TravelRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestTravelAdapter extends RecyclerView.Adapter<RequestTravelAdapter.ViewHolder> {

    Context context;
    ArrayList<TripPlan> pending_trips;
    public RequestTravelAdapter(Context context, ArrayList<TripPlan> pending_trips) {
        this.context = context;
        this.pending_trips = pending_trips;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request_travel, parent, false);
        RequestTravelAdapter.ViewHolder viewHolder = new RequestTravelAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripPlan tripPlan = pending_trips.get(position);

        holder.titleTextView.setText(tripPlan.getTitle());
        holder.locTextView.setText(tripPlan.getDestination().getName());

        DateFormat df = new SimpleDateFormat("yy.MM.dd");
        String start_date = df.format(tripPlan.getStart_date());
        String end_date = df.format(tripPlan.getEnd_date());
        holder.dateTextView.setText(start_date + " ~ " + end_date);

        String inviteString = tripPlan.getParticipants().get(0) + "님의 초대입니다";
        holder.inviteTextView.setText(inviteString);

        String backName = "back" + tripPlan.getDestination().getCountryEnum().ordinal();
        Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier(backName, "drawable", context.getPackageName()));
        holder.locImageView.setImageDrawable(drawable);

        String iconName = "icon" + tripPlan.getDestination().getCountryEnum().ordinal();
        Drawable drawable1 = context.getResources().getDrawable(context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
        holder.iconImageView.setImageDrawable(drawable1);

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

        MyAPI myapi = LoginActivity.get_MyAPI();

        String finalEmail = email;
        String finalToken = token;
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 요청 수락
                TravelRequest travelRequest = new TravelRequest(finalEmail, tripPlan.getTravel_id(), "ACCEPT");
                Call<TravelRequest> post_travel_request_accept = myapi.post_travel_request_handle("Bearer " + finalToken, travelRequest);

                post_travel_request_accept.enqueue(new Callback<TravelRequest>() {
                    @Override
                    public void onResponse(Call<TravelRequest> call, Response<TravelRequest> response) {
                        if(response.isSuccessful()) {
                            Log.d("TRAVEL REQUEST ACCEPT", "SUCCESS");
                            tripPlan.getParticipants().add(thisUser);
                        }
                        else {
                            Log.d("TRAVEL REQUEST ACCEPT", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<TravelRequest> call, Throwable t) {
                        Log.d("TRAVEL REQUEST ACCEPT", "FAILED");
                    }
                });

                thisUser.getMyTrips().add(tripPlan);

                pending_trips.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, pending_trips.size());
            }
        });

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 요청 거절 그냥 리스트에서 없애기
                TravelRequest travelRequest = new TravelRequest(finalEmail, tripPlan.getTravel_id(), "IGNORE");
                Call<TravelRequest> post_travel_request_ignore = myapi.post_travel_request_handle("Bearer " + finalToken, travelRequest);

                post_travel_request_ignore.enqueue(new Callback<TravelRequest>() {
                    @Override
                    public void onResponse(Call<TravelRequest> call, Response<TravelRequest> response) {
                        if(response.isSuccessful()) {
                            Log.d("TRAVEL REQUEST IGNORE", "SUCCESS");
                        }
                        else {
                            Log.d("TRAVEL REQUEST IGNORE", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<TravelRequest> call, Throwable t) {
                        Log.d("TRAVEL REQUEST IGNORE", "FAILED");
                    }
                });

                pending_trips.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, pending_trips.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return thisUser.getPending_trips().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView locImageView, iconImageView;
        TextView titleTextView, dateTextView, locTextView, inviteTextView;
        ImageButton cancelBtn, acceptBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locImageView = (ImageView) itemView.findViewById(R.id.locImageView_invite);
            iconImageView = (ImageView) itemView.findViewById(R.id.locIcon_invite);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView_invite);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView_invite);
            locTextView = (TextView) itemView.findViewById(R.id.locTextView_invite);
            inviteTextView = (TextView) itemView.findViewById(R.id.inviteTextView);
            cancelBtn = (ImageButton) itemView.findViewById(R.id.cancelBtn_invite);
            acceptBtn = (ImageButton) itemView.findViewById(R.id.acceptBtn_invite);
        }
    }
}
