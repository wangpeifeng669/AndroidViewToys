package com.peterwang.toys.basic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.peterwang.toys.R;

/**
 * 滚动控件 Scroller 滚动测试布局，实现手势滚动ScrollerTestLayout内部控件
 *
 * @author peter_wang
 * @create-time 15/11/9 08:52
 */
public class ScrollerTestLayout extends LinearLayout {

    private Scroller mScroller;
    private Context mContext;

    /**
     * 监听滚动的最小偏差值
     */
    private int mStartMoveSlop;
    /**
     * 记录起点touch的X坐标
     */
    private int mStartMotionX;
    /**
     * 记录上次touch的X坐标
     */
    private int mLastMotionX;

    public ScrollerTestLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public ScrollerTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mScroller = new Scroller(mContext);
        mStartMoveSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();

        setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        addChildView();
    }

    private void addChildView() {
        TextView tv = new TextView(mContext);
        tv.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        tv.setText(mContext.getString(R.string.drag_me));
        tv.setTextColor(mContext.getResources().getColor(android.R.color.white));
        addView(tv);
    }

    @Override
    public void computeScroll() {
        // 判断Scroll是否滚动完成
        if (mScroller.computeScrollOffset()) {
            // 调用View的scrollto完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            //必须调用该方法，否则不一定能看到滚动效果，具体原因查看源码分析
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartMotionX = x;
                mLastMotionX = x;
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = mLastMotionX - x;
                if (Math.abs(deltaX) > mStartMoveSlop) {
                    smoothScrollTo(deltaX);
                    mLastMotionX = x;
                }
                break;

            case MotionEvent.ACTION_UP:
                mLastMotionX = x;
                resetScroll();
                break;
        }
        return true;
    }

    private void resetScroll() {
        // 回到起点
        mScroller.startScroll(mStartMotionX - mLastMotionX, 0, mLastMotionX - mStartMotionX,
                0);
        // 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
        invalidate();
    }

    private void smoothScrollTo(int deltaX) {
        // 设置mScroller的滚动偏移量
        mScroller.startScroll(mStartMotionX - mLastMotionX, 0, deltaX,
                0);
        // 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
        invalidate();
    }
}
