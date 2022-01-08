package com.example.madcamp_project2.ui.home.addtrip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Country;
import com.example.madcamp_project2.ui.home.PlanSummaryAdapter;
import com.example.madcamp_project2.ui.home.detail.DetailTripActivity;

import java.util.ArrayList;

public class AddPlaceAdapter extends RecyclerView.Adapter<AddPlaceAdapter.ViewHolder>{

    Context context;
    Country[] countries;
    public AddPlaceAdapter(Context context) {
        this.context = context;
        countries = MainActivity.COUNTRIES;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_place, parent, false);
        AddPlaceAdapter.ViewHolder viewHolder = new AddPlaceAdapter.ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.placeNameTextView.setText(countries[position].getName());
        String backName = "back" + position;
        Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier(backName, "drawable", context.getPackageName()));
        holder.placeNameTextView.setBackground(drawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddDateActivity.class);
                intent.putExtra("place", countries[position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = (TextView) itemView.findViewById(R.id.placeNameTextView);
        }
    }
}
