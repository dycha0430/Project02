package com.example.madcamp_project2.ui.home.detail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madcamp_project2.R;
import com.example.madcamp_project2.ui.Country;

import java.util.List;

public class PlaceSpinnerAdapter extends BaseAdapter {
    Context context;
    String[] places;
    LayoutInflater inflater;
    public PlaceSpinnerAdapter(Context context, String[] places) {
        this.context = context;
        this.places = places;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return places.length;
    }

    @Override
    public Object getItem(int i) {
        return places[i];
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

        ImageView icon = view.findViewById(R.id.img_spinner);

        if (places != null) {
            String text = places[i];
            ((TextView) view.findViewById(R.id.spinner_item_tv)).setText(text);

            switch (i) {
                case 0:
                    icon.setImageResource(R.drawable.restaurant);
                    break;
                case 1:
                    icon.setImageResource(R.drawable.cafe);
                    break;
                case 2:
                    icon.setImageResource(R.drawable.bakery);
                    break;
                case 3:
                    icon.setImageResource(R.drawable.hotel);
                    break;
                case 4:
                    icon.setImageResource(R.drawable.bar);
            }
        }
        return view;
    }
}
