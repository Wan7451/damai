package com.yztc.damai.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yztc.core.image.ImageLoader;
import com.yztc.core.utils.SPUtils;
import com.yztc.damai.R;
import com.yztc.damai.config.NetConfig;
import com.yztc.damai.help.Constant;
import com.yztc.damai.http.HttpHandlerFactory;
import com.yztc.damai.http.NetResponse;
import com.yztc.damai.ui.recommend.RecommendBean;
import com.yztc.damai.ui.recommend.TypeViewBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanggang on 2016/12/15.
 */

public class Type6View extends TypeContainerView {
    public Type6View(Context context) {
        super(context);
    }

    public Type6View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(TypeViewBean data) {
        super.setData(data);
    }

    @Override
    protected void fillTypeView(final TypeViewBean data) {
        RecyclerView recycler=new RecyclerView(getContext());
        recycler.setNestedScrollingEnabled(false);
        LinearLayoutManager mgr=new LinearLayoutManager(getContext());
        mgr.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler.setLayoutManager(mgr);
        final ArrayList<RecommendBean> list=new ArrayList();
        final RecommendAdapter adapter=new RecommendAdapter(getContext(),list);
        recycler.setAdapter(adapter);
        addView(recycler);

        HashMap<String,String> maps=new HashMap<>();
        int cityId = (int) SPUtils.get(getContext(), Constant.SP_CURR_CITY, 852);
        maps.put("cityId", cityId + "");
        HttpHandlerFactory.getHttpHandler().get("proj/HotProjV1.aspx", maps, new NetResponse() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray array = object.getJSONArray("list");
                    Gson gson=new Gson();
                    for (int i = 0; i < array.length() ; i++) {
                        list.add(gson.fromJson(array.getString(i),RecommendBean.class));
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

    static class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
            if (holder instanceof RecommendHolder) {
                RecommendBean bean = data.get(position);
                ((RecommendHolder) holder).recommendTitle.setText(bean.getName());
                ((RecommendHolder) holder).recommendTime.setText(bean.getShowTime());


                String i = String.valueOf(bean.getProjectID());

                String imageURI = NetConfig.BASR_IMG + i.substring(0, i.length() - 2) + "/" + i + "_n.jpg";

                ImageLoader.getInstance().loadImages(
                        ((RecommendHolder) holder).recommendIcon,
                        imageURI,
                        true
                );
            }
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

        static class RecommendHolder extends RecyclerView.ViewHolder {
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

}
