package com.example.madcamp_project2.ui.home.detail;

import static com.example.madcamp_project2.ui.home.detail.DetailTripActivity.polylines;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.ScheduleAdapter;
import com.example.madcamp_project2.ui.home.ScheduleComparator;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewSchedule;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewTravel;
import com.google.android.gms.maps.model.LatLng;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        this.schedules = schedules;

        days = tripPlan.getDuration();
        start_date = tripPlan.getStart_date();
        this.tripPlan = tripPlan;

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
    public void onViewAttachedToWindow(@NonNull ViewHolderPage holder) {
        super.onViewAttachedToWindow(holder);
        Log.d("%%%%%%%%%%%%%%%%", "PLEASE");

        ((ScheduleAdapter)holder.recyclerView.getAdapter()).updateMap();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        ArrayList<Schedule> schedule = schedules[position];

        Calendar cal = Calendar.getInstance();
        cal.setTime(start_date);
        cal.add(Calendar.DATE, position);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd (E)");
        holder.dateTextView.setText(sdf.format(cal.getTime()));
        holder.recyclerView.setAdapter(scheduleAdapters[position]);
        Collections.sort(schedule, new ScheduleComparator());

        holder.addScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBottomSheetDialog addBottomSheetDialog = new AddBottomSheetDialog(context, tripPlan, position);
                addBottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "bottomSheet");
            }
        });

        holder.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO 데이터베이스에서 새로 스케줄 받아오는 것도 해야함
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

                Call<ArrayList<NewSchedule>> get_schedules = myapi.get_schedules("Bearer " + token, tripPlan.getTravel_id());
                get_schedules.enqueue(new Callback<ArrayList<NewSchedule>>() {
                    @Override
                    public void onResponse(Call<ArrayList<NewSchedule>> call, Response<ArrayList<NewSchedule>> response) {
                        if(response.isSuccessful()) {
                            ArrayList<NewSchedule> newSchedules = response.body();
                            for(NewSchedule newSchedule : newSchedules) {
                                Schedule schedule_elem = new Schedule(newSchedule);
                                tripPlan.getSchedules()[newSchedule.getDay()].add(schedule_elem);
                            }
                            Log.d("GET SCHEDULES", "SUCCESS");
                        }
                        else {
                            Log.d("GET SCHEDULES", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<NewSchedule>> call, Throwable t) {
                        Log.d("GET SCHEDULES", "FAILED");
                    }
                });

                Collections.sort(schedule, new ScheduleComparator());
                scheduleAdapters[position].setSchedules(schedule);
                scheduleAdapters[position].notifyDataSetChanged();
                scheduleAdapters[position].updateMap();
                holder.swipeRefreshLayout.setRefreshing(false);
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
        SwipeRefreshLayout swipeRefreshLayout;

        public ViewHolderPage(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.schedule_date_text_view);
            recyclerView = itemView.findViewById(R.id.schedule_recycler_view);
            addScheduleBtn = itemView.findViewById(R.id.schedule_add_btn);
            swipeRefreshLayout = itemView.findViewById(R.id.swipe_layout);
        }
    }
}
