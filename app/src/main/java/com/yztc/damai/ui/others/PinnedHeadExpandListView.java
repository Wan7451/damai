package com.yztc.damai.ui.others;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Created by wanggang on 2016/12/18.
 */

@Deprecated
public class PinnedHeadExpandListView extends ExpandableListView {

    public static final int PINNED_HEADER_GONE = 0;

    public static final int PINNED_HEADER_VISIBLE = 1;

    public static final int PINNED_HEADER_PUSHED_UP = 2;

    private ExpandableListAdapter mAdapter;
    private View mHeaderView;
    private boolean mHeaderViewVisible;

    private int mHeaderViewWidth;
    private int mHeaderViewHeight;

    public PinnedHeadExpandListView(Context context) {
        super(context);
    }

    public PinnedHeadExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PinnedHeadExpandListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setPinnedHeaderView(View view) {
        mHeaderView = view;
        if (null != mHeaderView) {
            setFadingEdgeLength(0);
        }
        requestLayout();
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = adapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null != mHeaderView) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (null != mHeaderView) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            configureHeaderView(getFirstVisiblePosition());
        }
    }

    private final Rect mRect = new Rect();
    private final int[] mLocation = new int[2];

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mHeaderView == null) return super.dispatchTouchEvent(ev);

        if (mHeaderViewVisible) {
            final int x = (int) ev.getX();
            final int y = (int) ev.getY();
            mHeaderView.getLocationOnScreen(mLocation);
            mRect.left = mLocation[0];
            mRect.top = mLocation[1];
            mRect.right = mLocation[0] + mHeaderView.getWidth();
            mRect.bottom = mLocation[1] + mHeaderView.getHeight();

            if (mRect.contains(x, y)) {
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    performViewClick(x, y);
                }
                return true;
            } else {
                return super.dispatchTouchEvent(ev);
            }
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    private void performViewClick(int x, int y) {
        if (null == mHeaderView) return;

        final ViewGroup container = (ViewGroup) mHeaderView;
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);

            /**
             * transform coordinate to find the child view we clicked
             * getGlobalVisibleRect used for android 2.x, getLocalVisibleRect
             * user for 3.x or above, maybe it's a bug
             */
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                view.getGlobalVisibleRect(mRect);
            } else {
                view.getLocalVisibleRect(mRect);
                int width = mRect.right - mRect.left;
                mRect.left = Math.abs(mRect.left);
                mRect.right = mRect.left + width;
            }

            if (mRect.contains(x, y)) {
                view.performClick();
                break;
            }
        }
    }

    public void configureHeaderView(int position) {
        if (null == mHeaderView) return;

        final int group = getPackedPositionGroup(getExpandableListPosition(position));
        int state, nextSectionPosition = getFlatListPosition(getPackedPositionForGroup(group + 1));

        if (mAdapter.getGroupCount() == 0) {
            state = PINNED_HEADER_GONE;
        } else if (position < 0) {
            state = PINNED_HEADER_GONE;
        } else if (nextSectionPosition != -1 && position == nextSectionPosition - 1) {
            state = PINNED_HEADER_PUSHED_UP;
        } else {
            state = PINNED_HEADER_VISIBLE;
        }

        switch (state) {
            case PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case PINNED_HEADER_VISIBLE: {
                if (mHeaderView.getTop() != 0) {
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }

                mHeaderViewVisible = true;
                break;
            }

            case PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(1);
                if (firstView == null) {
                    if (mHeaderView.getTop() != 0) {
                        mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                    }
                    mHeaderViewVisible = true;
                    break;
                }
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();
                int y;
                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    mHeaderViewVisible = true;
                } else {
                    y = 0;
                    mHeaderViewVisible = position != 0;
                }

                if (mHeaderView.getTop() != y) {
                    y = 0;
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                break;
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
}
