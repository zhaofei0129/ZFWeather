package com.muyi.zhaofei.zfweather;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
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
            Log.d(TAG, "onBindViewHolder: ");
//            holder.mCondDTextView.setText(weather.getConditionDaytime());
            // TODO 将图片名称以字母开头，根据不同天气代码返回相应。。。
            holder.mCondDImageView.setImageResource(R.drawable.w100);
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
            Log.d(TAG, "doInBackground: sadddd" +mCityName);
            try {
                str = URLEncoder.encode(mCityName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 3; i++) {

                Weather weather = new Weather();
                if (i == 0) {
                    String weatherNowAddress;

                    if (mCityName.isEmpty()) {
                        weatherNowAddress = HE_WEATHER_ADDRESS + "weather/now?location=" + mLon + "," + mLat + "&key=" + HE_WEATHER_API_KEY;
                        Log.d(TAG, "doInBackground: 123");
                    } else {
                        weatherNowAddress = HE_WEATHER_ADDRESS + "weather/now?location=" + str + "&key=" + HE_WEATHER_API_KEY;
                        Log.d(TAG, "doInBackground: 456" + mCityName);
                        Log.d(TAG, "doInBackground: 456" + weatherNowAddress);

//                        weatherNowAddress = HE_WEATHER_ADDRESS + "weather/now?location=" + mCityName + "&key=" + HE_WEATHER_API_KEY;
                    }
                    String weatherNowJsonStr = getJsonStr(weatherNowAddress);
                    Log.d(TAG, "doInBackground: 1142222" + weatherNowJsonStr);

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
                String weatherForecastAddress;
                if (mCityName.isEmpty()) {
                    weatherForecastAddress = HE_WEATHER_ADDRESS + "weather/forecast?location=" + mLon + "," + mLat + "&key=" + HE_WEATHER_API_KEY;
                    Log.d(TAG, "doInBackground: 777");
                } else {
                    weatherForecastAddress = HE_WEATHER_ADDRESS + "weather/forecast?location=" + str + "&key=" + HE_WEATHER_API_KEY;
                    Log.d(TAG, "doInBackground: 888");

                }
                String weatherForecastJsonStr = getJsonStr(weatherForecastAddress);
                Log.d(TAG, "doInBackground: 1141" + weatherForecastJsonStr);
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

        Location location = LocationUtils.getInstance(this).showLocation();
        // location为null，需要开权限
        if (location != null) {
            mLon = String.format("%.2f", location.getLongitude());
            mLat = String.format("%.2f", location.getLatitude());
            Log.d(TAG, "onCreate: mLon, mLat = " + mLon +", " + mLat);
        }
        if (CityLab.getSingleInstance(this).getCities().size() > 0) {
            mCityName = CityLab.getSingleInstance(this).getSelectedCity().getName();
        } else {
            mCityName = "";
        }

        new DoGetWeatherDataAsyncTask().execute();

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
        LocationUtils.getInstance(this).removeLocationUpdatesListener();
    }

    private String getJsonStr(String address) {
        HttpURLConnection connection = null;
        try {
            Log.d(TAG, "getJsonStr: try");
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
            Log.d(TAG, "getJsonStr: catch");
            e.printStackTrace();
            return e.getMessage();
        } finally {
            Log.d(TAG, "getJsonStr: finally");
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

