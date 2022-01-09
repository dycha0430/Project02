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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        if (tripPlanList == null) {
            tripPlanList = new ArrayList<>();
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.homeRecyclerView);
        planSummaryAdapter = new PlanSummaryAdapter(context, tripPlanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(planSummaryAdapter);

        return rootView;
    }
}