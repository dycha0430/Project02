package com.example.madcamp_project2.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.TripState;
import com.example.madcamp_project2.ui.home.detail.DetailTripActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
        holder.locImageView.setImageResource(R.drawable.back3);
        holder.iconImageView.setImageResource(R.drawable.tangerine);

        if (tripPlan.getState() == TripState.BEFORE) { holder.stateTextView.setBackgroundColor(context.getResources().getColor(R.color.before)); }
        else if (tripPlan.getState() == TripState.ING) { holder.stateTextView.setBackgroundColor(context.getResources().getColor(R.color.ing)); }
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
