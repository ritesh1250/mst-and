package com.meest.social.socialViewModel.viewModel.feelingAndActivityViewModel;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;

import com.meest.R;
import com.meest.databinding.ActivityFragmentModelBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.FeelingResponse;
import com.meest.social.socialViewModel.adapter.feelActAdapters.ActivityRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityFragmentViewModel extends ViewModel {
    private static final String TAG = "ActivityFragmentViewMod";
    public ActivityRecyclerAdapter adapter;
    public FeelingResponse feelingResponse1;
    public ArrayList<FeelingResponse.Row> data = new ArrayList<>();
    FragmentActivity activity;
    public Context context;
    public EditText edt_search_feeling;
    private ActivityFragmentModelBinding binding;

    public ActivityFragmentViewModel(Context context, FragmentActivity activity, ActivityFragmentModelBinding binding, EditText edt_search_feeling) {
        this.activity = activity;
        this.context = context;
        this.binding = binding;
        this.edt_search_feeling = edt_search_feeling;
    }


    public void fetchData() {
        binding.loading.setVisibility(View.VISIBLE);

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        HashMap<String, String> body = new HashMap<>();
        body.put("id", "");

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FeelingResponse> categoryCall = webApi.getFeelingSubList(header, body);
        categoryCall.enqueue(new Callback<FeelingResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeelingResponse> call, @NotNull Response<FeelingResponse> response) {
                binding.loading.setVisibility(View.GONE);

                if (response.code() == 200 && response.body() != null
                        && response.body().getSuccess()) {
                    feelingResponse1 = response.body();
                    if (feelingResponse1 != null) {
                        bindData((ArrayList<FeelingResponse.Row>) feelingResponse1.getData().getRows());

                    }


                } else {
                    Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<FeelingResponse> call, Throwable t) {
                binding.loading.setVisibility(View.GONE);
                Utilss.showToast(context, context.getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    public void bindData(ArrayList<FeelingResponse.Row> rows) {
        if (rows != null && rows.size() > 0) {
//            data.add(feelingResponse1.getData().getRows().get(i).getTitle());
//            imageUrls.add(feelingResponse1.getData().getRows().get(i).getIcon());
            data.addAll(rows);

            int numberOfColumns = 3;
            binding.activityRecycler.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
//            adapter = new ActivityRecyclerAdapter(context, activity, data);

            adapter = new ActivityRecyclerAdapter(context, activity, rows);
            binding.activityRecycler.setAdapter(adapter);
        }
        else {
        }

    }

    public void filter(String toString) {
        {
            ArrayList<FeelingResponse.Row> filteredList = new ArrayList<>();
            for (FeelingResponse.Row item : feelingResponse1.getData().getRows()) {
                if (item.getTitle().toLowerCase().contains(toString.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            adapter.filterList(filteredList);
        }
    }

    public void editText() {
        edt_search_feeling.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }
}
