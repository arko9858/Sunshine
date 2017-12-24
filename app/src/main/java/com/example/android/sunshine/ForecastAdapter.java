package com.example.android.sunshine;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by Arko on 21-Dec-17.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder1> {

    private String[] mWeatherData;

    final private ForecastAdapterOnClickHandler1 mClickHandler;

    public interface ForecastAdapterOnClickHandler1 {
        void interfaceMethodOnClick(String x);
    }

    public ForecastAdapter(ForecastAdapterOnClickHandler1 onClickHandler1) {

        mClickHandler = onClickHandler1;
    }

    public class ForecastAdapterViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder1(View itemView) {
            super(itemView);
            mWeatherTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String weatherForDay = mWeatherData[getAdapterPosition()];
            mClickHandler.interfaceMethodOnClick(weatherForDay);
        }
    }

    @Override
    public ForecastAdapterViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        Boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        ForecastAdapterViewHolder1 viewHolder1 = new ForecastAdapterViewHolder1(view);

        return viewHolder1;

    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder1 holder, int position) {
        String weatherForDay = mWeatherData[position];
        holder.mWeatherTextView.setText(weatherForDay);
    }

    @Override
    public int getItemCount() {
        if (mWeatherData == null) {
            return 0;
        }
        return mWeatherData.length;
    }

    public void setWeatherData(String[] weatherData1) {
        mWeatherData = weatherData1;
        notifyDataSetChanged();
    }
}
