package com.yztc.niuniu.ui.maomi.girls;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yztc.niuniu.R;
import com.yztc.niuniu.ui.detail.OpenDetailListener;
import com.yztc.niuniu.util.ImageLoager;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/20.
 */

public class GirlsAdapter extends RecyclerView.Adapter<GirlsAdapter.SihuViewHolder> {

    private final LayoutInflater inflater;
    private Context context;
    private ArrayList<GirlsBean> data;

    private OpenDetailListener l;

    public GirlsAdapter(Context context, ArrayList<GirlsBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public void setIOpenDetailListener(OpenDetailListener l) {
        this.l = l;
    }

    @Override
    public SihuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_image, parent, false);
        SihuViewHolder holder = new SihuViewHolder(view);
        holder.setL(l);
        return holder;
    }

    @Override
    public void onBindViewHolder(SihuViewHolder holder, int position) {
        holder.fill(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class SihuViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;

        public SihuViewHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView;
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (l != null) {
                        l.openDetailWithPosition(getAdapterPosition());
                    }
                }
            });
        }

        public void fill(GirlsBean data) {
            ImageLoager.load(imgView.getContext(), data.getImage(), imgView);
        }

        private OpenDetailListener l;

        public void setL(OpenDetailListener l) {
            this.l = l;
        }

    }
}
