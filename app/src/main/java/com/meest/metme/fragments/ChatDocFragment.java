package com.meest.metme.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.R;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.databinding.FragmentChatDocBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.adapter.ChatDocumentDataAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatDocFragment extends Fragment {
    FragmentChatDocBinding fragmentChatDocBinding;
    String ChatheadId,otherUserId;
    ChatDocumentDataAdapter doctumentAdapter;
    ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> MediaDocumentList = new ArrayList<>();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment8
        //   return inflater.inflate(R.layout.fragment_chat_doc, container, false);
        fragmentChatDocBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_doc, container, false);
        fragmentChatDocBinding.setLifecycleOwner(this);
        ChatheadId = getArguments().getString("chatHeadId");
        otherUserId = getArguments().getString("otherUserId");
        return fragmentChatDocBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentChatDocBinding.loading.setAnimation("loading.json");
        fragmentChatDocBinding.loading.playAnimation();
        fragmentChatDocBinding.loading.loop(true);
        getDocsMedia();
    }

    private void getDocsMedia() {
        fragmentChatDocBinding.loading.setVisibility(View.VISIBLE);
        Map<String, String> header = new HashMap<>();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        Map body = new HashMap<>();
        body.put("chatHeadId", ChatheadId);
        body.put("toUserId", otherUserId);
        body.put("attachmentType", new String[]{"File"});
        Call<GetDocsMediaChatsResponse> call = webApi.getDocsMedia(header, body);
        call.enqueue(new Callback<GetDocsMediaChatsResponse>() {
            @Override
            public void onResponse(Call<GetDocsMediaChatsResponse> call, Response<GetDocsMediaChatsResponse> response) {
                fragmentChatDocBinding.loading.setVisibility(View.GONE);
                if (response.code() == 200) {
                    try {
                        if (response.body().getData() != null) {

                            if (response.body().getData().getAll().size() > 0) {


//                            for (int i = 0; i <= response.body().getData().getLastWeek().size() - 1; i++) {
//                                MediaDocumentList.add(response.body().getData().getLastWeek().get(i));
//                            }
//                            for (int i = 0; i <= response.body().getData().getLastMonth().size() - 1; i++) {
//                                MediaDocumentList.add(response.body().getData().getLastMonth().get(i));
//                            }
                                for (int i = 0; i <= response.body().getData().getAll().size() - 1; i++) {
                                    MediaDocumentList.add(response.body().getData().getAll().get(i));
                                }
                                doctumentAdapter = new ChatDocumentDataAdapter(MediaDocumentList, getActivity());
                                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                                fragmentChatDocBinding.recyclerview.setLayoutManager(manager);
                                fragmentChatDocBinding.recyclerview.setItemAnimator(new DefaultItemAnimator());
                                fragmentChatDocBinding.recyclerview.setAdapter(doctumentAdapter);
                            } else {
                                fragmentChatDocBinding.rlMain.setVisibility(View.GONE);
                                fragmentChatDocBinding.emptyview.setVisibility(View.VISIBLE);
                            }

                        } else {
                            fragmentChatDocBinding.rlMain.setVisibility(View.GONE);
                            fragmentChatDocBinding.emptyview.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<GetDocsMediaChatsResponse> call, Throwable t) {
                fragmentChatDocBinding.loading.setVisibility(View.GONE);
                Log.w("error", t.toString());

                Log.e("TAg", "getmmediadocument=");
            }
        });
    }

}