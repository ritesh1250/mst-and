package com.meest.social.socialViewModel.view.comment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.meest.Interfaces.VideoCommentActionCallbackNew;
import com.meest.R;
import com.meest.databinding.VideoCommentActivityBinding;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.EmojIcon;
import com.meest.social.socialViewModel.adapter.comment.VideoCommentAdapter;
import com.meest.social.socialViewModel.viewModel.comment.VideoCommentViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;

public class VideoCommentActivityNew extends AppCompatActivity implements VideoCommentActionCallbackNew {


    boolean isSvs = true, isCommentAllowed = true, isAd = false;

    String postMessage;

    public static final int REPLY_CODE = 10;

    VideoCommentViewModel viewModel;
    VideoCommentActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.video_comment_activity);

        viewModel = new ViewModelProvider(this, new ViewModelFactory(new VideoCommentViewModel()).createFor()).get(VideoCommentViewModel.class);
        binding.setViewModel(viewModel);


        initObserver();

        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
            finish();
        }
        getData();
        if (!isSvs && postMessage.length() > 0) {
            String data = "";
            try {
                data = URLDecoder.decode(postMessage, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            binding.txtNewComment.setVisibility(View.VISIBLE);
            binding.txtNewComment.setText(data);
        }
        binding.imgBack.setOnClickListener(view -> onBackPressed());
        binding.loading.setAnimation("loading.json");
        binding.loading.playAnimation();
        binding.loading.loop(true);

        if (!isCommentAllowed) {
            binding.layoutBottomComment.setVisibility(View.GONE);
            binding.txtNewComment.setVisibility(View.VISIBLE);
            binding.txtNewComment.setText(getString(R.string.Comments_are_not_allowed));
        }

        ImageView emojiButton = findViewById(R.id.emoji_btn);
        EmojIcon emojiIcon;
        emojiIcon = new EmojIcon(this, binding.rootView,binding.messageInput,emojiButton);
        emojiIcon.ShowEmojIcon();
        emojiIcon.setIconsIds(R.drawable.ic_smiley_icons, R.drawable.ic_smiley_icons);
        emojiIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {

            }

            @Override
            public void onKeyboardClose() {

            }
        });


        binding.imgSent.setOnClickListener(view -> {
            try {
                String commentText = binding.messageInput.getText().toString().trim();
                if (commentText.length() == 0) {
                  //  Utilss.showToast(getApplicationContext(), getString(R.string.enter_message), R.color.msg_fail);
                    binding.messageInput.setError(Html.fromHtml("<font color='social_background_blue'>Enter Message</font>"));
                    binding.messageInput.requestFocus();
                    return;
                }

                String encodedComment = "";

                try {
                    encodedComment = URLEncoder.encode(commentText, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                binding.loading.setVisibility(View.VISIBLE);
                if (isSvs) {
                    viewModel.addVideoComment(VideoCommentActivityNew.this,encodedComment);
                } else {
                    if (isAd) {
                        viewModel.addAdPostComment(VideoCommentActivityNew.this,encodedComment);
                    } else {
                        viewModel.addPostComment(VideoCommentActivityNew.this,encodedComment);
                    }
                }
            } catch (Exception e) {
                Utilss.showToast(VideoCommentActivityNew.this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
                e.printStackTrace();
            }
        });

        if (isSvs) {
            viewModel.fetchVideoComments(this);
        } else {
            if (isAd) {
                viewModel.fetchAdComments(this);
            } else {
                viewModel.fetchPostComments(this);
            }
        }
    }

    private void getData()
    {
        viewModel.videoId = getIntent().getExtras().getString("videoId");
        postMessage = getIntent().getExtras().getString("msg", "");
        isSvs = getIntent().getExtras().getBoolean("svs", true);
        viewModel.position = getIntent().getExtras().getInt("position", 0);
        isCommentAllowed = getIntent().getExtras().getBoolean("isCommentAllowed", true);
        viewModel.commentCount = getIntent().getExtras().getInt("commentCount", 0);
        isAd = getIntent().getBooleanExtra("isAd", false);
    }

    private void setAdapter() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(VideoCommentActivityNew.this, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        viewModel.commentAdapter = new VideoCommentAdapter(VideoCommentActivityNew.this, viewModel.videoCommentResponse, isSvs, VideoCommentActivityNew.this, isAd);
        binding.recyclerView.setAdapter(viewModel.commentAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("commentCount", viewModel.totalCount);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        intent.putExtra("commentCount", viewModel.totalCount);
        setResult(RESULT_OK, intent);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (isSvs) {
            viewModel.fetchVideoComments(this);
        } else {
            if (isAd) {
                viewModel.fetchAdComments(this);
            } else {
                viewModel.fetchPostComments(this);
            }
        }
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REPLY_CODE && data != null) {
            boolean isChanged = data.getExtras().getBoolean("isChanged", false);
            if (isChanged) {
                if (isAd) {
                    viewModel.fetchAdComments(this);
                } else {
                    viewModel.fetchPostComments(this);
                }
            }
        }
    }

    @Override
    public void replyClicked(com.meest.social.socialViewModel.model.comment.VideoCommentResponse.Row model) {
        Intent intent = new Intent(this, VideoCommentReplyActivityNew.class);
        intent.putExtra("commentData", new Gson().toJson(model));
        intent.putExtra("commentCount", viewModel.commentCount);
        intent.putExtra("svs", isSvs);
        intent.putExtra("isAd", isAd);
        startActivityForResult(intent, REPLY_CODE);
    }

    @Override
    public void changedComment() {
        // updating comments
        if (isSvs) {
            viewModel.fetchVideoComments(this);
        } else {
            if (isAd) {
                viewModel.fetchAdComments(this);
            } else {
                viewModel.fetchPostComments(this);
            }
        }
    }

    public void initObserver()
    {
        viewModel.setBlank.observe(this, aBoolean -> {
            if (aBoolean)
                binding.messageInput.setText("");
        });
        viewModel.loader.observe(this, aBoolean -> {
            if (aBoolean)
                binding.loading.setVisibility(View.VISIBLE);
            else
                binding.loading.setVisibility(View.GONE);
        });

        viewModel.setData.observe(this, aBoolean -> {
            if (aBoolean)
                setAdapter();
        });
    }
}