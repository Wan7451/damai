package com.yztc.niuniu.ui.sihu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yztc.niuniu.ui.detail.OpenDetailListener;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/20.
 */

public class SihuAdapter extends RecyclerView.Adapter<SihuAdapter.SihuViewHolder> {

    private final LayoutInflater inflater;
    private Context context;
    private ArrayList<SihuBean> data;

    private OpenDetailListener l;

    public void setIOpenDetailListener(OpenDetailListener l) {
        this.l = l;
    }

    public SihuAdapter(Context context, ArrayList<SihuBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public SihuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
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

        private final TextView title;

        public SihuViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void fill(SihuBean data) {
            title.setText(data.getTitle());
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (l != null) {
                        l.openDetailWithPosition(getAdapterPosition());
                    }
                }
            });
        }

        private OpenDetailListener l;

        public void setL(OpenDetailListener l) {
            this.l = l;
        }
    }
}
