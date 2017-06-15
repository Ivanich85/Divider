package com.dmitriev.ivand.divider;

/**
 * Created by ivand on 12.03.2017.
 */

public class Divider {

    //Views defined
    private int mDividend;
    private int mDivider;

    //Constructor
    public Divider(int dividend, int divider) {
        mDividend = dividend;
        mDivider = divider;
    }

    public int getDividend() {
        return mDividend;
    }

    public void setDividend(int dividend) {
        mDividend = dividend;
    }

    public int getDivider() {
        return mDivider;
    }

    public void setDivider(int divider) {
        mDivider = divider;
    }

    //The method checks if dividend is divided by divider without redundant
    public boolean isDivide() {
        return mDividend % mDivider == 0;
    }
}
