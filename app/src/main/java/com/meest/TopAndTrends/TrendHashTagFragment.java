package com.meest.TopAndTrends;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Fragments.HasTagFragment;
import com.meest.model.HastagItem;
import com.meest.R;
import com.meest.responses.TrendHashtagResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class TrendHashTagFragment extends Fragment {

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    private static HasTagFragment instance = null;
    String Hastags = "";
    RecyclerView recyclerTrendHashTag;
    ArrayList<TrendHashtagResponse.Datum> HashTagList = new ArrayList();
    Context context;
    boolean isTrending;


    public TrendHashTagFragment(Context context, boolean isTrending) {
        this.context = context;
        this.isTrending = isTrending;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trend_hash_tag, container, false);
        ArrayList<HastagItem> Hitems = new ArrayList<>();

        recyclerTrendHashTag = view.findViewById(R.id.recyclerTrendHashTag);
        SearchPeoples(Hastags);
        return view;

    }

    public static HasTagFragment getInstance() {
        return instance;
    }

    public void SearchPeoples(String s) {
        Hastags = s;
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
//        SearchHomeParam searchHomeParam = new SearchHomeParam();
//        searchHomeParam.setKey(s);
        Call<TrendHashtagResponse> call;

        if (isTrending) {
            call = webApi.trend_hashTag(map);
        } else {
            call = webApi.top_hashTag(map);
        }

        call.enqueue(new Callback<TrendHashtagResponse>() {
            @Override
            public void onResponse(Call<TrendHashtagResponse> call, Response<TrendHashtagResponse> response) {
                try {

                    if (response.code() == 200) {

                        HashTagList.clear();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            HashTagList.add(response.body().getData().get(i));
                        }

                        manager = new LinearLayoutManager(getContext());
                        recyclerTrendHashTag.setItemAnimator(new DefaultItemAnimator());
                        adapter = new TrendHashTagAdapter(getActivity(), HashTagList);
                        recyclerTrendHashTag.setLayoutManager(manager);
                        recyclerTrendHashTag.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    } else {
                        Utilss.showToast(getContext(), response.body().getSuccess().toString(), R.color.msg_fail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(getContext(),"rwereqwreqwrwrwqrqewr",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TrendHashtagResponse> call, Throwable t) {

                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }
}
