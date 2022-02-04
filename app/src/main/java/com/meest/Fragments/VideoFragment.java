package com.meest.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.VideoAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoFragment extends Fragment {

    RecyclerView recyclerView;
    VideoAdapter adapter;
    RecyclerView.LayoutManager manager;
    String  Videos="";
    private static VideoFragment instance = null;
    ArrayList<SearchHomeResponse.Data.Video>VideoSerachList = new ArrayList<>();
    LottieAnimationView image;
    SharedPrefreances sharedPrefreances;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, viewGroup, false);

        instance = this;
        adapter = new VideoAdapter(VideoSerachList,getActivity());

        recyclerView = view.findViewById(R.id.video_recycleView);
        // recyclerView.setHasFixedSize(true);
//        adapter = new VideoAdapter(Vitems);
//        manager = new GridLayoutManager(getActivity(),2);
        // recyclerView.setLayoutManager(manager);
        // recyclerView.setAdapter(adapter);
        // int numberOfColumns = 2;
        //RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        // recyclerView.setLayoutManager(manager);

        image = view.findViewById(R.id.loading);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);

        SearchPeoples(Videos);

        return view;
    }

    public static VideoFragment getInstance() {
        return instance;
    }

    public  void SearchPeoples(String s){
        Videos=s;
        image.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", sharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        SearchHomeParam searchHomeParam = new SearchHomeParam();
        searchHomeParam.setKey(s);
        Call<SearchHomeResponse> call = webApi.search_home(map,searchHomeParam);
        call.enqueue(new Callback<SearchHomeResponse>() {
            @Override
            public void onResponse(Call<SearchHomeResponse> call, Response<SearchHomeResponse> response) {
                image.setVisibility(View.GONE);
                if (response.isSuccessful())
                {
                    try {
                        if (response.code()==200  ){
                            VideoSerachList.clear();
                            for (int i =0; i < response.body().getData().getVideos().size();i++){
                                VideoSerachList.add(response.body().getData().getVideos().get(i));
                            }

                            manager = new GridLayoutManager(getActivity(),2);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(manager);
                            adapter.notifyDataSetChanged();

                        }else {
                            image.setVisibility(View.GONE);
                            Utilss.showToast(getContext(),response.body().getSuccess().toString(),R.color.msg_fail);
                        }
                    }catch (NumberFormatException  e){
                        image.setVisibility(View.GONE);
                        e.printStackTrace();
                        //Toast.makeText(getContext(),"rwereqwreqwrwrwqrqewr",Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<SearchHomeResponse> call, Throwable t) {
                image.setVisibility(View.GONE);
                Log.e("TAg","videofragments="+t.getMessage());
            }
        });


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //Write down your refresh code here, it will call every time user come to this fragment.
            //If you are using listview with custom adapter, just call notifyDataSetChanged().
            Log.e("ATg","checkingtags="+"video");
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
            }
        }
    }
}
