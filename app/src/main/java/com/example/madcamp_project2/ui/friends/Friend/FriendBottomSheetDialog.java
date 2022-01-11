package com.example.madcamp_project2.ui.friends.Friend;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.TripPlan;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FriendBottomSheetDialog extends BottomSheetDialogFragment {
    RecyclerView recyclerView;
    Context context;
    int selected_position;
    TripPlan tripPlan;

    public FriendBottomSheetDialog(Context context, TripPlan tripPlan) {
        this.context = context;
        selected_position = -1;
        this.tripPlan = tripPlan;
    }

    public FriendBottomSheetDialog(Context context, int selected_position) {
        this.context = context;
        this.selected_position = selected_position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friend_bottom_sheet_dialog_layout, container, false);

        recyclerView = view.findViewById(R.id.selectFriendRecyclerView);
        SelectFriendAdapter adapter = new SelectFriendAdapter(context, this, selected_position, tripPlan);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
