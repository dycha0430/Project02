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
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.home.addtrip.AddExtraActivity;

public class FriendAddAdapter extends RecyclerView.Adapter<FriendAddAdapter.ViewHolder>{

    Context context;
    public FriendAddAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spinner_friend, parent, false);
        FriendAddAdapter.ViewHolder viewHolder = new FriendAddAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddFriendSpinnerAdapter adapter = new AddFriendSpinnerAdapter(context, position);
        holder.spinner.setAdapter(adapter);

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AddExtraActivity.selectedFriends.get(position).setUser(MainActivity.thisUser.getFriends().get(i));
                AddExtraActivity.selectedFriends.add(MainActivity.thisUser.getFriends().get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public int getItemCount() {
        Log.d("$$$$$$$$$$$$$$4", AddExtraActivity.spinnerNum + "");
        return AddExtraActivity.spinnerNum;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Spinner spinner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spinner = (Spinner) itemView.findViewById(R.id.add_friend_spinner);
        }
    }
}
