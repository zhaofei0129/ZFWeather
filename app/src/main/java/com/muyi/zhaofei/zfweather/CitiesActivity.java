package com.muyi.zhaofei.zfweather;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
    private static final String TAG = "CitiesActivity";
    private RecyclerView mCityRecyclerView;
    private RecyclerView.Adapter mCityAdapter;


    private class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mCityTextView;


            public ViewHolder(View view) {
                super(view);
                mCityTextView = (TextView)view;
            }
        }

        private List<String> mCitys;

        public CityAdapter(List<String> citys) {
            mCitys = citys;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null, false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 添加数据
                    City city = new City();
                    city.setName((String) holder.mCityTextView.getText());
                    CityLab.getSingleInstance(CitiesActivity.this).addCity(city);
//                    Intent intent = new Intent();
//                    intent.putExtra(EXTRA_SELECTED_CITY, city.getName());
//                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String city = mCitys.get(position);
            holder.mCityTextView.setText(city);
        }

        @Override
        public int getItemCount() {
            return mCitys.size();
        }
    }

    private class ProvinceAdapter extends RecyclerView.Adapter<ProvinceAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mProvinceTextView;


            public ViewHolder(View view) {
                super(view);
                mProvinceTextView = (TextView)view;
            }
        }

        private List<String> mProvinces;

        public ProvinceAdapter(List<String> provinces) {
            mProvinces = provinces;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null, false);
            final ViewHolder holder = new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //TODO
                    String province = (String) holder.mProvinceTextView.getText();
                    List<String> cities = Arrays.asList(getResources().getStringArray(getProvinceResID(province)));
                    mCityAdapter = new CityAdapter(cities);
                    mCityRecyclerView.setAdapter(mCityAdapter);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String province = mProvinces.get(position);
            holder.mProvinceTextView.setText(province);
        }

        @Override
        public int getItemCount() {
            return mProvinces.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        {
            mCityRecyclerView = (RecyclerView) findViewById(R.id.id_cities_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mCityRecyclerView.setLayoutManager(layoutManager);
            List<String> cities = Arrays.asList(getResources().getStringArray(R.array.北京));
            mCityAdapter = new CityAdapter(cities);
            mCityRecyclerView.setAdapter(mCityAdapter);
        }
        {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.id_province_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            List<String> provinces = new ArrayList<>();
            provinces.add("北京");
            provinces.add("上海");
            provinces.add("天津");
            provinces.add("重庆");
            provinces.add("香港");
            provinces.add("澳门");
            provinces.add("台湾");
            provinces.add("黑龙江");
            provinces.add("吉林");
            provinces.add("辽宁");
            provinces.add("内蒙古");
            provinces.add("河北");
            provinces.add("河南");
            provinces.add("山西");
            provinces.add("山东");
            provinces.add("江苏");
            provinces.add("浙江");
            provinces.add("福建");
            provinces.add("江西");
            provinces.add("安徽");
            provinces.add("湖北");
            provinces.add("湖南");
            provinces.add("广东");
            provinces.add("广西");
//            provinces.add("海南");
//            provinces.add("贵州");
//            provinces.add("云南");
//            provinces.add("四川");
//            provinces.add("西藏");
//            provinces.add("陕西");
//            provinces.add("宁夏");
//            provinces.add("甘肃");
//            provinces.add("青海");
//            provinces.add("新疆");

            ProvinceAdapter adapter = new ProvinceAdapter(provinces);
            recyclerView.setAdapter(adapter);
        }
    }

    private int getProvinceResID(String povince) {
        int resID = 0;
        switch (povince) {
            case "北京":
                resID = R.array.北京;
                break;
            case "上海":
                resID = R.array.上海;
                break;
            case "天津":
                resID = R.array.天津;
                break;
            case "重庆":
                resID = R.array.重庆;
                break;
            case "香港":
                resID = R.array.香港;
                break;
            case "澳门":
                resID = R.array.澳门;
                break;
            case "台湾":
                resID = R.array.台湾;
                break;
            case "黑龙江":
                resID = R.array.黑龙江;
                break;
            case "吉林":
                resID = R.array.吉林;
                break;
            case "辽宁":
                resID = R.array.辽宁;
                break;
            case "内蒙古":
                resID = R.array.内蒙古;
                break;
            case "河北":
                resID = R.array.河北;
                break;
            case "河南":
                resID = R.array.河南;
                break;
            case "山西":
                resID = R.array.山西;
                break;
            case "山东":
                resID = R.array.山东;
                break;
            case "江苏":
                resID = R.array.江苏;
                break;
            case "浙江":
                resID = R.array.浙江;
                break;
            case "福建":
                resID = R.array.福建;
                break;
            case "江西":
                resID = R.array.江西;
                break;
            case "安徽":
                resID = R.array.安徽;
                break;
            case "湖北":
                resID = R.array.湖北;
                break;
            case "湖南":
                resID = R.array.湖南;
                break;
            case "广东":
                resID = R.array.广东;
                break;
            case "广西":
                resID = R.array.广西;
                break;
//            case "海南":
//                resID = R.array.海南;
//                break;
//            case "贵州":
//                resID = R.array.贵州;
//                break;
//            case "云南":
//                resID = R.array.云南;
//                break;
//            case "四川":
//                resID = R.array.四川;
//                break;
//            case "西藏":
//                resID = R.array.西藏;
//                break;
//            case "陕西":
//                resID = R.array.陕西;
//                break;
//            case "宁夏":
//                resID = R.array.宁夏;
//                break;
//            case "甘肃":
//                resID = R.array.甘肃;
//                break;
//            case "青海":
//                resID = R.array.青海;
//                break;
//            case "新疆":
//                resID = R.array.新疆;
//                break;
            default:
        }
        return resID;
    }
}
