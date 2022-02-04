package com.meest.social.socialViewModel.view.searchLocation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.libraries.places.api.model.Place;
import com.meest.Adapters.PlacesAutoCompleteAdapter;
import com.meest.R;
import com.meest.databinding.SearchLocationModelBinding;
import com.meest.social.socialViewModel.viewModel.searchLocationViewModel.SearchLocationViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class SearchLocation extends AppCompatActivity {

    private static final String TAG = "SearchLocation";
    private SearchLocationModelBinding binding;
    private SearchLocationViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.search_location_model);
        binding = DataBindingUtil.setContentView(this, R.layout.search_location_model);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new SearchLocationViewModel(this, SearchLocation.this, binding)).createFor()).get(SearchLocationViewModel.class);
        viewModel.getTheData();
        clickEvents();
    }

    private void clickEvents() {
        binding.dialogBack.setOnClickListener(v -> finish());
        binding.tvCurrentLocation.setOnClickListener(v -> viewModel.currentLocationTv());
        viewModel.mAutoCompleteAdapter.setClickListener(new PlacesAutoCompleteAdapter.ClickListener() {
            @Override
            public void click(Place place) {
                viewModel.clickFuncForAutoComplete(place);
            }
        });
    }


}