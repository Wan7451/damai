# RecyclerView-FlexibleDivider

# GitHub

[GitHub URL] (https://github.com/hiphonezhu/RecyclerView-FlexibleDivider)
[GitHub URL] (https://github.com/yqritc/RecyclerView-FlexibleDivider)


# Usage

The following is the simplest usage.  
Drawing a divider drawable retrieved from android.R.attr.listDivider between each cell.
```java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
```

| ItemDecoration         | Usage |
|:------------------|:----------|
| HorizontalDividerItemDecoration         | For layout manager having vertical orientation to draw horizontal divider |
| VerticalDividerItemDecoration         | For layout manager having horizontal orientation to draw vertical divider |
_*Please note that you can only set one of above item decorations at a time._

If you want to set color, size and margin values, you can specify as the followings.
```java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
recyclerView.addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(this)
                .color(Color.RED)
                .sizeResId(R.dimen.divider)
                .marginResId(R.dimen.leftmargin, R.dimen.rightmargin)
                .build());
```


Instead of setting color and size, you can set paint object.
```java
Paint paint = new Paint();
paint.setStrokeWidth(5);
paint.setColor(Color.BLUE);
paint.setAntiAlias(true);
paint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
recyclerView.addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());
```

Also 9patch drawable can be used for drawing divider.
```java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
        .drawable(R.drawable.sample)
        .size(15)
        .build());
```

If you want to customize divider depending on the position, implement the following interfaces.

### List of provider
The following providers can be implemented and controllable for each divider drawn between cells.  
Please refer to ComplexAdapter class in the [sample](/sample/src/main/java/com/yqritc/recyclerviewflexibledivider/sample) for the usage of providers in detail.

- ColorProvider
Provide color for divider

- PaintProvider
Provide paint object for divider line to draw.

- DrawableDivider
Provide drawable object for divider line

- SizeProvider
Provide height for horizontal divider, width for vertical divider.

- VisibilityProvider  
Enables you to control the visibility of dividers.

- MarginProvider for horizontal divider (vertical list)  
Enables you to specify left and right margin of divider.

- MarginProvider for vertical divider (horizontal list)  
Enables you to specify top and bottom margin of divider.  

**For GridLayoutManager**, the position parameter of above providers is group index of items.
So, control your divider based on [group index](http://developer.android.com/intl/ja/reference/android/support/v7/widget/GridLayoutManager.SpanSizeLookup.html#getSpanGroupIndex(int, int)) instead of the position of items.

### Optional
- Builder.showLastDivider  
Draw divider line at the end of last item in RecyclerView.
If you enable this, the range of position parameter of providers listed above is 0 to itemCount-1.
Otherwise, the range is 0 to itemCount-2.  

- Builder.positionInsideItem  
Draw divider inside items.  
If you want to follow [material design guideline](https://www.google.com/design/spec/components/dividers.html#dividers-specs), enable this feature.


### Note
- When neither of color, paint, drawable is set, default divider retrieved from android.R.attr.listDivider will be used.
- When you set Paint, you must use setColor and setStrokeWidth methods of paint class.
- If you want to use DashPathEffect, please note the following issue.
https://code.google.com/p/android/issues/detail?id=29944

##### _Looking for custom ItemDecoration to achieve equal column space for GridLayoutManager?_
Check out [https://gist.github.com/yqritc/ccca77dc42f2364777e1](https://gist.github.com/yqritc/ccca77dc42f2364777e1)

