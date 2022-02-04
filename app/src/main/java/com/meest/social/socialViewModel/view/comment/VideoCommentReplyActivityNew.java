package com.meest.social.socialViewModel.view.comment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.meest.Interfaces.VideoCommentActionCallback;
import com.meest.R;
import com.meest.databinding.VideoCommentReplyActivityBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.responses.VideoCommentResponse;
import com.meest.social.EmojIcon;
import com.meest.social.socialViewModel.adapter.subcomment.FeedSubCommentAdapter;
import com.meest.social.socialViewModel.adapter.subcomment.VideoSubCommentAdapter;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.social.socialViewModel.viewModel.comment.VideoCommentReplyViewModel;
import com.meest.videomvvmmodule.viewmodelfactory.ViewModelFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;


public class VideoCommentReplyActivityNew extends AppCompatActivity implements VideoCommentActionCallback {

    int commentCount;

    VideoCommentReplyActivityBinding binding;
    VideoCommentReplyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.video_comment_reply_activity);
        viewModel = new ViewModelProvider(this, new ViewModelFactory(new VideoCommentReplyViewModel()).createFor()).get(VideoCommentReplyViewModel.class);
        binding.setViewModel(viewModel);
        getData();
        initObserver();

        viewModel.header = new HashMap<>();
        viewModel.header.put("x-token", SharedPrefreances.getSharedPreferenceString(
                VideoCommentReplyActivityNew.this, SharedPrefreances.AUTH_TOKEN));
        viewModel.header.put("Accept", "application/json");
        viewModel.header.put("Content-Type", "application/json");


        // biding parent comment data
        binding.txtUserName.setText(viewModel.commentData.getUser().getUsername());
        if (!viewModel.commentData.getUser().getDisplayPicture().isEmpty()) {
            Glide.with(this).load(viewModel.commentData.getUser().getDisplayPicture()).into(binding.imgProfile);
        }
        binding.txtTime.setText(viewModel.commentData.getCreatedAt());
        try {
            String test = URLDecoder.decode(viewModel.commentData.getComment(), "UTF-8");
            binding.txtComment.setText(test);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (viewModel.commentData.getLiked() == 1) {
            binding.txtLike.setImageDrawable(ContextCompat.getDrawable(VideoCommentReplyActivityNew.this, R.drawable.like_diamond_filled));
        } else {
            binding.txtLike.setImageDrawable(ContextCompat.getDrawable(VideoCommentReplyActivityNew.this, R.drawable.like_diamond));
        }

        if (viewModel.isSvs) {
            viewModel.fetchSvsComment(this);
        } else {
            if (viewModel.isAd) {
                viewModel.fetchAdFeedSubComment(this);
            } else {
                viewModel.fetchFeedSubComment(this);
            }
        }


        binding.layoutImages.setOnClickListener((View.OnClickListener) v -> {
//                Intent intent = new Intent(VideoCommentReplyActivity.this, OtherUserActivity.class);
            Intent intent = new Intent(VideoCommentReplyActivityNew.this, OtherUserAccount.class);
            intent.putExtra("userId", viewModel.commentData.getUser().getId());
            startActivity(intent);
        });
        binding.txtUserName.setOnClickListener(v -> {
            Intent intent = new Intent(VideoCommentReplyActivityNew.this, OtherUserAccount.class);
            intent.putExtra("userId", viewModel.commentData.getUser().getId());
            startActivity(intent);
        });

        binding.imgBack.setOnClickListener(view -> onBackPressed());

        binding.loading.setAnimation("loading.json");
        binding.loading.playAnimation();
        binding.loading.loop(true);

        ImageView emojiButton = findViewById(R.id.emoji_btn);
        EmojIcon emojiIcon;
        emojiIcon = new EmojIcon(this, binding.rootView, binding.messageInput, emojiButton);
        emojiIcon.ShowEmojIcon();
        emojiIcon.setIconsIds(R.drawable.ic_smiley_icons, R.drawable.ic_smiley_icons);
        emojiIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("harsh", "Keyboard opened!");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("harsh", "Keyboard closed");
            }
        });

        binding.imgSent.setOnClickListener(view -> {
            try {
                String commentText = binding.messageInput.getText().toString().trim();

                if (commentText.length() == 0) {
                    binding.messageInput.setError(Html.fromHtml("<font color='social_background_blue'>Enter Message</font>"));
                    binding.messageInput.requestFocus();
                  //  Utilss.showToast(getApplicationContext(), getString(R.string.enter_message), R.color.social_background_blue);
                    return;
                }

                String encodedComment = "";

                try {
                    encodedComment = URLEncoder.encode(commentText, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                binding.loading.setVisibility(View.VISIBLE);
                if (viewModel.isSvs) {
                    viewModel.sendSvsSubCommet(this,encodedComment);
                } else {
                    if (viewModel.isAd) {
                        viewModel.sendAdFeedSubCommet(this,encodedComment);
                    } else {
                        viewModel.sendFeedSubComment(VideoCommentReplyActivityNew.this,encodedComment);
                    }
                }

            } catch (Exception e) {
                Utilss.showToast(VideoCommentReplyActivityNew.this, getString(R.string.Something_went_wrong_please_try_again_later), R.color.msg_fail);
                e.printStackTrace();
            }
        });

        binding.txtLike.setOnClickListener(v -> viewModel.likeAPI(VideoCommentReplyActivityNew.this));



        // comment report section............................................
        binding.txtReport.setOnClickListener(v -> viewModel.reportAPI(VideoCommentReplyActivityNew.this));

        if (!viewModel.commentData.getUserId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(VideoCommentReplyActivityNew.this, SharedPrefreances.ID))) {
            binding.txtDelate.setVisibility(View.GONE);
            binding.txtReport.setVisibility(View.VISIBLE);
        } else {
            binding.txtReport.setVisibility(View.GONE);
            binding.txtDelate.setVisibility(View.VISIBLE);
        }

        // comment delete section..........................................
        binding.txtDelate.setOnClickListener(v -> viewModel.deleteCommment(this));

    }

    private void getData()
    {
        if (getIntent().getExtras() != null) {
            String data = getIntent().getExtras().getString("commentData");
            viewModel.commentData = new Gson().fromJson(data, VideoCommentResponse.Row.class);
            viewModel.isSvs = getIntent().getExtras().getBoolean("svs", true);
            viewModel.isAd = getIntent().getBooleanExtra("isAd", false);
            commentCount = getIntent().getExtras().getInt("commentCount", 0);
        }
    }

    private void bindFeedSubComment() {
        binding.subCommentRecycler.setLayoutManager(new LinearLayoutManager(VideoCommentReplyActivityNew.this, LinearLayoutManager.VERTICAL, false));
        binding.subCommentRecycler.setItemAnimator(new DefaultItemAnimator());
        FeedSubCommentAdapter subCommentAdapter = new FeedSubCommentAdapter(VideoCommentReplyActivityNew.this, viewModel.subCommentResponse.getData().getRows(), viewModel.isAd);
        binding.subCommentRecycler.setAdapter(subCommentAdapter);
    }

    // binding svs sub comments data
    private void bindSvsData() {
        if (viewModel.svsSubCommentResponse.getData() != null && viewModel.svsSubCommentResponse.getData().size() > 0) {
            binding.subCommentRecycler.setLayoutManager(new LinearLayoutManager(VideoCommentReplyActivityNew.this,
                    LinearLayoutManager.VERTICAL, false));
            binding.subCommentRecycler.setItemAnimator(new DefaultItemAnimator());
            VideoSubCommentAdapter subCommentAdapter = new VideoSubCommentAdapter(this,
                    viewModel.svsSubCommentResponse.getData(), viewModel.isSvs, viewModel.isAd);
            binding.subCommentRecycler.setAdapter(subCommentAdapter);
        } else {
            binding.subCommentRecycler.setLayoutManager(new LinearLayoutManager(VideoCommentReplyActivityNew.this,
                    LinearLayoutManager.VERTICAL, false));
            binding.subCommentRecycler.setItemAnimator(new DefaultItemAnimator());
            VideoSubCommentAdapter subCommentAdapter = new VideoSubCommentAdapter(this,
                    viewModel.svsSubCommentResponse.getData(), viewModel.isSvs, viewModel.isAd);
            binding.subCommentRecycler.setAdapter(subCommentAdapter);
        }

    }

    // press back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("commentCount", commentCount);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        intent.putExtra("commentCount", commentCount);
        setResult(RESULT_OK, intent);
        super.onDestroy();
    }


    // not in use for this activity
    @Override
    public void replyClicked(VideoCommentResponse.Row model) {

    }

    @Override
    public void changedComment() {
        if (viewModel.isSvs) {
            viewModel.fetchSvsComment(this);
        } else {
            if (viewModel.isAd) {
                viewModel.fetchAdFeedSubComment(this);
            } else {
                viewModel.fetchFeedSubComment(this);
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
                bindFeedSubComment();
        });

        viewModel.bindSVSData.observe(this, aBoolean -> {
            if (aBoolean)
                bindSvsData();
        });

        viewModel.isLike.observe(this, aBoolean -> {
            if (aBoolean)
                binding.txtLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like_diamond_filled));
            else
                binding.txtLike.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.like_diamond));
        });

        viewModel.finishActivity.observe(this, aBoolean -> {
            if (aBoolean)
                finish();
        });


    }
}