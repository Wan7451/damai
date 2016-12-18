package com.yztc.damai.ui.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yztc.core.image.ImageLoader;
import com.yztc.damai.R;
import com.yztc.damai.net.NetConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/16.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<RecommendBean> data;

    public RecommendAdapter(Context context, ArrayList<RecommendBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_recommend, null);
        return new RecommendHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof RecommendHolder){
            RecommendBean bean = data.get(position);
            ((RecommendHolder) holder).recommendTitle.setText(bean.getName());
            ((RecommendHolder) holder).recommendTime.setText(bean.getShowTime());


            String i = String.valueOf(bean.getProjectID());

            String imageURI = NetConfig.BASR_IMG  + i.substring(0, i.length()-2) + "/" + i + "_n.jpg";

            ImageLoader.getInstance().loadImages(
                    ((RecommendHolder) holder).recommendIcon,
                    imageURI,
                    true
            );
        }
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static class RecommendHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.recommend_icon)
        ImageView recommendIcon;
        @BindView(R.id.recommend_title)
        TextView recommendTitle;
        @BindView(R.id.recommend_time)
        TextView recommendTime;

        RecommendHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
