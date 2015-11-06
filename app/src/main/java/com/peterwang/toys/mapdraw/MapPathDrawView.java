package com.peterwang.toys.mapdraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.peterwang.toys.main.WeakReferenceHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势绘制映射到地图
 *
 * @author peter_wang
 * @create-time 15/11/4 09:15
 */
public class MapPathDrawView extends View {
    /**
     * Handler消息：画成闭合图像时刻点
     */
    private static final int MESSAGE_FINISH_DRAW = 0;
    /**
     * 闭合图像绘制时间分割数
     */
    public static final int FINISH_DRAW_SUM_STEP = 30;
    /**
     * 触摸移动最小感知距离
     */
    private static final float TOUCH_TOLERANCE = 4;

    private Paint mPaint;
    private Path mPath;

    private float mX, mY;

    private List<PathPoint> mPathPointList;
    private PathPointSelectListener mPathPointSelectListener;

    private int mFinishDrawStep = 1;

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler
            extends WeakReferenceHandler<MapPathDrawView> {

        public MyHandler(MapPathDrawView reference) {
            super(reference);
        }

        @Override
        protected void handleMessage(MapPathDrawView view, Message msg) {
            switch (msg.what) {
                case MESSAGE_FINISH_DRAW:
                    float startX = view.mPathPointList.get(view.mPathPointList.size() - 1).x;
                    float startY = view.mPathPointList.get(view.mPathPointList.size() - 1).y;
                    float endX = view.mPathPointList.get(0).x;
                    float endY = view.mPathPointList.get(0).y;
                    float moveX = (endX - startX) / FINISH_DRAW_SUM_STEP;
                    float moveY = (endY - startY) / FINISH_DRAW_SUM_STEP;
                    view.mX = moveX + view.mX;
                    view.mY = moveY + view.mY;
                    view.mPath.lineTo(view.mX, view.mY);
                    view.invalidate();
                    view.mFinishDrawStep++;
                    if (view.mFinishDrawStep <= FINISH_DRAW_SUM_STEP) {
                        sendEmptyMessageDelayed(MESSAGE_FINISH_DRAW, 5);
                    } else {
                        view.mFinishDrawStep = 1;
                        if (view.mPathPointSelectListener != null) {
                            view.mPathPointSelectListener.onPathSelect(view.mPathPointList);
                        }
                        view.mPath.reset();
                        view.mPathPointList.clear();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public MapPathDrawView(Context context) {
        super(context);
        init();
    }

    public MapPathDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPath = new Path();

        // 设置画笔属性
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        mPathPointList = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0x88AAAAAA);

        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 开始画图，并保存绘制点位置
     *
     * @param x 点击位置x坐标
     * @param y 点击位置y坐标
     */
    private void touchStart(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;

        mPathPointList.clear();
        PathPoint pathPoint = new PathPoint();
        pathPoint.x = x;
        pathPoint.y = y;
        mPathPointList.add(pathPoint);
    }

    /**
     * 滑动画图，并保存绘制点位置
     *
     * @param x 点击位置x坐标
     * @param y 点击位置y坐标
     */
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            PathPoint pathPoint = new PathPoint();
            pathPoint.x = x;
            pathPoint.y = y;
            mPathPointList.add(pathPoint);
        }
    }

    /**
     * 画图结束，并保存绘制点位置
     *
     * @param x 点击位置x坐标
     * @param y 点击位置y坐标
     */
    private void touchUp(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            PathPoint pathPoint = new PathPoint();
            pathPoint.x = x;
            pathPoint.y = y;
            mPathPointList.add(pathPoint);
        }

        finishPath();
    }

    /**
     * 画成闭合图形
     */
    private void finishPath() {
        mHandler.sendEmptyMessageDelayed(MESSAGE_FINISH_DRAW, 5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                break;

            case MotionEvent.ACTION_UP:
                touchUp(x, y);
                break;
        }
        invalidate();
        return true;
    }

    public void setPathPointSelectListener(PathPointSelectListener pathPointSelectListener) {
        mPathPointSelectListener = pathPointSelectListener;
    }

    public class PathPoint {
        public float x;
        public float y;
    }

    /**
     * 检测手势结束传递路径点
     */
    public interface PathPointSelectListener {
        void onPathSelect(List<PathPoint> pathPointList);
    }
}