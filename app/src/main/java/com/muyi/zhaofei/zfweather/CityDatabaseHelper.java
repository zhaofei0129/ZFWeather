package com.muyi.zhaofei.zfweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;

/**
 * Created by Zhao Fei on 2018/3/2.
 */

public class CityDatabaseHelper extends SQLiteOpenHelper {
    public static final String CITY_TABLE = "City";
    public static final String CITY_COL = "city";
    public static final String IS_SELECTED_COL = "isSelected";
    public static final String IS_LOCATED_COL = "isLocated";

    public static final String CREATE_CITY = "create table "
            + CITY_TABLE + " (id integer primary key autoincrement, "
            + CITY_COL + " text, "
            + IS_SELECTED_COL + " integer, "
            + IS_LOCATED_COL + " integer)";

    private Context mContext;

//    public CityDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//        mContext = context;
//    }
    public CityDatabaseHelper(Context context) {
        super(context, "City.db", null, 64);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + CITY_TABLE);
        File file= new File("/data/data/"+ mContext.getPackageName()+ "/shared_prefs","WeatherActivity" + ".xml");
        if(file.exists()){
            file.delete();
            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
        }
        onCreate(db);
    }
}
