package com.example.madcamp_project2.ui.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.DialogBehavior;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.internal.main.DialogLayout;
import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.databinding.FragmentFriendsBinding;
import com.example.madcamp_project2.ui.User;
import com.example.madcamp_project2.ui.friends.Friend.FriendRequest;
import com.example.madcamp_project2.ui.home.addtrip.Travel.NewTravel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Ref;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    RecyclerView recyclerView;
    private FriendsAdapter friendsAdapter;
    String input_text;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SwipeRefreshLayout refreshLayout = root.findViewById(R.id.friendsSwipe);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO 친구 목록 가져오기..? 바로 가져와지는지 아닌지 보고 하기
                refreshLayout.setRefreshing(false);
            }
        });

        recyclerView = root.findViewById(R.id.friend_recycler_view);
        friendsAdapter = new FriendsAdapter(getActivity(), MainActivity.thisUser.getFriends());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(friendsAdapter);

        FloatingActionButton fab = root.findViewById(R.id.fab_friend);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 친구 추가 창 띄우기

                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                builder.setTitle("추가할 이메일을 입력해주세요");

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
                        String token = "";
                        String email = "";

                        String file_path = MainActivity.get_filepath();
                        JSONParser parser = new JSONParser();

                        try {
                            FileReader reader = new FileReader(file_path+"/userinfo.json");
                            Object obj = parser.parse(reader);
                            JSONObject jsonObject = (JSONObject) obj;
                            reader.close();

                            token = jsonObject.get("token").toString();
                            email = jsonObject.get("email").toString();
                        }
                        catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }

                        FriendRequest friendRequest = new FriendRequest(email, input_text);
                        MyAPI myapi = LoginActivity.get_MyAPI();

                        Call<FriendRequest> post_friend_request = myapi.post_friend_request("Bearer " + token, friendRequest);

                        post_friend_request.enqueue(new Callback<FriendRequest>() {
                            @Override
                            public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                                if(response.isSuccessful()) {
                                    FriendRequest bodyRequest = response.body();
                                    if (bodyRequest.getStatus().equals("False")) {
                                        Toast.makeText(root.getContext(), "가입되지 않은 사용자입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    if (bodyRequest.getStatus().equals("Self")) {
                                        Toast.makeText(root.getContext(), "본인에게 요청을 보낼 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    if (bodyRequest.getStatus().equals("Onrequest")) {
                                        Toast.makeText(root.getContext(), "이미 친구 요청이 온 사용자입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    if (bodyRequest.getStatus().equals("Duplicated")) {
                                        Toast.makeText(root.getContext(), "처리 중인 요청입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    if (bodyRequest.getStatus().equals("Already")) {
                                        Toast.makeText(root.getContext(), "이미 친구인 사용자입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    if (bodyRequest.getStatus().equals("True")){
                                        Toast.makeText(root.getContext(), "요청이 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.d("FRIEND REQUEST", "SUCCESS");
                                }
                                else {
                                    Log.d("FRIEND REQUEST", "FAILED");
                                    Toast.makeText(root.getContext(), "유효하지 않은 요청입니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<FriendRequest> call, Throwable t) {
                                Log.d("FRIEND REQUEST", "FAILED");
                                Toast.makeText(root.getContext(), "요청이 정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
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