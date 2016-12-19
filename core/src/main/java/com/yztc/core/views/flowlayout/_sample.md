# FlowLayout
Android流式布局，支持单选、多选等，适合用于产品标签等。


##特色
* 以setAdapter形式注入数据
* 直接设置selector为background即可完成标签选则的切换，类似CheckBox
* 支持控制选择的Tag数量，比如：单选、多选
* 支持setOnTagClickListener，当点击某个Tag回调
* 支持setOnSelectListener，当选择某个Tag后回调
* 支持adapter.notifyDataChanged
* Activity重建（或者旋转）后，选择的状态自动保存



### 声明
布局文件中声明：

```
 <com.yztc.core.views.flowlayout.TagFlowLayout
        android:id="@+id/id_flowlayout"
        app:max_select="-1"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:padding="20dp">
    </com.yztc.core.views.flowlayout.TagFlowLayout>
```

支持属性：

`max_select`：-1为不限制选择数量，>=1的数字为控制选择tag的数量
`auto_select_effect` 是否开启默认的选中效果，即为selector中设置的效果，默认为true；如果设置为false，则无选中效果，需要自己在回调中处理。

###设置数据

```
mFlowLayout.setAdapter(new TagAdapter<String>(mVals)
   {
       @Override
       public View getView(FlowLayout parent, int position, String s)
       {
           TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                   mFlowLayout, false);
           tv.setText(s);
           return tv;
       }
   });
```

getView中回调，类似ListView等用法。

### 对于选中状态

选中后标签的显示效果

```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="@color/tag_select_textcolor"
          android:drawable="@drawable/checked_bg"
          android:state_checked="true"></item>
    <item android:drawable="@drawable/normal_bg"></item>
</selector>

```

设置个background，上面一个状态为android:state_checked，另一个为正常。


###事件

```
mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
{
  @Override
  public boolean onTagClick(View view, int position, FlowLayout parent)
  {
      Toast.makeText(getActivity(), mVals[position], Toast.LENGTH_SHORT).show();
      return true;
  }
});
```

点击标签时的回调。

```
mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
{
  @Override
  public void onSelected(Set<Integer> selectPosSet)
  {
      getActivity().setTitle("choose:" + selectPosSet.toString());
  }
});
```
选择多个标签时的回调。

##预先设置Item选中

```
//预先设置选中
mAdapter.setSelectedList(1,3,5,7,8,9);
//获得所有选中的pos集合
flowLayout.getSelectedList();
```



