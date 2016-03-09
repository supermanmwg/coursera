package com.utils.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.NoSuchElementException;

/**
 * Created by weiguangmeng on 16/3/9.
 */
public class StickLayout extends LinearLayout {
    private final static String TAG = "StickLayout";

    private View mHeader;
    private int mScaleSlop;
    private int mHeaderHeight;
    private int mOriginalHeaderHeight;
    private int mLastY;
    private int mLastYIntercept;
    private boolean mInitDataSuccess = false;
    private IGiveUpTouchListener  giveUpTouchListener;

    public StickLayout(Context context) {
        super(context);
    }

    public StickLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public StickLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        init();
    }

    private void init() {
        int headerId = getResources().getIdentifier("stick_header", "id", getContext().getPackageName());
        if(headerId != 0) {
            mHeader = findViewById(headerId);
            mScaleSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
            mHeaderHeight = mOriginalHeaderHeight = mHeader.getMeasuredHeight();
            Log.d(TAG, "init mHeaderHeight is " + mHeaderHeight);
            if(mOriginalHeaderHeight > 0) {
                mInitDataSuccess = true;
            }
        } else {
            throw new NoSuchElementException("stick header is not exist");
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;

        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastYIntercept = y;
                mLastY = y;  //这里面如果没有这句就是一个坑啊,因为在aciton down事件的时候,因为返回为false,所以不会调用StickLayout的onTouchEvent
                intercepted = false;
                Log.d(TAG, "1 action down");
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastYIntercept;
                if(giveUpTouchListener.giveUpTouchEvent(event) && deltaY > mScaleSlop) {
                    intercepted = true;
                }
                Log.d(TAG, "2 action move, y is " + y + ", mLastYIntercept is " + mLastYIntercept);
                Log.d(TAG, "2 action move, intercepted is " + intercepted + ", deltaY is " + deltaY + ",mScaleSlop is " + mScaleSlop);
                break;
            case MotionEvent.ACTION_UP:
                mLastYIntercept = 0;
                break;
        }

        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastY;
                Log.d(TAG, "move header height is " + mHeaderHeight);
                mHeaderHeight -= deltaY;
                Log.d(TAG, "y is " + y + ", mLastY is " + mLastY + ", mHeaderHeight is " + mHeaderHeight);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mHeader.getLayoutParams();
                p.setMargins(0, 0 - mHeaderHeight , 0, 0);
                mHeader.requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        mLastY = y;

        return true;
    }

    public void setGiveUpTouchListener(IGiveUpTouchListener giveUpTouchListener) {
        this.giveUpTouchListener = giveUpTouchListener;
    }
}
