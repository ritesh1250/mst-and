package com.meest.metme;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Activities.base.ApiCallActivity;
import com.meest.R;
import com.meest.databinding.ActivityChatUserContactBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.metme.adapter.MediaLinkDocAdapter;
import com.meest.metme.fragments.BlockFragment;
import com.meest.metme.fragments.ChatReportFragment;
import com.meest.metme.fragments.RestrictedFragment;
import com.meest.metme.model.ChatUserContactModel;
import com.meest.metme.viewmodels.ChatUserContactViewModel;
import com.meest.responses.ChatSettingResponse;
import com.meest.responses.GetDocsMediaChatsResponse;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.utils.Helper;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ChatUserContactActivity extends ApiCallActivity {
    LinearLayout restrictedLayout, reportLayout, blockLayout, btBack;
    TextView restrict, report, block, tvTitle, tvWallpaper;
    ImageView restrictImg, reportImg, blockImg;
    String chatHeadId, fullName, otherUserId;
    ActivityChatUserContactBinding binding;
    ChatUserContactViewModel chatUserContactViewModel;
    RecyclerView recyclerView;
    RelativeLayout media_recyclerview;
    ChatUserContactModel chatUserContactModel;
    private String followerCount, followingCount;
    private String profileImage, userBio, posts;
    private boolean isMute;
    private String firstColor;
    private String secondColor;
    private String fontName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat_user_contact);
        chatHeadId = getIntent().getStringExtra("chatHeadID");
        otherUserId = getIntent().getStringExtra("userId");
        fullName = getIntent().getStringExtra("fullName");
        firstColor = getIntent().getStringExtra("firstColor");
        secondColor = getIntent().getStringExtra("secondColor");
        if (secondColor == null) {
            secondColor = "#143988"; //Assign default string
        }
        fontName = SharedPrefreances.getSharedPreferenceString(ChatUserContactActivity.this, chatHeadId);
        Log.e("TAG", "onCreate: fontName" + fontName);
        Log.e("TAG", "onCreate: " + getIntent().getStringExtra("followerCount"));
        Log.e("TAG", "onCreate: " + firstColor);
        Log.e("TAG", "onCreate: " + secondColor);
        followerCount = getIntent().getStringExtra("followerCount");
        followingCount = getIntent().getStringExtra("followingCount");
        profileImage = getIntent().getStringExtra("profileImage");
        userBio = getIntent().getStringExtra("userBio");
        posts = getIntent().getStringExtra("posts");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_user_contact);
        binding.setLifecycleOwner(this);

        chatUserContactModel = new ChatUserContactModel();
        chatUserContactModel.setUserId(otherUserId);
        chatUserContactModel.setChatHeadId(chatHeadId);
        chatUserContactModel.setChatUserName(fullName);
        chatUserContactModel.setChatProfileImage(profileImage);
        chatUserContactModel.setFollower(followerCount);
        chatUserContactModel.setFollowing(followingCount);
        chatUserContactModel.setBio(userBio);
        chatUserContactModel.setPost(posts);

        chatUserContactViewModel = new ChatUserContactViewModel(chatUserContactModel, this, secondColor, otherUserId);
        binding.setChatUserContactViewModel(chatUserContactViewModel);
        Log.e("font---------------", "font:" + SharedPrefreances.getSharedPreferenceString(this, chatHeadId));
        GetChatSetting();
        restrictedLayout = findViewById(R.id.restrictedLayout);
        reportLayout = findViewById(R.id.reportLayout);
        blockLayout = findViewById(R.id.blockLayout);
        restrict = findViewById(R.id.restrict);
        report = findViewById(R.id.report);
        block = findViewById(R.id.block);
        media_recyclerview = findViewById(R.id.media_recyclerview);
        restrictImg = findViewById(R.id.restrictImg);
        reportImg = findViewById(R.id.reportImg);
        blockImg = findViewById(R.id.blockImg);
        tvTitle = findViewById(R.id.tvTitle);
        tvWallpaper = findViewById(R.id.tvWallpaper);
        btBack = findViewById(R.id.btBack);
        recyclerView = findViewById(R.id.recyclerView);
        restrict.setTextColor(getResources().getColor(R.color.first_color));

//        setSelection("restrict");
        defaultRestrictedFragment();
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        restrictedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection("restrict");
                restrictImg.setVisibility(View.VISIBLE);
                reportImg.setVisibility(View.GONE);
                blockImg.setVisibility(View.GONE);

                restrict.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.first_color));
                report.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.black));
                block.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.black));

                Bundle bundle = new Bundle();
                bundle.putString("chatHeadID", chatHeadId);
                bundle.putString("fullName", chatUserContactViewModel.name);
                bundle.putString("toUserId", otherUserId);
                bundle.putString("secondColor", secondColor);
                RestrictedFragment restrictedFragment = new RestrictedFragment();
                restrictedFragment.setArguments(bundle);
                pushFragment(restrictedFragment);
            }
        });

        reportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection("report");

                restrictImg.setVisibility(View.GONE);
                reportImg.setVisibility(View.VISIBLE);
                blockImg.setVisibility(View.GONE);

                restrict.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.black));
                report.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.first_color));
                block.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.black));

                Bundle bundle = new Bundle();
                bundle.putString("chatHeadID", chatHeadId);
                bundle.putString("toUserId", otherUserId);
                bundle.putString("fullName", chatUserContactViewModel.name);
                bundle.putString("secondColor", secondColor);
                ChatReportFragment chatReportFragment = new ChatReportFragment();
                chatReportFragment.setArguments(bundle);
                pushFragment(chatReportFragment);
            }
        });

        blockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection("block");

                restrictImg.setVisibility(View.GONE);
                reportImg.setVisibility(View.GONE);
                blockImg.setVisibility(View.VISIBLE);

                restrict.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.black));
                report.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.black));
                block.setTextColor(ChatUserContactActivity.this.getResources().getColor(R.color.first_color));

                Bundle bundle = new Bundle();
                bundle.putString("chatHeadID", chatHeadId);
                bundle.putString("toUserId", otherUserId);
                bundle.putString("fullName", chatUserContactViewModel.name);
                bundle.putString("secondColor", secondColor);
                BlockFragment blockFragment = new BlockFragment();
                blockFragment.setArguments(bundle);
                pushFragment(blockFragment);
            }
        });

        apiCall();
        initListener();
        changeHeaderColor(firstColor, secondColor);
        if (secondColor != null && !secondColor.equals("")) {
            setColor();
        }
        chatUserContactViewModel.setFontName();
    }

    private void setColor() {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#ffffff"), Color.parseColor("#ffffff")});
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(8f);
        drawable.setStroke(2, Color.parseColor(secondColor));
        binding.viewProfile.setBackground(drawable);
        binding.restrict.setTextColor(Color.parseColor(secondColor));
        if (secondColor != null && !secondColor.equals("")) {
            restrictImg.setColorFilter(Color.parseColor(secondColor));
        } else {
            restrict.setTextColor(getResources().getColor(R.color.first_color));
        }
    }

    private void changeHeaderColor(String firstColor, String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
            binding.mainContainer.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            binding.mainContainer.setBackground(gd);
        }
    }

    void apiCall() {
        Map body = new HashMap<>();
        body.put("chatHeadId", chatHeadId);
        body.put("toUserId", otherUserId);
        body.put("attachmentType", new String[]{"Image", "Video"});
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        apiCallBack(getApi().getDocsMedia(header, body));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        chatUserContactViewModel.GetChatSetting();
    }

    @Subscribe
    public void getResponse(GetDocsMediaChatsResponse response) {
        if (response.isSuccess() && response.getData() != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(new MediaLinkDocAdapter(response.getData(), fullName, this, secondColor, chatHeadId));
        } else {
            dialogFailed(response.getErrorMessage());
            media_recyclerview.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        binding.openAppearance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatUserContactActivity.this, TextAppearanceActivity.class);
                intent.putExtra("otherUserId", otherUserId);
                intent.putExtra("chatHeadId", chatHeadId);
                intent.putExtra("secondColor", secondColor);
                startActivityForResult(intent, 100);
            }
        });
        binding.openWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatUserContactActivity.this, WallpaperActivity.class);
                intent.putExtra("otherUserId", otherUserId);
                intent.putExtra("chatHeadId", chatHeadId);
                intent.putExtra("secondColor", secondColor);
                startActivityForResult(intent, 100);
            }
        });
        binding.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.isNetworkAvailable(getApplicationContext())) {
                    Intent intent = new Intent(ChatUserContactActivity.this, OtherUserAccount.class);
                    intent.putExtra("userId", otherUserId);
                    intent.putExtra("chatHeadId", chatHeadId);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.switchMuteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timber.e("onClick: " + chatUserContactViewModel.isMute);
                if (isMute) {
                    chatUserContactViewModel.changeMuteSetting(!isMute, chatHeadId, SharedPrefreances.getSharedPreferenceString(ChatUserContactActivity.this, SharedPrefreances.AUTH_TOKEN));
                } else {
                    chatUserContactViewModel.changeMuteSetting(!isMute, chatHeadId, SharedPrefreances.getSharedPreferenceString(ChatUserContactActivity.this, SharedPrefreances.AUTH_TOKEN));
                }
            }
        });
    }

    private void defaultRestrictedFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("chatHeadID", chatHeadId);
        bundle.putString("fullName", chatUserContactViewModel.name);
        bundle.putString("secondColor", secondColor);
        RestrictedFragment restrictedFragment = new RestrictedFragment();
        restrictedFragment.setArguments(bundle);
        pushFragment(restrictedFragment);
    }

    public void pushFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
//                    .addToBackStack("fragment")
                .commit();
    }

    void setSelection(String type) {
        if (type.equals("restrict")) {
            if (secondColor != null && !secondColor.equals("")) {
                restrict.setTextColor(Color.parseColor(secondColor));
                restrictImg.setColorFilter(Color.parseColor(secondColor));
            } else {
                restrict.setTextColor(getResources().getColor(R.color.first_color));
            }
            report.setTextColor(getResources().getColor(R.color.black));
            block.setTextColor(getResources().getColor(R.color.black));
            restrictImg.setVisibility(View.VISIBLE);
            reportImg.setVisibility(View.GONE);
            blockImg.setVisibility(View.GONE);
        } else if (type.equals("report")) {
            if (secondColor != null && !secondColor.equals("")) {
                report.setTextColor(Color.parseColor(secondColor));
                reportImg.setColorFilter(Color.parseColor(secondColor));
            } else {
                report.setTextColor(getResources().getColor(R.color.first_color));
            }
            restrict.setTextColor(getResources().getColor(R.color.black));
            block.setTextColor(getResources().getColor(R.color.black));

            restrictImg.setVisibility(View.GONE);
            reportImg.setVisibility(View.VISIBLE);
            blockImg.setVisibility(View.GONE);
        } else if (type.equals("block")) {
//            Toast.makeText(this, ""+secondColor, Toast.LENGTH_SHORT).show();
            if (secondColor != null && !secondColor.equals("")) {
                block.setTextColor(Color.parseColor(secondColor));
                blockImg.setColorFilter(Color.parseColor(secondColor));
            } else {
                block.setTextColor(getResources().getColor(R.color.first_color));
            }

            restrict.setTextColor(getResources().getColor(R.color.black));
            report.setTextColor(getResources().getColor(R.color.black));

            restrictImg.setVisibility(View.GONE);
            reportImg.setVisibility(View.GONE);
            blockImg.setVisibility(View.VISIBLE);
        }
    }

    public void GetChatSetting() {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("userId", otherUserId);
        body.put("chatHeadId", chatHeadId);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ChatSettingResponse> call = webApi.GetChatsSetting(map, body);
        call.enqueue(new Callback<ChatSettingResponse>() {

            @Override
            public void onResponse(Call<ChatSettingResponse> call, Response<ChatSettingResponse> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body().getCode() == 1) {
                            try {
                                isMute = response.body().getData().getIsNotificationMute();
                                binding.switchMuteMessage.setChecked(isMute);
//                                isMute = response.body().getData().getNotificationMute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (response.code() == 500) {
                        Utilss.showToast(ChatUserContactActivity.this, getResources().getString(R.string.error_msg), R.color.grey);
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<ChatSettingResponse> call, Throwable t) {
                Log.e("Atg", "test =" + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                if (data.getStringExtra("return").equals("true")) {
                    finish();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}