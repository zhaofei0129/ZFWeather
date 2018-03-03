package com.muyi.zhaofei.zfweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class SelectedCitiesActivity extends BasicActivity {
    private static final String EXTRA_CITY_NAME = "com.muyi.zhaofei.zfweather.CitysActivity_city";

    public static Intent newIntent(Context context, String city) {
        Intent intent = new Intent(context, SelectedCitiesActivity.class);
        intent.putExtra(EXTRA_CITY_NAME, city);
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
        private List<City> mCitys;

        public CityAdapter(List<City> citys) {
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
            String name = mCitys.get(position).getName();
            holder.mCityTextView.setText(name);
        }

        @Override
        public int getItemCount() {
            return mCitys.size();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_selected_cities);


        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_CITY_NAME);
        City city = new City();
        city.setName(name);
        city.setLocated(true);
        CityLab.newInstance(this).addCity(city);
//
//        mCitys.set(0, "city");
        List<City> cities = CityLab.newInstance(this).getCities();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.id_selected_cities_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CityAdapter adapter = new CityAdapter(cities);
        recyclerView.setAdapter(adapter);
//        final CityDatabaseHelper helper = new CityDatabaseHelper(this, "SelectedCitys.db", null, 1);

        Button addButton = (Button)findViewById(R.id.id_add_city_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                City city = new City();
//                city.setName("吉林");
//                CityLab.newInstance(SelectedCitiesActivity.this).addCity(city);
//                city.setName("哈尔冰");
//                CityLab.newInstance(SelectedCitiesActivity.this).addCity(city);
//                city.setName("南京");
//                CityLab.newInstance(SelectedCitiesActivity.this).addCity(city);
                Intent intent = new Intent(SelectedCitiesActivity.this, CitiesActivity.class);
                startActivity(intent);
            }
        });
//        Button dButton = (Button)findViewById(R.id.id_d_city_button);
//        dButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                City city = new City();
//                city.setName("南京");
//                CityLab.newInstance(SelectedCitiesActivity.this).deleteCity(city);
//            }
//        });
//        Button cButton = (Button)findViewById(R.id.id_c_city_button);
//        cButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                City city2 = new City();
//                city2.setName("吉林");
//                CityLab.newInstance(SelectedCitiesActivity.this).updateCity(city2);
//            }
//        });
//        Button sButton = (Button)findViewById(R.id.id_s_city_button);
//        sButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CityLab.newInstance(SelectedCitiesActivity.this).query();
//            }
//        });
    }
}
