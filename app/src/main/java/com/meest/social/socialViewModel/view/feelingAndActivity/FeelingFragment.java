package com.meest.social.socialViewModel.view.feelingAndActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.meest.databinding.FeelingFragmentModelBinding;
import com.meest.responses.FeelingResponse;
import com.meest.social.socialViewModel.viewModel.feelingAndActivityViewModel.FeelingFragmentViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.ArrayList;

public class FeelingFragment extends Fragment {

    private static final String TAG = "FeelingFragment";

    private FeelingFragmentModelBinding binding;
    private FeelingFragmentViewModel viewModel;
    private Context context;
    EditText edt_search_feeling;
    FeelingResponse feelingResponse;

    ArrayList<FeelingResponse.Row> data = new ArrayList<>();

    public FeelingFragment (EditText edt_search_feeling) {
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

        binding = DataBindingUtil.inflate(inflater, R.layout.feeling_fragment_model, container, false);
        viewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(new FeelingFragmentViewModel(context,getActivity(), binding, edt_search_feeling)).createFor()).get(FeelingFragmentViewModel.class);
        binding.setCreateTextPostModel(viewModel);
        viewModel.fetchData();

        edt_search_feeling.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.filter(s.toString());
            }
        });
//


        return binding.getRoot();

    }

}
