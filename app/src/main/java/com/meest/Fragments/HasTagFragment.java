package com.meest.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.HastagAdapter;
import com.meest.model.HastagItem;
import com.meest.Paramaters.SearchHomeParam;
import com.meest.R;
import com.meest.responses.SearchHomeResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HasTagFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    private static HasTagFragment instance = null;
    String Hastags = "";
    LottieAnimationView image;
    ArrayList<SearchHomeResponse.Data.Hashtag> HashTagList = new ArrayList();
    int pageno = 1;
    int pastVisibleItemCount = 0;
    int totalItemCount = 0;
    int visibleItemCount = 0;
    int pagePerRecord = 20;
    int totalCount = 0;
    boolean Loading = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hashtag, viewGroup, false);
        ArrayList<HastagItem> Hitems = new ArrayList<>();
        instance = this;


        image = view.findViewById(R.id.loading);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);


        Hitems.add(new HastagItem("#queen", "1.4M Videos"));
        Hitems.add(new HastagItem("#queenharley", "2.9M Videos"));
        Hitems.add(new HastagItem("#queendance", "298k Videos"));
        Hitems.add(new HastagItem("#queen", "1.4M Videos"));
        Hitems.add(new HastagItem("#queenharley", "2.9M Videos"));
        Hitems.add(new HastagItem("#queendance", "298k Videos"));
        Hitems.add(new HastagItem("#queen", "1.4M Videos"));
        Hitems.add(new HastagItem("#queenharley", "2.9M Videos"));
        Hitems.add(new HastagItem("#queendance", "298k Videos"));
        Hitems.add(new HastagItem("#queen", "1.4M Videos"));
        Hitems.add(new HastagItem("#queenharley", "2.9M Videos"));
        Hitems.add(new HastagItem("#queendance", "298k Videos"));
        Hitems.add(new HastagItem("#queen", "1.4M Videos"));
        Hitems.add(new HastagItem("#queenharley", "2.9M Videos"));
        Hitems.add(new HastagItem("#queendance", "298k Videos"));

        recyclerView = view.findViewById(R.id.hashtag_recycleView);
        SearchPeoples(Hastags, String.valueOf(pageno));


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            SearchPeoples(Hastags, String.valueOf(pageno));

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

    public static HasTagFragment getInstance() {
        return instance;
    }


    public void SearchPeoples(String s, String pageno) {
        Hastags = s;
        image.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        SearchHomeParam searchHomeParam = new SearchHomeParam();
        searchHomeParam.setKey(s);
        searchHomeParam.setPageNo(pageno);
        searchHomeParam.setPageRecord(String.valueOf(pagePerRecord));
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", pageno);
        body.put("pageSize", String.valueOf(pagePerRecord));
        Call<SearchHomeResponse> call = webApi.search_home(map, searchHomeParam);
        call.enqueue(new Callback<SearchHomeResponse>() {
            @Override
            public void onResponse(Call<SearchHomeResponse> call, Response<SearchHomeResponse> response) {
                try {
                    image.setVisibility(View.GONE);
                    if (response.code() == 200) {
                        if (response.body().getData().getHashtags().size() > 0) {
                            Loading = true;
                        }

//                        HashTagList.clear();
                        HashTagList.addAll(response.body().getData().getHashtags());
                        manager = new LinearLayoutManager(getContext());
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        adapter = new HastagAdapter(HashTagList, getActivity());
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(adapter);
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
            public void onFailure(Call<SearchHomeResponse> call, Throwable t) {
                image.setVisibility(View.GONE);
                Utilss.showToast(getContext(), getString(R.string.Server_Error), R.color.msg_fail);
            }
        });
    }
}
