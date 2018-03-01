package com.muyi.zhaofei.zfweather;

import java.util.UUID;

/**
 * Created by Zhao Fei on 2018/2/16.
 */

public class Weather {
    private String mCity;   // 市
    private String mConditionNow;  // 当前天气状况
    private String mTmpNow;    // 当前温度
    private String mConditionDaytime;
    private String mMaxTmp;
    private String mMinTmp;
    private UUID mUUID;

    public Weather() {
        mUUID = UUID.randomUUID();
    }
    public UUID getUUID() {
        return mUUID;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getConditionNow() {
        return mConditionNow;
    }

    public void setConditionNow(String conditionNow) {
        mConditionNow = conditionNow;
    }

    public String getTmpNow() {
        return mTmpNow;
    }

    public void setTmpNow(String tmpNow) {
        mTmpNow = tmpNow;
    }

    public String getConditionDaytime() {
        return mConditionDaytime;
    }

    public void setConditionDaytime(String conditionDaytime) {
        mConditionDaytime = conditionDaytime;
    }

    public String getMaxTmp() {
        return mMaxTmp;
    }

    public void setMaxTmp(String maxTmp) {
        mMaxTmp = maxTmp;
    }

    public String getMinTmp() {
        return mMinTmp;
    }

    public void setMinTmp(String minTmp) {
        mMinTmp = minTmp;
    }

}
