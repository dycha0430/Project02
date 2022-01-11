package com.example.madcamp_project2.ui.request;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.FragmentRequestBinding;

public class RequestFragment extends Fragment {

    private FragmentRequestBinding binding;
    RecyclerView recyclerView, travelRecyclerView;
    SwipeRefreshLayout requestSwipe, inviteSwipe;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        recyclerView = root.findViewById(R.id.request_recycler_view);
        travelRecyclerView = root.findViewById(R.id.request_travel_recycler_view);
        requestSwipe = root.findViewById(R.id.requestSwipe);
        inviteSwipe = root.findViewById(R.id.inviteSwipe);

        requestSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO DB에서 친구요청 목록 가져오기

                requestSwipe.setRefreshing(false);
            }
        });

        inviteSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO DB에서 여행초대 목록 가져오기
                inviteSwipe.setRefreshing(false);
            }
        });

        RequestAdapter requestAdapter = new RequestAdapter(getActivity(), MainActivity.thisUser.getPending_requests());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(requestAdapter);

        RequestTravelAdapter requestTravelAdapter = new RequestTravelAdapter(getActivity(), MainActivity.thisUser.getPending_trips());
        travelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        travelRecyclerView.setAdapter(requestTravelAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}