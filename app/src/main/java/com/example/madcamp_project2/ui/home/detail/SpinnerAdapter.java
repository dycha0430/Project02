package com.example.madcamp_project2.ui.home.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Country;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    Context context;
    List<Country> data;
    LayoutInflater inflater;
    public SpinnerAdapter(Context context, List<Country> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i).getName();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_spinner, viewGroup, false);
        }

        if (data != null) {
            String text = data.get(i).getName();
            ((TextView) view.findViewById(R.id.spinner_item_tv)).setText(text);
            // TODO 알맞는 아이콘으로 바꾸기.
            ((ImageView) view.findViewById(R.id.img_spinner)).setImageResource(R.drawable.tangerine);
        }
        return view;
    }
}
