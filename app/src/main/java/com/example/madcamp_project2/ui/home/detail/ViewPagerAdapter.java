package com.example.madcamp_project2.ui.home.detail;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.ScheduleAdapter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolderPage> {

    Context context;
    ArrayList<Schedule>[] schedules;
    int days;
    Date start_date;
    public static ScheduleAdapter[] scheduleAdapters;
    TripPlan tripPlan;

    @RequiresApi(api = Build.VERSION_CODES.O)
    ViewPagerAdapter(Context context, TripPlan tripPlan) {
        this.context = context;

        ArrayList<Schedule>[] schedules =tripPlan.getSchedules();
        Date start = tripPlan.getStart_date();
        Date end = tripPlan.getEnd_date();
        this.schedules = schedules;

        LocalDate localStartDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period period = Period.between(localStartDate, localEndDate);

        days = period.getDays();
        start_date = start;
        this.tripPlan = tripPlan;

        Log.d("******", days + "");
        scheduleAdapters = new ScheduleAdapter[days];
        for (int i = 0; i < days; i++) {
            scheduleAdapters[i] = new ScheduleAdapter(context, schedules[i], i, tripPlan);
        }
    }

    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_viewpager, parent, false);
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

        holder.addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBottomSheetDialog addBottomSheetDialog = new AddBottomSheetDialog(context, tripPlan, position);
                addBottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "bottomSheet");
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.length;
    }

    class ViewHolderPage extends RecyclerView.ViewHolder{

        TextView dateTextView;
        RecyclerView recyclerView;
        Button addScheduleBtn;

        public ViewHolderPage(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.schedule_date_text_view);
            recyclerView = itemView.findViewById(R.id.schedule_recycler_view);
            addScheduleBtn = itemView.findViewById(R.id.schedule_add_btn);
        }
    }
}
