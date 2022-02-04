package com.meest.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.PeopleAdapter;
import com.meest.Paramaters.SearchHomeParam;
import com.meest.R;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.model.people_item;
import com.meest.responses.SearchHomeResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PeopleFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    PeopleAdapter peopleAdapter;
    RecyclerView.LayoutManager manager;
    EditText edit_people_data;
    private static PeopleFragment instance = null;
    String Peoples = "";
    ArrayList<SearchHomeResponse.Data.Persons> Peopleslist = new ArrayList<>();
    LottieAnimationView image;
    TextView no_data;

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people, viewGroup, false);


        instance = this;
        ArrayList<people_item> Pitems = new ArrayList<>();
        peopleAdapter = new PeopleAdapter(Peopleslist, getActivity());


        recyclerView = view.findViewById(R.id.people_recycleView);
        no_data = view.findViewById(R.id.no_data);

        image = view.findViewById(R.id.loading);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);


        peopleAdapter.setOnItemClickListenerUnFollow(new PeopleAdapter.OnItemClickListenerUnFollow() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), OtherUserAccount.class);
                intent.putExtra("userId", Peopleslist.get(position).getId());
                intent.putExtra("position", position);
                startActivityForResult(intent, 111);
            }
        });


        SearchPeoples(Peoples);
        return view;
    }

    public static PeopleFragment getInstance() {
        return instance;
    }

    public void SearchPeoples(String s) {
        Log.v("Shaban", "Search string == " + s);
        Peoples = s;
        image.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        SearchHomeParam searchHomeParam = new SearchHomeParam();
        searchHomeParam.setKey(s);
        Call<SearchHomeResponse> call = webApi.search_home(map, searchHomeParam);
        call.enqueue(new Callback<SearchHomeResponse>() {
            @Override
            public void onResponse(Call<SearchHomeResponse> call, Response<SearchHomeResponse> response) {
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        image.setVisibility(View.GONE);
                        Peopleslist.clear();
                        for (int i = 0; i < response.body().getData().getPeoples().size(); i++) {
                            Peopleslist.add(response.body().getData().getPeoples().get(i));
                        }
                        manager = new LinearLayoutManager(getContext());
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(peopleAdapter);
                        recyclerView.setLayoutManager(manager);
                        peopleAdapter.notifyDataSetChanged();
                    } else {
                        no_data.setVisibility(View.VISIBLE);
                        image.setVisibility(View.GONE);
                        Utilss.showToast(getContext(), response.body().getSuccess().toString(), R.color.msg_fail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchHomeResponse> call, Throwable t) {
                image.setVisibility(View.GONE);
                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
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
                Peopleslist.get(position).setFirendStatus(friendStatus);
                peopleAdapter.notifyItemChanged(position);
            }
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //Write down your refresh code here, it will call every time user come to this fragment.
            //If you are using listview with custom adapter, just call notifyDataSetChanged().

            Log.e("ATg", "checkingtags=" + "Peoples");
            if (getFragmentManager() != null) {


                getFragmentManager()
                        .beginTransaction()
                        .detach(this)
                        .attach(this)
                        .commit();
            }
        }
    }


   /* void filter(String text) {
        ArrayList<SearchHomeResponse.Data.Persons> temp = new ArrayList();
        for (SearchHomeResponse.Data.Persons d : Peopleslist) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getUsername().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        peopleAdapter.updateList(temp);
    }
*/
}
