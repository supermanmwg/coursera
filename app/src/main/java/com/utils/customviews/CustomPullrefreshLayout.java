package com.utils.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.coursera.R;


/**
 * Created by supermanmwg on 15-9-10.
 */
public class CustomPullrefreshLayout extends ViewGroup {

    //for debug

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 500;
    //status
    private final int STATUS_MOVE = 1;
    private final int STATUS_UP = 2;
    private final int STATUS_REFRESH_COMPLETE = 3;
    private final int STATUS_DOWN = 4;
    //for  animation Offset To CorrectPosition
    private final DecelerateInterpolator mDecelerateInterpolator;
    /**
     * layoutManager的类型（枚举）
     */
    protected LAYOUT_MANAGER_TYPE layoutManagerType;
    //for pull down view
    private View mContentView;
    private View mHeaderView;
    private ImageView mHeaderImageView;
    //for pull up view
    private View mPeopleLineView;
    private View mShitView;
    //for listener
    private OnRefreshListener mListener;
    //working parameter
    private CustomIndicator mCustomIndicator;
    private RotateAnimation mAnimation;
    private PullUpIndicator mPullUpIndicator;
    private final Animation mPullUpAnimationToCorrPos = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int currentOffsetY = (int) ((mPullUpIndicator.getBaseOffsetY() - mPullUpIndicator.getDestOffsetY()) * (1 - interpolatedTime)) + mPullUpIndicator.getDestOffsetY();
            int tempMove = mPullUpIndicator.getCurrentOffsetY() - currentOffsetY;
            mPullUpIndicator.setCurrentOffsetY(currentOffsetY);
            mPullUpIndicator.setTempMove(tempMove);

            mShitView.offsetTopAndBottom(0 - mPullUpIndicator.getShitCurrentOffsetY());
            mPullUpIndicator.setShitCurrentOffsetY(0);
            mPullUpIndicator.setShitTempOffsetY(0);

            if (1.0f == interpolatedTime) {
                mPullUpIndicator.setCurrentOffsetY(0);
                mPullUpIndicator.setTempOffsetY(0);
            }
            updatePullUp();
            invalidate();
        }
    };
    //for change anim pic
    private boolean mPicChangState;
    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int currentOffsetY = (int) ((mCustomIndicator.getmUpBaseOffsetY() - mCustomIndicator.getmUpDesOffsetY()) * (1 - interpolatedTime)) + mCustomIndicator.getmUpDesOffsetY();
            int tempMove = mCustomIndicator.getmCurrentOffsetY() - currentOffsetY;
            mCustomIndicator.setmTempMove(tempMove);
            mCustomIndicator.setmCurrentOffsetY(currentOffsetY);
            if (1.0f == interpolatedTime) {
                mCustomIndicator.setmCurrentOffsetY(mCustomIndicator.getmUpDesOffsetY());
            }
            update();
            invalidate();
        }
    };
    private boolean mIsDraging = false;
    private int mTouchSlop;
    //enable pull up
    private boolean enablePullUp;
    //no footer
    private boolean isNoFooter = true;
    /**
     * 第一个可见的item的位置
     */
    private int firstVisibleItemPosition;
    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;
    /**
     * 第一个位置
     */
    private int[] firstPositions;
    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    public CustomPullrefreshLayout(Context context) {
        this(context, null);
    }

    public CustomPullrefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPullrefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mHeaderView = LayoutInflater.from(context).inflate(R.layout.custom_pullrefresh_header, null);
        mHeaderImageView = (ImageView) mHeaderView.findViewById(R.id.custom_header);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mHeaderView, lp);

        mPeopleLineView = LayoutInflater.from(context).inflate(R.layout.people_line, null);
        LayoutParams peopleLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mPeopleLineView, peopleLp);

        mShitView = LayoutInflater.from(context).inflate(R.layout.shit, null);
        LayoutParams shitLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mShitView, shitLp);

        mCustomIndicator = new CustomIndicator(mTouchSlop);
        mPullUpIndicator = new PullUpIndicator();
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        initToCorrectPosAnimation();
        initPullUpToCorrectPosAnimation();
        initAnimation();
    }

    private void initAnimation() {
        mAnimation = new RotateAnimation(0f, 3600f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimation.setDuration(5000);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.setRepeatCount(Animation.INFINITE);
    }

    private void initToCorrectPosAnimation() {
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
    }

    private void initPullUpToCorrectPosAnimation() {
        mPullUpAnimationToCorrPos.reset();
        mPullUpAnimationToCorrPos.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mPullUpAnimationToCorrPos.setInterpolator(mDecelerateInterpolator);
    }

    @Override
    protected void onFinishInflate() {
        mContentView = getChildAt(3);
        super.onFinishInflate();
    }

    public void setShitBackgroudColor(String color) {
        if (mShitView != null) {
            mShitView.setBackgroundColor(Color.parseColor(color));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (null != mHeaderView) {
            LayoutParams lp = mHeaderView.getLayoutParams();
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight(), lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom(), lp.height);
            mHeaderView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            mCustomIndicator.setmHeaderHeight(mHeaderView.getMeasuredHeight());
        }

        if (null != mContentView) {
            final MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight(), lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom(), lp.height);
            mContentView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }

        //设置上拉刷新效果人的view
        if (null != mPeopleLineView) {
            LayoutParams lp = mPeopleLineView.getLayoutParams();
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight(), lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom(), lp.height);
            mPeopleLineView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            mPullUpIndicator.setFooterLineHeight(getResources().getDimensionPixelOffset(R.dimen.line_height));
            mPullUpIndicator.setFooterPeopleHeight(getResources().getDimensionPixelOffset(R.dimen.people_height));
        }

        //设置上拉刷新效果shit的view
        if (null != mShitView) {
            LayoutParams lp = mShitView.getLayoutParams();
            final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight(), lp.width);
            final int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom(), lp.height);
            mShitView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            mPullUpIndicator.setFooterShitHeight(mShitView.getHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int headerHeight = mCustomIndicator.getmHeaderHeight();
        int currentOffsetY = mCustomIndicator.getmCurrentOffsetY();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();

        // pull up
        int screenBottom = getMeasuredHeight();
        int pullUpCurrentOffsetY = mPullUpIndicator.getCurrentOffsetY();
        if (isEnablePullUp()) {
            currentOffsetY = pullUpCurrentOffsetY;
        }

        //layout headerview
        if (null != mHeaderView) {
            final int left = paddingLeft;
            final int top = paddingTop - headerHeight + currentOffsetY;
            final int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();
            mHeaderView.layout(left, top, right, bottom);
        }

        //layout contentview
        if (null != mContentView) {
            final int left = paddingLeft;
            final int top = paddingTop + currentOffsetY;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();
            mContentView.layout(left, top, right, bottom);
        }

        //对上拉刷新人进行布局
        if (null != mPeopleLineView) {
            final int left = paddingLeft;
            final int top = screenBottom + pullUpCurrentOffsetY;
            final int right = left + mPeopleLineView.getMeasuredWidth();
            final int bottom = top + mPeopleLineView.getMeasuredHeight();
            mPeopleLineView.layout(left, top, right, bottom);
        }

        //对上拉刷新shit进行布局
        if (null != mShitView) {
            final int left = paddingLeft;
            final int top = screenBottom + mPullUpIndicator.getFooterPeopleHeight() + mPullUpIndicator.getShitCurrentOffsetY();
            final int right = left + mShitView.getMeasuredWidth();
            final int bottom = top + mShitView.getMeasuredHeight();
            mShitView.layout(left, top, right, bottom);
            mShitView.bringToFront();
        }
    }

    //TouchEvent传递给子view
    public boolean dispatchTouchEventSupper(MotionEvent e) {
        return super.dispatchTouchEvent(e);
    }

    public boolean dispatchTouchEventPullUp(MotionEvent e) {
        int action = e.getAction();
        boolean isNotDispatch = false;
        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsDraging = false;
                if (!isEnablePullUp() || mPullUpIndicator.getCurrentOffsetY() >= 0) {
                    return dispatchTouchEventSupper(e);
                }
                mPullUpIndicator.setStatus(STATUS_UP);
                mPullUpIndicator.setDestOffsetY(0);
                mPullUpIndicator.setBaseOffsetY(mPullUpIndicator.getCurrentOffsetY());

                ((ViewGroup) (mPeopleLineView.getParent())).startAnimation(mPullUpAnimationToCorrPos);
                e.setAction(MotionEvent.ACTION_CANCEL);
                return dispatchTouchEventSupper(e);
            case MotionEvent.ACTION_DOWN:
                ((ViewGroup) (mPeopleLineView.getParent())).clearAnimation();
                mPullUpIndicator.setStatus(STATUS_DOWN);
                mPullUpIndicator.setLastPosY(e.getY());
                return dispatchTouchEventSupper(e);
            case MotionEvent.ACTION_MOVE:
                //set pull up
                if (Math.abs(e.getY() - mPullUpIndicator.getLastPosY()) > mTouchSlop) {
                    mIsDraging = true;
                }
                mPullUpIndicator.setCurrentPosY(e.getY());
                mPullUpIndicator.setShitCurrentPosY(e.getY());
                mPullUpIndicator.setStatus(STATUS_MOVE);
                isNotDispatch = updatePullUp();
                invalidate();
                if (isNotDispatch) {
                    e.setAction(MotionEvent.ACTION_DOWN);
                    if (mIsDraging) {
                        e.setAction(MotionEvent.ACTION_CANCEL);
                    }
                }
        }
        return dispatchTouchEventSupper(e);
    }

    private boolean updatePullUp() {
        int status = mPullUpIndicator.getStatus();
        int tempOffsetY = mPullUpIndicator.getTempOffsetY();
        int tempMove = mPullUpIndicator.getTempMove();
        int currentOffsetY = mPullUpIndicator.getCurrentOffsetY();
        int shitTempOffsetY = mPullUpIndicator.getShitTempOffsetY();

        if (STATUS_MOVE == status) {
            if (mPullUpIndicator.isPeopleAppear()) {
                mPeopleLineView.offsetTopAndBottom(tempOffsetY);
                mContentView.offsetTopAndBottom(tempOffsetY);
                mShitView.offsetTopAndBottom(shitTempOffsetY);

            } else {
                mPeopleLineView.offsetTopAndBottom((0 - currentOffsetY + tempOffsetY));
                mContentView.offsetTopAndBottom((0 - currentOffsetY + tempOffsetY));
                mPullUpIndicator.setCurrentOffsetY(0);
                mPullUpIndicator.setTempOffsetY(0);

                mShitView.offsetTopAndBottom(0 - mPullUpIndicator.getShitCurrentOffsetY() + mPullUpIndicator.getShitTempOffsetY());
                mPullUpIndicator.setShitCurrentOffsetY(0);
                mPullUpIndicator.setShitTempOffsetY(0);
                return false;
            }
            return true;
        }

        if (STATUS_UP == status) {
            mPeopleLineView.offsetTopAndBottom((0 - tempMove));
            mContentView.offsetTopAndBottom((0 - tempMove));
            return true;
        }

        return false;
    }

    public boolean dispatchTouchEventPullDown(MotionEvent e) {
        int action = e.getAction();
        boolean isNotDispatch = false;

        switch (action) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsDraging = false;
                if (mCustomIndicator.hasLeftStartPosition()) {
                    if (mPicChangState && mCustomIndicator.getmTempOffsetY() == 0) {
                        return dispatchTouchEventSupper(e);
                    }
                    mCustomIndicator.setmStatus(STATUS_UP);
                    mCustomIndicator.setmUpBaseOffsetY(mCustomIndicator.getmCurrentOffsetY());
                    if (mCustomIndicator.isCanRefresh()) {
                        mHeaderImageView.setImageResource(R.drawable.custom_pull_refresh_release);
                        mHeaderImageView.setTag(R.drawable.custom_pull_refresh_release);
                        mHeaderImageView.startAnimation(mAnimation);
                        mCustomIndicator.setmUpDesOffsetY(mCustomIndicator.getmHeaderHeight());
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onRefresh();
                            }
                        }, 1500);
                        mPicChangState = true;
                    } else {
                        mCustomIndicator.setmUpDesOffsetY(0);
                    }
                    ((ViewGroup) (mHeaderView.getParent())).clearAnimation();
                    ((ViewGroup) (mHeaderView.getParent())).startAnimation(mAnimateToCorrectPosition);
                    e.setAction(MotionEvent.ACTION_CANCEL);
                    return dispatchTouchEventSupper(e);

                } else {
                    return dispatchTouchEventSupper(e);
                }
            case MotionEvent.ACTION_DOWN:
                mCustomIndicator.setmLastPosY(e.getY());
                return dispatchTouchEventSupper(e);

            case MotionEvent.ACTION_MOVE:
                if (Math.abs(e.getY() - mCustomIndicator.getmLastPosY()) > mTouchSlop) {
                    mIsDraging = true;
                }
                mCustomIndicator.setmCurrentPosY(e.getY());
                mCustomIndicator.setmStatus(STATUS_MOVE);
                isNotDispatch = update();
                invalidate();
                if (isNotDispatch) {
                    if (mIsDraging) {
                        e.setAction(MotionEvent.ACTION_CANCEL);
                        return dispatchTouchEventSupper(e);
                    }
                    return true;
                }

                return dispatchTouchEventSupper(e);
        }

        return dispatchTouchEventSupper(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (!isEnabled() || null == mContentView || null == mHeaderView || canChildScrollUp()) {
            isScrollToLast();
            if (isEnablePullUp() && isNoFooter) {
                return dispatchTouchEventPullUp(e);
            }
            return dispatchTouchEventSupper(e);
        }
        return dispatchTouchEventPullDown(e);
    }

    private boolean update() {
        int status = mCustomIndicator.getmStatus();
        int tempOffsetY = mCustomIndicator.getmTempOffsetY();
        int tempMove = mCustomIndicator.getmTempMove();
        int currentOffsetY = mCustomIndicator.getmCurrentOffsetY();
        int interval = getResources().getDimensionPixelSize(R.dimen.alter_push_length);

        if (STATUS_MOVE == status) {
            ((ViewGroup) (mHeaderView.getParent())).clearAnimation();
            if (mCustomIndicator.isCanMove()) {
                mHeaderView.offsetTopAndBottom(tempOffsetY);
                mContentView.offsetTopAndBottom(tempOffsetY);
            } else {
                mHeaderView.offsetTopAndBottom((0 - currentOffsetY + tempOffsetY));
                mContentView.offsetTopAndBottom((0 - currentOffsetY + tempOffsetY));
                mCustomIndicator.setmCurrentOffsetY(0);

                return false;
            }

            if (!mPicChangState) {
                Object tag = mHeaderImageView.getTag();
                int drawable = 0;
                if (tag != null) {
                    drawable = (int) tag;
                }
                if (mCustomIndicator.isCanAlterPic(interval)) {
                    if (drawable != R.drawable.custom_pull_refresh_second) {
                        mHeaderImageView.clearAnimation();
                        mHeaderImageView.setImageResource(R.drawable.custom_pull_refresh_second);
                        mHeaderImageView.setTag(R.drawable.custom_pull_refresh_second);
                        AnimationDrawable animationDrawable = (AnimationDrawable) mHeaderImageView.getDrawable();
                        animationDrawable.start();
                    }
                } else {
                    if (drawable != R.drawable.custom_pull_refresh_first) {
                        mHeaderImageView.clearAnimation();
                        mHeaderImageView.setImageResource(R.drawable.custom_pull_refresh_first);
                        mHeaderImageView.setTag(R.drawable.custom_pull_refresh_first);
                        AnimationDrawable animationDrawable = (AnimationDrawable) mHeaderImageView.getDrawable();
                        animationDrawable.start();
                    }
                }
            }

            return true;
        }

        if (STATUS_UP == status) {
            mHeaderView.offsetTopAndBottom((0 - tempMove));
            mContentView.offsetTopAndBottom((0 - tempMove));
            return true;
        }

        if (STATUS_REFRESH_COMPLETE == status) {
            mHeaderView.offsetTopAndBottom((0 - tempMove));
            mContentView.offsetTopAndBottom((0 - tempMove));
            return true;
        }

        return false;
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        if (!refreshing) {
            mPicChangState = false;
            mCustomIndicator.setmStatus(STATUS_REFRESH_COMPLETE);
            mHeaderView.clearAnimation();
            mCustomIndicator.setmUpBaseOffsetY(mCustomIndicator.getmCurrentOffsetY());
            mCustomIndicator.setmUpDesOffsetY(0);
            ((ViewGroup) (mHeaderView.getParent())).clearAnimation();
            ((ViewGroup) (mHeaderView.getParent())).startAnimation(mAnimateToCorrectPosition);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    public boolean isEnablePullUp() {
        return enablePullUp;
    }

    public void setEnablePullUp(boolean enablePullUp) {
        this.enablePullUp = enablePullUp;
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (Build.VERSION.SDK_INT < 14) {
            if (mContentView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mContentView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mContentView, -1) || mContentView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mContentView, -1);
        }
    }

    public void initScrollToLast() {
        if (mContentView instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) mContentView;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            //  int lastVisibleItemPosition = -1;
            if (layoutManagerType == null) {
                if (layoutManager instanceof LinearLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
                } else if (layoutManager instanceof GridLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
                } else {
                    throw new RuntimeException(
                            "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
                }
            }

            switch (layoutManagerType) {
                case LINEAR:
                    firstVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                            .findFirstVisibleItemPosition();
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    break;
                case GRID:
                    firstVisibleItemPosition = ((GridLayoutManager) layoutManager)
                            .findFirstVisibleItemPosition();
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager)
                            .findLastVisibleItemPosition();
                    break;
                case STAGGERED_GRID:
                    StaggeredGridLayoutManager staggeredGridLayoutManager
                            = (StaggeredGridLayoutManager) layoutManager;
                    if (firstPositions == null) {
                        firstPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    if (lastPositions == null) {
                        lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                    }
                    staggeredGridLayoutManager.findFirstVisibleItemPositions(firstPositions);
                    staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                    firstVisibleItemPosition = findMin(firstPositions);
                    lastVisibleItemPosition = findMax(lastPositions);
                    break;
            }

        }
    }

    public void isScrollToLast() {
        initScrollToLast();
        if (mContentView instanceof RecyclerView) {
            final RecyclerView recyclerView = (RecyclerView) mContentView;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            if ((visibleItemCount > 0 &&
                    (lastVisibleItemPosition) >= totalItemCount - 1)) {

                if (recyclerView.getChildAt(lastVisibleItemPosition - firstVisibleItemPosition).getBottom() <= getMeasuredHeight()) {
                    enablePullUp = true;
                } else {
                    enablePullUp = false;
                }
                return;
            } else {
                enablePullUp = false;
                return;
            }
        }

        enablePullUp = false;
        return;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private int findMin(int[] firstPositions) {
        int min = firstPositions[0];
        for (int value : firstPositions) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    public void setIsNoFooter(boolean isNoFooter) {
        this.isNoFooter = isNoFooter;
    }

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
