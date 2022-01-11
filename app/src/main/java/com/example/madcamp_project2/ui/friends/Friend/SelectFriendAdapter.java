package com.example.madcamp_project2.ui.friends.Friend;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madcamp_project2.LoginActivity;
import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.MyAPI;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.TripPlan;
import com.example.madcamp_project2.ui.User;
import com.example.madcamp_project2.ui.home.addtrip.AddExtraActivity;
import com.example.madcamp_project2.ui.home.addtrip.Travel.TravelRequestSend;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

                    MyAPI myapi = LoginActivity.get_MyAPI();
                    ArrayList<User> to_send = new ArrayList<>();
                    to_send.add(user);
                    TravelRequestSend travelRequestSend = new TravelRequestSend(email, to_send, tripPlan.getTravel_id());

                    Call<TravelRequestSend> post_travel_request = myapi.post_travel_request("Bearer " + token, travelRequestSend);
                    post_travel_request.enqueue(new Callback<TravelRequestSend>() {
                        @Override
                        public void onResponse(Call<TravelRequestSend> call, Response<TravelRequestSend> response) {
                            if(response.isSuccessful()) {
                                Log.d("REQUEST TO JOIN", "SUCCESS");
                            }
                            else {
                                Log.d("REQUEST TO JOIN", "FAILED");
                            }
                        }

                        @Override
                        public void onFailure(Call<TravelRequestSend> call, Throwable t) {
                            Log.d("REQUEST TO JOIN", "FAILED");
                        }
                    });

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
