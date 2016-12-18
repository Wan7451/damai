package com.yztc.damai.ui.cls;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yztc.core.image.ImageLoader;
import com.yztc.damai.R;
import com.yztc.damai.net.NetConfig;
import com.yztc.damai.ui.cls.ClassBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/12.
 */

public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private ArrayList<ClassBean> data;
    private Context context;
    private LayoutInflater inflater;

    public ClassAdapter(Context context, ArrayList<ClassBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_class, parent, false);
        return new ClassHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ClassHolder) {
            ClassHolder clsHolder = (ClassHolder) holder;
            ClassBean clsBean = data.get(position);
            clsHolder.classPrice.setText(clsBean.getPriceName());
            clsHolder.classTime.setText(clsBean.getT());
            clsHolder.classTheatre.setText(clsBean.getVenName());
            clsHolder.classTitle.setText(clsBean.getN());

            clsHolder.classSeat.setText("座");
            clsHolder.classState.setText("售票中");

            if(TextUtils.isEmpty(clsBean.getSummary())){
                clsHolder.classSummery.setVisibility(View.GONE);
            }else {
                clsHolder.classSummery.setVisibility(View.VISIBLE);
                clsHolder.classSummery.setText("『"+clsBean.getSummary()+"』");
            }


            clsHolder.classImg.setImageResource(R.mipmap.ic_launcher);

            String i = String.valueOf(clsBean.getI());

            String imageURI = NetConfig.BASR_IMG + i.substring(0, i.length()-2) + "/" + i + "_n.jpg";

            ImageLoader.getInstance().loadImages(clsHolder.classImg, imageURI, false);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class ClassHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.class_img)
        ImageView classImg;
        @BindView(R.id.class_title)
        TextView classTitle;
        @BindView(R.id.class_seat)
        TextView classSeat;
        @BindView(R.id.class_state)
        TextView classState;
        @BindView(R.id.class_time)
        TextView classTime;
        @BindView(R.id.class_theatre)
        TextView classTheatre;
        @BindView(R.id.class_price)
        TextView classPrice;
        @BindView(R.id.class_summery)
        TextView classSummery;
        public ClassHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
