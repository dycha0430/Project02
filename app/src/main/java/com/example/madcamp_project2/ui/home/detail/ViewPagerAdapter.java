package com.example.madcamp_project2.ui.home.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcamp_project2.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolderPage> {
    private ArrayList<DataPage> listData;

    ViewPagerAdapter(ArrayList<DataPage> data) {
        this.listData = data;
    }


    @NonNull
    @Override
    public ViewHolderPage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolderPage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPage holder, int position) {
        if (holder instanceof ViewHolderPage) {
            ViewHolderPage viewHolderPage = (ViewHolderPage) holder;
            viewHolderPage.onBind(listData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ViewHolderPage extends RecyclerView.ViewHolder{

        DataPage data;

        public ViewHolderPage(@NonNull View itemView) {
            super(itemView);
        }

        public void onBind(DataPage data) {
            this.data = data;
        }
    }
}
