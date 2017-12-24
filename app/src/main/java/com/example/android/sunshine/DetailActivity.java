package com.example.android.sunshine;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView mDummyTextView;
    private static String mWeatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDummyTextView = findViewById(R.id.tv_dummy);
        Intent intent = getIntent();

        if(intent.hasExtra("WeatherDataFromActivityForecast")==true){
            mWeatherInfo = intent.getStringExtra("WeatherDataFromActivityForecast");
            mDummyTextView.setText(mWeatherInfo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail,menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(shareIntent());
        return true;
    }

    private Intent shareIntent(){
        String mimeType = "text/plain";
        String title = "Share With";
        return ShareCompat.IntentBuilder.from(this).setChooserTitle(title).setType(mimeType).setText(mWeatherInfo).getIntent();

    }


}
