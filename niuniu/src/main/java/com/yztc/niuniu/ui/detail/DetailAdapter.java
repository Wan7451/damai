package com.yztc.niuniu.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yztc.niuniu.R;
import com.yztc.niuniu.util.ImageLoager;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/14.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailHolder> {

    private final LayoutInflater inflater;
    private Context context;
    private ArrayList<String> data;

    public DetailAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_image, parent, false);
        return new DetailHolder(view, data);
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position) {
        holder.fill(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class DetailHolder extends RecyclerView.ViewHolder {

        ImageView imgView;

        public DetailHolder(View itemView, final ArrayList<String> data) {
            super(itemView);
            imgView = (ImageView) itemView;
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(v.getContext(), PagerActivity.class);
                    intent.putExtra("imgs", data);
                    intent.putExtra("curr", getAdapterPosition());
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void fill(String path) {
            ImageLoager.load(imgView.getContext(), path, imgView);
        }
    }
}
