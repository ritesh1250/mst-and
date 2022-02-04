package com.meest.TopAndTrends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.responses.TrendPeopleResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrendPeopleFragment extends Fragment {

    TrendPeopleAdapter adapter;
    RecyclerView.LayoutManager manager;
    private Context context;
    RecyclerView recyclerTrendPeople;
    String TrendingPeople = "";

    boolean isTrending;
    ArrayList<TrendPeopleResponse.Datum> trendPeopleList = new ArrayList();
    int pageno = 1;
    int pastVisibleItemCount = 0;
    int totalItemCount = 0;
    int visibleItemCount = 0;
    int pagePerRecord = 20;
    int totalCount = 0;
    boolean Loading = true;

    public TrendPeopleFragment(Context context, boolean isTrending) {
        this.context = context;
        this.isTrending = isTrending;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trend_people, container, false);

        recyclerTrendPeople = view.findViewById(R.id.recyclerTrendPeople);

        bindData(TrendingPeople, String.valueOf(pageno));

        recyclerTrendPeople.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = Objects.requireNonNull(recyclerView.getLayoutManager()).getChildCount();
                    totalItemCount = recyclerView.getLayoutManager().getItemCount();
                    pastVisibleItemCount = ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                    if (Loading) {
                        if ((visibleItemCount + pastVisibleItemCount) >= totalItemCount) {
                            Loading = false;
                            pageno = pageno + 1;
                            bindData(TrendingPeople, String.valueOf(pageno));

                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 111) {
            String friendStatus = data.getStringExtra("friendStatus");
            int position = data.getIntExtra("position", -1);
            Log.e("alhaj", "1 " + friendStatus);
            if (!friendStatus.equals("")) {
                trendPeopleList.get(position).setFirendStatus(friendStatus);
                adapter.notifyItemChanged(position);
            }
        }
    }

    public void bindData(String s, String pageno) {
        {
            {

                Map<String, String> map = new HashMap<>();
                map.put("Accept", "application/json");
                map.put("Content-Type", "application/json");
                map.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                HashMap<String, Object> body = new HashMap<>();
                body.put("key", "days");
                body.put("value", 30);
                body.put("pageNumber", pageno);
                body.put("pageSize", String.valueOf(pagePerRecord));
                Call<TrendPeopleResponse> call;
                if (isTrending) {
                    call = webApi.trend_people(map, body);
                } else {
                    call = webApi.top_people(map, body);
                }
                call.enqueue(new Callback<TrendPeopleResponse>() {
                    @Override
                    public void onResponse(Call<TrendPeopleResponse> call, Response<TrendPeopleResponse> response) {
                        try {

                            if (response.code() == 200 && response.body().getSuccess()) {

                                trendPeopleList.clear();
                                for (int i = 0; i < response.body().getData().size(); i++) {
                                    trendPeopleList.add(response.body().getData().get(i));
                                }

                                manager = new LinearLayoutManager(getContext());
                                recyclerTrendPeople.setItemAnimator(new DefaultItemAnimator());
                                adapter = new TrendPeopleAdapter(getActivity(), trendPeopleList);
                                recyclerTrendPeople.setLayoutManager(manager);
                                recyclerTrendPeople.setAdapter(adapter);

                                adapter.setOnItemClickListenerUnFollow(new TrendPeopleAdapter.OnItemClickListenerUnFollow() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(getActivity(), OtherUserAccount.class);
                                        intent.putExtra("userId", trendPeopleList.get(position).getId());
                                        intent.putExtra("position", position);
                                        startActivityForResult(intent, 111);
                                    }
                                });
                            } else {
                                Utilss.showToast(getContext(), response.body().getSuccess().toString(), R.color.msg_fail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(getContext(),"server Error",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TrendPeopleResponse> call, Throwable t) {
                        Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                    }
                });
            }

        }
    }
}