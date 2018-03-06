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
    public static final String EXTRA_SELECTED_CITY_NAME = "com.muyi.zhaofei.zfweather.SelectedCitiesActivity_selected_city_name";

    private static final String TAG = "SelectedCitiesActivity";
    private static final int REQUEST_CODE_CITIES_ACTIVITY = 1;

    private CityAdapter mAdapter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SelectedCitiesActivity.class);
        return intent;
    }

    private class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mCityTextView;
            TextView mLocatedTextView;
            TextView mSelectedTextView;

            public ViewHolder(View view) {
                super(view);
                mCityTextView = (TextView)view.findViewById(R.id.id_city_text_view);
                mLocatedTextView = (TextView)view.findViewById(R.id.id_locate_text_view);
                mSelectedTextView = (TextView)view.findViewById(R.id.id_selected_text_view);

            }
        }
        private List<City> mCitys;

        public CityAdapter(List<City> citys) {
            mCitys = citys;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, null, false);
            final ViewHolder holder = new ViewHolder(view);
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
        public void onBindViewHolder(ViewHolder holder, int position) {
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
        setContentView(R.layout.activity_selected_cities);

        List<City> cities = CityLab.getSingleInstance(this).getCities();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.id_selected_cities_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new CityAdapter(cities);
        recyclerView.setAdapter(mAdapter);

        Button addButton = (Button)findViewById(R.id.id_add_city_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(SelectedCitiesActivity.this, CitiesActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CITIES_ACTIVITY);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
