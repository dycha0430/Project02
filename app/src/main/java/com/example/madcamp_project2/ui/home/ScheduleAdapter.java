package com.example.madcamp_project2.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Schedule;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    Context context;
    ArrayList<Schedule> schedules;
    public ScheduleAdapter(Context context, ArrayList<Schedule> schedules) {
        this.context = context;
        this.schedules = schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_recycler_item, parent, false);
        ScheduleAdapter.ViewHolder viewHolder = new ScheduleAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);

        holder.startTimeTextView.setText(schedule.getStart_time().getHour() + ":" + schedule.getEnd_time().getMinute());
        holder.endTimeTextView.setText(schedule.getEnd_time().getHour() + ":" + schedule.getEnd_time().getMinute());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView startTimeTextView, endTimeTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startTimeTextView = (TextView) itemView.findViewById(R.id.start_time_text_view);
            endTimeTextView = (TextView) itemView.findViewById(R.id.end_time_text_view);
        }
    }
}
