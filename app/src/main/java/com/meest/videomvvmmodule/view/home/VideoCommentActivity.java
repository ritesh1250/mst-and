package com.meest.videomvvmmodule.view.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import com.meest.R;
import com.meest.databinding.FragmentCommentSheetBinding;
import com.meest.databinding.ItemCommentListBinding;
import com.meest.videomvvmmodule.model.comment.Comment;
import com.meest.videomvvmmodule.utils.CommonUtils;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;
import com.meest.videomvvmmodule.viewmodel.CommentSheetViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

public class VideoCommentActivity extends AppCompatActivity {
    FragmentCommentSheetBinding binding;
    CommentSheetViewModel viewModel;
    CustomDialogBuilder customDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_comment_sheet);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory(new CommentSheetViewModel()).createFor()).get(CommentSheetViewModel.class);
        customDialogBuilder = new CustomDialogBuilder(VideoCommentActivity.this);
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

    private void initListeners() {
        viewModel.adapter.setOnRecyclerViewItemClick((Comment.Data comment, int position, int type, ItemCommentListBinding binding)
                -> {
            switch (type) {
                // On delete click
                case 1:
                    viewModel.callApitoDeleteComment(comment.getCommentsId(), position, comment);
                    break;
                // On userProfile/userName Click
                case 2:
                    Intent intent = new Intent(this, FetchUserActivity.class);
                    intent.putExtra("userId", comment.getUserId());
                    startActivity(intent);
                    break;
//                    video comment reply
                case 3:
                    /*Intent intent1 = new Intent(this, VideoCommentReplyActivity.class);
                    intent1.putExtra("commentData", new Gson().toJson(comment));
                    intent1.putExtra("commentCount", comment.getVideoSubComments().size());
                    intent1.putExtra("svs", true);
                    startActivityForResult(intent1, 101);*/
                    break;
//                    like click
                case 4:
//                    viewModel.callApiToLikeComment(comment.getId(), comment.getVideoId(), binding, comment, this);
                    break;
                case 5:
//                    customDialogBuilder.showReportDialog(viewModel.title, new CustomDialogBuilder.OnReportDismissListener() {
//                        @Override
//                        public void onReportData(DialogReportCommentsBinding binding) {
//
//                        }
//                    });
//                    customDialogBuilder.showVideoCommentReportDialog(comment.getId());
                    break;
                default:
                    break;
            }
        });

        binding.imgClose.setOnClickListener(v -> onBackPressed());

        binding.imgSend.setOnClickListener(v -> {
//            if (Global.accessToken.isEmpty()) {
//                if (getActivity() instanceof MainActivity) {
//                    ((MainActivity) getActivity()).initLogin(getActivity(), () -> viewModel.addComment());
//                }
//                closeKeyboard();
//            } else {
//                viewModel.addComment();
//            }
//            viewModel.addComment(this);
        });

        binding.refreshlout.setOnLoadMoreListener(refreshLayout -> viewModel.onLoadMore());
    }

    private void initView() {
        if (getIntent().getStringExtra("videoId") != null) {
            viewModel.postId = getIntent().getStringExtra("videoId");
//            viewModel.msg = getIntent().getStringExtra("msg");
//            viewModel.isAllowComment = getIntent().getBooleanExtra("isAllowComment", true);
            viewModel.commentCount.set(getIntent().getIntExtra("commentCount", 0));
        }
        binding.refreshlout.setEnableRefresh(false);
        viewModel.fetchComments(false);
    }
}