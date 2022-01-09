package com.example.madcamp_project2.ui.friends;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madcamp_project2.MainActivity;
import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.User;

import java.util.ArrayList;

public class AddFriendSpinnerAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    int position;
    public AddFriendSpinnerAdapter(Context context, int position) {
        this.context = context;
        this.position = position;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return MainActivity.thisUser.getFriends().size();
    }

    @Override
    public Object getItem(int i) {

        return MainActivity.thisUser.getFriends().get(i).getName();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_spinner_friend, viewGroup, false);
        }

        ArrayList<User> friends = MainActivity.thisUser.getFriends();
        if (friends != null) {
            ((TextView) view.findViewById(R.id.friend_name_text_view)).setText(friends.get(i).getName());
            ((TextView) view.findViewById(R.id.friend_email_text_view)).setText(friends.get(i).getEmail());
            // ((ImageView) view.findViewById(R.id.friend_profile)).setImageDrawable();
        }
        return view;
    }
}
