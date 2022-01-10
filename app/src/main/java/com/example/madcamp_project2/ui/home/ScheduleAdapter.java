package com.example.madcamp_project2.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Schedule;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.home.detail.BottomSheetDialog;
import com.example.madcamp_project2.ui.home.detail.DetailTripActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder>{

    Context context;
    ArrayList<Schedule> schedules;
    int day;
    TripPlan tripPlan;
    ScheduleAdapter thisContext;

    public ScheduleAdapter(Context context, ArrayList<Schedule> schedules, int day, TripPlan tripPlan) {
        this.context = context;
        this.schedules = schedules;
        this.day = day;
        this.tripPlan = tripPlan;
        thisContext = this;
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

        int start_hour = schedule.getStart_time().getHour();
        int start_minute = schedule.getStart_time().getMinute();
        int end_hour = schedule.getEnd_time().getHour();
        int end_minute = schedule.getEnd_time().getMinute();
        String startText = "";
        String endText = "";

        if (start_hour < 10) startText += "0" + start_hour;
        else startText += start_hour + "";

        startText += " : ";
        if (start_minute < 19) startText += "0" + start_minute;
        else startText += start_minute;

        if (end_hour < 10) endText += "0" + end_hour;
        else endText += end_hour + "";

        endText += " : ";
        if (end_minute < 19) endText += "0" + end_minute;
        else endText += end_minute;

        holder.startTimeTextView.setText( startText);
        holder.endTimeTextView.setText(endText);
        holder.placeTextView.setText(schedule.getPlace().getName());
        holder.addressTextView.setText(schedule.getPlace().getAddress());
        holder.moneyTextView.setText(schedule.getMoney());

        if (schedule.getMemo().equals("")) {
            holder.memoTextView.setText("메모가 없습니다");
        } else {
            holder.memoTextView.setText(schedule.getMemo());
        }

        holder.memoTextView.setMovementMethod(new ScrollingMovementMethod());

        holder.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailTripActivity.moveMap(schedule.getPlace().getAddress(), schedule.getPlace().getName());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, schedule, day, tripPlan);
                bottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "bottomSheet");
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("해당 일정을 삭제하시겠습니까?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeSchedule(position);
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

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d("!!!!!!!!!!!!!!!!!!1", "HIHIHI");
    }

    private void removeSchedule(int position) {
        // TODO DB에서 스케줄 삭제
        schedules.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, schedules.size());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView startTimeTextView, endTimeTextView, placeTextView, addressTextView, moneyTextView, memoTextView;
        ImageButton locationBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startTimeTextView = (TextView) itemView.findViewById(R.id.start_time_text_view);
            endTimeTextView = (TextView) itemView.findViewById(R.id.end_time_text_view);
            placeTextView = (TextView) itemView.findViewById(R.id.schedule_place);
            addressTextView = (TextView) itemView.findViewById(R.id.schedule_address);
            moneyTextView = (TextView) itemView.findViewById(R.id.schedule_money);
            memoTextView = (TextView) itemView.findViewById(R.id.schedule_memo);
            locationBtn = (ImageButton) itemView.findViewById(R.id.schedule_location);
        }
    }
}
