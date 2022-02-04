package com.meest.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.meest.Adapters.InterestChooseAdapter;
import com.meest.Adapters.UserInterestAdapter;
import com.meest.Paramaters.UpdateTopicsParams;
import com.meest.R;
import com.meest.responses.UserSelectedTopics;
import com.meest.responses.UserUpdatedSelectedTopics2;
import com.meest.meestbhart.register.fragment.choosetopic.model.ChooseTopicResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicParam;
import com.meest.meestbhart.register.fragment.choosetopic.model.TopicsResponse;
import com.meest.meestbhart.register.fragment.choosetopic.model.UpdateSelectedTopicsResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class InterestBottomSheetFragment extends BottomSheetCommentFragment {
    String TAG = "InterestBottomSheetFragment";
    RecyclerView selected_interests_recycler, all_interests_recycler;
    Button close, updateBtn;
    //    ChooseAdapter adapter;
    InterestChooseAdapter interestChooseAdapter;
    UserInterestAdapter userInterestAdapter;
    LottieAnimationView loading, loading1;
    ProgressDialog dialog;
    Dialog dialog1;
    TextView unselected_size;
    private Context mContext;
    List<String> mData1 = new ArrayList<>();
    List<String> rowList1 = new ArrayList<>();
    List<String> selectedIds1 = new ArrayList<>();
    List<TopicsResponse.Row> allData = new ArrayList<>();

//    InterestChooseAdapter interestChooseAdapter;
//    private List<TopicsResponse.Row> mData;
    //    private List<TopicsResponse.Row> newData = new ArrayList<>();
//    public List<UserSelectedTopics.Row> rowList;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Initialize everything
        View v = inflater.inflate(R.layout.interest_bottom_sheet_fragment, container, false);
        selected_interests_recycler = v.findViewById(R.id.selected_interests_recycler);
        all_interests_recycler = v.findViewById(R.id.all_interests_recycler);
        loading = v.findViewById(R.id.loading);
        loading1 = v.findViewById(R.id.loading1);
        close = v.findViewById(R.id.close);
        Constant.mData = new ArrayList<>();
        Constant.rowList = new ArrayList<>();

        loading1.setVisibility(View.VISIBLE);


        unselected_size = v.findViewById(R.id.unselected_size);
        dialog = new ProgressDialog(mContext);
        dialog1 = new Dialog(getActivity());
        dialog1.setContentView(R.layout.dailog_loader);
        dialog1.setTitle("");

//        onClicks
        close.setOnClickListener(v1 -> {

            loading.setVisibility(View.VISIBLE);

            for (int i = 0; i < rowList1.size(); i++) {
                for (int j = 0; j < allData.size(); j++) {
                    if (rowList1.get(i).equalsIgnoreCase(allData.get(j).getTopic())) {
                        selectedIds1.add(allData.get(j).getId());
                    }
                }
            }

            updateApi(selectedIds1);
//
        });

//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
//        setContentView(R.layout.activity_interest);
//        interest_btn = findViewById(R.id.interest_btn);
//        all_interests_recycler = (RecyclerView) findViewById(R.id.recyclerView);
        getAllTheActivities();
        System.out.println("mData Outside Func " + Constant.mData.size());
        System.out.println("rowList Outside Func " + Constant.rowList.size());
//        System.out.println("First list length"+mData.size());
//        System.out.println("Second list length"+rowList.size());

//        for(int i = 0 ; i <= rowList.size() ; i++){
//            for(int j=0 ; j<= mData.size(); j++){
//                if(mData.get(j).equals(rowList.get(i)))
//                    mData.remove(j);
//            }
//        }


//        Log.e(TAG, "onCreateView: before remove\n"+ mData);
//        System.out.println(mData);
//        mData.removeAll(rowList);
//        System.out.println(mData);


        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setChildGravity(Gravity.CENTER)
                .setScrollingEnabled(true)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.CENTER;
                    }
                })
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build();


        userInterestAdapter = new UserInterestAdapter(mContext, rowList1, new UserInterestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<String> mData, UserInterestAdapter.ViewHolder holder) {

//                dialog.setMessage("Please wait.");
//                dialog.show();
//                loading1.setVisibility(View.VISIBLE);

////                int newPosition = position;
////                mData.remove(newPosition);
//                int newPosition = position;
//                Constant.rowList.remove(newPosition);
                mData1.add(rowList1.get(position));
                interestChooseAdapter.notifyDataSetChanged();
                rowList1.remove(position);
                userInterestAdapter.notifyItemRemoved(position);
                userInterestAdapter.notifyDataSetChanged();


                /* Constant.selectedTopicIDs.clear();
                for (int j = 0; j < Constant.rowList.size(); j++) {
                    Constant.selectedTopicIDs.add(Constant.rowList.get(j).getTopic().getId());
                }

                Log.e(TAG, "ITEM CLICK ON THE TOP ROW INDEX = " + position);
                Log.e("TAG", "selectedTopicIDs list size: \n" + Constant.selectedTopicIDs.size());
                updateApi(Constant.selectedTopicIDs);*/


//                String temp = Constant.allTopics.get(newPosition);
//                Constant.allTopics.remove(newPosition);
//                Constant.selectedTopics.add(temp);

            }
        });

        interestChooseAdapter = new InterestChooseAdapter(mContext, mData1, new InterestChooseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, List<String> mData, InterestChooseAdapter.ViewHolder holder) {
//                dialog.setMessage("Please wait.");
//                dialog.show();
                int newPosition = position;
//                insertApi(Constant.mData.get(newPosition).getId());
                rowList1.add(mData1.get(position));
                userInterestAdapter.notifyDataSetChanged();
                mData1.remove(position);
//                interestChooseAdapter.notifyItemRemoved(newPosition);
                interestChooseAdapter.notifyDataSetChanged();
//                unselected_size.setText("Size:" + Constant.mData.size());
//                interestChooseAdapter.notifyItemRangeChanged(newPosition, Constant.unselectedInterests.size());

            }
        });

//        userInterestAdapter.notifyDataSetChanged();
//        interestChooseAdapter.notifyDataSetChanged();

        all_interests_recycler.setLayoutManager(chipsLayoutManager);
        all_interests_recycler.setAdapter(interestChooseAdapter);

        selected_interests_recycler.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true));
        selected_interests_recycler.setAdapter(userInterestAdapter);

        return v;
    }

    private void updateApi(List<String> ids) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(mContext, SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        UpdateTopicsParams updateProfileParam = new UpdateTopicsParams();
        updateProfileParam.setUpdatedTopic(ids);
        Call<UserUpdatedSelectedTopics2> call = webApi.getUpdatedSelectedTopics(map, updateProfileParam);

        call.enqueue(new Callback<UserUpdatedSelectedTopics2>() {
            @Override
            public void onResponse(Call<UserUpdatedSelectedTopics2> call, Response<UserUpdatedSelectedTopics2> response) {
                Log.e("UserInterestAdapter", "onResponse: " + "API run");
//                Utilss.showToast(mContext, mContext.getString(R.string.saved), R.color.grey);
//                Toast.makeText(mContext, response.body().getSuccess().toString(), Toast.LENGTH_SHORT).show(getActivity(), "","Loading. Please wait...", true);

//                if (Constant.rowList.size() > 0) {
//                    Log.e(TAG, "onItemClick: " + "REMOVE SELECTED RUNNING");
//                    removeSelected();
//                }
//                userInterestAdapter.notifyItemRemoved(pos);
//                userInterestAdapter.notifyDataSetChanged();
//                getAllTheActivities();
//                interestChooseAdapter.notifyDataSetChanged();
//                dialog.dismiss();
                loading.setVisibility(View.GONE);
                dismiss();
            }

            @Override
            public void onFailure(Call<UserUpdatedSelectedTopics2> call, Throwable t) {
                Log.e("UserInterestAdapter", "onFailure: " + "API Didn't Run");
                Utilss.showToast(mContext, mContext.getString(R.string.error_msg), R.color.grey);
//                loading1.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                dismiss();

//                dialog.dismiss();
            }
        });
    }

    private void removeSelected() {

//        for (int i = 0; i < Constant.rowList.size(); i++) {
//            for (int j = 0; j < Constant.mData.size(); j++) {
//                if (Constant.mData.get(j).getTopic().equals(Constant.rowList.get(i).getTopic().get_topic()))
//                    Constant.mData.remove(j);
//            }
//        }

        Timber.e("removeSelected: CAME INTO THE FOR LOOP");

        for (int k = 0; k < rowList1.size(); k++) {
            for (int l = 0; l < mData1.size(); l++) {
                if (mData1.get(l).equalsIgnoreCase(rowList1.get(k))) {
                    mData1.remove(l);
                }
            }
        }

        interestChooseAdapter.notifyDataSetChanged();

        Log.e(TAG, "saveAdapter: " + Constant.rowList.size());
        Log.e(TAG, "saveAdapter: " + Constant.mData.size());

    }

    private void getAllTheActivities() {
        final WebApi webApi = ApiUtils.getClientHeader(mContext);
        //WebApi webApi = ApiUtils.getClient().create(WebApi.class);
//        customDialogBuilder.showLoadingDialog();
        loading.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(mContext, SharedPrefreances.AUTH_TOKEN));
//        Log.e("TAG", "onCreateView:"+ SharedPrefreances.getSharedPreferenceString(mContext, SharedPrefreances.AUTH_TOKEN));

        TopicParam topicParam = new TopicParam("100", "1");
        Call<TopicsResponse> call = webApi.getAll_topic(map, topicParam);
        call.enqueue(new Callback<TopicsResponse>() {
            @Override
            public void onResponse(Call<TopicsResponse> call, Response<TopicsResponse> response) {
                try {
                    if (response.body().getCode() == 1) {
//                        customDialogBuilder.hideLoadingDialog();
                        loading.setVisibility(View.GONE);
//                        for (int i = 0; i < response.body().getData().getRows().size(); i++) {
//                            mData.add(response.body().getData().getRows().get(i));
//                        }
                        allData.clear();
                        allData.addAll(response.body().getData().getRows());

                        /*******************************************/
                        mData1.clear();
                        for (int i = 0; i < response.body().getData().getRows().size(); i++) {
                            mData1.add(response.body().getData().getRows().get(i).getTopic());
                        }
                        /*******************************************/

//                        System.out.println("mData Size Inside Func " + mData.size());
//                        System.out.println("mData length inside the func "+ mData.size());

//                        for (int i = 0; i <= rowList.size(); i++) {
//                            for (int j = 0; j <= mData.size(); j++) {
//                                if (mData.get(j).equals(rowList.get(i)))
//                                    mData.remove(j);
//                            }
//                        }
//                        mData.removeAll(rowList);\
                        getSelectedTopics("getAllTheActivities");

                        userInterestAdapter.notifyDataSetChanged();
                        interestChooseAdapter.notifyDataSetChanged();

                        loading.setVisibility(View.GONE);
                        dialog.dismiss();
                    } else {
                        Utilss.showToast(mContext, getString(R.string.error_msg), R.color.grey);
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TopicsResponse> call, Throwable t) {
                Log.w("t", t);
                loading1.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });
    }



    private void getSelectedTopics(String pos) {
        final WebApi webApi = ApiUtils.getClientHeader(mContext);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(mContext, SharedPrefreances.AUTH_TOKEN));
        Call<UserSelectedTopics> call = webApi.getSelectedTopics(map);

        call.enqueue(new Callback<UserSelectedTopics>() {
            @Override
            public void onResponse(Call<UserSelectedTopics> call, Response<UserSelectedTopics> response) {
                try {
                    if (response.body().getCode() == 1) {
                        loading.setVisibility(View.GONE);
//                        if(!pos.equalsIgnoreCase("insertApi")){
//                            Constant.rowList.clear();
//                        }
//                        Constant.rowList.addAll(response.body().getData().getRows());

                        /**********************************************/
                        rowList1.clear();
                        for (int j = 0; j < response.body().getData().getRows().size(); j++) {
                            rowList1.add(response.body().getData().getRows().get(j).getTopic().get_topic());
                        }

                        System.out.println("rowList size inside func " + Constant.rowList.size());
                        userInterestAdapter.notifyDataSetChanged();
                        loading.setVisibility(View.GONE);
//                        if (Constant.rowList.size() > 0) {
//                            removeSelected();
//                        }
                        if (rowList1.size() > 0) {
                            removeSelected();
                        }
                        loading1.setVisibility(View.GONE);

                    } else {
                        Utilss.showToast(mContext, getString(R.string.error_msg), R.color.grey);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserSelectedTopics> call, Throwable t) {
                Log.w("t", t);
                loading1.setVisibility(View.GONE);

            }
        });
    }

    private void insertApi(String newTopicID) {
        Constant.array_list_2.clear();
        Constant.array_list_2.add(newTopicID);

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(mContext, SharedPrefreances.AUTH_TOKEN));
        ChooseTopicResponse updateProfileParam = new ChooseTopicResponse(Constant.array_list_2);

        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
        Call<UpdateSelectedTopicsResponse> call = webApi1.userTopics_insert(map, updateProfileParam);

        call.enqueue(new Callback<UpdateSelectedTopicsResponse>() {
            @Override
            public void onResponse(@NotNull Call<UpdateSelectedTopicsResponse> call, @NotNull Response<UpdateSelectedTopicsResponse> response) {
                Log.e("InterestChooseAdapter", "onResponse: " + "API run");
//                Constant.selectedInterests.clear();
//                Constant.selectedInterests.addAll(response.body().getData());
                getSelectedTopics("insetApi");
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<UpdateSelectedTopicsResponse> call, Throwable t) {
                Utilss.showToast(mContext, mContext.getString(R.string.error_msg), R.color.grey);
                dialog.dismiss();
            }
        });
    }

//    @Override
//    public void onDismiss(@NonNull DialogInterface dialog) {
//
//        for (int i = 0; i < rowList1.size(); i++) {
//            for (int j = 0; j < allData.size(); j++) {
//                if (rowList1.get(i).equalsIgnoreCase(allData.get(j).getTopic())) {
//                    selectedIds1.add(allData.get(j).getId());
//                }
//            }
//        }
//
//        updateApi(selectedIds1);
//
//        super.onDismiss(dialog);
//    }
}
