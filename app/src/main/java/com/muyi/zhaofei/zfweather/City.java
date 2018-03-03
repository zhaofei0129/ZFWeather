package com.muyi.zhaofei.zfweather;

/**
 * Created by Zhao Fei on 2018/3/3.
 */

public class City {
    private String mName;
    private boolean mIsSelected = false;
    private boolean mIsLocated = false;

    public boolean isLocated() {
        return mIsLocated;
    }

    public void setLocated(boolean located) {
        mIsLocated = located;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }
}
