package com.muyi.zhaofei.zfweather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhao Fei on 2018/3/3.
 */

public class CityLab {
//    private List<City> mCities;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private static CityLab sCityLab;

    public static CityLab newInstance(Context context) {
        if (sCityLab == null) {
            sCityLab = new CityLab(context);
        }
        return sCityLab;
    }

    private CityLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CityDatabaseHelper(mContext).getWritableDatabase();
//        mCities = new ArrayList<>();

    }

    public List<City> getCities() {
//        return mCities;
        return new ArrayList<>();
    }

    public City getSelectedCity(boolean isSelected) {
//        for (City city: mCities) {
//            if (city.isSelected()) {
//                return city;
//            }
//        }
        return null;
    }

    public void addCity(City city) {
//        mCities.add(city);
        ContentValues values = getContentValues(city);
        mDatabase.insert(CityDatabaseHelper.CITY_TABLE, null, values);
    }

    public void updateCity(City city) {
        ContentValues values = getContentValues(city);
//        mDatabase.update(CityDatabaseHelper.CITY_TABLE, values, CityDatabaseHelper.IS_LOCATED_COL + " = ?", new String[]{"1"});
        mDatabase.update(CityDatabaseHelper.CITY_TABLE, values, CityDatabaseHelper.CITY_COL + " = ?", new String[]{"南京"});

    }

    public void deleteCity(City city) {
        mDatabase.delete(CityDatabaseHelper.CITY_TABLE, CityDatabaseHelper.CITY_COL + " = ?", new String[]{city.getName()});
    }

    public void query() {
                Cursor cursor = mDatabase.query("City", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex(CityDatabaseHelper.CITY_COL));
                        Log.d("CityLab", "city: " + name);
                    } while (cursor.moveToNext());
                }
                cursor.close();
    }

    private ContentValues getContentValues(City city) {
        ContentValues values = new ContentValues();
        values.put(CityDatabaseHelper.CITY_COL, city.getName());
        values.put(CityDatabaseHelper.IS_SELECTED_COL, city.isSelected() ? 1 : 0);
        values.put(CityDatabaseHelper.IS_LOCATED_COL, city.isLocated() ? 1 : 0);
        return values;
    }
}
