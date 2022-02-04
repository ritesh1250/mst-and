package com.meest.social.socialViewModel.viewModel.searchLocationViewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.meest.Activities.preview.PreviewPhotoActivity;
import com.meest.Adapters.PlacesAutoCompleteAdapter;
import com.meest.R;
import com.meest.databinding.SearchLocationModelBinding;
import com.meest.utils.Variables;
import com.meest.utils.goLiveUtils.CommonUtils;

public class SearchLocationViewModel extends ViewModel {
    private Context context;
    private Activity activity;
    private SearchLocationModelBinding binding;
    public String apiKey;
    public String mapUrl,type,latitude,longitude,address_name;
    public PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    public PlacesAutoCompleteAdapter.ClickListener clickListener;
    public SearchLocationViewModel(Context context, Activity activity, SearchLocationModelBinding binding) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
    }

    public void getTheData() {
        apiKey = context.getString(R.string.google_maps_key);
        if (!Places.isInitialized()) {
            Places.initialize(context, apiKey);
        }
        initRecyclerView();
        if (activity.getIntent()!=null)
        {
            mapUrl=activity.getIntent().getStringExtra("imagePath");
            type=activity.getIntent().getStringExtra("type");
            latitude=activity.getIntent().getStringExtra("lat");
            longitude=activity.getIntent().getStringExtra("log");
        }
        if (!CommonUtils.getAddress(context,Double.parseDouble(latitude),Double.parseDouble(longitude)).isEmpty())
        {
            binding.tvCurrentLocation.setText(CommonUtils.getAddress(context,Double.parseDouble(latitude),Double.parseDouble(longitude)));
        }
        else
        {
            binding.tvCurrentLocation.setText(context.getString(R.string.unable_to_find_current_location));
        }
    }

    private void initRecyclerView() {
        binding.placeSearch.addTextChangedListener(filterTextWatcher);
        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(context);
        binding.placesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.placesRecyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
    }

    public void currentLocationTv() {
        String url = Variables.local(latitude + "", longitude + "");
        Intent i = new Intent(context, PreviewPhotoActivity.class);
        address_name= CommonUtils.getAddress(context,Double.parseDouble(latitude),Double.parseDouble(longitude));
        i.putExtra("imagePath", url);
        i.putExtra("type", type);
        i.putExtra("lat", latitude);
        i.putExtra("log", longitude);
        i.putExtra("address_name", address_name);
        activity.startActivity(i);
        activity.finish();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (binding.placesRecyclerView.getVisibility() == View.GONE) {binding.placesRecyclerView.setVisibility(View.VISIBLE);}
            } else {
                if (binding.placesRecyclerView.getVisibility() == View.VISIBLE) {binding.placesRecyclerView.setVisibility(View.GONE);}
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    public void clickFuncForAutoComplete(Place place) {
        latitude= String.valueOf(place.getLatLng().latitude);
        longitude= String.valueOf(place.getLatLng().longitude);
        address_name=place.getAddress();
        String url = Variables.local(latitude + "", longitude + "");
        Intent i = new Intent(context, PreviewPhotoActivity.class);
        i.putExtra("imagePath", url);
        i.putExtra("type", type);
        i.putExtra("lat", latitude);
        i.putExtra("log", longitude);
        i.putExtra("address_name", address_name);
        context.startActivity(i);
        activity.finish();
    }
}
