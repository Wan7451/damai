package com.yztc.damai.ui.cls;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.signature.StringSignature;
import com.yztc.damai.R;
import com.yztc.damai.config.NetConfig;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/12.
 */

public class ClassAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ClassBean> data;
    private Fragment fragment;
    private LayoutInflater inflater;
    private RequestManager requestManager;

    public ClassAdapter(Fragment fragment, RequestManager requestManager, ArrayList<ClassBean> data) {
        this.fragment = fragment;
        this.data = data;
        this.requestManager = requestManager;
        inflater = LayoutInflater.from(fragment.getContext());
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

            StringSignature signature = new StringSignature(imageURI);

            requestManager.load(imageURI).dontAnimate().signature(signature).into(clsHolder.classImg);
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
