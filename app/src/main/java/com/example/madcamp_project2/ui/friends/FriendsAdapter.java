package com.example.madcamp_project2.ui.friends;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder>{
    ArrayList<User> friends;
    Context context;

    public FriendsAdapter(Context context, ArrayList<User> friends) {
        this.context = context;
        this.friends = friends;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        FriendsAdapter.ViewHolder viewHolder = new FriendsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User friend = friends.get(position);

        holder.nameTextView.setText(friend.getName());
        holder.emailTextView.setText(friend.getEmail());
        Glide.with(context).load(friend.getProfile()).into(holder.friendProfile);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("해당 친구를 삭제하시겠습니까?");
                dialog.setCancelable(true);
                dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeFriend(position);
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

    private void removeFriend(int position) {
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

        FriendRequest friendRequest = new FriendRequest(email, friend.getEmail());
        MyAPI myapi = LoginActivity.get_MyAPI();

        Call<FriendRequest> post_friend_delete = myapi.post_friend_delete("Bearer " + token, friendRequest);

        post_friend_delete.enqueue(new Callback<FriendRequest>() {
            @Override
            public void onResponse(Call<FriendRequest> call, Response<FriendRequest> response) {
                if(response.isSuccessful()) {
                    Log.d("REMOVE FRIEND", "SUCCESS");
                }
                else {
                    Log.d("REMOVE FRIEND", "FAILED");
                }
            }

            @Override
            public void onFailure(Call<FriendRequest> call, Throwable t) {
                Log.d("REMOVE FRIEND", "FAILED");
            }
        });

        friends.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, friends.size());
    }

    @Override
    public int getItemCount() {
        Log.d("############", friends.size() + "");
        return friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView friendProfile;
        TextView nameTextView, emailTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            friendProfile = (ImageView) itemView.findViewById(R.id.friend_profile);
            nameTextView = (TextView) itemView.findViewById(R.id.friend_name_text_view);
            emailTextView = (TextView) itemView.findViewById(R.id.friend_email_text_view);
        }
    }
}
