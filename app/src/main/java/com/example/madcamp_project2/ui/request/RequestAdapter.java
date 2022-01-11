package com.example.madcamp_project2.ui.request;


import static com.example.madcamp_project2.MainActivity.thisUser;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.User;
import com.example.madcamp_project2.ui.friends.Friend.FriendRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder>{
    ArrayList<User> friends;
    Context context;

    public RequestAdapter(Context context, ArrayList<User> friends) {
        this.context = context;
        this.friends = friends;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        RequestAdapter.ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User friend = friends.get(position);
        Log.e("BindViewHolder Debug", friend.getName());
        holder.nameTextView.setText(friend.getName());
        holder.emailTextView.setText(friend.getEmail());
        Glide.with(context).load(friend.getProfile()).into(holder.friendProfile);

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User friend = friends.get(position);

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

                FriendRequest friendRequest = new FriendRequest(friend.getEmail(), email);
                friendRequest.setStatus("IGNORE");
                MyAPI myapi = LoginActivity.get_MyAPI();

                Call<FriendRequest> post_friend_ignore = myapi.post_friend_request_handle("Bearer " + token, friendRequest);
                post_friend_ignore.enqueue(new Callback<FriendRequest>() {
                    @Override
                    public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                        if (response.isSuccessful()) {
                            Log.d("FRIEND REQUEST IGNORE", "SUCCESS");
                            friends.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, friends.size());
                        }
                        else {
                            Log.d("FRIEND REQUEST IGNORE", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<FriendRequest> call, Throwable t) {
                        Log.d("FRIEND ADD", "FAILED");
                    }
                });
            }
        });

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User friend = friends.get(position);

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

                FriendRequest friendRequest = new FriendRequest(friend.getEmail(), email);
                friendRequest.setStatus("ACCEPT");
                MyAPI myapi = LoginActivity.get_MyAPI();

                Call<FriendRequest> post_friend_add = myapi.post_friend_request_handle("Bearer " + token, friendRequest);
                post_friend_add.enqueue(new Callback<FriendRequest>() {
                    @Override
                    public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                        if (response.isSuccessful()) {
                            Log.d("FRIEND ADD", "SUCCESS");

                            // TODO Front) 서로의 친구목록에 추가
                            thisUser.getFriends().add(friend);
                            friend.getFriends().add(thisUser);

                            friends.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, friends.size());
                        }
                        else {
                            Log.d("FRIEND ADD", "FAILED");
                        }
                    }

                    @Override
                    public void onFailure(Call<FriendRequest> call, Throwable t) {
                        Log.d("FRIEND ADD", "FAILED");
                    }
                });
            }

        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendProfile;
        TextView nameTextView, emailTextView;
        ImageButton cancelBtn, acceptBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friendProfile = (ImageView) itemView.findViewById(R.id.friend_profile_request);
            nameTextView = (TextView) itemView.findViewById(R.id.friend_name_text_view_request);
            emailTextView = (TextView) itemView.findViewById(R.id.friend_email_text_view_request);

            cancelBtn = (ImageButton) itemView.findViewById(R.id.cancelBtn);
            acceptBtn = (ImageButton) itemView.findViewById(R.id.acceptBtn);
        }
    }
}
