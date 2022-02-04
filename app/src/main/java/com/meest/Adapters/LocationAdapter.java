package com.meest.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Interfaces.LocationCallback;
import com.meest.R;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private Context context;
    private List<String> locationList;
    private LocationCallback locationCallback;

    public LocationAdapter(Context context, List<String> locationList, LocationCallback locationCallback) {
        this.context = context;
        this.locationList = locationList;
        this.locationCallback = locationCallback;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.multiple_location_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        holder.txtLocation.setText(locationList.get(position));

        holder.imvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationCallback.removeLocation(position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView txtLocation;
        ImageView imvClose;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            imvClose = itemView.findViewById(R.id.imvClose);
        }
    }
}
