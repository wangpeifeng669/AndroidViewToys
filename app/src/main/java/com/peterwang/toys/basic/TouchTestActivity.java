package com.peterwang.toys.basic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * touch 事件测试
 *
 * @author peter_wang
 * @create-time 15/11/6 08:23
 */
public class TouchTestActivity extends Activity {
    public static final String TAG = TouchTestActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchLayout touchOuterLayout = new TouchLayout(this,"out");
        TouchLayout touchInnerLayout = new TouchLayout(this,"in");
        touchInnerLayout.isInterceptTouchEvent = true;
        touchInnerLayout.isTouchEvent = true;
        TouchView touchView = new TouchView(this);
        touchView.setText("测试的View");
        touchInnerLayout.addView(touchView);
        touchOuterLayout.addView(touchInnerLayout);
        setContentView(touchOuterLayout);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "call Activity dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "call Activity onTouchEvent");
        return true;
    }

    class TouchLayout extends LinearLayout {
        private String mName;
        public boolean isInterceptTouchEvent = false;
        public boolean isTouchEvent = false;

        public TouchLayout(Context context, String name) {
            super(context);
            mName = name;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.i(TAG, "call ViewGroup onInterceptTouchEvent,name " + mName);
            return isInterceptTouchEvent;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.i(TAG, "call ViewGroup onTouchEvent,name " + mName);
            return isTouchEvent;
        }

        @Override
        public void setOnTouchListener(OnTouchListener l) {
            super.setOnTouchListener(l);
            Log.i(TAG, "call ViewGroup OnTouchListener,name " + mName);
        }
    }

    class TouchView extends TextView {
        public TouchView(Context context) {
            super(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.i(TAG, "call View onTouchEvent " + event.getAction());
            return true;
        }
    }
}
