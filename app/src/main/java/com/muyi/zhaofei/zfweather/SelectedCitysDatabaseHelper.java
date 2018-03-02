package com.muyi.zhaofei.zfweather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Zhao Fei on 2018/3/2.
 */

public class SelectedCitysDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_CITY = "create table City (id integer primary key autoincrement, city text)";

    private Context mContext;

    public SelectedCitysDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists City");
        onCreate(db);
    }
}
