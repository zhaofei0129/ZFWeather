package com.muyi.zhaofei.zfweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CitiesActivity extends BasicActivity {

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
        setContentView(R.layout.activity_cities);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.id_cities_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> cities = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            cities.add("ss" + i);
        }
        CityAdapter adapter = new CityAdapter(cities);
        recyclerView.setAdapter(adapter);
    }
}
