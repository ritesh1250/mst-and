package com.meest.svs.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.meest.R;
import com.meest.responses.FollowResponse;
import com.meest.responses.VideosResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.svs.adapters.AccountVideoAdapter;
import com.meest.svs.adapters.FavHeartAccountVideoAdapter;
import com.meest.svs.models.FavMusicAudioResponse;
import com.meest.svs.models.SVSProfileModel;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meest.Fragments.HomeFragments.PAGE_SIZE;

@SuppressLint("SetTextI18n")
public class VideoOtherUserActivity extends AppCompatActivity {

    ImageView ivGridList, ivHeartFavList;
    String userId;
    ImageView id_back;
    TextView username;
    EmojiconTextView about;
    CircleImageView profileImage;
    Button shareButton, followButton;
    RecyclerView recyclerView, vaf_recyclerFav;
    LinearLayout layout_following, layout_followers;
    private ImageView back_arrow;
    LottieAnimationView loadingIndicator;
    private SVSProfileModel profileModel;
    VideosResponse VideosResponse;
    FavMusicAudioResponse favMusicAudioResponse;
    TextView followerCount, followingCount, diamondCount;
    boolean isFollowing = false;
    Map<String, String> header;
    HashMap<String, String> body;
    WebApi webApi;
    List<VideosResponse.Row> userVideosList = new ArrayList<>();
    private TextView txt_full_name;
    List<FavMusicAudioResponse.Row> audioList = new ArrayList<>();

    int pastVisibleItemCount = 0;
    int totalItemCount = 0;
    int visibleItemCount = 0;
    int pagePerRecord = 20;
    int pageno = 1;
    boolean Loading = true;
    int totalCount = 0;
    public static boolean isLastPage = false;
    LinearLayoutManager manager = new LinearLayoutManager(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_other_user_activity);

        if (getIntent().getExtras() == null) {
            Utilss.showToast(this, "Server Error", R.color.msg_fail);
            finish();
        }

        findIds();

        recyclerView.addOnScrollListener(new PaginationListener((GridLayoutManager) recyclerView.getLayoutManager()) {
            @Override
            public void loadMoreItems() {
                System.out.println("==PAGINATION STARTED " + pageno);
                if (Loading) {
                    Loading = false;
                    pageno = pageno + 1;
                    getDataFromServer(String.valueOf(pageno));
                }
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });


        userId = getIntent().getExtras().getString("userId", "");

        getDataFromServer(String.valueOf(pageno));


        ivGridList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
                recyclerView.setVisibility(View.VISIBLE);
                vaf_recyclerFav.setVisibility(View.GONE);
                ivGridList.setColorFilter(getResources().getColor(R.color.black));
                ivHeartFavList.setColorFilter(getResources().getColor(R.color.lan_gray));

            }
        });

        ivHeartFavList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFavList();
                vaf_recyclerFav.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ivHeartFavList.setColorFilter(getResources().getColor(R.color.black));
                ivGridList.setColorFilter(getResources().getColor(R.color.lan_gray));

            }
        });

        loadingIndicator.setAnimation("loading.json");
        loadingIndicator.playAnimation();
        loadingIndicator.loop(true);

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFollowing();
            }
        });

        layout_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoOtherUserActivity.this, FollowListActivity.class);
                intent.putExtra("type", "following");
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        layout_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoOtherUserActivity.this, FollowListActivity.class);
                intent.putExtra("type", "follower");
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }

    // region Listeners
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
            int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

            if (!Loading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {

                }
            }
        }
    };
    private void getDataFromServer(String pageno) {

        header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        body = new HashMap<>();
        body.put("userId", userId);
        body.put("svs", String.valueOf(true));

        body.put("pageNumber", pageno);
        body.put("pageSize", String.valueOf(pagePerRecord));

        webApi = ApiUtils.getClient().create(WebApi.class);

        fetchData();
    }

    private void fetchDataFavList() {
        loadingIndicator.setVisibility(View.VISIBLE);

        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");

        HashMap<String, Object> body = new HashMap<>();
        body.put("userId", userId);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FavMusicAudioResponse> apiCall = webApi.favMusicByUser(header, body);
        apiCall.enqueue(new Callback<FavMusicAudioResponse>() {
            @Override
            public void onResponse(Call<FavMusicAudioResponse> call, Response<FavMusicAudioResponse> response) {
                loadingIndicator.setVisibility(View.GONE);
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    try {
                        audioList = response.body().getData().getUploadedAudios().getRows();
                        if (audioList.size() > 0) {
                            bindOtherUserData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }
                } else {
                    Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<FavMusicAudioResponse> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void bindOtherUserData() {
        FavHeartAccountVideoAdapter favHeartAccountVideoAdapter = new FavHeartAccountVideoAdapter(this, audioList);
        vaf_recyclerFav.setLayoutManager(new LinearLayoutManager(this));
        vaf_recyclerFav.setHasFixedSize(true);
        vaf_recyclerFav.setAdapter(favHeartAccountVideoAdapter);
    }


    private void fetchData() {
        loadingIndicator.setVisibility(View.VISIBLE);

        Call<SVSProfileModel> profileCall = webApi.getSVSProfile(header, body);
        profileCall.enqueue(new Callback<SVSProfileModel>() {
            @Override
            public void onResponse(Call<SVSProfileModel> call, Response<SVSProfileModel> response) {
                loadingIndicator.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getSuccess()) {
                    profileModel = response.body();
                    fetchVideosByUser();
                } else {
                    Utilss.showToast(VideoOtherUserActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<SVSProfileModel> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Utilss.showToast(VideoOtherUserActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void fetchVideosByUser() {
        loadingIndicator.setVisibility(View.VISIBLE);

        Call<VideosResponse> profileCall = webApi.getVideoByUser(header, body);
        profileCall.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                loadingIndicator.setVisibility(View.GONE);
                if (response.code() == 200 && response.body().getSuccess()) {
                    VideosResponse = response.body();

                    bindData();
                } else {
                    Utilss.showToast(VideoOtherUserActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
                loadingIndicator.setVisibility(View.GONE);
                Utilss.showToast(VideoOtherUserActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void bindData() {
        if (profileModel.getData().getUser().getIsFriend() > 0) {
            followButton.setText(getString(R.string.Unfollow));
            isFollowing = true;
        } else {
            followButton.setText(getString(R.string.follow));
            isFollowing = false;
        }
//        txt_full_name.setText(profileModel.getData().getUser().getFirstName() + " " + profileModel.getData().getUser().getLastName());
        username.setText("@" + profileModel.getData().getUser().getUsername());

        String caption;
        try {
            caption = URLDecoder.decode(profileModel.getData().getUser().getAbout(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            caption = profileModel.getData().getUser().getAbout();
        }

        about.setText(caption);
        followingCount.setText(String.valueOf(profileModel.getData().getUser().getTotalFollowings()));
        followerCount.setText(String.valueOf(profileModel.getData().getUser().getTotalFollowers()));

        if (profileModel.getData().getUser().getDisplayPicture() != null) {
            Glide.with(this).load(profileModel.getData().getUser().getDisplayPicture()).into(profileImage);
        }

        // binding posts data
        if (VideosResponse != null && VideosResponse.getData().getRows().size() > 0) {
            userVideosList.clear();

            for (com.meest.responses.VideosResponse.Row model : VideosResponse.getData().getRows()) {
                if (!model.getPrivate()) {
                    userVideosList.add(model);
                }
            }

            AccountVideoAdapter videoAdapter = new AccountVideoAdapter(this, userVideosList);
            recyclerView.setAdapter(videoAdapter);

            int averageLikes = (int) (profileModel.getData().getUser().getLikes()
                    / VideosResponse.getData().getRows().size());
            diamondCount.setText(String.valueOf(averageLikes));
        } else {
            diamondCount.setText("0");
        }
    }

    @SuppressLint("SetTextI18n")
    private void changeFollowing() {
        // body
        HashMap<String, String> followingBody = new HashMap<>();
        followingBody.put("followingId", userId);
        followingBody.put("svs", String.valueOf(true));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FollowResponse> call;
        if (isFollowing) {
            followingBody.put("status", String.valueOf(false));
            call = webApi.removeFollow(header, followingBody);
        } else {
            call = webApi.followRequest(header, followingBody);
        }
        call.enqueue(new Callback<FollowResponse>() {

            @Override
            public void onResponse(@NotNull Call<FollowResponse> call, @NotNull Response<FollowResponse> response) {
                if (response.code() == 200 && response.body().getSuccess()) {
//                    if (isFollowing) {
//                        isFollowing = false;
//                        followButton.setText("Follow");
//                    } else {
//                        isFollowing = true;
//                        followButton.setText("Unfollow");
//                    }
                    fetchData();
                } else {
                    Utilss.showToast(VideoOtherUserActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(@NotNull Call<FollowResponse> call, @NotNull Throwable t) {
                Utilss.showToast(VideoOtherUserActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void findIds() {
        ivGridList = findViewById(R.id.ivGridList);
        ivHeartFavList = findViewById(R.id.ivHeartFavList);
        back_arrow = findViewById(R.id.back_arrow);
        recyclerView = findViewById(R.id.vaf_recycler);
        vaf_recyclerFav = findViewById(R.id.vaf_recyclerFav);
        layout_following = findViewById(R.id.vaf_followingLayout);
        layout_followers = findViewById(R.id.vaf_followersLayout);
        profileImage = findViewById(R.id.vaf_profileImage);
        username = findViewById(R.id.vaf_username);
        about = findViewById(R.id.vaf_about);
        loadingIndicator = findViewById(R.id.loading);
        shareButton = findViewById(R.id.vaf_share);
        followButton = findViewById(R.id.vaf_follow);
        followerCount = findViewById(R.id.vaf_followersCount);
        followingCount = findViewById(R.id.vaf_followingCount);
        diamondCount = findViewById(R.id.vaf_diamondCount);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
//        txt_full_name = findViewById(R.id.txt_full_name);
    }
}


abstract class PaginationListener extends RecyclerView.OnScrollListener {

    public static final int PAGE_START = 1;

    @NonNull
    private LinearLayoutManager layoutManager;

    /**
     * Set scrolling threshold here (for now i'm assuming 10 item in one page)
     */
    private static final int PAGE_SIZE = 20;
    private Long timeStamp = 0L;

    /**
     * Supporting only LinearLayoutManager for now.
     */
    public PaginationListener(@NonNull LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        int currentLastItem = layoutManager.findLastVisibleItemPosition();
        if (currentLastItem == totalItemCount - 1) {
//            requestNextPage();
        }
        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                if (System.currentTimeMillis() - timeStamp > 1000) {
                    timeStamp = System.currentTimeMillis();
                    loadMoreItems();

                }
            }
        }
    }

    public abstract void loadMoreItems();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}

