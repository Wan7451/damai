package com.yztc.damai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yztc.core.image.ImageLoader;
import com.yztc.core.utils.DensityUtil;
import com.yztc.core.views.NoScrollListView;
import com.yztc.damai.R;
import com.yztc.damai.net.NetConfig;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;
import com.yztc.damai.ui.recommend.GuessLikeBean;
import com.yztc.damai.ui.recommend.TypeViewBean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type11View extends TypeContainerView {
    public Type11View(Context context) {
        super(context);
    }

    public Type11View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(TypeViewBean data) {
        super.setData(data);
    }

    @Override
    protected void fillTypeView(final TypeViewBean data) {

        NoScrollListView listView = new NoScrollListView(getContext());

        final ArrayList<GuessLikeBean> list = new ArrayList();
        final GuessLikeAdapter adapter = new GuessLikeAdapter(getContext(), list);
        listView.setAdapter(adapter);
        listView.setDividerHeight(DensityUtil.dip2px(getContext(),1));
        addView(listView);

        HashMap<String, String> maps = new HashMap<>();
        maps.put("cityId", "852");
        maps.put("lon", "116.391");
        maps.put("lat", "39.907");
        maps.put("visitorId", "WDBSUDg%2FcM8DADI8fZp%2FGofv");
        NetUtils.getInstance().get("proj/Intelligence/index.aspx", maps, new NetResponse() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {
                        list.add(gson.fromJson(array.getString(i), GuessLikeBean.class));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String erroe) {

            }
        });
    }

    static class GuessLikeAdapter extends BaseAdapter {


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
            holder.guesslikeTime.setText("时间：" + bean.getShow_time());
            holder.guesslikeVenue.setText("场馆：" + bean.getVenue_name());

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

}
