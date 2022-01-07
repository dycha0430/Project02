package com.example.madcamp_project2.ui.home.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Schedule;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolderPage> {

    Context context;
    ArrayList<Schedule>[] schedules;

    ViewPagerAdapter(Context context, ArrayList<Schedule>[] schedules) {
        this.context = context;
        this.schedules = schedules;
    }


    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        ArrayList<Schedule> schedule = schedules[position];
//        holder.dateTextView.setText();
//        holder.recyclerView.set
    }

    @Override
    public int getItemCount() {
        return schedules.length;
    }

    class ViewHolderPage extends RecyclerView.ViewHolder{

        TextView dateTextView;
        RecyclerView recyclerView;

        public ViewHolderPage(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.schedule_date_text_view);
            recyclerView = itemView.findViewById(R.id.schedule_recycler_view);
        }
    }
}
