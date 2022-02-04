package com.meest.videomvvmmodule.view.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.meest.R;
import com.meest.databinding.ActivityVideoCommentReplyBinding;
import com.meest.videomvvmmodule.model.comment.VideoCommentResponse;
import com.meest.videomvvmmodule.utils.CommonUtils;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;
import com.meest.videomvvmmodule.viewmodel.VideoReplyCommentViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;
import com.google.gson.Gson;

public class VideoCommentReplyActivity extends AppCompatActivity {
    ActivityVideoCommentReplyBinding binding;
    VideoReplyCommentViewModel viewModel;
    CustomDialogBuilder customDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_comment_reply);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new VideoReplyCommentViewModel()).createFor()).get(VideoReplyCommentViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(VideoCommentReplyActivity.this);
        initView();
        initListeners();
        initObserve();
        binding.setViewmodel(viewModel);
    }

    private void initObserve() {
        viewModel.onLoadMoreComplete.observe(this, onLoadMore -> {
            binding.refreshlout.finishLoadMore();
            if (onLoadMore != null && !onLoadMore) {
                binding.etComment.setText("");
                CommonUtils.hideSoftKeyboard(this);
            }
        });
    }

    private void initView() {
        if (getIntent().getExtras() != null) {
            String data = getIntent().getExtras().getString("commentData");
            viewModel.commentData = new Gson().fromJson(data, VideoCommentResponse.Data.Row.class);
            viewModel.isSvs = getIntent().getExtras().getBoolean("svs", true);
            viewModel.commentCount.set(getIntent().getIntExtra("commentCount",0));
//            isAd = getIntent().getBooleanExtra("isAd", false);
        }
        binding.refreshlout.setEnableRefresh(false);
        viewModel.setData(binding,this);
//        viewModel.fetchSubComments(false);
        binding.imgClose.setOnClickListener(v -> onBackPressed());
//        binding.txtLike.setOnClickListener(v ->  viewModel.callApiToLikeComment(binding,VideoCommentReplyActivity.this));
//        binding.txtReport.setOnClickListener(v -> customDialogBuilder.showVideoCommentReportDialog(viewModel.commentData.getId()));
//        binding.txtDelete.setOnClickListener(v -> customDialogBuilder.showVideoCommentReportDialog(viewModel.callApitoDeleteComment()));
    }

    private void initListeners() {
        viewModel.adapter.setOnRecyclerViewItemClick((comment, position, type, binding) -> {
            switch (type) {
                // On delete click
                case 1:
//                    viewModel.callApitoDeleteComment(comment.getId(), this,position);
                    break;
                // On user Profile Click
                case 2:
                    Intent intent = new Intent(this, FetchUserActivity.class);
                    intent.putExtra("userid", comment.getUserId());
                    startActivity(intent);
                    break;
                case 3:
//                    customDialogBuilder.showVideoCommentReportDialog(comment.getId());
                    break;
                default:
                    break;
            }
        });

        binding.imgClose.setOnClickListener(v -> finish());

        binding.imgSend.setOnClickListener(v -> {
//            if (Global.accessToken.isEmpty()) {
//                if (getActivity() instanceof MainActivity) {
//                    ((MainActivity) getActivity()).initLogin(getActivity(), () -> viewModel.addComment());
//                }
//                closeKeyboard();
//            } else {
//                viewModel.addComment();
//            }
            viewModel.addComment(this);
        });

//        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> viewModel.onLoadMore());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isBack", true);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}