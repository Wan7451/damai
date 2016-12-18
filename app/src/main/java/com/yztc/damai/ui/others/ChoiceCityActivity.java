package com.yztc.damai.ui.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.google.gson.Gson;
import com.yztc.core.base.BaseActivity;
import com.yztc.core.utils.DensityUtil;
import com.yztc.damai.R;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;
import com.yztc.damai.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoiceCityActivity extends BaseActivity {

    @BindView(R.id.cityList)
    FloatingGroupExpandableListView cityList;

    @BindView(R.id.activity_choice_city)
    RelativeLayout activityChoiceCity;

    @BindView(R.id.sideBar)
    SideBar sideBar;

    private ArrayList<GroupBean> data = new ArrayList<>();
    private CityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("选择城市");

        adapter = new CityAdapter(this, data);

        cityList.setDividerHeight(1);
        cityList.setGroupIndicator(null);
        View header = getLayoutInflater().inflate(R.layout.layout_choicecity_header, null, false);
        cityList.addHeaderView(header);

        WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        cityList.setAdapter(wrapperAdapter);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getSpellCode().startsWith(s)) {
                        cityList.setSelectedGroup(i);
                        break;
                    }
                }
            }
        });
        loadCityData();
    }

    private void loadCityData() {

        HashMap<String, String> maps = new HashMap<>();
        maps.put("useCash", false + "");
        NetUtils.getInstance().get("ncitylistv1.aspx", maps, new NetResponse() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("groups");
                    data.clear();
                    Gson gson = new Gson();
                    for (int i = 0, len = array.length(); i < len; i++) {
                        data.add(gson.fromJson(array.getString(i), GroupBean.class));
                    }
                    adapter.notifyDataSetChanged();
                    String[] b = new String[data.size()];
                    for (int i = 0; i < data.size(); i++) {
                        cityList.expandGroup(i);
                        b[i] = data.get(i).getSpellCode();
                    }
                    sideBar.setData(b);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String erroe) {

            }
        });

    }


    static class CityAdapter extends BaseExpandableListAdapter {


        private Context context;
        private ArrayList<GroupBean> data;
        private LayoutInflater inflater;

        public CityAdapter(Context context, ArrayList<GroupBean> data) {
            this.context = context;
            this.data = data;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getGroupCount() {
            return data.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return data.get(groupPosition).getSites().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return data.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return data.get(groupPosition).getSites().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(context);
                int px = DensityUtil.dip2px(context, 5);
                convertView.setPadding(DensityUtil.dip2px(context, 16), px, 0, px);
                int color = context.getResources().getColor(R.color.divider);
                convertView.setBackgroundColor(color);
            }
            ((TextView) convertView).setText(data.get(groupPosition).getSpellCode());

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null, false);
            String text = data.get(groupPosition).getSites().get(childPosition).getN();
            ((TextView) convertView).setText(text);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


    public static void startForResult(Fragment context, int resquest) {
        Intent i = new Intent();
        i.setClass(context.getContext(), ChoiceCityActivity.class);
        context.startActivityForResult(i, resquest);
    }
}
