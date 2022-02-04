package com.meest.Activities.StoryPackage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import com.meest.storyview.screen.StoryActivity;
import com.meest.Interfaces.FeedDataCallback;

import com.meest.Paramaters.UserStoryParameter;
import com.meest.R;
import com.meest.responses.UserFollowerStoryResponse;
import com.meest.event.CommonEvent;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.model.AdMediaData;

import org.greenrobot.eventbus.EventBus;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatStoryFragment extends Fragment implements FeedDataCallback {
    private View rootView;
    private RecyclerView mRecyclerViewStatus;
    private RecyclerView mRecyclerViewMuteList;
    private ChatStoryListAdapter adapter;

    private LottieAnimationView image;
    private UserFollowerStoryResponse userStoryListResponse;
    private UserFollowerStoryResponse userOwnStoryResponse;
    private LinearLayout addStory;
    private ImageView plusIcon,userProfile;
    private TextView tvTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat_story, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        mRecyclerViewStatus = rootView.findViewById(R.id.mRecyclerViewStatus);
        mRecyclerViewMuteList = rootView.findViewById(R.id.mRecyclerViewMuteList);
        image = rootView.findViewById(R.id.loading);
        addStory = rootView.findViewById(R.id.addStory);
        plusIcon = rootView.findViewById(R.id.plusIcon);
        userProfile = rootView.findViewById(R.id.userProfile);
        tvTime = rootView.findViewById(R.id.tvTime);
        plusIcon.bringToFront();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewStatus.setLayoutManager(layoutManager);
        mRecyclerViewMuteList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        //  setUpMuteDummyData();

        addStory.setOnClickListener(v -> {
          //  Utilss.showToast(getContext(),"", R.color.msg_fail);
        });

        addStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent;
                    if (userOwnStoryResponse.getData().getRows() != null && userOwnStoryResponse.getData().getRows().get(0).getCount() > 0) {
                        intent = new Intent(getActivity(), StoryActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("StoryResponse", (Serializable) userOwnStoryResponse);
                        intent.putExtra("BUNDLE", args);
                        intent.putExtra("iSUser", "yes");
                        intent.putExtra("position", 0);
                        startActivity(intent);
                    }else {
                        EventBus.getDefault().post(new CommonEvent());
                    }

                } catch (Exception e) {
                    EventBus.getDefault().post(new CommonEvent());
                    e.printStackTrace();
                }
            }
        });

       /* addStory.setOnLongClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateStoryActivity.class);
            startActivity(intent);
            return true;
        });*/
    }

    @Override
    public void onResume() {
        getUserStories();
        getOwnUserStories();
        super.onResume();
    }

    private void getOwnUserStories() {
        try {
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
            UserStoryParameter paramter = new UserStoryParameter(SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID));
            Call<UserFollowerStoryResponse> call = webApi.getOwnStories(header, paramter);
            image.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<UserFollowerStoryResponse>() {
                @Override
                public void onResponse(Call<UserFollowerStoryResponse> call, Response<UserFollowerStoryResponse> response) {
                    image.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        userOwnStoryResponse = new UserFollowerStoryResponse();
                        userOwnStoryResponse = response.body();
                        if (userOwnStoryResponse.getData()!=null && userOwnStoryResponse.getData().getRows().size()>0){
                            UserFollowerStoryResponse.Row row = userOwnStoryResponse.getData().getRows().get(0);
                            if(row.getAllStories()!=null && row.getAllStories().size()>0){
                                tvTime.setText(row.getAllStories().get(0).getCreatedAt());
                                if(row.getAllStories().get(0).getImage()!=null && row.getAllStories().get(0).getImage()==true)
                                    Glide.with(getActivity()).load(row.getAllStories().get(0).getStory()).placeholder(R.drawable.chat_user_placeholder).into(userProfile);
                                else
                                    Glide.with(getActivity()).load(row.getUser().getDisplayPicture()).placeholder(R.drawable.chat_user_placeholder).into(userProfile);
                            }else {
                                tvTime.setText(getString(R.string.tap_to_add_status_update));
                                Glide.with(getActivity()).load(row.getUser().getDisplayPicture()).placeholder(R.drawable.chat_user_placeholder).into(userProfile);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserFollowerStoryResponse> call, Throwable t) {
                    t.printStackTrace();
                    image.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getUserStories() {
        try {
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));
            HashMap<String, String> body = new HashMap<>();
            body.put("dislayType", "chat");
            Call<UserFollowerStoryResponse> call = webApi.getUserStories(header, body);
            image.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<UserFollowerStoryResponse>() {
                @Override
                public void onResponse(Call<UserFollowerStoryResponse> call, Response<UserFollowerStoryResponse> response) {
                    image.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        userStoryListResponse = new UserFollowerStoryResponse();
                        userStoryListResponse = response.body();
                        if (userStoryListResponse.getSuccess()) {
                            if (userStoryListResponse.getData() != null && userStoryListResponse.getData().getRows().size() > 0) {
                                adapter = new ChatStoryListAdapter(getContext(), userStoryListResponse.getData().getRows(), ChatStoryFragment.this);
                                mRecyclerViewStatus.setAdapter(adapter);
                                mRecyclerViewStatus.setNestedScrollingEnabled(false);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserFollowerStoryResponse> call, Throwable t) {
                    t.printStackTrace();
                    image.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     @Override
    public void commentClicked(int position, String id, String caption, boolean isCommentAllowed, Boolean isAD, int comments) {

    }

    @Override
    public void optionsClicked(int position) {

    }

    @Override
    public void storyClicked(int position) {
        if (position == -1) {
            addStory.performClick();
        } else if (position == -2) {
            addStory.performLongClick();
        } else {
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            Bundle args = new Bundle();
            args.putSerializable("StoryResponse", (Serializable) userStoryListResponse);
            intent.putExtra("BUNDLE", args);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }

    @Override
    public void myStoryClicked(int position) {


    }

    @Override
    public void storyAddClicked(int position) {

    }

    @Override
    public void onSignUpClicked(List<AdMediaData> mediaList, int position, String id) {

    }

}
