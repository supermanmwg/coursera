package com.utils.customviews;

/**
 * Created by supermanmwg on 15-10-8.
 */
public class PullUpIndicator {

    public final static int POS_START = 0;
    private int status;   // 1:移动 2:释放

    private float currentPosY;
    private float lastPosY;

    private int footerLineHeight;  //人和线的高度
    private int footerPeopleHeight;   //人的高度
    private int footerShitHeight;     //shit的高度

    private int currentOffsetY;   //相对与起始位置的位移（总的）
    private int tempOffsetY;      //一次移动的位移
    private int baseOffsetY;      //上拉释放时位移起始值
    private int destOffsetY;      //上拉释放时位移结束值
    private int tempMove;

    private float shitCurrentPosY;
    private float shitLastPosY;
    private int shitCurrentOffsetY;   //相对与起始位置的位移（总的）
    private int shitTempOffsetY;      //一次移动的位移
    private int shitBaseOffsetY;      //上拉释放时位移起始值
    private int shitDestOffsetY;      //上拉释放时位移结束值
    private int shitTempMove;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getCurrentPosY() {
        return currentPosY;
    }

    public void setCurrentPosY(float mCurrentPosY) {
        this.currentPosY = mCurrentPosY;
        this.tempOffsetY = (int) ((this.currentPosY - this.lastPosY) * 0.5);
        this.currentOffsetY += this.tempOffsetY;
        if ((currentOffsetY + this.tempOffsetY) < (0 - getFooterLineHeight())) {
            this.tempOffsetY -= (this.currentOffsetY + getFooterLineHeight());
            this.currentOffsetY = 0 - getFooterLineHeight();
        }

        this.lastPosY = this.currentPosY;
    }

    public void setShitCurrentPosY(float mCurrentPosY) {
        this.shitCurrentPosY = mCurrentPosY;
        this.shitTempOffsetY = (int) ((this.shitCurrentPosY - this.shitLastPosY) * 0.5);
        if (this.shitTempOffsetY > 0) this.shitTempOffsetY = 0;
        this.shitCurrentOffsetY += this.shitTempOffsetY;
        if ((shitCurrentOffsetY + this.shitTempOffsetY) < (0 - getFooterShitHeight() - getFooterPeopleHeight())) {
            this.shitTempOffsetY -= (this.shitCurrentOffsetY + getFooterPeopleHeight() + getFooterShitHeight());
            this.shitCurrentOffsetY = 0 - getFooterPeopleHeight() - getFooterShitHeight();
        }
        this.shitLastPosY = this.shitCurrentPosY;
    }

    public int getShitTempOffsetY() {
        return shitTempOffsetY;
    }

    public void setShitTempOffsetY(int shitTempOffsetY) {
        this.shitTempOffsetY = shitTempOffsetY;
    }

    public int getShitCurrentOffsetY() {
        return shitCurrentOffsetY;
    }

    public void setShitCurrentOffsetY(int shitCurrentOffsetY) {
        this.shitCurrentOffsetY = shitCurrentOffsetY;
    }

    public float getLastPosY() {
        return lastPosY;
    }

    public void setLastPosY(float lastPosY) {
        this.lastPosY = lastPosY;
        this.shitLastPosY = lastPosY;
    }

    public int getFooterLineHeight() {
        return footerLineHeight;
    }

    public void setFooterLineHeight(int footerPeopleLineHeight) {
        this.footerLineHeight = footerPeopleLineHeight;
    }

    public int getFooterPeopleHeight() {
        return footerPeopleHeight;
    }

    public void setFooterPeopleHeight(int footerPeopleHeight) {
        this.footerPeopleHeight = footerPeopleHeight;
    }

    public int getFooterShitHeight() {
        return footerShitHeight;
    }

    public void setFooterShitHeight(int footerShitHeight) {
        this.footerShitHeight = footerShitHeight;
    }

    public int getCurrentOffsetY() {
        return currentOffsetY;
    }

    public void setCurrentOffsetY(int currentOffsetY) {
        this.currentOffsetY = currentOffsetY;
    }

    public int getTempOffsetY() {
        return tempOffsetY;
    }

    public void setTempOffsetY(int tempOffsetY) {
        this.tempOffsetY = tempOffsetY;
    }

    public int getBaseOffsetY() {
        return baseOffsetY;
    }

    public void setBaseOffsetY(int baseOffsetY) {
        this.baseOffsetY = baseOffsetY;
    }

    public int getDestOffsetY() {
        return destOffsetY;
    }

    public void setDestOffsetY(int destOffsetY) {
        this.destOffsetY = destOffsetY;
    }

    public int getTempMove() {
        return tempMove;
    }

    public void setTempMove(int tempMove) {
        this.tempMove = tempMove;
    }

    public boolean isPeopleAppear() {
        return currentOffsetY <= 0;
    }

    public boolean isShitAppear() {
        return currentOffsetY <= (0 - getFooterShitHeight()
                - getFooterPeopleHeight());
    }
}

