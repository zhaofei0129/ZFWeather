package com.muyi.zhaofei.zfweather;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeatherActivity extends BasicActivity {
    private static final String TAG = "WeatherActivity";
    private static final int REQUEST_CODE_SELECTED_CITIES_ACTIVITY = 1;
    private static final String KEY_IS_THE_FIRST_TIME_OPEN = "KEY_IS_THE_FIRST_TIME_OPEN";

    public LocationClient mLocationClient = null;
    //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
    //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
    private String mLon;
    private String mLat;
    private Date mRefreshTime;
    private List<Weather> mWeathers;
    private String mCityName;

    private class WeatherViewHolder extends RecyclerView.ViewHolder {

        TextView mWeekdayTextView;
        TextView mTodayTextView;
//        TextView mCondDTextView;
        ImageView mCondDImageView;
        TextView mMaxTmpTextView;
        TextView mMinTmpTextView;

        public WeatherViewHolder(View view) {
            super(view);
            mWeekdayTextView = (TextView)view.findViewById(R.id.id_weekday_text_view);
            mTodayTextView = (TextView)view.findViewById(R.id.id_today_text_view);
//            mCondDTextView = (TextView)view.findViewById(R.id.id_cond_d_text_view);
            mCondDImageView = (ImageView)view.findViewById(R.id.id_cond_d_image_view);
            mMaxTmpTextView = (TextView)view.findViewById(R.id.id_max_tmp_text_view);
            mMinTmpTextView = (TextView)view.findViewById(R.id.id_min_tmp_text_view);
        }
    }

    private class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
        private List<Weather> mWeathers;

        public WeatherAdapter(List<Weather> weathers) {
            mWeathers = weathers;
        }

        @Override
        public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item, null, false);
            WeatherViewHolder holder = new WeatherViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(WeatherViewHolder holder, int position) {
            Weather weather = mWeathers.get(position);
            String[] weeks = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            Calendar c = Calendar.getInstance();
            c.setTime(mRefreshTime);
            int weekday=c.get(Calendar.DAY_OF_WEEK);
            holder.mWeekdayTextView.setText(weeks[(weekday + position - 1) % 7]);
            if (position == 0) {
                holder.mTodayTextView.setText("今天");
            }
//            holder.mCondDTextView.setText(weather.getConditionDaytime());
            //  将图片名称以字母开头，根据不同天气代码返回相应。。。
            Resources resources = WeatherActivity.this.getResources();
            int resID = resources.getIdentifier(weather.getConditionDaytimeCode(), "drawable", getPackageName());
            holder.mCondDImageView.setImageResource(resID);
            holder.mMaxTmpTextView.setText(weather.getMaxTmp());
            holder.mMinTmpTextView.setText(weather.getMinTmp());
        }

        @Override
        public int getItemCount() {
            return mWeathers.size();
        }
    }

    private class DoGetWeatherDataAsyncTask extends AsyncTask<Void, Void, List<Weather>> {

        @Override
        protected List<Weather> doInBackground(Void... voids) {
            // 得到weather数据
            List<Weather> weathers = new ArrayList<>();
            final String HE_WEATHER_API_KEY = "56271dea21834020848a6e838fd53ecf";
            final String HE_WEATHER_ADDRESS = "https://free-api.heweather.com/s6/";
            String str = "";
            try {
                str = URLEncoder.encode(mCityName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 3; i++) {

                Weather weather = new Weather();
                if (i == 0) {
                    String weatherNowAddress = HE_WEATHER_ADDRESS + "weather/now?location=" + str + "&key=" + HE_WEATHER_API_KEY;
                    String weatherNowJsonStr = getJsonStr(weatherNowAddress);
                    try {
                        JSONObject weatherNowJson = new JSONObject(weatherNowJsonStr);
                        JSONArray heWeather6Array = weatherNowJson.getJSONArray("HeWeather6");
                        JSONObject heWeather6 = heWeather6Array.getJSONObject(0);
                        JSONObject now = heWeather6.getJSONObject("now");
                        JSONObject basic = heWeather6.getJSONObject("basic");
                        weather.setCity(basic.getString("parent_city"));
                        weather.setConditionNow(now.getString("cond_txt"));
                        weather.setTmpNow(now.getString("tmp"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String weatherForecastAddress = HE_WEATHER_ADDRESS + "weather/forecast?location=" + str + "&key=" + HE_WEATHER_API_KEY;
                String weatherForecastJsonStr = getJsonStr(weatherForecastAddress);
                try {
                    JSONObject weatherForecastJson = new JSONObject(weatherForecastJsonStr);
                    JSONArray heWeather6Array = weatherForecastJson.getJSONArray("HeWeather6");
                    JSONObject heWeather6 = heWeather6Array.getJSONObject(0);
                    JSONArray dailyForecastArray = heWeather6.getJSONArray("daily_forecast");
                    JSONObject dailyForcast = dailyForecastArray.getJSONObject(i);
//                    weather.setConditionDaytime(dailyForcast.getString("cond_txt_d"));
                    weather.setConditionDaytimeCode(dailyForcast.getString("cond_code_d"));
                    weather.setMaxTmp(dailyForcast.getString("tmp_max"));
                    weather.setMinTmp(dailyForcast.getString("tmp_min"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                weathers.add(weather);
            }

            return weathers;
        }

        @Override
        protected void onPostExecute(List<Weather> weathers) {
            super.onPostExecute(weathers);
            Weather weatherToday = weathers.get(0);
            TextView cityTextView = (TextView)findViewById(R.id.id_city_text_view);
            cityTextView.setText(weatherToday.getCity());
            TextView condNowTextView = (TextView)findViewById(R.id.id_cond_now_text_view);
            condNowTextView.setText(weatherToday.getConditionNow());
            TextView tmpNowTextView = (TextView)findViewById(R.id.id_tmp_now_text_view);
            tmpNowTextView.setText(weatherToday.getTmpNow() + " °");
            TextView refreshTimeTextView = (TextView)findViewById(R.id.id_refresh_time_text_view);
            mRefreshTime = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            String refreshTimeStr = simpleDateFormat.format(mRefreshTime);
            refreshTimeTextView.setText(refreshTimeStr);

            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.id_3_days_recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(WeatherActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            WeatherAdapter adapter = new WeatherAdapter(weathers);
            recyclerView.setAdapter(adapter);
            mWeathers = weathers;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_weather);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
                //以下只列举部分获取经纬度相关（常用）的结果信息
                mCityName = bdLocation.getCity();    //获取城市
                Log.d(TAG, "onReceiveLocation: " + mCityName);
                City c = new City();
                c.setName(mCityName);
                c.setLocated(true);
                c.setSelected(true);
                CityLab.getSingleInstance(WeatherActivity.this).addCity(c);
                new DoGetWeatherDataAsyncTask().execute();
            }
        });
        //注册监听函数
        setLocationClient();
//mLocationClient为第二步初始化过的LocationClient对象
//调用LocationClient的start()方法，便可发起定位请求



        CityLab.getSingleInstance(this);
        boolean isTheFirstTimeOpen;
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        isTheFirstTimeOpen = preferences.getBoolean(KEY_IS_THE_FIRST_TIME_OPEN, true);
        if (isTheFirstTimeOpen) {
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putBoolean(KEY_IS_THE_FIRST_TIME_OPEN, false);
            editor.apply();
            Log.d(TAG, "onCreate: is first time");
            mLocationClient.start();
        } else {
            Log.d(TAG, "onCreate: is not first time");
            mCityName = CityLab.getSingleInstance(this).getSelectedCity().getName();
            new DoGetWeatherDataAsyncTask().execute();
        }

        Button citysButton = (Button)findViewById(R.id.id_citys_button);
        citysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SelectedCitiesActivity.newIntent(WeatherActivity.this, mWeathers.get(0).getCity());
                startActivityForResult(intent, REQUEST_CODE_SELECTED_CITIES_ACTIVITY);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();//停止定位

    }

    private void setLocationClient() {
        LocationClientOption option = new LocationClientOption();

//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
////可选，设置定位模式，默认高精度
////LocationMode.Hight_Accuracy：高精度；
////LocationMode. Battery_Saving：低功耗；
////LocationMode. Device_Sensors：仅使用设备；
//
//        option.setCoorType("bd09ll");
////可选，设置返回经纬度坐标类型，默认gcj02
////gcj02：国测局坐标；
////bd09ll：百度经纬度坐标；
////bd09：百度墨卡托坐标；
////海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
//
        option.setScanSpan(0);
////可选，设置发起定位请求的间隔，int类型，单位ms
////如果设置为0，则代表单次定位，即仅定位一次，默认为0
////如果设置非0，需设置1000ms以上才有效
//
//        option.setOpenGps(true);
////可选，设置是否使用gps，默认false
////使用高精度和仅用设备两种定位模式的，参数必须设置为true
//
//        option.setLocationNotify(true);
////可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
//
//        option.setIgnoreKillProcess(false);
////可选，定位SDK内部是一个service，并放到了独立进程。
////设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
//
//        option.SetIgnoreCacheException(false);
////可选，设置是否收集Crash信息，默认收集，即参数为false
//
//        option.setWifiCacheTimeOut(5 * 60 * 1000);
////可选，7.2版本新增能力
////如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
//
//        option.setEnableSimulateGps(false);
////可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
    }

    private String getJsonStr(String address) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: 11");

    }

    //    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECTED_CITIES_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    mCityName = data.getStringExtra(SelectedCitiesActivity.EXTRA_SELECTED_CITY_NAME);
                    Log.d(TAG, "onActivityResult: " + mCityName);
                            new DoGetWeatherDataAsyncTask().execute();

                }
        }
    }

}

