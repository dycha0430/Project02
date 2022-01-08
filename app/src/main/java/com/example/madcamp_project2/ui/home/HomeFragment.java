package com.example.madcamp_project2.ui.home;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.FragmentHomeBinding;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.TripState;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    public static ArrayList<TripPlan> tripPlanList;
    private PlanSummaryAdapter planSummaryAdapter;
    private Context context;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        tripPlanList = new ArrayList<>();
        /* Test Data Setting */
        for (int i = 0; i < 10; i++) {
            tripPlanList.add(new TripPlan());
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.homeRecyclerView);
        planSummaryAdapter = new PlanSummaryAdapter(context, tripPlanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(planSummaryAdapter);

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        planSummaryAdapter.setTripPlanList(tripPlanList);
        planSummaryAdapter.notifyDataSetChanged();

        long today = System.currentTimeMillis();
        for (TripPlan tripPlan : tripPlanList) {
            LocalDate localStartDate = tripPlan.getStart_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localEndDate = tripPlan.getEnd_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localTodayDate = LocalDate.now();

            if (localTodayDate.isBefore(localStartDate)) {
                tripPlan.setState(TripState.BEFORE);
            } else if (localEndDate.isBefore(localTodayDate)) {
                tripPlan.setState(TripState.AFTER);
            } else {
                tripPlan.setState(TripState.ING);
            }
        }
    }
}