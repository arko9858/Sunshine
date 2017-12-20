package com.example.android.sunshine;

import android.graphics.Path;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.zip.Inflater;

public class activity_forecast extends AppCompatActivity {

    TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mWeatherTextView = (TextView)findViewById(R.id.tv_weather_data);

        loadWeatherData();
    }

    public void loadWeatherData(){
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new ConnectingToTheInternet().execute(location);
    }

    public class ConnectingToTheInternet extends AsyncTask<String ,Void,String[]>{

        @Override
        protected String[] doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }
            String location = strings[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);
            try{
                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
                String[] simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(activity_forecast.this,jsonWeatherResponse);
                return simpleJsonWeatherData;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (strings != null) {
                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                for (String weatherString : strings) {
                    mWeatherTextView.append((weatherString) + "\n\n\n");
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.forecast,menu   );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();

        if(selectedItemId == R.id.action_refresh){
            loadWeatherData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
