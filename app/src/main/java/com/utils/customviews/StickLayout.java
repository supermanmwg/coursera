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
import android.widget.Scroller;

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
    private Scroller mScroller;

    public StickLayout(Context context) {
        super(context);
        mScroller = new Scroller(getContext());
    }

    public StickLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(getContext());
    }

    @TargetApi(11)
    public StickLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(getContext());
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
                if(!mScroller.isFinished()) {
                    intercepted = true;
                }
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
        int deltaY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "move header height is " + mHeaderHeight);
                if(mHeaderHeight > 0) {
                     deltaY = y - mLastY;
                    mHeaderHeight -= deltaY;
                } else {
                    deltaY = 0;
                }

                if(mHeaderHeight < 0) {
                    deltaY =  mHeaderHeight + deltaY;
                    mHeaderHeight = 0;
                }
                Log.d(TAG, "y is " + y + ", mLastY is " + mLastY + ", mHeaderHeight is " + mHeaderHeight + "deltaY is " + deltaY);
            /*    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mHeader.getLayoutParams();
                p.setMargins(0, 0 - mHeaderHeight, 0, 0);
                mHeader.requestLayout();*/
                scrollBy(0, -deltaY);
                break;
            case MotionEvent.ACTION_UP:
                smoothScrollBy(0, mOriginalHeaderHeight - mHeaderHeight);
                mHeaderHeight = mOriginalHeaderHeight;
                break;
        }

        mLastY = y;

        return true;
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(0, getScrollY(), 0, dy, 500);
        invalidate();

    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setGiveUpTouchListener(IGiveUpTouchListener giveUpTouchListener) {
        this.giveUpTouchListener = giveUpTouchListener;
    }
}
