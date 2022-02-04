package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meest.model.TimeTrendingModel;
import com.meest.R;

import java.util.ArrayList;

public class TrendingTimeAdapter extends ArrayAdapter<TimeTrendingModel> {

    public TrendingTimeAdapter(Context context,
                               ArrayList<TimeTrendingModel> timeList)
    {
        super(context, 0, timeList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_time_treding, parent, false);
        }

        TextView timeTrending = convertView.findViewById(R.id.Tv_timeTrend);
        TimeTrendingModel currentItem = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (currentItem != null) {
            timeTrending.setText(currentItem.getTrendingTime());
        }
        return convertView;
    }
}

