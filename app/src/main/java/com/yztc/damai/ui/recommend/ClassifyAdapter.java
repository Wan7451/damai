package com.yztc.damai.ui.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yztc.damai.R;
import com.yztc.damai.image.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/15.
 */

public class ClassifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ClassifyBean> classifys;

    public ClassifyAdapter(Context context, ArrayList<ClassifyBean> classifys) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.classifys=classifys;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_classify, null);
        return new ClassifyHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ClassifyHolder){
            ((ClassifyHolder) holder).classifyIcon.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.getInstance().loadImages(((ClassifyHolder) holder).classifyIcon,classifys.get(position).getImg(),false);
            ((ClassifyHolder) holder).classifyTitle.setText(classifys.get(position).getName());
        }
    }

    @Override
    public int getItemCount() {
        return classifys!=null?classifys.size():0;
    }

    static class ClassifyHolder  extends RecyclerView.ViewHolder{
        @BindView(R.id.classify_icon)
        ImageView classifyIcon;

        @BindView(R.id.classify_title)
        TextView classifyTitle;

        ClassifyHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
