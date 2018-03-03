package com.muyi.zhaofei.zfweather;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CitiesActivity extends BasicActivity {
    public static final String EXTRA_SELECTED_CITY = "com.muyi.zhaofei.zfweather.CitysActivity_selected_city";

    private class CityViewHolder extends RecyclerView.ViewHolder {
        TextView mCityTextView;


        public CityViewHolder(View view) {
            super(view);
            mCityTextView = (TextView)view;
        }
    }

    private class CityAdapter extends RecyclerView.Adapter<CityViewHolder> {
        private List<String> mCitys;

        public CityAdapter(List<String> citys) {
            mCitys = citys;
        }

        @Override
        public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null, false);
            final CityViewHolder holder = new CityViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 添加数据
                    City city = new City();
                    city.setName((String) holder.mCityTextView.getText());
                    CityLab.getSingleInstance(CitiesActivity.this).addCity(city);
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_SELECTED_CITY, city.getName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
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
        setContentView(R.layout.activity_cities);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.id_cities_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> cities = Arrays.asList(getResources().getStringArray(R.array.JiangSu));
        CityAdapter adapter = new CityAdapter(cities);
        recyclerView.setAdapter(adapter);
    }
}
