package com.meest.videomvvmmodule.view.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.ActivityHashtagBinding;
import com.meest.videomvvmmodule.adapter.VideoListAdapter;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.video.PlayerActivity;
import com.meest.videomvvmmodule.viewmodel.HashTagViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class HashTagActivity extends BaseActivity {

    ActivityHashtagBinding binding;
    HashTagViewModel viewModel;
    int color;

    ActivityResultLauncher<Intent> someActivityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hashtag);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new HashTagViewModel()).createFor()).get(HashTagViewModel.class);
        initView();
        initObserve();
        initListeners();

        binding.setViewmodel(viewModel);

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null && data.getStringExtra("videoDeleted") != null) {
                            if (viewModel.adapter.getmList().removeIf(vid -> vid.getPostId().equals(data.getStringExtra("videoDeleted")))) {
                                Video v = viewModel.video.getValue();
                                v.setPost_count(v.getPost_count() - 1);
                                viewModel.video.setValue(v);
                            }
                        }

                        viewModel.adapter.notifyDataSetChanged();
                    }
                });

    }

    private void initListeners() {
        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> viewModel.onLoadMore());
        binding.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void initView() {
        if (getIntent().getStringExtra("hashtag") != null) {
            viewModel.hashtag = getIntent().getStringExtra("hashtag");
//            binding.tvVideoCount.setText(Global.prettyCount(viewModel.explore.getHashTagVideosCountl()) + " Videos");
            color = getIntent().getIntExtra("colorPosition", 0);
        }
        if (color == 0) {
            color = getRandomNumber(0, 9999999);
        }
        binding.refreshlout.setEnableRefresh(false);
//        Log.e("======colorCode", " " + color);
//        binding.hashTagCard.setBackgroundColor(color);
        viewModel.adapter = new VideoListAdapter(new VideoListAdapter.OnRecyclerViewItemClick() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(HashTagActivity.this, PlayerActivity.class);
//                intent.putExtra("video_list", new Gson().toJson(mList));

                List<Video.Data> singleList = new ArrayList<>();
                singleList.add(viewModel.adapter.getmList().get(position));
                intent.putExtra("video_list", new Gson().toJson(singleList));

                intent.putExtra("position", position);
//                if (true) {
//                    intent.putExtra("type", 2);
//                    intent.putExtra("hash_tag", word);
//                } else {
//                    intent.putExtra("type", 3);
//                    intent.putExtra("keyword", word);
//                }

                intent.putExtra("type", 2);
                intent.putExtra("hash_tag", viewModel.hashtag);
                // HashTagActivity.this.startActivity(intent);

                someActivityResultLauncher.launch(intent);
            }
        });
//        viewModel.adapter.setHashTag(true);
//        viewModel.adapter.setWord(viewModel.hashtag);
        viewModel.fetchHashTagVideos(false);
    }

    public int getRandomNumber(int min, int max) {

        return (int) ((Math.random() * (min - max)) + min);
    }

    private void initObserve() {
        CustomDialogBuilder customDialogBuilder = new CustomDialogBuilder(this);
        viewModel.isloading.observe(this, isLoading -> {
            if (isLoading) {
                customDialogBuilder.showLoadingDialog();
            } else {
                customDialogBuilder.hideLoadingDialog();
            }
        });
        viewModel.onLoadMoreComplete.observe(this, onLoadMore -> binding.refreshlout.finishLoadMore());

        viewModel.video.observe(this, video ->
                {
                    if (video.getPost_count() == 1) {
                        binding.tvVideoCount.setText(Global.prettyCount(video.getPost_count()).concat(" " + getString(R.string.video_only)));
                    } else {
                        binding.tvVideoCount.setText(Global.prettyCount(video.getPost_count()).concat(" " + getString(R.string.video)));
                    }
                }
        );
    }
}