package com.yztc.niuniu.ui.av;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yztc.niuniu.R;
import com.yztc.niuniu.ui.detail.OpenDetailListener;
import com.yztc.niuniu.util.ImageLoager;

import java.util.ArrayList;

/**
 * Created by wanggang on 2017/2/13.
 */

public class AvAdapter extends RecyclerView.Adapter<AvAdapter.ListHolder> {

    private final LayoutInflater inflater;

    private Context context;
    private ArrayList<AvBean> data;
    private OpenDetailListener l;

    public void setIOpenDetailListener(OpenDetailListener l) {
        this.l = l;
    }

    public AvAdapter(Context context, ArrayList<AvBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view, parent, false);
        ListHolder holder = new ListHolder(view);
        holder.setL(l);
        return holder;
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        holder.fill(data.get(position));
    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    static class ListHolder extends RecyclerView.ViewHolder {


        private ImageView itemImgs;
        private TextView itemText;
        private OpenDetailListener l;

        public ListHolder(View view) {
            super(view);
            itemImgs = (ImageView) view.findViewById(R.id.item_imgs);
            itemText = (TextView) view.findViewById(R.id.item_text);

        }

        public void setL(OpenDetailListener l) {
            this.l = l;
        }

        public void fill(final AvBean bean) {
            ImageLoager.load(itemText.getContext(),
                    bean.getImg(),
                    itemImgs);
            itemText.setText(bean.getTitle());
            itemImgs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (l != null) {
                        l.openDetailWithPosition(getAdapterPosition());
                    }
                }
            });
        }
    }

}
