package com.utils.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.transition.Slide;
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
    public static final int LAYOUT_MODE = 1;
    public static final int SCROLL_MODE = 2;

    private int slideMode = SCROLL_MODE;

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
                if(!mScroller.isFinished() && SCROLL_MODE == slideMode) {
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
                if(LAYOUT_MODE == slideMode) {
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mHeader.getLayoutParams();
                    p.setMargins(0, 0 - mHeaderHeight, 0, 0);
                    mHeader.requestLayout();
                } else if (SCROLL_MODE == slideMode) {
                    scrollBy(0, -deltaY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(LAYOUT_MODE == slideMode) {
                    smoothSetHeaderHeight(0 - mHeaderHeight, 0 - mOriginalHeaderHeight, 500);
                } else if (SCROLL_MODE == slideMode) {
                    smoothScrollBy(0, mOriginalHeaderHeight - mHeaderHeight, 500);
                }
               mHeaderHeight = mOriginalHeaderHeight;
                break;
        }

        mLastY = y;

        return true;
    }

    private void smoothScrollBy(int dx, int dy, int duration) {
        mScroller.startScroll(dx, getScrollY(), dx, dy, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }


    public void smoothSetHeaderHeight(final int from, final int to, long duration) {
        final int frameCount = (int) (duration / 1000f * 30) + 1;  //一秒钟大约30帧
        final float partation = (to - from) / (float) frameCount;  //每一帧的距离
        new Thread("Thread#smoothSetHeaderHeight") {

            @Override
            public void run() {
                for (int i = 0; i < frameCount; i++) {
                    final int height;
                    if (i == frameCount - 1) {
                        height = to;
                    } else {
                        height = (int) (from + partation * i);
                    }
                    post(new Runnable() {
                        public void run() {
                            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) mHeader.getLayoutParams();
                            p.setMargins(0, height, 0, 0);
                            mHeader.requestLayout();
                        }
                    });
                    try {
                        sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

        }.start();
    }

    public void setGiveUpTouchListener(IGiveUpTouchListener giveUpTouchListener) {
        this.giveUpTouchListener = giveUpTouchListener;
    }
}
