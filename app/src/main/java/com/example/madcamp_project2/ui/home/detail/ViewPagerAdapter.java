package com.example.madcamp_project2.ui.home.detail;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.home.ScheduleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolderPage> {

    Context context;
    ArrayList<Schedule>[] schedules;
    int days;
    Date start_date;
    Context thisContext;
    public static ScheduleAdapter[] scheduleAdapters;


    ViewPagerAdapter(Context context, ArrayList<Schedule>[] schedules, Date start, Date end) {
        this.context = context;
        this.schedules = schedules;

        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        days = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS) + 1;
        start_date = start;

        Log.d("******", days + "");
        scheduleAdapters = new ScheduleAdapter[days];
        for (int i = 0; i < days; i++) {
            scheduleAdapters[i] = new ScheduleAdapter(thisContext, schedules[i]);
        }
    }


    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        thisContext = parent.getContext();
        View view = LayoutInflater.from(thisContext).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        ArrayList<Schedule> schedule = schedules[position];

        Calendar cal = Calendar.getInstance();
        cal.setTime(start_date);
        cal.add(Calendar.DATE, position);

        Log.d("%%%****", position + " ");

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd (E)");
        holder.dateTextView.setText(sdf.format(cal.getTime()));
        holder.recyclerView.setAdapter(scheduleAdapters[position]);
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
