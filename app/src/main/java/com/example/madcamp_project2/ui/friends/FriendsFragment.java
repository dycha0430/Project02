package com.example.madcamp_project2.ui.friends;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogBehavior;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.main.DialogLayout;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.FragmentFriendsBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    RecyclerView recyclerView;
    private FriendsAdapter friendsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.friend_recycler_view);
        //friendsAdapter = new FriendsAdapter(getActivity(), 나의 친구 목록 어레이리스트);
        //recyclerView.setAdapter(friendsAdapter);

        FloatingActionButton fab = root.findViewById(R.id.fab_friend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 친구 추가 창 띄우기
                
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}