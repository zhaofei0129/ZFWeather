package com.muyi.zhaofei.zfweather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class SelectedCitiesActivity extends BasicActivity {
    private static final String EXTRA_CURRENT_CITY_NAME = "com.muyi.zhaofei.zfweather.SelectedCitiesActivity_current_city_name";
    public static final String EXTRA_SELECTED_CITY_NAME = "com.muyi.zhaofei.zfweather.SelectedCitiesActivity_selected_city_name";

    private static final String TAG = "SelectedCitiesActivity";
    private static final int REQUEST_CODE_CITIES_ACTIVITY = 1;

    private CityAdapter mAdapter;

    public static Intent newIntent(Context context, String city) {
        Intent intent = new Intent(context, SelectedCitiesActivity.class);
        intent.putExtra(EXTRA_CURRENT_CITY_NAME, city);
        return intent;
    }
    private class CityViewHolder extends RecyclerView.ViewHolder {
        TextView mCityTextView;
        TextView mLocatedTextView;
        TextView mSelectedTextView;

        public CityViewHolder(View view) {
            super(view);
            mCityTextView = (TextView)view.findViewById(R.id.id_city_text_view);
            mLocatedTextView = (TextView)view.findViewById(R.id.id_locate_text_view);
            mSelectedTextView = (TextView)view.findViewById(R.id.id_selected_text_view);

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
            final CityViewHolder holder = new CityViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    City city = new City();
                    city.setName((String) holder.mCityTextView.getText());
                    city.setSelected(true);
                    CityLab.getSingleInstance(SelectedCitiesActivity.this).updateCity(city);
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_SELECTED_CITY_NAME, city.getName());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(CityViewHolder holder, int position) {
            String name = mCitys.get(position).getName();
            holder.mCityTextView.setText(name);
            if (mCitys.get(position).isSelected()) {
                holder.mSelectedTextView.setText("选择");
            } else {
                holder.mSelectedTextView.setText("");
            }
            if (mCitys.get(position).isLocated()) {
                holder.mLocatedTextView.setText("定位");
            } else {
                holder.mLocatedTextView.setText("");
            }
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
        String name = intent.getStringExtra(EXTRA_CURRENT_CITY_NAME);

        if (CityLab.getSingleInstance(this).getCities().size() == 0)
        {
            City city = new City();
            city.setName(name);
            city.setLocated(true);
            city.setSelected(true);
            CityLab.getSingleInstance(this).addCity(city);
        }


        List<City> cities = CityLab.getSingleInstance(this).getCities();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.id_selected_cities_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CityAdapter(cities);
        recyclerView.setAdapter(mAdapter);
//        final CityDatabaseHelper helper = new CityDatabaseHelper(this, "SelectedCitys.db", null, 1);

        Button addButton = (Button)findViewById(R.id.id_add_city_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(SelectedCitiesActivity.this, CitiesActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CITIES_ACTIVITY);
            }
        });
//        Button sButton = (Button)findViewById(R.id.id_s_city_button);
//        sButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CityLab.getSingleInstance(SelectedCitiesActivity.this).query();
//            }
//        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case REQUEST_CODE_CITIES_ACTIVITY:
//                if (resultCode == RESULT_OK) {
//                    String returnedData = data.getStringExtra(CitiesActivity.EXTRA_SELECTED_CITY);
//                    Log.d(TAG, "onActivityResult: " + returnedData);
//                }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
