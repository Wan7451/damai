package com.yztc.damai.ui.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class GuessLikeAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater inflater;
    private ArrayList<GuessLikeBean> data;

    public GuessLikeAdapter(Context context, ArrayList<GuessLikeBean> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GuessLikeHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_guesslike, null);
            holder = new GuessLikeHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GuessLikeHolder) convertView.getTag();
        }
            GuessLikeBean bean = data.get(position);
            holder.guesslikeTitle.setText(bean.getName());
            holder.guesslikeTime.setText("时间："+bean.getShow_time());
            holder.guesslikeVenue.setText("场馆："+bean.getVenue_name());

            String i = String.valueOf(bean.getId());

            String imageURI = NetConfig.BASR_IMG + i.substring(0, i.length() - 2) + "/" + i + "_n.jpg";

            ImageLoader.getInstance().loadImages(
                    holder.guesslikeIcon,
                    imageURI,
                    true);

        return convertView;
    }

    static class GuessLikeHolder {
        @BindView(R.id.guesslike_icon)
        ImageView guesslikeIcon;
        @BindView(R.id.guesslike_title)
        TextView guesslikeTitle;
        @BindView(R.id.guesslike_time)
        TextView guesslikeTime;
        @BindView(R.id.guesslike_venue)
        TextView guesslikeVenue;

        GuessLikeHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
