package com.meest.Fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.master.exoplayer.MasterExoPlayer;
import com.master.exoplayer.MasterExoPlayerHelper;
import com.master.exoplayer.MuteStrategy;
import com.meest.Activities.EditProfileActivity;
import com.meest.Activities.SearchGridActivity;
import com.meest.Activities.WriteTextActivity;
import com.meest.Activities.base.ApiCallFragemt;
import com.meest.Adapters.HomeAdapter;
import com.meest.Adapters.HomeStoryAdapter;
import com.meest.Adapters.TagPersonAdaptor;
import com.meest.Adapters.UserPostAdapter;
import com.meest.Interfaces.FeedDataCallback;
import com.meest.Interfaces.OnHomeAnimClick;
import com.meest.Interfaces.PostItemsCallback;
import com.meest.MainActivity;
import com.meest.Paramaters.InsertLikeParameters;
import com.meest.Paramaters.UnFollowPeoplesParam;
import com.meest.Paramaters.UserStoryParameter;
import com.meest.R;
import com.meest.logPressAnim.enums.ButtonPosition;
import com.meest.medley_camera2.camera2.utills.CameraUtil;
import com.meest.medley_camera2.camera2.view.activity.MeestCameraActivity;
import com.meest.meestbhart.login.model.ApiResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Constant;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.meestcamera.MeestMainCameraActivity;
import com.meest.metme.ChatActivity;
import com.meest.metme.ChatBoatActivity;
import com.meest.metme.MetMeUtils.ToolTipWindow;
import com.meest.model.AdMediaData;
import com.meest.model.HomeModel;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.responses.DeleteStoryResponse;
import com.meest.responses.DisLikeResponse;
import com.meest.responses.FeedResponse;
import com.meest.responses.FollowsPeoplesResponse;
import com.meest.responses.GetReportResponse;
import com.meest.responses.InsertPostLikeResponse;
import com.meest.responses.ShowFeedAdOne;
import com.meest.responses.UserFeedbackResponse;
import com.meest.responses.UserFollowerStoryResponse;
import com.meest.social.socialViewModel.model.CheckChatHeadNFollowResponse1;
import com.meest.social.socialViewModel.model.CreateGroupResponse;
import com.meest.social.socialViewModel.model.MessageResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.meest.social.socialViewModel.model.UserProfileRespone1;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.view.SplashScreen;
import com.meest.social.socialViewModel.view.comment.VideoCommentActivityNew;
import com.meest.social.socialViewModel.view.createTextPost.CreateTextPost;
import com.meest.social.socialViewModel.view.otherUserAccount.OtherUserAccount;
import com.meest.storyview.screen.StoryActivity;
import com.meest.svs.adapters.ReportAdapter;
import com.meest.utils.HomeAnimDialog;
import com.meest.utils.PaginationListenerHideUnhide;
import com.meest.utils.Permission;
import com.meest.utils.goLiveUtils.CommonUtils;
import com.meest.videomvvmmodule.utils.Const;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.view.View.GONE;
import static com.meest.meestbhart.utilss.SharedPrefreances.HOME_INTRO;
import static com.meest.meestbhart.utilss.SharedPrefreances.PROFILE_IMAGE;
import static com.meest.utils.goLiveUtils.CommonUtils.getTargetView;

public class HomeFragments extends ApiCallFragemt implements FeedDataCallback, PostItemsCallback, OnNoInternetRetry, OnHomeAnimClick, UserPostAdapter.RecycleListener {

    public static final int CAMERA_PERMISSION_CODE = 125;
    public static final int PAGE_SIZE = 20;
    private static final String TAG = "HomeFragments";
    private static final int CREATE_STORY_CONST = 9988;
    //checkpermission
    private static final int PERMISSION_REQUEST_CODE = 200;
    public static int pagePerRecord = 15;
    public static int totalCount = 0;
    public static RecyclerView postRecyclerView;
    public static SimpleExoPlayer previousPlayer;
    public static SimpleExoPlayer previousPlayerNew;
    public static ArrayList<HomeModel> feedArrayList = new ArrayList<>();
    //static String
    public static String BUNDLE_STORY_RESPONSE = "StoryResponse";
    public static String BUNDLE_CALL_TYPE = "call_type";
    public static String BUNDLE = "BUNDLE";
    public static String BUNDLE_POSITION = "position";
    public static LinearLayoutManager homeManager;
    public static boolean isLastPage = false;
    public static UserFollowerStoryResponse userOwnStoryResponse = null;
    public static NestedScrollView nestedScrollView;
    private static HomeFragments instanceHomeFragment;
    HomeAdapter homeAdapter;
    ArrayList<UserFollowerStoryResponse.Row> userStoryList = new ArrayList<>();
    TextView txt_name, tvChatNotificationCount;
    CardView layoutBottomSheet;
    ImageView img_chat;
    ImageView img_logout, img_video_call, img_camera;
    View view;
    LinearLayout layout_bottom;
    LottieAnimationView image;
    ImageView img_search;
    int pageno = 1;
    boolean loading = true;
    boolean swipRefresh = false;
    //  new changes 02-March-2021
    boolean isUp;
    LinearLayout liShowHide;
    LinearLayout liStory, ll_hide_visibility;
    ImageView img_profile_top;
    RecyclerView recyclerView_Story;
    ToolTipWindow tipWindow;
    UserPostAdapter adapter;
    LinearLayout _headerLayout;
    UserFollowerStoryResponse userStoryListResponse = null;
    AlertDialog sendFeedbackAlertDialog = null;
    //animation hide
    Socket mSocket;
    String c_userid, c_fName, c_lName, c_username, c_profile_pic;
    private WebApi webApi;
    private ImageView layout_story;
    private SwipeRefreshLayout swipre_refresh_layout;
    private int COMMENT_OPEN_CODE = 30, openedPosition = 0;
    public static  MasterExoPlayerHelper masterExoPlayerHelper;
    //    public HomeFragments(Boolean scrollBool) {
//        this.scrollBool=scrollBool;
//    }
    private HomeStoryAdapter homeStoryAdapter;
    //home Story
    private ImageView ivStory;
    private int currentPage = 1;
    private boolean isLoading = false;

    //Animations
    private CustomDialogBuilder customDialogBuilder;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private Boolean scrollBool = false;

    public static HomeFragments getInstanceHomeFragment() {
        return instanceHomeFragment;
    }


//    public static void scrollToPosition(final int position) {
//        if (postRecyclerView != null) {
//                    postRecyclerView.scrollToPosition(position);
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) postRecyclerView.getLayoutManager();
//                    layoutManager.scrollToPositionWithOffset(0, 0);
//                    nestedScrollView.fullScroll(View.FOCUS_UP);
//                    nestedScrollView.scrollTo(0, 0);
//        }
//    }


    public static void startPlayer(SimpleExoPlayer exoPlayer) {

        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(true);

        }
    }

    public static void pausePlayer(SimpleExoPlayer exoPlayer) {

        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);

        }
    }

    public static void releaseExoPlayer(SimpleExoPlayer exoPlayer) {

        if (exoPlayer != null) {
            exoPlayer.release();

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.DarkTheme);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        view = inflater.inflate(R.layout.home_fragment, null);
        instanceHomeFragment = this;
        swipre_refresh_layout = view.findViewById(R.id.swipre_refresh_layout);
        img_search = view.findViewById(R.id.img_search);
        swipre_refresh_layout.setProgressViewOffset(true, 50, 150);
        swipre_refresh_layout.setRefreshing(false);
        layout_bottom = view.findViewById(R.id.layout_bottom);
        liShowHide = view.findViewById(R.id.liShowHide);
        ll_hide_visibility = view.findViewById(R.id.ll_hide_visibility);
        layout_story = view.findViewById(R.id.ivStory);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        layoutBottomSheet = view.findViewById(R.id.layoutBottomSheet);
        img_logout = view.findViewById(R.id.img_logout);
        txt_name = view.findViewById(R.id.txt_name);
        recyclerView_Story = view.findViewById(R.id.recyclerView_Story);
        liStory = view.findViewById(R.id.ln_story);
        ivStory = view.findViewById(R.id.ivStory);
        image = view.findViewById(R.id.home_loading);
        img_profile_top = view.findViewById(R.id.img_profile_top);
        img_chat = view.findViewById(R.id.img_chat);
        img_video_call = view.findViewById(R.id.img_video_call);
        img_camera = view.findViewById(R.id.img_camera);
        postRecyclerView = view.findViewById(R.id.recyclerView_post);
        isUp = false;
        webApi = ApiUtils.getClient().create(WebApi.class);
        SharedPrefreances.setSharedPreferenceString(getActivity(), "VideoPostActive", "0");
        SharedPrefreances.setSharedPreferenceString(getActivity(), "back", "1");
        tipWindow = new ToolTipWindow(getActivity());
        homeAdapter = new HomeAdapter(getActivity(), null, this, false, this);
        setFeedsRecycler();
        CommonUtils.loadImage(ivStory, SharedPrefreances.getSharedPreferenceString(getActivity(), PROFILE_IMAGE), getActivity(), R.drawable.chat_user_placeholder);
        CommonUtils.loadImage(img_profile_top, SharedPrefreances.getSharedPreferenceString(getActivity(), PROFILE_IMAGE), getActivity(), R.drawable.chat_user_placeholder);


        img_search.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), SearchGridActivity.class));
            //  pushFragment(new SearchGridFragment(getActivity()), "SearchGrid");
        });
        liShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CreateTextPostActivity.class);
                Intent intent = new Intent(getActivity(), CreateTextPost.class);
                intent.putExtra(BUNDLE_CALL_TYPE, "feed");
                startActivity(intent);
            }
        });

        customDialogBuilder = new CustomDialogBuilder(getActivity());
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        layout_story.performClick();
                    } else {

                    }
                });

        layout_story.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                checkSelfPermission();
                return true;
            }
        });

        layout_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (userOwnStoryResponse.getData().getRows() != null && userOwnStoryResponse.getData().getRows().size() > 0) {
                        Intent intent = new Intent(getActivity(), StoryActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("StoryResponse", (Serializable) userOwnStoryResponse);
                        intent.putExtra("BUNDLE", args);
                        intent.putExtra("iSUser", "yes");
                        intent.putExtra("position", 0);
                        startActivityForResult(intent, 9901);
                    } else {
                        checkSelfPermission();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        img_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);


        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefreances.setSharedPreferenceString(getActivity(), "login", "0");
//                Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                Intent intent = new Intent(getActivity(), SplashScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        if (ConnectionUtils.isConnected(getActivity())) {
            swipRefresh = true;
            pageno = 1;
            homeFragmentFeedListingService(String.valueOf(pageno));
//            fetchCurrentUserData();
        } else {
            ConnectionUtils.showNoConnectionDialog(getActivity(), this);
        }
        swipre_refresh_layout.setColorSchemeResources(R.color.social_background_blue);
        swipre_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(SharedPrefreances.getSharedPreferenceInt(getContext(), SharedPrefreances.POSTWALL_REFRESH_COUNT)>0){
                    SharedPrefreances.setSharedPreferenceInt(getContext(), SharedPrefreances.POSTWALL_REFRESH_COUNT,0);
                    swipre_refresh_layout.setRefreshing(false);
                    pageno = 1;
                    swipRefresh = true;
                    homeFragmentFeedListingService(String.valueOf(pageno));
                    getUserStories();
                }else {
                    swipre_refresh_layout.setRefreshing(false);
                }
            }
        });
        homeAdapter.setOnItemClickListenerDislike(new HomeAdapter.OnItemClickListenerDisLike() {
            @Override
            public void onItemClick(final int position) {
                Map<String, String> map = new HashMap<>();
                map.put("x-token",SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
                InsertLikeParameters insertLikeParameters = new InsertLikeParameters(feedArrayList.get(position).getFeedResponse().getId());
                WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
                Call<DisLikeResponse> call1 = webApi1.insertDislike(map, insertLikeParameters);
                call1.enqueue(new Callback<DisLikeResponse>() {
                    @Override
                    public void onResponse(Call<DisLikeResponse> call, Response<DisLikeResponse> response) {
                        try {
                            if (response.body().getCode() == 200) {
                                //Utilss.showToast(getActivity(),"Disliked",R.color.green);
                                homeAdapter.notifyDataSetChanged();
                            } else {
                                Utilss.showToast(getActivity(), response.body().getSuccess().toString(), R.color.msg_fail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<DisLikeResponse> call, Throwable t) {
                        image.setVisibility(GONE);
                        Log.w("error", t);
                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                    }
                });
            }
        });


        homeAdapter.setOnItemClickListenerComment(new HomeAdapter.OnItemClickListenerComment() {
            @Override
            public void onItemClick(final int position) {

                Intent intent = new Intent(getActivity(), VideoCommentActivityNew.class);
                intent.putExtra("videoId", feedArrayList.get(position).getFeedResponse().getId());
                intent.putExtra("msg", feedArrayList.get(position).getFeedResponse().getCaption());
                intent.putExtra("svs", false);
                startActivity(intent);

            }
        });

//        manageScroll();img_home
        if (SharedPrefreances.getSharedPreferenceString(getActivity(), HOME_INTRO) == null || SharedPrefreances.getSharedPreferenceString(getActivity(), HOME_INTRO).equals("")) {
            SharedPrefreances.setSharedPreferenceString(getActivity(), HOME_INTRO, "false");
            TapTargetSequence tapTargetSequence = new TapTargetSequence(getActivity()).targets(getTargetView(getActivity(), img_chat, getResources().getString(R.string.chatIntro), ""),
                    getTargetView(getActivity(), img_profile_top, getResources().getString(R.string.sharePost), "", 50)
                    //  getTargetView(getActivity(), img_camera, getResources().getString(R.string.comingsoon), "")
            ).listener(new TapTargetSequence.Listener() {
                @Override
                public void onSequenceFinish() {
                    ((MainActivity) getActivity()).showTargetForMainActivity();
                }

                @Override
                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                }

                @Override
                public void onSequenceCanceled(TapTarget lastTarget) {

                }
            });
            tapTargetSequence.start();
        }

        return view;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("Log Brodcast", ":HITTT1");
            String status = intent.getStringExtra("status");
            if (status.equalsIgnoreCase("ok")) {
                onRetry();
            }
            if (status.equalsIgnoreCase("story")) {
                getUserStories();
            }
//            if(status.equalsIgnoreCase("NewPost")) {
//                System.out.println("NewPost::  " + status);
//                pageno = 1;
//                homeFragmentFeedListingService(String.valueOf(pageno));
//            }
        }
    };

    private void setFeedsRecycler() {
        try {
            masterExoPlayerHelper = new MasterExoPlayerHelper(getActivity(), R.id.frame, true, 0.5f, MuteStrategy.ALL, true, false, 1, Integer.MAX_VALUE);
            masterExoPlayerHelper.makeLifeCycleAware(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFeedsData(ArrayList<HomeModel> feedArrayList) {
        try {
            if (adapter == null) {
                homeManager = new LinearLayoutManager(getActivity());
                Log.e("masterExoPlayerHelper", " null");
                adapter = new UserPostAdapter(feedArrayList, masterExoPlayerHelper, getActivity(), HomeFragments.this, userStoryList, this, postRecyclerView, this);
                postRecyclerView.setAdapter(adapter);
                postRecyclerView.setNestedScrollingEnabled(false);
                postRecyclerView.setLayoutManager(homeManager);
                masterExoPlayerHelper.attachToRecyclerView(postRecyclerView);
                masterExoPlayerHelper.makeLifeCycleAware(this);
                masterExoPlayerHelper.getPlayerView().setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);


            } else {
                Log.e("masterExoPlayerHelper", "not null");
                adapter.notifyFeedsDataSetChanged(feedArrayList);
            }

            postRecyclerView.addOnScrollListener(new PaginationListenerHideUnhide((LinearLayoutManager) postRecyclerView.getLayoutManager(), PaginationListenerHideUnhide.PAGE_SIZE_15, ll_hide_visibility) {
                @Override
                public void loadMoreItems() {
                    System.out.println("==PAGINATION STARTED " + pageno);
                    if (loading)
                        getAD();
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
//            postRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);
        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }

    public void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        } else {
            checkPermission();
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), getActivity().getResources().getString(R.string.social_to_capture_photos_and_videos),
                        getActivity().getResources().getString(R.string.not_now), getActivity().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_camera_permission, null), getActivity().getResources().getString(R.string.social_to_capture_photos_and_videos),
                        getActivity().getResources().getString(R.string.not_now), getActivity().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.CAMERA);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), getActivity().getResources().getString(R.string.social_to_capture_storage),
                        getActivity().getResources().getString(R.string.not_now), getActivity().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), getActivity().getResources().getString(R.string.social_to_capture_storage),
                        getActivity().getResources().getString(R.string.not_now), getActivity().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.READ_EXTERNAL_STORAGE);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), getActivity().getResources().getString(R.string.social_to_capture_storage),
                        getActivity().getResources().getString(R.string.not_now), getActivity().getResources().getString(R.string.setting), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            } else {
                customDialogBuilder.showPermissionDialog(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_storage_permission, null), getActivity().getResources().getString(R.string.social_to_capture_storage),
                        getActivity().getResources().getString(R.string.not_now), getActivity().getResources().getString(R.string.continuee), new CustomDialogBuilder.OnDismissListener() {

                            @Override
                            public void onPositiveDismiss() {
                                requestPermissionLauncher.launch(
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            }

                            @Override
                            public void onNegativeDismiss() {

                            }
                        });
            }
        } else {
            afterPermissionGranted();
        }


    }

    private void afterPermissionGranted() {
        Intent intent = new Intent(getActivity(), MeestCameraActivity.class);
//        Intent intent = new Intent(getActivity(), MeestMainCameraActivity.class);
        intent.putExtra("call_type", "story");
        intent.putExtra("filter", " ");
        intent.putExtra("isVideo", false);
        startActivityForResult(intent, CREATE_STORY_CONST);
    }


    private void fetchCurrentUserData() {
        Map<String, String> header = new HashMap<>();
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        Call<UserProfileRespone1> call = webApi.getUserProfileData(header);
        call.enqueue(new Callback<UserProfileRespone1>() {
            @Override
            public void onResponse(Call<UserProfileRespone1> call, Response<UserProfileRespone1> response) {
                image.setVisibility(View.GONE);
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        getUserStories();
                        SharedPrefreances.setSharedPreferenceString(getActivity(), PROFILE_IMAGE, response.body().getDataUser().getThumbnail());

                        if (response.body().getDataUser().getGender().equalsIgnoreCase(getString(R.string.male))) {

                            CommonUtils.loadImage(img_profile_top, response.body().getDataUser().getThumbnail(), getActivity(), R.drawable.male_place_holder);

                        } else if (response.body().getDataUser().getGender().equalsIgnoreCase(getString(R.string.female))) {

                            CommonUtils.loadImage(img_profile_top, response.body().getDataUser().getThumbnail(), getActivity(), R.drawable.female_cardplaceholder);

                        } else {
                            CommonUtils.loadImage(img_profile_top, response.body().getDataUser().getThumbnail(), getActivity(), R.drawable.placeholder);

                        }

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<UserProfileRespone1> call, Throwable t) {
                Log.w("error", t.toString());
            }
        });
    }

    public void hideBottom() {
        layout_bottom.setVisibility(View.INVISIBLE);
    }

    public void showBottom() {
        layout_bottom.setVisibility(View.VISIBLE);
    }

    public void showBottomSheetDialog(List<FeedResponse.UserTags> list) {
        View view = getLayoutInflater().inflate(R.layout.tagged_users_adaptor, null);
        RecyclerView rv = view.findViewById(R.id.recycler_view);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(view);
        dialog.show();
        TagPersonAdaptor tagPersonAdaptor = new TagPersonAdaptor(list);
        rv.setAdapter(tagPersonAdaptor);
        tagPersonAdaptor.notifyDataSetChanged();
    }

    private void homeFragmentFeedListingService(String pageNumber) {
        image.setVisibility(View.VISIBLE);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
//        map.put("pageNumber", pageNumber);
//        map.put("pageSize", String.valueOf(pagePerRecord));
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", pageNumber);
        body.put("pageSize", String.valueOf(pagePerRecord));
        isLoading = true;
        final Call<FeedResponse> call = webApi.getAllFeed(map, body);
        Log.e("update ",map +body.toString()+"");
        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                image.setVisibility(GONE);
                isLoading = false;
                swipre_refresh_layout.setRefreshing(false); // Disables the refresh icon
                if (pageNumber.equals("1")) {
                    swipRefresh = false;
                    feedArrayList.clear();
                }
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        totalCount = response.body().getData().getCount();
                        for (int i = 0; i < response.body().getData().getRows().size(); i++) {
                            HomeModel homeModel = new HomeModel();
                            homeModel.setFeedResponse(response.body().getData().getRows().get(i));
                            homeModel.setAD(false);
                            feedArrayList.add(homeModel);
                        }

                        if (feedArrayList.size() == totalCount) {
                            isLastPage = true;
                        }
                        setFeedsData(feedArrayList);

//                        homeAdapter.setHasStableIds(true);
//                        postRecyclerView.setAdapter(homeAdapter);
//                        homeAdapter.notifyDataSetChanged();

//                        showAdvertisement();
                        SharedPrefreances.setSharedPreferenceInt(getActivity(), SharedPrefreances.NOIFICATION_COUNT, response.body().getData().getUnreadNotification());
                        ((MainActivity) getActivity()).setNotificationCount(String.valueOf(SharedPrefreances.getSharedPreferenceInt(getActivity(), SharedPrefreances.NOIFICATION_COUNT)));

                    } else {
//                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                isLoading = false;
                image.setVisibility(GONE);
                Log.w("error", t);
                swipre_refresh_layout.setRefreshing(false);
            }
        });
    }

    private void getUserStories() {
        try {
            userStoryListResponse = new UserFollowerStoryResponse();
            userStoryList.clear();
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
//                header.put("x-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6IjNmZmI0MmQ5LWUxYjQtNGU0ZC04NzdjLTYwODRjMGZhMzYwNSIsImVtYWlsIjoiIiwiY3JlYXRlZEF0IjoiMjAyMC0xMC0xMlQxNDowMzoyMC41MTRaIn0sImlhdCI6MTYwMjUxMTQwMH0.zVS3c5B24b-r2PU6hYPANWwavs409IuLXPNPbcMLWdM");
            HashMap<String, String> body = new HashMap<>();
            body.put("dislayType", "feed");
            Call<UserFollowerStoryResponse> call = webApi.getUserStories(header, body);
            image.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<UserFollowerStoryResponse>() {
                @Override
                public void onResponse(Call<UserFollowerStoryResponse> call, Response<UserFollowerStoryResponse> response) {
                    image.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        userStoryListResponse = response.body();
                        if (userStoryListResponse.getSuccess()) {
                            userStoryList.addAll(userStoryListResponse.getData().getRows());
                            getOwnUserStories();
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

    private void getOwnUserStories() {
        try {
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
//          header.put("x-token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJJZCI6IjNmZmI0MmQ5LWUxYjQtNGU0ZC04NzdjLTYwODRjMGZhMzYwNSIsImVtYWlsIjoiIiwiY3JlYXRlZEF0IjoiMjAyMC0xMC0xMlQxNDowMzoyMC41MTRaIn0sImlhdCI6MTYwMjUxMTQwMH0.zVS3c5B24b-r2PU6hYPANWwavs409IuLXPNPbcMLWdM");
            UserStoryParameter paramter = new UserStoryParameter(SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID));
//          UserStoryParameter paramter = new UserStoryParameter("946b4230-a87b-4712-afbc-355338c041ec");
            Call<UserFollowerStoryResponse> call = webApi.getOwnStories(header, paramter);
            call.enqueue(new Callback<UserFollowerStoryResponse>() {
                @Override
                public void onResponse(Call<UserFollowerStoryResponse> call, Response<UserFollowerStoryResponse> response) {
                    image.setVisibility(View.GONE);
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        userOwnStoryResponse = response.body();
                        if (userOwnStoryResponse != null) {
                            showStories();
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

    private void showStories() {
        if (adapter != null)
            adapter.notifyDataSetChanged(userStoryList);
        homeStoryAdapter = new HomeStoryAdapter(getActivity(), userStoryList, HomeFragments.this, userOwnStoryResponse);
        recyclerView_Story.setAdapter(homeStoryAdapter);
        if (userOwnStoryResponse.getData().getRows() != null) {
            for (int i = 0; i < userOwnStoryResponse.getData().getRows().size(); i++) {
                Glide.with(getActivity()).load(userOwnStoryResponse.getData().getRows().get(0).getUser().getDisplayPicture()).placeholder(R.drawable.chat_user_placeholder).into(ivStory);
            }
        } else {
            Glide.with(getActivity()).load("").placeholder(R.drawable.chat_user_placeholder).into(ivStory);

        }
    }

    @Override
    public void commentClicked(int position, String id, String caption, boolean isCommentAllowed, Boolean isAd, int comments) {
        openedPosition = position;
        try {
            Intent intent = new Intent(getActivity(), VideoCommentActivityNew.class);
            intent.putExtra("videoId", id);
            intent.putExtra("msg", caption);
            if (isAd) {
                intent.putExtra("commentCount", comments);
            } else {
                intent.putExtra("commentCount", feedArrayList.get(position).getFeedResponse().getCommentCounts());
            }
            intent.putExtra("isCommentAllowed", isCommentAllowed);
            intent.putExtra("svs", false);
            intent.putExtra("isAd", isAd);
            startActivityForResult(intent, COMMENT_OPEN_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void optionsClicked(int position) {
        if (feedArrayList.get(position).getFeedResponse().getUserId().equalsIgnoreCase(SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID))) {
            showOwnOption(position);
            return;
        }
        final GetReportResponse[] userSettings = new GetReportResponse[1];
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_home_feed, viewGroup, false);

        TextView txt_unfollow = dialogView.findViewById(R.id.txt_unfollow);
        TextView txt_mute = dialogView.findViewById(R.id.txt_mute);
        TextView txt_report = dialogView.findViewById(R.id.txt_report);
        TextView txt_notification = dialogView.findViewById(R.id.txt_notification);
        TextView txt_copyLink = dialogView.findViewById(R.id.txt_copyLink);
        TextView txt_shareTo = dialogView.findViewById(R.id.txt_shareTo);
        TextView txt_chat = dialogView.findViewById(R.id.txt_chat);


        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        Call<GetReportResponse> call = webApi1.postusersetting(map, feedArrayList.get(position).getFeedResponse().getId(),
                feedArrayList.get(position).getFeedResponse().getUserId());
        call.enqueue(new Callback<GetReportResponse>() {
            @Override
            public void onResponse(Call<GetReportResponse> call, Response<GetReportResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    if (response.body().getData() == null || response.body().getData().getIsMute() == null) {
                        // adding post settings
                        Call<ApiResponse> call2 = webApi1.updateuserpost(map,
                                feedArrayList.get(position).getFeedResponse().getId(),
                                feedArrayList.get(position).getFeedResponse().getUserId(),
                                true, false, "", false);
                        call2.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    image.setVisibility(GONE);
                                    optionsClicked(position);
                                } else {
                                    image.setVisibility(GONE);
                                    Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                image.setVisibility(GONE);
                                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                            }
                        });
                        return;
                    }
                    userSettings[0] = response.body();

                    alertDialog.show();
                    if (response.body().getData().getIsMute()) {
                        txt_mute.setText(getString(R.string.Unmute));
                    } else {
                        txt_mute.setText(getString(R.string.Mute));
                    }
                    if (response.body().getData().getPushNotification()) {

                        txt_notification.setText(getString(R.string.Turn_On_Post_Notification));
                    } else {
                        txt_notification.setText(getString(R.string.Turn_Off_Post_Notification));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetReportResponse> call, Throwable t) {
                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
            }
        });

        txt_copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                String copylink_postID = feedArrayList.get(position).getFeedResponse().getId();
                alertDialog.dismiss();
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.meest4bharat.com/?post=" + copylink_postID + "&userid=" + feedArrayList.get(position).getFeedResponse().getUserId()))
                        .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                        .buildShortDynamicLink()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", "====================" + e.getMessage());
                            }
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                String shareBody = getString(R.string.meest_share_content) + task.getResult().getShortLink();
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("", task.getResult().getShortLink().toString());
                                clipboard.setPrimaryClip(clip);
                                Utilss.showToast(getActivity(), getString(R.string.Link_copied), R.color.green);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.SOME_ERROR), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        txt_shareTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                alertDialog.dismiss();
                String postID = feedArrayList.get(position).getFeedResponse().getId();
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.meest4bharat.com/?post=" + postID + "&userid=" + feedArrayList.get(position).getFeedResponse().getUserId()))
                        .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                        .buildShortDynamicLink()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", "====================" + e.getMessage());
                            }
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                String shareBody = getString(R.string.meest_share_content) + task.getResult().getShortLink();
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Share_post));
                                share.putExtra(Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(share, getString(R.string.Share_post)));
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.Error_Creating_Link), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                c_userid = feedArrayList.get(position).getFeedResponse().getUserId();
                c_fName = feedArrayList.get(position).getFeedResponse().getUser().getFirstName();
                c_lName = feedArrayList.get(position).getFeedResponse().getUser().getLastName();
                c_username = feedArrayList.get(position).getFeedResponse().getUser().getUsername();
                c_profile_pic = feedArrayList.get(position).getFeedResponse().getUser().getThumbnail();
                Map<String, String> header = new HashMap<>();
                header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
                Map request = new HashMap();
                request.put("followingId", feedArrayList.get(position).getFeedResponse().getUserId());
                request.put("userId", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID));
                apiCallBack(getApi().checkFollowNChatHead(header, request));
            }
        });
        txt_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnFollowPeoplesParam unFollowPeoplesParam = new UnFollowPeoplesParam(feedArrayList.get(position).getFeedResponse().getUserId(), false);
                Call<FollowsPeoplesResponse> call1 = webApi1.Unfollow_peoples(map, unFollowPeoplesParam);
                call1.enqueue(new Callback<FollowsPeoplesResponse>() {
                    @Override
                    public void onResponse(Call<FollowsPeoplesResponse> call, Response<FollowsPeoplesResponse> response) {
                        // image.setVisibility(View.GONE);
                        try {
                            if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                Utilss.showToast(getActivity(), getString(R.string.Unfollow), R.color.green);
                                alertDialog.dismiss();
//                                refreshData();

                                swipRefresh = true;
                                pageno = 1;
                                homeFragmentFeedListingService(String.valueOf(pageno));

                            } else {
                                alertDialog.dismiss();
                                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                            }
                        } catch (Exception e) {
                            image.setVisibility(GONE);
                            e.printStackTrace();
                            alertDialog.dismiss();
                            Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<FollowsPeoplesResponse> call, Throwable t) {
                        Log.w("error", t);
                        alertDialog.dismiss();
                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                    }
                });
            }
        });


        txt_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ApiResponse> call = webApi1.updateuserpost(map,
                        feedArrayList.get(position).getFeedResponse().getId(),
                        feedArrayList.get(position).getFeedResponse().getUserId(), userSettings[0].getData().getPushNotification(),
                        !userSettings[0].getData().getIsMute(), userSettings[0].getData().getReport(),
                        userSettings[0].getData().getIsReportedByme());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                            alertDialog.dismiss();
                            String messageText = getString(R.string.post);
                            if (userSettings[0].getData().getIsMute()) {
                                messageText += getString(R.string.Unmute);
                            } else {
                                messageText += getString(R.string.Mute);
                            }
                            Utilss.showToast(getActivity(), messageText, R.color.green);
                            alertDialog.dismiss();
                        } else {
                            alertDialog.dismiss();
                            Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        alertDialog.dismiss();
                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                    }
                });
            }
        });

        txt_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ApiResponse> call = webApi1.updateuserpost(map,
                        feedArrayList.get(position).getFeedResponse().getId(),
                        feedArrayList.get(position).getFeedResponse().getUserId(), !userSettings[0].getData().getPushNotification(),
                        !userSettings[0].getData().getIsMute(), userSettings[0].getData().getReport(),
                        userSettings[0].getData().getIsReportedByme());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                            if (userSettings[0].getData().getPushNotification()) {
                                String messageoff = getString(R.string.Post_notification_turned) + " " + getString(R.string.off);
                                Utilss.showToast(getActivity(), messageoff, R.color.green);
                            } else {
                                String messageon = getString(R.string.Post_notification_turned) + " " + getString(R.string.on);
                                Utilss.showToast(getActivity(), messageon, R.color.green);
                            }

                            alertDialog.dismiss();
                        } else {
                            alertDialog.dismiss();
                            Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        alertDialog.dismiss();
                    }
                });
            }
        });

        txt_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final androidx.appcompat.app.AlertDialog.Builder builder = new
                        androidx.appcompat.app.AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
                final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_svs, null, false);

                builder.setView(dialogView);

                androidx.appcompat.app.AlertDialog alertDialogReport = builder.create();

                TextView report_option = dialogView.findViewById(R.id.report_option);
                ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
                RecyclerView report_recycler = dialogView.findViewById(R.id.report_recycler);

                report_option.setVisibility(GONE);
                progressBar.setVisibility(View.VISIBLE);

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Map<String, String> myHeader = new HashMap<>();
                myHeader.put("Accept", "application/json");
                myHeader.put("Content-Type", "application/json");
                myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));

                // body
                String type = "Post";
                HashMap<String, Object> body = new HashMap<>();
                body.put("reportType", type);
                body.put("reportedData", false);

                Call<ReportTypeResponse> call = webApi.getReportTypes(myHeader, body);
                call.enqueue(new Callback<ReportTypeResponse>() {
                    @Override
                    public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                        progressBar.setVisibility(GONE);

                        if (response.code() == 200 && response.body().getSuccess()) {
                            ReportAdapter reportAdapter = new ReportAdapter(getActivity(),
                                    response.body(), alertDialogReport, progressBar, feedArrayList.get(position).getFeedResponse().getId(), type);
                            report_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                            report_recycler.setAdapter(reportAdapter);

                            report_recycler.setVisibility(View.VISIBLE);
                        } else {
                            Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                            alertDialogReport.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                        progressBar.setVisibility(GONE);

                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        alertDialogReport.dismiss();
                    }
                });
                alertDialogReport.show();
            }
        });
    }

    private void showOwnOption(int positionInner) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_home_feed, viewGroup, false);

        TextView txt_unfollow = dialogView.findViewById(R.id.txt_unfollow);
        TextView txt_mute = dialogView.findViewById(R.id.txt_mute);
        TextView txt_report = dialogView.findViewById(R.id.txt_report);
        TextView txt_notification = dialogView.findViewById(R.id.txt_notification);
        TextView txt_copyLink = dialogView.findViewById(R.id.txt_copyLink);
        TextView txt_shareTo = dialogView.findViewById(R.id.txt_shareTo);
        TextView txt_delete = dialogView.findViewById(R.id.txt_delete);
        TextView txt_chat = dialogView.findViewById(R.id.txt_chat);


        txt_delete.setVisibility(View.VISIBLE);
        txt_unfollow.setVisibility(GONE);
        txt_mute.setVisibility(GONE);
        txt_report.setVisibility(GONE);
        txt_notification.setVisibility(GONE);
        txt_chat.setVisibility(GONE);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        txt_copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                String copylink_postID = feedArrayList.get(positionInner).getFeedResponse().getId();
                alertDialog.dismiss();
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.meest4bharat.com/?post=" + copylink_postID + "&userid=" + feedArrayList.get(positionInner).getFeedResponse().getUserId()))
                        .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                        .buildShortDynamicLink()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", "====================" + e.getMessage());
                            }
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                String shareBody = getString(R.string.meest_share_content) + task.getResult().getShortLink();
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("", task.getResult().getShortLink().toString());
                                clipboard.setPrimaryClip(clip);
                                Utilss.showToast(getActivity(), getString(R.string.Link_copied), R.color.green);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.SOME_ERROR), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        txt_shareTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                alertDialog.dismiss();
                String postID = feedArrayList.get(positionInner).getFeedResponse().getId();
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.meest4bharat.com/?post=" + postID + "&userid=" + feedArrayList.get(positionInner).getFeedResponse().getUserId()))
                        .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                        .buildShortDynamicLink()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", "====================" + e.getMessage());
                            }
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                String shareBody = getString(R.string.meest_share_content) + task.getResult().getShortLink();
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Share_post));
                                share.putExtra(Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(share, getString(R.string.Share_post)));
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.Error_Creating_Link), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
                final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.delete_post_popup, null, false);
                Button btYes, btNo;
                btYes = dialogView.findViewById(R.id.btYes);
                btNo = dialogView.findViewById(R.id.btNo);
                builder.setView(dialogView);
                final AlertDialog alertDialogDelete = builder.create();
                btYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogDelete.dismiss();
                        detetePost(feedArrayList.get(positionInner).getFeedResponse().getId(), positionInner, alertDialogDelete);
                        adapter.remove(positionInner);

                    }
                });
                btNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogDelete.dismiss();
                    }

                });

                alertDialogDelete.show();
            }
        });
        alertDialog.show();
    }

    @Override
    public void storyClicked(int position) {
        Intent intent = new Intent(getActivity(), StoryActivity.class);
        Bundle args = new Bundle();
        args.putSerializable("StoryResponse", (Serializable) userStoryListResponse);
        intent.putExtra("BUNDLE", args);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void myStoryClicked(int position) {
        try {
            if (userOwnStoryResponse != null && userOwnStoryResponse.getData() != null && userOwnStoryResponse.getData().getRows() != null
                    && userOwnStoryResponse.getData().getRows().size() > 0
                    && userOwnStoryResponse.getData().getRows().get(0).getCount() > 0) {
                Intent intent = new Intent(getActivity(), StoryActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("StoryResponse", (Serializable) userOwnStoryResponse);
                intent.putExtra("BUNDLE", args);
                intent.putExtra("iSUser", "yes");
                intent.putExtra("position", 0);
                startActivity(intent);
            } else {
                storyAdd();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void storyAddClicked(int position) {

    }

    public void storyAdd() {
        //   Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
        Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.create_status_activity);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        dialog.show();

        LinearLayout layout_text = dialog.findViewById(R.id.layout_text);
        LinearLayout layout_photo = dialog.findViewById(R.id.layout_photo);
        LinearLayout layout_video = dialog.findViewById(R.id.layout_video);
        ImageView img_close = dialog.findViewById(R.id.img_close);
        img_close.setOnClickListener(v -> {
            dialog.dismiss();
        });
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.layout_text: {
                        Intent intent = new Intent(getActivity(), WriteTextActivity.class);
                        startActivityForResult(intent, CREATE_STORY_CONST);
                        break;
                    }
                    case R.id.layout_photo: {
                        Intent intent = new Intent(getActivity(), MeestCameraActivity.class);
//                        Intent intent = new Intent(getActivity(), MeestMainCameraActivity.class);
                        intent.putExtra("call_type", "story");
                        intent.putExtra("filter", "Image");
                        intent.putExtra("isVideo", false);
                        startActivityForResult(intent, CREATE_STORY_CONST);
                        break;
                    }
                    case R.id.layout_video: {
                        Utilss.showToast(getActivity(), "Coming soon!!!", R.color.social_background_blue);
                       /*  Intent intent = new Intent(getActivity(), MeestMainCameraActivity.class);
                            intent.putExtra("call_type", "story");
                            intent.putExtra("filter", "Video");
                            intent.putExtra("isVideo", true);
                            startActivityForResult(intent, CREATE_STORY_CONST);*/
                        break;
                    }


                }
            }
        };
        layout_text.setOnClickListener(onClickListener);
        layout_photo.setOnClickListener(onClickListener);
        layout_video.setOnClickListener(onClickListener);


    }

    @Override
    public void onSignUpClicked(List<AdMediaData> mediaList, int position, String id) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == COMMENT_OPEN_CODE) {
                int commentCount = data.getExtras().getInt("commentCount", 0);
                Log.d("commentCount", "onActivityResult: " + commentCount);
//                feedArrayList.get(openedPosition).getFeedResponse().setCommentCounts(commentCount);
//                adapter.notifyItemChanged(openedPosition);
                HomeModel homeModel = feedArrayList.get(openedPosition);
                homeModel.getFeedResponse().setCommentCounts(commentCount);
                feedArrayList.set(openedPosition, homeModel);
                adapter.notifyItemChanged(openedPosition, homeModel);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void openTagsBottomSheet(List<FeedResponse.UserTags> list) {
        showBottomSheetDialog(list);
    }

    @Override
    public void openOtherUserProfile(String userId) {
//        getActivity().startActivity(new Intent(getActivity(), OtherUserActivity.class)
        getActivity().startActivity(new Intent(getActivity(), OtherUserAccount.class)
                .putExtra("userId", userId));
    }

    @Override
    public void likePost(String userId, TextView txt_like_txt, ImageView img_like, TextView txt_like, LinearLayout layout_likes_details,
                         TextView txt_recent_user_name, TextView text_others_like, TextView and_text, int position, LinearLayout layout_like_list) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
            map.put("Accept", "application/json");
            map.put("Content-Type", "application/json");

            WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

            InsertLikeParameters insertLikeParameters = new InsertLikeParameters(userId);
            Call<InsertPostLikeResponse> call1 = webApi1.insertLike(map, insertLikeParameters);
            call1.enqueue(new Callback<InsertPostLikeResponse>() {
                @Override
                public void onResponse(Call<InsertPostLikeResponse> call, Response<InsertPostLikeResponse> response) {
                    try {
                        if (response.code() == 200 && response.body().getSuccess()) {

                            if (feedArrayList.get(position).getFeedResponse().getLiked() == 0) {
                                txt_like_txt.setText(getString(R.string.Liked));
                                feedArrayList.get(position).getFeedResponse().setLikeCounts(response.body().getData().getLikeCount());
                                feedArrayList.get(position).getFeedResponse().setLiked(1);
                                img_like.setImageResource(R.drawable.like_diamond_filled);
                            } else {
                                txt_like_txt.setText(getString(R.string.Like));
                                feedArrayList.get(position).getFeedResponse().setLikeCounts(response.body().getData().getLikeCount());
                                feedArrayList.get(position).getFeedResponse().setLiked(0);
                                img_like.setImageResource(R.drawable.like_diamond);
                            }

                            // changing like counts and data
                            // changing like counts and data
                            final int li = response.body().getData().getLikeCount();
                            Log.d("onResponse:", String.valueOf(li));
                            if (li != 0) {
                                txt_like.setText("" + li);
                                txt_recent_user_name.setText(" " + response.body().getData().getRecentUsername() + " ");

                                if (li == 1) {
                                    text_others_like.setVisibility(GONE);
                                    and_text.setVisibility(GONE);
                                } else {
                                    text_others_like.setVisibility(View.VISIBLE);
                                    and_text.setVisibility(View.VISIBLE);
                                    text_others_like.setText(" " + (li - 1) + " " + getString(R.string.others));
                                }
                                layout_likes_details.setVisibility(View.VISIBLE);
                                layout_like_list.setVisibility(View.VISIBLE);
                            } else {
                                txt_like.setText("0");
                                layout_likes_details.setVisibility(GONE);
                                layout_like_list.setVisibility(GONE);
                            }
                        } else {
                            Utilss.showToast(getActivity(), response.body().getSuccess().toString(), R.color.msg_fail);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<InsertPostLikeResponse> call, Throwable t) {
                    Log.w("error", t);
                    Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void userCommentClicked(int position, String id, String caption, boolean isCommentAllowed, boolean isAd, int commentCount) {
        try {
            openedPosition = position;
            Intent intent = new Intent(getActivity(), VideoCommentActivityNew.class);
            intent.putExtra("videoId", id);
            intent.putExtra("msg", caption);
            intent.putExtra("isCommentAllowed", isCommentAllowed);
            intent.putExtra("commentCount", commentCount);
            intent.putExtra("svs", false);
            intent.putExtra("isAd", isAd);
            startActivityForResult(intent, COMMENT_OPEN_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shareClicked() {
        try {
            final String appPackageName = getActivity().getPackageName();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Checkout_this_awesome_app));
            intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
            getActivity().startActivity(Intent.createChooser(intent, getString(R.string.Share_Post)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sharePostClicked(int position) {
        try {
            String postUrl = feedArrayList.get(position).getFeedResponse().getThumbnail();
            try {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
                ViewGroup viewGroup = ((AppCompatActivity) getActivity()).findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog_reshare_post, viewGroup, false);
                Button okBtn = dialogView.findViewById(R.id.okBtn);
                ImageView ivPostView = dialogView.findViewById(R.id.ivPostView);
                EmojiconEditText etCaptions = dialogView.findViewById(R.id.etCaptions);
                ImageView ivCancel = dialogView.findViewById(R.id.ivCancel);
                ivCancel.setOnClickListener(v -> {
                    if (sendFeedbackAlertDialog != null && sendFeedbackAlertDialog.isShowing())
                        sendFeedbackAlertDialog.cancel();
                });

                CommonUtils.loadImage(ivPostView, postUrl, getActivity());

                Log.d("TESSSSSS", "sharePostClicked: " + postUrl);


                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String encodedText = CommonUtils.encodeEmoji(etCaptions.getText().toString().trim());
                        if (encodedText.length() == 0) {
                            etCaptions.setError(getResources().getString(R.string.enter_post_caption));
//                            Utilss.showToast(getActivity(), getResources().getString(R.string.enter_post_caption), R.color.msg_fail);
                        } else {
                            okBtn.setVisibility(View.INVISIBLE);
                            HashMap<String, String> stringHashMap = new HashMap<>();
                            stringHashMap.put("postId", feedArrayList.get(position).getFeedResponse().getId());
                            stringHashMap.put("userId", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID));
                            stringHashMap.put("caption", encodedText);
                            uploadUserFeedback(stringHashMap);
                            sendFeedbackAlertDialog.dismiss();
                        }

                    }

                });
                builder.setView(dialogView);
                sendFeedbackAlertDialog = builder.create();
                sendFeedbackAlertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadUserFeedback(HashMap<String, String> stringHashMap) {
        try {
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();

            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
            Call<UserFeedbackResponse> call = webApi.reSharePost(header, stringHashMap);

            call.enqueue(new Callback<UserFeedbackResponse>() {
                @Override
                public void onResponse(Call<UserFeedbackResponse> call, Response<UserFeedbackResponse> response) {

                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        pageno=1;
                        homeFragmentFeedListingService(String.valueOf(pageno));
                        postRecyclerView.scrollToPosition(0);
                        Utilss.showToast(getActivity(), getResources().getString(R.string.post_shared_wall), R.color.social_background_blue);
                    }
                }

                @Override
                public void onFailure(Call<UserFeedbackResponse> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveClicked(String postId, int position, ImageView img_save) {
        onsaveClick(position, img_save);
        try {
            Map<String, String> map = new HashMap<>();
            map.put("Accept", "application/json");
            map.put("Content-Type", "application/json");
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));

            WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

            HashMap<String, Object> body = new HashMap<>();
            body.put("postId", postId);
            body.put("status", true);

            Call<ApiResponse> call1 = webApi1.savePost(map, body);
            call1.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    try {
                        if (response.code() == 200 && response.body().getSuccess()) {
                            // userViewHolder.loading.setVisibility(GONE);
                            if (feedArrayList.get(position).getFeedResponse().getPostSaved() == 0) {
                                feedArrayList.get(position).getFeedResponse().setPostSaved(1);

                                img_save.setImageResource(R.drawable.ic_bookmark_fill);
                            } else {
                                feedArrayList.get(position).getFeedResponse().setPostSaved(0);
                                img_save.setImageResource(R.drawable.ic_bookmark_outline);
                            }
                        } else {
                            Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.w("error", t);
                    Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onsaveClick(int position, ImageView img_save) {

    }

    void getAD() {
        loading = false;
        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

//        HashMap<String, Object> body = new HashMap<>();

        Call<ShowFeedAdOne> call1 = webApi1.showAdvtFeedOne(map);
        call1.enqueue(new Callback<ShowFeedAdOne>() {
            @Override
            public void onResponse(Call<ShowFeedAdOne> call, Response<ShowFeedAdOne> response) {
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getData() != null && response.body().getSuccess()) {
                        HomeModel homeModel = new HomeModel();
                        homeModel.setAD(true);
                        homeModel.setShowAdvtResponse(response.body());
                        feedArrayList.add(homeModel);
                        if (feedArrayList.size() == (totalCount + (feedArrayList.size() / PaginationListenerHideUnhide.PAGE_SIZE_10))) {
                            isLastPage = true;
                        }
                        loading = true;
                        if (!isLastPage)
                            homeFragmentFeedListingService((++pageno) + "");
//                        showAdvtResponse=response.body();
////                       showAdAdapter = new ShowAdAdapter(getActivity(), showAdvtResponse);
//                       showAdAdapter.setHasStableIds(true);
//                        recyclerView_post.setAdapter(showAdAdapter);
//                        showAdAdapter.notifyDataSetChanged();

                    } else {
                        loading = true;
                        homeFragmentFeedListingService((++pageno) + "");
                    }
                } catch (Exception e) {
                    loading = true;
                    homeFragmentFeedListingService((++pageno) + "");
                    e.printStackTrace();
                    Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<ShowFeedAdOne> call, Throwable t) {
                loading = true;
                homeFragmentFeedListingService((++pageno) + "");
                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
            }
        });
    }

    private void detetePost(String postId, int innerPosition, AlertDialog alertDialog) {
        image.setVisibility(View.VISIBLE);
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("postId", postId);

        Call<DeleteStoryResponse> call = webApi.deletePostByID(header, body);
        call.enqueue(new Callback<DeleteStoryResponse>() {
            @Override
            public void onResponse(Call<DeleteStoryResponse> call, Response<DeleteStoryResponse> response) {
                image.setVisibility(GONE);
                alertDialog.dismiss();
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
//                    feedArrayList.remove(innerPosition);
//                    adapter.notifyItemChanged(innerPosition);
//                    adapter.notifyItemRangeRemoved((innerPosition), 1);
                } else {
                    Utilss.showToast(getActivity(), CommonUtils.COMMON_MSG, R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<DeleteStoryResponse> call, Throwable t) {
                image.setVisibility(GONE);
                Log.v("harsh", "t == " + t.getMessage());
                Utilss.showToast(getActivity(), getResources().getString(R.string.SOME_ERROR), R.color.grey);
                alertDialog.dismiss();
            }
        });

    }

    @Override
    public void menuClicked(int position) {

        final GetReportResponse[] userSettings = new GetReportResponse[1];

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_home_feed, viewGroup, false);

        TextView txt_unfollow = dialogView.findViewById(R.id.txt_unfollow);
        TextView txt_mute = dialogView.findViewById(R.id.txt_mute);
        TextView txt_report = dialogView.findViewById(R.id.txt_report);

        TextView txt_notification = dialogView.findViewById(R.id.txt_notification);
        TextView txt_copyLink = dialogView.findViewById(R.id.txt_copyLink);
        TextView txt_shareTo = dialogView.findViewById(R.id.txt_shareTo);
        TextView txt_delete = dialogView.findViewById(R.id.txt_delete);
        TextView txt_chat = dialogView.findViewById(R.id.txt_chat);
        txt_delete.setVisibility(GONE);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
        String ownId = SharedPrefreances.getSharedPreferenceString(getActivity(),
                SharedPrefreances.ID);
        if (feedArrayList.get(position).getFeedResponse().getUserId().equalsIgnoreCase(ownId)) {
            txt_report.setVisibility(GONE);
            txt_notification.setVisibility(GONE);
            txt_unfollow.setVisibility(GONE);
            txt_mute.setVisibility(GONE);
            txt_chat.setVisibility(GONE);
            txt_delete.setVisibility(View.VISIBLE);
        }

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        Call<GetReportResponse> call = webApi1.postusersetting(map, feedArrayList.get(position).getFeedResponse().getId(),
                feedArrayList.get(position).getFeedResponse().getUserId());
        call.enqueue(new Callback<GetReportResponse>() {
            @Override
            public void onResponse(Call<GetReportResponse> call, Response<GetReportResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    if (response.body().getData() == null || response.body().getData().getIsMute() == null) {
                        // adding post settings
                        Call<ApiResponse> call2 = webApi1.updateuserpost(map,
                                feedArrayList.get(position).getFeedResponse().getId(),
                                feedArrayList.get(position).getFeedResponse().getUserId(),
                                true, false, "", false);
                        call2.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    optionsClicked(position);
                                    alertDialog.dismiss();
                                } else {

                                    Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                                    alertDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {

                                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                            }
                        });
                        return;
                    }
                    userSettings[0] = response.body();

                    alertDialog.show();
                    if (response.body().getData().getIsMute()) {
                        txt_mute.setText(getString(R.string.Unmute));
                    } else {
                        txt_mute.setText(getString(R.string.Mute));
                    }
                    if (response.body().getData().getPushNotification()) {
                        txt_notification.setText(getString(R.string.Turn_Off_Post_Notification));
                    } else {
                        txt_notification.setText(getString(R.string.Turn_On_Post_Notification));
                    }
                }
            }

            @Override
            public void onFailure(Call<GetReportResponse> call, Throwable t) {
                image.setVisibility(GONE);
                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
            }
        });

        txt_copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                String copylink_postID = feedArrayList.get(position).getFeedResponse().getId();
                alertDialog.dismiss();
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.meest4bharat.com/?post=" + copylink_postID + "&userid=" + feedArrayList.get(position).getFeedResponse().getUserId()))
                        .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                        .buildShortDynamicLink()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", "====================" + e.getMessage());
                            }
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                String shareBody = getString(R.string.meest_share_content) + task.getResult().getShortLink();
                                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("", task.getResult().getShortLink().toString());
                                clipboard.setPrimaryClip(clip);
                                Utilss.showToast(getActivity(), getString(R.string.Link_copied), R.color.green);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.SOME_ERROR), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        txt_shareTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                alertDialog.dismiss();
                String postID = feedArrayList.get(position).getFeedResponse().getId();
                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://www.meest4bharat.com/?post=" + postID + "&userid=" + feedArrayList.get(position).getFeedResponse().getUserId()))
                        .setDomainUriPrefix(Const.deepLinkingUriPrefix)
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .setIosParameters(new DynamicLink.IosParameters.Builder(Const.iosBundleId).build())
                        .buildShortDynamicLink()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Tag", "====================" + e.getMessage());
                            }
                        })
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                String shareBody = getString(R.string.meest_share_content) + task.getResult().getShortLink();
                                share.setType("text/plain");
                                share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.Share_post));
                                share.putExtra(Intent.EXTRA_TEXT, shareBody);
                                startActivity(Intent.createChooser(share, getString(R.string.Share_post)));
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.Error_Creating_Link), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
                final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.delete_post_popup, null, false);
                Button btYes, btNo;
                btYes = dialogView.findViewById(R.id.btYes);
                btNo = dialogView.findViewById(R.id.btNo);
                builder.setView(dialogView);
                final AlertDialog alertDialogDelete = builder.create();
                btYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogDelete.dismiss();
                        detetePost(feedArrayList.get(position).getFeedResponse().getId(), position, alertDialogDelete);
                        adapter.remove(position);
                    }
                });
                btNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogDelete.dismiss();
                    }
                });

                alertDialogDelete.show();
            }
        });


        txt_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UnFollowPeoplesParam unFollowPeoplesParam = new UnFollowPeoplesParam(feedArrayList.get(position).getFeedResponse().getUserId(), false);
                Call<FollowsPeoplesResponse> call1 = webApi1.Unfollow_peoples(map, unFollowPeoplesParam);
                call1.enqueue(new Callback<FollowsPeoplesResponse>() {
                    @Override
                    public void onResponse(Call<FollowsPeoplesResponse> call, Response<FollowsPeoplesResponse> response) {
                        // image.setVisibility(View.GONE);
                        try {
                            if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                Utilss.showToast(getActivity(), getString(R.string.Unfollow), R.color.green);
                                alertDialog.dismiss();
//                                refreshData();

                                swipRefresh = true;
                                pageno = 1;
                                homeFragmentFeedListingService(String.valueOf(pageno));

                            } else {
                                alertDialog.dismiss();
                                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                            }
                        } catch (Exception e) {
                            image.setVisibility(GONE);
                            e.printStackTrace();
                            alertDialog.dismiss();
                            Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<FollowsPeoplesResponse> call, Throwable t) {
                        Log.w("error", t);
                        alertDialog.dismiss();
                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                    }
                });

            }
        });

        txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                   /* Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("from_push", "0");
                    intent.putExtra("type", "0");
                    startActivity(intent);*/
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                startActivity(intent);
            }
        });


        txt_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ApiResponse> call = webApi1.updateuserpost(map,
                        feedArrayList.get(position).getFeedResponse().getId(),
                        feedArrayList.get(position).getFeedResponse().getUserId(), userSettings[0].getData().getPushNotification(),
                        !userSettings[0].getData().getIsMute(), userSettings[0].getData().getReport(),
                        userSettings[0].getData().getIsReportedByme());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                            alertDialog.dismiss();
                            String messageText = "Post";
                            if (userSettings[0].getData().getIsMute()) {
                                messageText += getString(R.string.Unmute);
                            } else {
                                messageText += getString(R.string.Mute);
                            }
                            Utilss.showToast(getActivity(), messageText, R.color.green);
                            alertDialog.dismiss();
                        } else {
                            alertDialog.dismiss();
                            Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        alertDialog.dismiss();
                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                    }
                });
            }
        });

        txt_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ApiResponse> call = webApi1.updateuserpost(map,
                        feedArrayList.get(position).getFeedResponse().getId(),
                        feedArrayList.get(position).getFeedResponse().getUserId(), !userSettings[0].getData().getPushNotification(),
                        !userSettings[0].getData().getIsMute(), userSettings[0].getData().getReport(),
                        userSettings[0].getData().getIsReportedByme());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                            String messageText = getString(R.string.Post_notification_turned);
                            if (userSettings[0].getData().getPushNotification()) {
                                String messageOn = messageText + " " + getString(R.string.off);
                                Utilss.showToast(getActivity(), messageOn, R.color.green);

                            } else {
                                String messageOff = messageText + " " + getString(R.string.on);
                                Utilss.showToast(getActivity(), messageOff, R.color.green);

                            }
                            alertDialog.dismiss();
                        } else {
                            alertDialog.dismiss();
                            Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                        alertDialog.dismiss();
                    }
                });
            }
        });

        txt_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                callReport(position);
            }
        });

    }

    private void callUpdateFollowStatus(int cuurentposition) {
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);
        UnFollowPeoplesParam unFollowPeoplesParam = new UnFollowPeoplesParam(feedArrayList.get(cuurentposition).getFeedResponse().getUserId(), false);
        Call<FollowsPeoplesResponse> call1 = webApi1.Unfollow_peoples(map, unFollowPeoplesParam);
        call1.enqueue(new Callback<FollowsPeoplesResponse>() {
            @Override
            public void onResponse(Call<FollowsPeoplesResponse> call, Response<FollowsPeoplesResponse> response) {
                // image.setVisibility(View.GONE);
                try {
                    if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                        Utilss.showToast(getActivity(), getString(R.string.Unfollow), R.color.green);
//                                refreshData();

                        swipRefresh = true;
                        pageno = 1;
                        homeFragmentFeedListingService(String.valueOf(pageno));

                    } else {

                        Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                    }
                } catch (Exception e) {
                    image.setVisibility(GONE);
                    e.printStackTrace();

                    Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<FollowsPeoplesResponse> call, Throwable t) {
                Log.w("error", t);
                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
            }
        });
    }

    public boolean pushFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragments(), tag)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();
            return true;
        }
        return false;
    }

    private void callReport(int reportPosition) {
        final androidx.appcompat.app.AlertDialog.Builder builder = new
                androidx.appcompat.app.AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        final View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_svs, null, false);

        builder.setView(dialogView);

        androidx.appcompat.app.AlertDialog alertDialogReport = builder.create();

        TextView report_option = dialogView.findViewById(R.id.report_option);
        ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
        RecyclerView report_recycler = dialogView.findViewById(R.id.report_recycler);

        report_option.setVisibility(GONE);
        progressBar.setVisibility(View.VISIBLE);

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> myHeader = new HashMap<>();
        myHeader.put("Accept", "application/json");
        myHeader.put("Content-Type", "application/json");
        myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));

        String type = "Post";
        HashMap<String, Object> body = new HashMap<>();
        body.put("reportType", type);
        body.put("reportedData", false);


        Call<ReportTypeResponse> call = webApi.getReportTypes(myHeader, body);
        call.enqueue(new Callback<ReportTypeResponse>() {
            @Override
            public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                progressBar.setVisibility(GONE);

                if (response.code() == 200 && response.body().getSuccess()) {
                    ReportAdapter reportAdapter = new ReportAdapter(getActivity(),
                            response.body(), alertDialogReport, progressBar, feedArrayList.get(reportPosition).getFeedResponse().getId(), type);
                    report_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    report_recycler.setAdapter(reportAdapter);

                    report_recycler.setVisibility(View.VISIBLE);
                } else {
                    Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                    alertDialogReport.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                progressBar.setVisibility(GONE);

                Utilss.showToast(getActivity(), getString(R.string.SOME_ERROR), R.color.grey);
                alertDialogReport.dismiss();
            }
        });
        alertDialogReport.show();
    }

    @Override
    public void onLongClicked(String id, TextView txt_like_txt, ImageView img_like, TextView txt_like, LinearLayout layout_likes_details, TextView txt_recent_user_name, TextView text_others_like, TextView and_text, int position, ImageView img_save) {
        //Toast.makeText(getActivity(), "Current-Post" + position, Toast.LENGTH_SHORT).show();
//        HomeAnimDialog.display(((AppCompatActivity) getActivity()).getSupportFragmentManager(), this,id, txt_like_txt,img_like,txt_like,layout_likes_details,txt_recent_user_name,text_others_like,and_text,position, img_save);
    }



    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(getActivity())) {
            swipRefresh = true;
            pageno = 1;
            fetchCurrentUserData();
            homeFragmentFeedListingService(String.valueOf(pageno));
        } else {
            ConnectionUtils.showNoConnectionDialog(getActivity(), this::onRetry);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (Permission.hasPermissions(getActivity(), permissions)) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        Intent intent = new Intent(getActivity(), MeestCameraActivity.class);
                        intent.putExtra("call_type", "story");
                        intent.putExtra("filter", "Image");
                        intent.putExtra("isVideo", false);
                        CameraUtil.comeFrom = "Meest";
                        startActivityForResult(intent, CREATE_STORY_CONST);
                    }
                } else {
                        checkPermission();
                    }

            } else {
                Constant.buildAlertForPermission(getActivity());
            }
        }
    }

    @Override
    public void onHomeAnimClick(ButtonPosition animPosotion, String id, TextView txt_like_txt, ImageView img_like, TextView txt_like, LinearLayout layout_likes_details, TextView txt_recent_user_name, TextView text_others_like, TextView and_text, int postPosition, HomeAnimDialog homeAnimDialog, ImageView img_save) {
        String ownId = SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID);
        if (animPosotion == null)
            return;

        switch (animPosotion) {
            case ORIGIN:
                break;
            case LEFT:
            case RIGHT_DOWN:
                Toast.makeText(getActivity(), getString(R.string.comming_soon), Toast.LENGTH_LONG).show();
                homeAnimDialog.dismiss();
                break;
            case RIGHT:
//                Toast.makeText(getActivity(),"Unfollow your post",Toast.LENGTH_LONG).show();
                if (!feedArrayList.get(postPosition).getFeedResponse().getUserId().equalsIgnoreCase(ownId)) {
                    callUpdateFollowStatus(postPosition);
                } else {
                    Utilss.showToast(getActivity(), getString(R.string.You_cant_follow_yourself), R.color.red);
                }

                homeAnimDialog.dismiss();
                break;
            case UP:
//                Toast.makeText(getActivity(),"like your post",Toast.LENGTH_LONG).show();

                // likePost(id,txt_like_txt,img_like,txt_like,layout_likes_details,txt_recent_user_name,text_others_like,and_text,postPosition);
                homeAnimDialog.dismiss();
                break;
            case DOWN:

//                Toast.makeText(getActivity(),"Share your post",Toast.LENGTH_LONG).show();

                sharePostClicked(postPosition);
                homeAnimDialog.dismiss();
                break;
            case LEFT_UP:
//                Toast.makeText(getActivity(),"Download your post",Toast.LENGTH_LONG).show();
                saveClicked(id, postPosition, img_save);
                homeAnimDialog.dismiss();
                break;
            case LEFT_DOWN:
//                Toast.makeText(getActivity(),"Comment your post",Toast.LENGTH_LONG).show();
                userCommentClicked(postPosition, id, feedArrayList.get(postPosition).getFeedResponse().getCaption(), feedArrayList.get(postPosition).getFeedResponse().getAllowComment(), feedArrayList.get(postPosition).getAD(), feedArrayList.get(postPosition).getFeedResponse().getCommentCounts());
                homeAnimDialog.dismiss();
                break;
            case RIGHT_UP:

//                Toast.makeText(getActivity(),"Report your post",Toast.LENGTH_LONG).show();

                if (!feedArrayList.get(postPosition).getFeedResponse().getUserId().equalsIgnoreCase(ownId)) {
                    callReport(postPosition);
                } else {
                    Utilss.showToast(getActivity(), getString(R.string.Report_on_own_post_not_allowed), R.color.red);
                }
                homeAnimDialog.dismiss();
                break;


        }
    }

    private void onError(Throwable throwable) {

    }

    private void handleError(Throwable throwable) {

    }

    public void setChatNotificationCount(int count) {
        if (count == 0)
            tvChatNotificationCount.setVisibility(GONE);
        else if (count >= 100) {
            tvChatNotificationCount.setText("99+");
            tvChatNotificationCount.setVisibility(View.VISIBLE);
        } else {
            tvChatNotificationCount.setText("" + count);
            tvChatNotificationCount.setVisibility(View.VISIBLE);
        }
    }

    void dialogMessage() {
//        dialog = new Dialog(this);
//        dialog.getWindow().requestFeature(1);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
//        dialog.setContentView(R.layout.dialog_message);
//        dialog.setCancelable(true);
//        dialog.show();
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        EditText message = dialog.findViewById(R.id.message);
        Button submit = dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (message.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.enter_message), Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessageApi(message.getText().toString(), true);
            }
        });
    }

    void sendMessageApi(String msg, boolean status) {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> myHeader = new HashMap<>();
        myHeader.put("Accept", "application/json");
        myHeader.put("Content-Type", "application/json");
        myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));


        HashMap<String, Object> body = new HashMap<>();
        body.put("friendId", c_userid);
        body.put("status", status);
        body.put("msg", msg);

        Call<MessageResponse> call = webApi.message(myHeader, body);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.body() != null) {
                    if (response.code() == 200 && response.body().isSuccess()) {

                        createChatHeadApi(msg, true, false);
//                        createChatHeadApi();
                    } else {
                        Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
                    }
                } else {
                    Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
                }

            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    void createChatHeadApi(String msg, boolean isRequest, boolean isAccpeted) {
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getContext(), SharedPrefreances.AUTH_TOKEN));
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map request = new HashMap();
        request.put("toUserId", c_userid);
        request.put("isAccepted", isAccpeted);
        Call<CreateGroupResponse> apiCall = webApi.createChatHeads2(header, request);
        apiCall.enqueue(new Callback<CreateGroupResponse>() {
            @Override
            public void onResponse(Call<CreateGroupResponse> call, Response<CreateGroupResponse> response) {
                try {
                    if (isRequest) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID));
                            jsonObject.put("toUserId", c_userid);
                            jsonObject.put("chatHeadId", response.body().getData().getId());
                            jsonObject.put("msg", msg);
                            jsonObject.put("lastMsg", msg);
                            jsonObject.put("isGroup", false);
                            jsonObject.put("attachment", false);
                            jsonObject.put("attachmentType", "Message");
                            jsonObject.put("toReplyId", "");
                            mSocket.emit("send", jsonObject);
                            Toast.makeText(getActivity(), getString(R.string.success), Toast.LENGTH_SHORT).show();
                            Log.e("onSentClick", jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (response.code() == 200) {
                            Log.e("TAG", "responseooooo=" + response.body());
                            Intent intent = new Intent(getActivity(), ChatBoatActivity.class);
                            intent.putExtra("user_id_to", c_userid);
                            intent.putExtra("chatHeadId", response.body().getData().getId());
                            intent.putExtra("from_push", "0");
                            intent.putExtra("isSingle", true);
                            intent.putExtra("firstName", c_fName);
                            intent.putExtra("lastName", c_lName);
                            intent.putExtra("username", c_username);
                            intent.putExtra("profilePicture", c_profile_pic);
                            startActivity(intent);
                        } else {
                            Utilss.showToast(getActivity(), response.body().getSuccess().toString(), R.color.msg_fail);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CreateGroupResponse> call, Throwable t) {
                Utilss.showToast(getActivity(), getString(R.string.error_msg), R.color.msg_fail);
                Log.e("TAG", "maintaerrororoororooo=" + t.getMessage());
            }
        });
    }

    @Subscribe
    public void checkHeadNFollow(CheckChatHeadNFollowResponse1 response) {
        if (response.isSuccess()) {
            if (response.getData().getData().equals("following")) {
                createChatHeadApi("", false, true);
            } else if (response.getData().getData().equals("chatHead")) {
                Intent intent = new Intent(getActivity(), ChatBoatActivity.class);
                intent.putExtra("user_id_to", c_userid);
                intent.putExtra("chatHeadId", response.getData().getChatHeadId());
                intent.putExtra("from_push", "0");
                intent.putExtra("isSingle", true);
                intent.putExtra("firstName", c_fName);
                intent.putExtra("lastName", c_lName);
                intent.putExtra("username", c_username);
                intent.putExtra("profilePicture", c_profile_pic);
                startActivity(intent);
            } else {
                dialogMessage();
            }
        } else {
            dialogMessage();
        }
    }

    @Override
    public void itemClicked(int position) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("Exoplayer",hidden+"onHiddenChanged"+masterExoPlayerHelper.isMute());
        if (hidden){
            masterExoPlayerHelper.getExoPlayerHelper().mute();
        }else {
            if(!masterExoPlayerHelper.isMute()){
                masterExoPlayerHelper.getExoPlayerHelper().unMute();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (masterExoPlayerHelper != null) {
//            masterExoPlayerHelper = new MasterExoPlayerHelper(getActivity(), R.id.frame, true, 0.5f, MuteStrategy.ALL, true, false, 1, Integer.MAX_VALUE);
//            masterExoPlayerHelper.makeLifeCycleAware(this);
//        }


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("post_upload");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (previousPlayerNew != null)
            pausePlayer(previousPlayerNew);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        if (previousPlayerNew != null)
            releaseExoPlayer(previousPlayerNew);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (previousPlayerNew != null)
            pausePlayer(previousPlayerNew);
    }

    @Override
    public void onResume() {
        super.onResume();
        instanceHomeFragment = this;
        if (previousPlayerNew != null) {
            startPlayer(previousPlayerNew);
        }        //  initSocket();
        fetchCurrentUserData();
//        feedArrayList.clear();
//        homeFragmentFeedListingService(String.valueOf(pageno));


    }

    //    Emitter.Listener onConnect = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            if (getActivity() == null)
//                return;
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.w("objjj", args.toString());
//                    JSONObject data = (JSONObject) args[0];
//                    try {
//                        Log.w("demodemo", data.getString("msg"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("userId", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.ID));
//                        jsonObject.put("name", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.F_NAME) + " " + SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.L_NAME));
//                        jsonObject.put("isGroup", false);
//                        mSocket.emit("createSession", jsonObject);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        }
//    };

//    private Emitter.Listener session = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    try {
//                        username = data.getString("msg");
//                        Log.w("session", username);
//                    } catch (JSONException e) {
//                        Log.e("TAGUserJoin", e.getMessage());
//                        return;
//                    }
//                }
//            });
//        }
//    };

}