package com.meest.metme.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meest.R;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.databinding.FragmentChatLinkBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.adapter.ChatLinkDataAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatLinkFragment extends Fragment {
    FragmentChatLinkBinding fragmentChatDocBinding;
    String ChatheadId,otherUserId;
    ChatLinkDataAdapter chatLinkDataAdapter;

    ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> MediaDocumentList = new ArrayList<>();

    public ChatLinkFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentChatDocBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat_link, container, false);
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
        getLinkMedia();
    }

    private void getLinkMedia() {
        fragmentChatDocBinding.loading.setVisibility(View.VISIBLE);
        Map<String, String> header = new HashMap<>();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        HashMap body = new HashMap<>();
        body.put("chatHeadId", ChatheadId);
        body.put("toUserId", otherUserId);
        body.put("attachmentType", new String[]{"links"});
        Call<GetDocsMediaChatsResponse> call = webApi.getDocsMedia(header, body);
        call.enqueue(new Callback<GetDocsMediaChatsResponse>() {
            @Override
            public void onResponse(Call<GetDocsMediaChatsResponse> call, Response<GetDocsMediaChatsResponse> response) {
                fragmentChatDocBinding.loading.setVisibility(View.GONE);
                if (response.code() == 200) {
                    Log.e("TAg", "getLinkMedia=" + response.body());
                    try {
                        if (response.body().getData() != null) {

                            if (response.body().getData().getAll().size() > 0) {

                                for (int i = 0; i <= response.body().getData().getLastWeek().size() - 1; i++) {
                                    MediaDocumentList.add(response.body().getData().getLastWeek().get(i));
                                }
                                chatLinkDataAdapter = new ChatLinkDataAdapter(MediaDocumentList, getActivity());
                                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                                fragmentChatDocBinding.recyclerview.setLayoutManager(manager);
                                fragmentChatDocBinding.recyclerview.setItemAnimator(new DefaultItemAnimator());
                                fragmentChatDocBinding.recyclerview.setAdapter(chatLinkDataAdapter);
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
                Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }
}