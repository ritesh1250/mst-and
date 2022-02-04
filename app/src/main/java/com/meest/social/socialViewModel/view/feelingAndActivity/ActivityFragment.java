package com.meest.social.socialViewModel.view.feelingAndActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.meest.R;
import com.meest.databinding.ActivityFragmentModelBinding;
import com.meest.social.socialViewModel.viewModel.feelingAndActivityViewModel.ActivityFragmentViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class ActivityFragment extends Fragment {
    ActivityFragmentModelBinding binding;
    ActivityFragmentViewModel viewModel;
    Context context;
    EditText edt_search_feeling;
    int numberOfColumns = 3;

    public ActivityFragment(EditText edt_search_feeling) {
        this.edt_search_feeling = edt_search_feeling;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_fragment_model, container, false);
        viewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(new ActivityFragmentViewModel(context, getActivity(), binding, edt_search_feeling)).createFor()).get(ActivityFragmentViewModel.class);
        binding.setCreateTextPostModel(viewModel);
        viewModel.editText();
        viewModel.fetchData();
        return binding.getRoot();
    }
}
