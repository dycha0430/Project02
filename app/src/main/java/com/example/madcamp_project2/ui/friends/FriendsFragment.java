package com.example.madcamp_project2.ui.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.DialogBehavior;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.main.DialogLayout;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.FragmentFriendsBinding;
import com.example.madcamp_project2.ui.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    RecyclerView recyclerView;
    private FriendsAdapter friendsAdapter;
    String input_text;
    Button selectCompleteBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.friend_recycler_view);
        friendsAdapter = new FriendsAdapter(getActivity(), MainActivity.thisUser.getFriends());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(friendsAdapter);

        selectCompleteBtn.setVisibility(View.INVISIBLE);
        FloatingActionButton fab = root.findViewById(R.id.fab_friend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 친구 추가 창 띄우기

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setTitle("추가할 아이디를 입력해주세요");

// Set up the input
                final EditText input = new EditText(getActivity());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setTextColor(Color.BLACK);
                input.setBackgroundResource(R.color.transparent);

                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                params.setMargins(60, 30, 60, 0);
                CardView cardView = new CardView(getActivity());
                CardView.LayoutParams params1 = new CardView.LayoutParams(
                        CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);
                cardView.setRadius(10);
                cardView.setBackgroundResource(R.drawable.dialog_back);
                cardView.addView(input, params1);
                layout.addView(cardView, params);

                builder.setView(layout);

// Set up the buttons
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        input_text = input.getText().toString();
                        // TODO DB에서 해당 아이디 찾아서 친구 ArrayList에 추가하고 DB에도 추가 /
                        //  Friends 탭 리사이클러뷰 업데이트
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();




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