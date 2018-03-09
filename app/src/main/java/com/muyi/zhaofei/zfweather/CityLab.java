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
    private List<City> mCities;
    private SQLiteDatabase mDatabase;

    private static CityLab sCityLab;

    public static CityLab getSingleInstance(Context context) {
        if (sCityLab == null) {
            sCityLab = new CityLab(context);
        }
        return sCityLab;
    }

    private CityLab(Context context) {
        mDatabase = new CityDatabaseHelper(context.getApplicationContext()).getWritableDatabase();
        // 初始化mCities
        mCities = new ArrayList<>();
        Cursor cursor = mDatabase.query(CityDatabaseHelper.CITY_TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(CityDatabaseHelper.CITY_COL));
                boolean isSelected = cursor.getInt(cursor.getColumnIndex(CityDatabaseHelper.IS_SELECTED_COL)) != 0;
                boolean isLocated = cursor.getInt(cursor.getColumnIndex(CityDatabaseHelper.IS_LOCATED_COL)) != 0;
                City city = new City();
                city.setName(name);
                city.setLocated(isLocated);
                city.setSelected(isSelected);
                mCities.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();

    }
//    private CityLab(Context context) {
//        mDatabase = new CityDatabaseHelper(context.getApplicationContext()).getWritableDatabase();
////        mCities = new ArrayList<>();
//
//    }

    public List<City> getCities() {
        return mCities;
    }

    public City getSelectedCity() {
        for (City city: mCities) {
            if (city.isSelected()) {
                return city;
            }
        }
        return null;
    }

    public City getLocatedCity() {
        for (City city: mCities) {
            if (city.isLocated()) {
                return city;
            }
        }
        return null;
    }

    public void addCity(City city) {
        boolean isRepeated = false;
        for (City c: mCities) {
            if (city.getName().equals(c.getName())) {
                isRepeated = true;
                break;
            }
        }
        if (!isRepeated) {
            mCities.add(city);
            ContentValues values = getContentValues(city);
            mDatabase.insert(CityDatabaseHelper.CITY_TABLE, null, values);
        }

    }

    public void updateSelectedCity(City city) {
        for (int i = 0; i < mCities.size(); i++) {
            if (mCities.get(i).isSelected() == true) {
                mCities.get(i).setSelected(false);
                ContentValues values = getContentValues(mCities.get(i));
                mDatabase.update(CityDatabaseHelper.CITY_TABLE, values, CityDatabaseHelper.IS_SELECTED_COL + " = ?", new String[]{"1"});
                break;
            }

        }
        for (int i = 0; i < mCities.size(); i++) {
            if (mCities.get(i).getName().equals(city.getName())) {
                mCities.get(i).setSelected(true);
                ContentValues values = getContentValues(mCities.get(i));
                mDatabase.update(CityDatabaseHelper.CITY_TABLE, values, CityDatabaseHelper.CITY_COL + " = ?", new String[]{city.getName()});
                break;
            }
        }
    }

    public void updateLocatedCity(City city) {
        for (int i = 0; i < mCities.size(); i++) {
            if (mCities.get(i).isLocated() == true) {
                mCities.get(i).setName(city.getName());
                ContentValues values = getContentValues(mCities.get(i));
                mDatabase.update(CityDatabaseHelper.CITY_TABLE, values, CityDatabaseHelper.IS_LOCATED_COL + " = ?", new String[]{"1"});
                break;
            }
        }
    }
    public void deleteSelectedCity(City city) {
        for (int i = 1; i < mCities.size(); i++) {
            if (mCities.get(i).getName().equals(city.getName())) {
                if (mCities.get(i).isSelected()) {
                    mCities.get(0).setSelected(true);
                    ContentValues values = getContentValues(mCities.get(0));
                    mDatabase.update(CityDatabaseHelper.CITY_TABLE, values, CityDatabaseHelper.IS_LOCATED_COL + " = ?", new String[]{"1"});
                }
                mDatabase.delete(CityDatabaseHelper.CITY_TABLE, CityDatabaseHelper.CITY_COL + " = ? AND " +  CityDatabaseHelper.IS_LOCATED_COL + " = ?", new String[]{city.getName(), "0"});
                mCities.remove(i);
                break;
            }
        }
    }

    public void query() {
          Cursor cursor = mDatabase.query(CityDatabaseHelper.CITY_TABLE, null, null, null, null, null, null);
          if (cursor.moveToFirst()) {
              do {
                  String name = cursor.getString(cursor.getColumnIndex(CityDatabaseHelper.CITY_COL));
                  boolean isSelected = cursor.getInt(cursor.getColumnIndex(CityDatabaseHelper.IS_SELECTED_COL)) != 0;
                  boolean isLocated = cursor.getInt(cursor.getColumnIndex(CityDatabaseHelper.IS_LOCATED_COL)) != 0;
                  Log.d("CityLab", "    city: " + name + "  Selected: " + isSelected + "    Located: " + isLocated);
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
