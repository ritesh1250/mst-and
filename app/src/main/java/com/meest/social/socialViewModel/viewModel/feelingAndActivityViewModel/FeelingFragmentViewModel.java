package com.meest.social.socialViewModel.viewModel.feelingAndActivityViewModel;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.GridLayoutManager;

import com.meest.R;
import com.meest.databinding.FeelingFragmentModelBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.FeelingResponse;
import com.meest.social.socialViewModel.adapter.feelActAdapters.FeelingRecyclerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeelingFragmentViewModel extends ViewModel {
    private static final String TAG = "FeelingFragmentViewMode";

    public ArrayList<FeelingResponse.Row> data = new ArrayList<>();
    public FeelingResponse feelingResponse;
    private FeelingFragmentModelBinding binding;
    FragmentActivity activity;
    public Context context;
    public EditText edt_search_feeling;
    public FeelingRecyclerAdapter adapter;


    public FeelingFragmentViewModel(Context context, FragmentActivity activity, FeelingFragmentModelBinding binding, EditText edt_search_feeling) {
        this.context = context;
        this.activity = activity;
        this.binding = binding;
        this.edt_search_feeling = edt_search_feeling;

    }

    public void fetchData() {
        binding.loading.setVisibility(View.VISIBLE);

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FeelingResponse> categoryCall = webApi.getFeelingList(header);
        categoryCall.enqueue(new Callback<FeelingResponse>() {
            @Override
            public void onResponse(@NotNull Call<FeelingResponse> call, @NotNull Response<FeelingResponse> response) {
                binding.loading.setVisibility(View.GONE);

                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    feelingResponse = response.body();
                    if (feelingResponse!=null && feelingResponse.getData()!=null)
                    {
                        bindData(feelingResponse.getData().getRows());
                    }
                    else
                        Utilss.showToast(context, context.getString(R.string.no_data_available), R.color.grey);
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

    public void bindData(List<FeelingResponse.Row> rows) {
        if (rows!=null && rows.size()>0){
            data = new ArrayList<>();
            data.addAll(rows);
            int numberOfColumns = 4;
            binding.feelingRecycler.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
            adapter = new FeelingRecyclerAdapter(context, data, activity, feelingResponse, edt_search_feeling);
           // adapter.notifyDataSetChanged();
            binding.feelingRecycler.setAdapter(adapter);

//            adapter.setOnItemClickListener(new FeelingRecyclerAdapter.OnEmojiClickListener() {
//                @Override
//                public void feelingData(String feelingID, String feelingTitle) {
//                }
//            });

        }
    }

    public void filter(String toString) {
        ArrayList<FeelingResponse.Row> filteredList = new ArrayList<>();
        for (FeelingResponse.Row item : feelingResponse.getData().getRows()) {
            if (item.getTitle().toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}

