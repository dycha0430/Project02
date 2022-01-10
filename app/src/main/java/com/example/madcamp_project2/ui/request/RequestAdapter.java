package com.example.madcamp_project2.ui.request;


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

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.User;

import java.util.ArrayList;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User friend = friends.get(position);

        holder.nameTextView.setText(friend.getName());
        holder.emailTextView.setText(friend.getEmail());
        // TODO profile 도 설정

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO pending 목록에서 해당 요청 그냥 삭제
            }
        });

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO pending 목록에서 해당 요청 삭제하고 서로의 친구목록에 추가
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
