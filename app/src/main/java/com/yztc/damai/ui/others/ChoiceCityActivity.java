package com.yztc.damai.ui.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.google.gson.Gson;
import com.yztc.core.base.BaseActivity;
import com.yztc.core.utils.DensityUtil;
import com.yztc.core.utils.SPUtils;
import com.yztc.core.views.SideBar;
import com.yztc.core.views.flowlayout.FlowLayout;
import com.yztc.core.views.flowlayout.TagAdapter;
import com.yztc.core.views.flowlayout.TagFlowLayout;
import com.yztc.damai.R;
import com.yztc.damai.help.Constant;
import com.yztc.damai.help.Event;
import com.yztc.damai.net.NetResponse;
import com.yztc.damai.net.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

        initList();

        //设置右侧触摸监听
        initRightBar();


        loadCityData();
    }

    private void initList() {
        adapter = new CityAdapter(this, data);

        cityList.setDividerHeight(1);
        cityList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        cityList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                CityBean cityBean = data.get(groupPosition).getSites().get(childPosition);
                setResultData(cityBean.getI(), cityBean.getN());
                return false;
            }
        });
        cityList.setGroupIndicator(null);
        View header = getLayoutInflater().inflate(R.layout.layout_choicecity_header, null, false);
        cityList.addHeaderView(header);

        initFlowLayout(header);

        WrapperExpandableListAdapter wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        cityList.setAdapter(wrapperAdapter);
    }

    private void initFlowLayout(View header) {
        final TagFlowLayout flowLayout = (TagFlowLayout)
                header.findViewById(R.id.flowLayout);
        flowLayout.setMaxSelectCount(-1);


        final List<Integer> code = new ArrayList<>();
        code.add(852);
        code.add(872);
        code.add(893);
        code.add(906);
        code.add(586);
        code.add(1377);
        final List<String> citys = new ArrayList<>();
        citys.add("北京");
        citys.add("上海");
        citys.add("广州");
        citys.add("深圳");
        citys.add("武汉");
        citys.add("成都");

        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {

                setResultData(code.get(position), citys.get(position));

                return true;
            }
        });


        flowLayout.setAdapter(new TagAdapter<String>(citys) {

            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView text = (TextView) getLayoutInflater().inflate(R.layout.item_hot_city, null, false);
                text.setText(o);
                return text;
            }
        });
    }

    private void initRightBar() {
        int color = getResources().getColor(R.color.colorPrimaryDark);
        sideBar.setColor(color);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getSpellCode().startsWith(s)) {
                        cityList.setSelectedGroup(i - 1);
                        break;
                    }
                }
            }
        });
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

    public void setResultData(int cityId, String cityName) {
        SPUtils.put(this, Constant.SP_CURR_CITY, cityId);
        SPUtils.put(this, Constant.SP_CURR_CITY_N, cityName);
        setResult(RESULT_OK);
        EventBus.getDefault().post(new Event(Event.EVENT_CITY_CHANGE, null));
        finish();
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


    public static void start(Context context) {
        Intent i = new Intent();
        i.setClass(context, ChoiceCityActivity.class);
        context.startActivity(i);
    }
}
