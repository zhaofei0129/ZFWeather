package com.muyi.zhaofei.zfweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CitysActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_citys);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.id_citys_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<String> citys = new ArrayList<>();
        citys.add("北京");
        citys.add("上海");
        citys.add("深圳");

        CityAdapter adapter = new CityAdapter(citys);
        recyclerView.setAdapter(adapter);

        Button addButton = (Button)findViewById(R.id.id_add_city_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
