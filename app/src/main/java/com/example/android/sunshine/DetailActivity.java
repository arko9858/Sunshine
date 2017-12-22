package com.example.android.sunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    TextView mDummyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mDummyTextView = findViewById(R.id.tv_dummy);
        Intent intent = getIntent();

        if(intent.hasExtra("WeatherDataFromActivityForecast")==true){
            String val = intent.getStringExtra("WeatherDataFromActivityForecast");
            mDummyTextView.setText(val);
        }
    }
}
