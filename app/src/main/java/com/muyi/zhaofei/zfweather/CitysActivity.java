package com.muyi.zhaofei.zfweather;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CitysActivity extends BasicActivity {
    private static final String EXTRA_CITY = "com.muyi.zhaofei.zfweather.CitysActivity_city";

    List<String> mCitys = new ArrayList<>();

    public static Intent newInstance(Context context, String city) {
        Intent intent = new Intent(context, CitysActivity.class);
        intent.putExtra(EXTRA_CITY, city);
        return intent;
    }
    private class CityViewHolder extends RecyclerView.ViewHolder {
        TextView mCityTextView;


        public CityViewHolder(View view) {
            super(view);
            mCityTextView = (TextView)view.findViewById(R.id.id_city_text_view);
        }
    }

    private class CityAdapter extends RecyclerView.Adapter<CityViewHolder> {
        private List<String> mCitys;

        public CityAdapter(List<String> citys) {
            mCitys = citys;
        }

        @Override
        public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, null, false);
            CityViewHolder holder = new CityViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(CityViewHolder holder, int position) {
            String city = mCitys.get(position);
            holder.mCityTextView.setText(city);
        }

        @Override
        public int getItemCount() {
            return mCitys.size();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_citys);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.id_citys_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        String city = intent.getStringExtra(EXTRA_CITY);
        mCitys.add(city);
        mCitys.add(city);

        mCitys.set(0, "city");

        CityAdapter adapter = new CityAdapter(mCitys);
        recyclerView.setAdapter(adapter);
        final SelectedCitysDatabaseHelper helper = new SelectedCitysDatabaseHelper(this, "SelectedCitys.db", null, 1);

        Button addButton = (Button)findViewById(R.id.id_add_city_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("city", "吉林");
                db.insert("City", null, values);
                values.clear();
                values.put("city", "哈尔冰");
                db.insert("City", null, values);
                values.clear();
                values.put("city", "南京");
                db.insert("City", null, values);
                values.clear();
            }
        });
        Button dButton = (Button)findViewById(R.id.id_d_city_button);
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete("City", "city = ?", new String[]{"南京"});
            }
        });
        Button cButton = (Button)findViewById(R.id.id_c_city_button);
        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("city", "广州");
                db.update("City", values, "city = ?", new String[]{"吉林"});
            }
        });
        Button sButton = (Button)findViewById(R.id.id_s_city_button);
        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor cursor = db.query("City", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String city = cursor.getString(cursor.getColumnIndex("city"));
                        Log.d("CitysActivity", "city: " + city);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        });
    }
}
