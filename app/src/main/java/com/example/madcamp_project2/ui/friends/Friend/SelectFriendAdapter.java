package com.example.madcamp_project2.ui.friends.Friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.User;
import com.example.madcamp_project2.ui.home.addtrip.AddExtraActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SelectFriendAdapter extends RecyclerView.Adapter<SelectFriendAdapter.ViewHolder> {
    Context context;
    BottomSheetDialogFragment bottomSheetDialogFragment;
    int selected_position;
    TripPlan tripPlan;
    public SelectFriendAdapter(Context context, BottomSheetDialogFragment bottomSheetDialogFragment, int selected_position, TripPlan tripPlan) {
        this.context = context;
        this.bottomSheetDialogFragment = bottomSheetDialogFragment;
        this.selected_position = selected_position;
        this.tripPlan = tripPlan;
    }

    public SelectFriendAdapter(Context context, BottomSheetDialogFragment bottomSheetDialogFragment, int selected_position) {
        this.context = context;
        this.bottomSheetDialogFragment = bottomSheetDialogFragment;
        this.selected_position = selected_position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_small, parent, false);
        SelectFriendAdapter.ViewHolder viewHolder = new SelectFriendAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = AddExtraActivity.unSelectedFriends.get(position);

        Glide.with(context).load(user.getProfile()).into(holder.profileImage);
        holder.nameTextView.setText(user.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selected_position == -1) {
                    // TODO 해당 친구에게 요청 보내기
                    bottomSheetDialogFragment.dismiss();
                    return;
                }
                AddExtraActivity.selectedFriends.get(selected_position).setUser(user);
                AddExtraActivity.unSelectedFriends.remove(user);

                AddExtraActivity.friendAddAdapter.notifyDataSetChanged();
                bottomSheetDialogFragment.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return AddExtraActivity.unSelectedFriends.size();
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
