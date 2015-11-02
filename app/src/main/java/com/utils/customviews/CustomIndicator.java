package com.utils.customviews;


/**
 * Created by supermanmwg on 15-9-10.
 */
public class CustomIndicator {
    private final int mTouchSlop;

    //1:move 2:release
    private int mStatus;

    private float mCurrentPosY;
    private float mLastPosY;

    private int mCurrentOffsetY;  //相对于起始位置的位移（总的）
    private int mTempOffsetY;     //一次移动的位移
    private int mUpBaseOffsetY;   //下拉释放时的位移起始值
    private int mUpDesOffsetY; //下拉释放时的位移结束值；
    private int mLastOffsetY;
    private int mTempMove;

    private int mHeaderHeight;   //header的高度，并且当位移大于此高度时，下拉释放时，可进行刷新

    public CustomIndicator(int touchSlop) {
        this.mTouchSlop = touchSlop;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public int getmUpBaseOffsetY() {
        return mUpBaseOffsetY;
    }

    public void setmUpBaseOffsetY(int mUpBaseOffsetY) {
        this.mUpBaseOffsetY = mUpBaseOffsetY;
    }

    public int getmUpDesOffsetY() {
        return mUpDesOffsetY;
    }

    public void setmUpDesOffsetY(int mUpDesOffsetY) {
        this.mUpDesOffsetY = mUpDesOffsetY;
    }

    public float getmCurrentPosY() {
        return mCurrentPosY;
    }

    public void setmCurrentPosY(float mCurrentPosY) {
        this.mCurrentPosY = mCurrentPosY;
        this.mTempOffsetY = (int) ((this.mCurrentPosY - this.mLastPosY) * 0.5);
        this.mCurrentOffsetY += this.mTempOffsetY;
        this.mLastPosY = this.mCurrentPosY;
    }

    public float getmLastPosY() {
        return mLastPosY;
    }

    public void setmLastPosY(float mLastPosY) {
        this.mLastPosY = mLastPosY;
    }

    public int getmCurrentOffsetY() {
        return mCurrentOffsetY;
    }

    public void setmCurrentOffsetY(int mCurrentOffsetY) {
        this.mCurrentOffsetY = mCurrentOffsetY;
    }

    public int getmTempOffsetY() {
        return mTempOffsetY;
    }

    public void setmTempOffsetY(int mTempOffsetY) {
        this.mTempOffsetY = mTempOffsetY;
    }

    public int getmHeaderHeight() {
        return mHeaderHeight;
    }

    public void setmHeaderHeight(int mHeaderHeight) {
        this.mHeaderHeight = mHeaderHeight;
    }


    public int getmTempMove() {
        return mTempMove;
    }

    public void setmTempMove(int mTempMove) {
        this.mTempMove = mTempMove;
    }

    public boolean isCanRefresh() {
        return mCurrentOffsetY > mHeaderHeight;
    }

    public boolean isCanMove() {
        return mCurrentOffsetY > 0;
    }

    public boolean isCanAlterPic(int interval) {
        return mCurrentOffsetY > (mHeaderHeight + interval);
    }

    public boolean hasLeftStartPosition() {
        return mCurrentOffsetY > mTouchSlop;
    }
}
