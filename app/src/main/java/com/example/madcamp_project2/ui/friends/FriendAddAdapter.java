package com.example.madcamp_project2.ui.friends;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.User;
import com.example.madcamp_project2.ui.friends.Friend.FriendBottomSheetDialog;
import com.example.madcamp_project2.ui.home.addtrip.AddExtraActivity;
import com.example.madcamp_project2.ui.home.detail.BottomSheetDialog;

public class FriendAddAdapter extends RecyclerView.Adapter<FriendAddAdapter.ViewHolder>{

    Context context;
    public FriendAddAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_small, parent, false);
        FriendAddAdapter.ViewHolder viewHolder = new FriendAddAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = AddExtraActivity.selectedFriends.get(position);

        Glide.with(context).load(user.getProfile()).into(holder.profileImage);
        holder.nameTextView.setText(user.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 0) return;
                FriendBottomSheetDialog bottomSheetDialog = new FriendBottomSheetDialog(context, position);
                bottomSheetDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "bottomSheet");
            }
        });
    }


    @Override
    public int getItemCount() {
        return AddExtraActivity.selectedFriendNum;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView nameTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.friend_profile_small);
            nameTextView = itemView.findViewById(R.id.friend_name_text_view_small);
        }
    }
}
