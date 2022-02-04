package com.meest.metme.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meest.R;

import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.databinding.ChatImageFragmentBinding;
import com.meest.metme.adapter.ChatImageDataAdapter;

import org.jetbrains.annotations.NotNull;

import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;
import java.util.Map;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatImageFragment extends Fragment {
    String ChatheadId, fullname, otherUserId;
    ChatImageFragmentBinding chatImageFragmentBinding;

    public static ChatImageFragment newInstance() {
        return new ChatImageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // return inflater.inflate(R.layout.chat_image_fragment, container, false);
        chatImageFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.chat_image_fragment, container, false);
        chatImageFragmentBinding.setLifecycleOwner(this);
        ChatheadId = getArguments().getString("chatHeadId");
        otherUserId = getArguments().getString("otherUserId");
        fullname = getArguments().getString("fullname");

//        ChatImageDataAdapter chatImageDataAdapter= new ChatImageDataAdapter(MediaDocumentList,this);
        return chatImageFragmentBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatImageFragmentBinding.loading.setAnimation("loading.json");
        chatImageFragmentBinding.loading.playAnimation();
        chatImageFragmentBinding.loading.loop(true);
        getMedia();
    }

    private void getMedia() {
        chatImageFragmentBinding.loading.setVisibility(View.VISIBLE);
        Map<String, String> header = new HashMap<>();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
        HashMap body = new HashMap<>();
        body.put("chatHeadId", ChatheadId);
        body.put("toUserId", otherUserId);
        body.put("attachmentType", new String[]{"Image", "Video"});
        Call<GetDocsMediaChatsResponse> call = webApi.getDocsMedia(header, body);
        call.enqueue(new Callback<GetDocsMediaChatsResponse>() {
            @Override
            public void onResponse(Call<GetDocsMediaChatsResponse> call, Response<GetDocsMediaChatsResponse> response) {
                chatImageFragmentBinding.loading.setVisibility(View.GONE);
                if (response.code() == 200) {
                    Log.e("TAg", "getmmediadocument=" + response.body());
                    try {
                        if (response.body().getData() != null) {
                            if (response.body().getData().getAll().size() > 0) {
                                Log.e("TAg", "respcondit" + response.body().getData().getAll());
//                            documentMonthdataAdapter = new DoctumentAdapter(response.body().getData().getLastWeek(), getActivity());
//                            RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 4);
//                            recycler_document.setLayoutManager(manager);
//                            recycler_document.setItemAnimator(new DefaultItemAnimator());
//                            recycler_document.setAdapter(documentMonthdataAdapter);

//                            documentMonthdataAdapter1 = new DoctumentAdapter(response.body().getData().getLastMonth(), getActivity());
//                            RecyclerView.LayoutManager manager1 = new GridLayoutManager(getActivity(), 4);
//                            recycler_month.setLayoutManager(manager1);
//                            recycler_month.setItemAnimator(new DefaultItemAnimator());
//                            recycler_month.setAdapter(documentMonthdataAdapter1);
//
//                            documentMonthdataAdapter2 = new DoctumentAdapter(response.body().getData().getAll(), getActivity());
//                            RecyclerView.LayoutManager manager2 = new GridLayoutManager(getActivity(), 4);
//                            recycler_all.setLayoutManager(manager2);
//
//                            recycler_all.setItemAnimator(new DefaultItemAnimator());
//                            recycler_all.setAdapter(documentMonthdataAdapter2);
                                HashMap<Integer, ArrayList<GetDocsMediaChatsResponse.Data.LastWeek>> hashMap1 = new HashMap<>();
                                HashMap<String, ArrayList<GetDocsMediaChatsResponse.Data.LastWeek>> hashMap = new HashMap<>();

                                hashMap.put("Lastweek", response.body().getData().getLastWeek());
                                hashMap.put("LastMonth", response.body().getData().getLastMonth());

                                ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> all = response.body().getData().getAll();

                                for (int i = 0; i < all.size(); i++) {
                                    GetDocsMediaChatsResponse.Data.LastWeek data = all.get(i);

//                                    DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy'T'HH:mm:ss.SSSXXX");
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                                    Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
                                    try {
                                        date = dateFormat.parse(data.getCreatedAt());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    DateFormat formatter = new SimpleDateFormat("MMMM"); //If you need time just put specific format for time like 'HH:mm:ss'
                                    String dateStr = formatter.format(date);
//                                    Log.e("TAg", "merimasti=" + dateStr);

                                    DateFormat fo = new SimpleDateFormat("MM");
                                    int monthNum = Integer.parseInt(fo.format(date));
                                    if (!hashMap1.containsKey(monthNum)) {
                                        ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> allData = new ArrayList<>();
                                        allData.add(data);
                                        hashMap1.put(monthNum, allData);
                                    } else {
                                        hashMap1.get(monthNum).add(data);
                                    }

                                    if (!hashMap.containsKey(dateStr)) {
                                        ArrayList<GetDocsMediaChatsResponse.Data.LastWeek> allData = new ArrayList<>();
                                        allData.add(data);
                                        hashMap.put(dateStr, allData);
                                    } else {
                                        hashMap.get(dateStr).add(data);
                                    }
                                }
                                Log.e("TAg ", "hashData" + hashMap1.size());

                                Map<Integer, ArrayList<GetDocsMediaChatsResponse.Data.LastWeek>> m1 = new TreeMap(hashMap1).descendingMap();
                                Log.e("TAg ", "hashData" + m1.size());
                                if (response.body().getData().getLastWeek().size() > 0) {
                                    m1.put(14, response.body().getData().getLastWeek());
                                }
                                if (response.body().getData().getLastMonth().size() > 0) {
                                    m1.put(13, response.body().getData().getLastMonth());
                                }

                                for (Integer i : m1.keySet()) {
                                    Log.e("TAg ", "hashData" + i);
                                }
                                Set set = m1.keySet();

                                if (set.size() > 3) {
                                    Iterator iterator = set.iterator();
                                    int i = 0;
                                    while (iterator.hasNext()) {
                                        iterator.next();
                                        i++;
                                        if (i > 2) {
                                            m1.remove(iterator.next());
                                        }
                                    }
                                }
//                              MediaTestAdapter mediaChatImageAdapter = new MediaTestAdapter(hashMap, getActivity());
                                chatImageFragmentBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//                                chatImageFragmentBinding.recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

                                ChatImageDataAdapter mediaChatImageAdapter = new ChatImageDataAdapter(m1, getContext(), fullname);
                                chatImageFragmentBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

                                chatImageFragmentBinding.recyclerview.setItemAnimator(new DefaultItemAnimator());
                                chatImageFragmentBinding.recyclerview.setAdapter(mediaChatImageAdapter);

                                mediaChatImageAdapter.setOnItemClickListener(new ChatImageDataAdapter.OnItemClickListener() {
                                    @SuppressLint("WrongConstant")
                                    @Override
                                    public void onItemClick(GetDocsMediaChatsResponse.Data.LastWeek data) {
//                                        Intent intent = new Intent(getContext(), ChatImageViewActivity.class);
//                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
//                                        Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
//                                        try {
//                                            date = dateFormat.parse(data.getCreatedAt());
//                                        } catch (ParseException e) {
//                                            e.printStackTrace();
//                                        }
//                                        DateFormat formatter = new SimpleDateFormat("dd/MMMM/yyyy, hh:mm"); //If you need time just put specific format for time like 'HH:mm:ss'
//                                        String dateStr = formatter.format(date);

//                                        intent.putExtra("title", user_id_name);
//                                        intent.putExtra("iv_url", data.getFileURL());
//                                        intent.putExtra("type", data.getAttachmentType());
//                                        intent.putExtra("dateAndTime", dateStr);
//                                        startActivity(intent);
                                    }
                                });
                            } else {

                                chatImageFragmentBinding.rlMain.setVisibility(View.GONE);
                                chatImageFragmentBinding.emptyview.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            chatImageFragmentBinding.rlMain.setVisibility(View.GONE);
                            chatImageFragmentBinding.emptyview.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("err", "err::" + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetDocsMediaChatsResponse> call, Throwable t) {
                chatImageFragmentBinding.loading.setVisibility(View.GONE);
                Log.w("error", t.toString());

            }
        });
    }

}