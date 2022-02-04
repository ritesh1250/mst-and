package com.meest.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Adapters.FollowingsAdapter;
import com.meest.R;
import com.meest.responses.FollowerListResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingsFragment extends Fragment {
    RecyclerView recyclerView;
    FollowingsAdapter adapter;
    ArrayList<FollowerListResponse.Following> arrayList = new ArrayList<>();
    private ProgressDialog progressDialog;
    String  user_id;
    EditText edit_search;

    public FollowingsFragment(String user_id) {
        this.user_id = user_id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.followings_fragment, viewGroup, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        edit_search = view.findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FollowerListResponse> call = webApi.followsData(Constant.token(getActivity()),user_id);
        call.enqueue(new Callback<FollowerListResponse>() {
            @Override
            public void onResponse(Call<FollowerListResponse> call, Response<FollowerListResponse> response) {
                progressDialog.dismiss();
                if (response.code() == 200){
                    if (response.body().getCode() == 1){

                        for (int i =0; i < response.body().getData().getFollowing().size();i++){
                            arrayList.add(response.body().getData().getFollowing().get(i));
                        }

                        adapter = new FollowingsAdapter(getActivity(),arrayList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        recyclerView.setAdapter(adapter);



                    }else {

                    }
                }else {
                  // Utilss.showToast(getContext(),"No", R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<FollowerListResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.w("error",t.toString());
                Utilss.showToast(getContext(), getString(R.string.Server_Error), R.color.msg_fail);
            }
        });





       /* recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = new FollowingsAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));*/
        return view;
    }


    void filter(String text) {
        ArrayList<FollowerListResponse.Following> temp = new ArrayList();
        for (FollowerListResponse.Following d : arrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getUsername().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
    }
}
