package com.yztc.core.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by wanggang on 2016/12/16.
 */

//ListView 嵌套ScrollView
//ListView 的高度 = 所有ItemView高度的和
// ListView  的ItemView 不能复用
// addHeaderView   addFooterView

//ScrollVoew  ->  LinearLayout  - > 头布局  —> NoScrollListView    占内存
//ListView     addHeaderView  添加其他的头部视图  addHeaderView其实就是 ItemView
//             其实就是ListView的多ItemView

public class NoScrollListView extends ListView {
    public NoScrollListView(Context context) {
        super(context);
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
