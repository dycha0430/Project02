package com.example.madcamp_project2.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.TripState;
import com.example.madcamp_project2.ui.home.detail.DetailTripActivity;

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

public class PlanSummaryAdapter extends RecyclerView.Adapter<PlanSummaryAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TripPlan> tripPlanList;

    public PlanSummaryAdapter(Context context, ArrayList<TripPlan> tripPlans) {
        this.context = context;
        this.tripPlanList = tripPlans;
    }

    public void setTripPlanList(ArrayList<TripPlan> tripPlanList) {
        this.tripPlanList = tripPlanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_recycler_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TripPlan tripPlan = tripPlanList.get(position);

        holder.titleTextView.setText(tripPlan.getTitle());
        holder.stateTextView.setText(tripPlan.getState().toString());
        holder.locTextView.setText(tripPlan.getDestination().getName());

        DateFormat df = new SimpleDateFormat("yy.MM.dd");
        String start_date = df.format(tripPlan.getStart_date());
        String end_date = df.format(tripPlan.getEnd_date());
        holder.dateTextView.setText(start_date + " ~ " + end_date);

        String withString = "";
        if (tripPlan.getParticipants().size() == 1) {
            withString = "나 혼자 여행";
        }
        withString = MainActivity.thisUser.getName() + "님 외" + (tripPlan.getParticipants().size()-1) + "명과 함께";
        holder.withTextView.setText(withString);

        String backName = "back" + tripPlan.getDestination().getCountryEnum().ordinal();
        Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier(backName, "drawable", context.getPackageName()));
        holder.locImageView.setImageDrawable(drawable);

        String iconName = "icon" + tripPlan.getDestination().getCountryEnum().ordinal();
        Drawable drawable1 = context.getResources().getDrawable(context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
        holder.iconImageView.setImageDrawable(drawable1);


        TripState state = tripPlan.getState();

        if (state == TripState.BEFORE) { holder.stateTextView.setBackgroundColor(context.getResources().getColor(R.color.before)); }
        else if (state == TripState.ING) { holder.stateTextView.setBackgroundColor(context.getResources().getColor(R.color.ing)); }
        else { holder.stateTextView.setBackgroundColor(context.getResources().getColor(R.color.after)); }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailTripActivity.class);
                // intent.putExtra ...
//                intent.putExtra("tripPlan", tripPlan);
                intent.putExtra("tripPlan", position);
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("해당 여행을 삭제하시겠습니까?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeTrip(position);
                    }
                });

                dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                dialog.show();
                return false;
            }
        });
    }

    private void removeTrip(int position) {
        // TODO DB에서 Travel 삭제
        TripPlan travel_to_remove = tripPlanList.get(position);

        String token = "";

        String file_path = MainActivity.get_filepath();
        JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader(file_path+"/userinfo.json");
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            reader.close();

            token = jsonObject.get("token").toString();
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        MyAPI myapi = LoginActivity.get_MyAPI();
        Call<Integer> post_del_travel= myapi.post_del_travel("Bearer " + token, travel_to_remove.getTravel_id());

        post_del_travel.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()) {
                    Log.d("TRAVEL REMOVE", "SUCCESS");
                }
                else {
                    Log.d("TRAVEL REMOVE", "FAILED");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("TRAVEL REMOVE", "FAILED");
            }
        });

        tripPlanList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tripPlanList.size());
    }

    @Override
    public int getItemCount() {
        return tripPlanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView locImageView, iconImageView;
        TextView titleTextView, dateTextView, locTextView, withTextView, stateTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locImageView = (ImageView) itemView.findViewById(R.id.locImageView);
            iconImageView = (ImageView) itemView.findViewById(R.id.locIcon);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.dateTextView);
            locTextView = (TextView) itemView.findViewById(R.id.locTextView);
            withTextView = (TextView) itemView.findViewById(R.id.withTextView);
            stateTextView = (TextView) itemView.findViewById(R.id.stateTextView);
        }
    }
}
