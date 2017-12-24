package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.zip.Inflater;

public class activity_forecast extends AppCompatActivity implements ForecastAdapter.ForecastAdapterOnClickHandler1 {

    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;
    private Toast mToast;
    TextView mErrorTextView;
    ProgressBar loadingProgressBar;
    private String mWeatherLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mRecyclerView = findViewById(R.id.rv_forecast);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);

        mErrorTextView = findViewById(R.id.tv_error_msg);
        loadingProgressBar = findViewById(R.id.pb_loading);
        loadWeatherData();
    }

    public void loadWeatherData() {
        showWeatherDataView();
        mWeatherLocation = SunshinePreferences.getPreferredWeatherLocation(this);

        Double[] locationCoordinates;

        new ConnectingToTheInternet().execute(mWeatherLocation);
    }

    //interfaceMethod is onClick
    @Override
    public void interfaceMethodOnClick(String x) {
        Context context = this;

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("WeatherDataFromActivityForecast", x);
        startActivity(intent);

//        if (mToast!=null){
//            mToast.cancel();
//        }
//        mToast =  Toast.makeText(this, x, Toast.LENGTH_LONG);
//        mToast.show();
    }

    public void showWeatherDataView() {
        mErrorTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMsg() {
        mErrorTextView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    public class ConnectingToTheInternet extends AsyncTask<String, Void, String[]> {

        @Override
        protected void onPreExecute() {
            loadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String location = strings[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);
            try {
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(activity_forecast.this, jsonWeatherResponse);
                return simpleJsonWeatherData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String[] strings) {
            loadingProgressBar.setVisibility(View.INVISIBLE);
            if (strings != null) {
                showWeatherDataView();
                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                mForecastAdapter.setWeatherData(strings);
            } else {
                showErrorMsg();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();


        switch (selectedItemId) {
            case R.id.action_refresh:
                mRecyclerView.setAdapter(null);
                mRecyclerView.setAdapter(mForecastAdapter);
                loadWeatherData();
                break;
            case R.id.action_open_map:
                openMap(null,null,mWeatherLocation);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void openMap(Double x, Double y, String searchLocationQuery) {

        String scheme = "geo:";
        Double defaultZoom = 5.0;

        if((x==null || y==null) && searchLocationQuery ==null){
            x = 0.0;
            y = 0.0;
            searchLocationQuery = "Kuala Lampur";
        }



        String uriString = scheme + String.valueOf(x) + "," + String.valueOf(y) +"?q=" + searchLocationQuery /*+ "&z=" +
                String.valueOf(defaultZoom)*/;

        Uri uri = Uri.parse(uriString);



        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}
