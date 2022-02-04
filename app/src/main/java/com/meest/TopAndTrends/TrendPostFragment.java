package com.meest.TopAndTrends;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import com.meest.Adapters.OtherPostAdapter;
import com.meest.Interfaces.FeedDataCallback;
import com.meest.model.AdMediaData;
import com.meest.Paramaters.UnFollowPeoplesParam;
import com.meest.R;
import com.meest.responses.FeedResponse;
import com.meest.responses.FollowsPeoplesResponse;
import com.meest.responses.GetReportResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.view.comment.VideoCommentActivityNew;
import com.meest.svs.adapters.ReportAdapter;
import com.meest.meestbhart.login.model.ApiResponse;
//import com.meest.svs.models.ReportTypeResponse;
import com.meest.social.socialViewModel.model.ReportTypeResponse;
import com.master.exoplayer.MasterExoPlayerHelper;
import com.master.exoplayer.MuteStrategy;
import com.master.exoplayer.PlayStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meest.Fragments.HomeFragments.PAGE_SIZE;


public class TrendPostFragment extends Fragment implements FeedDataCallback {

    RecyclerView recyclerTrendPost;
    String type;
    private Context context;
    ArrayList<FeedResponse.Row> feedArrayList = new ArrayList<>();
    LottieAnimationView loading;
    //    HomeAdapter homeAdapter;
    OtherPostAdapter userPostAdapter;
    private boolean isTrending = true;
    private int COMMENT_OPEN_CODE = 30, openedPosition = 0;
    View view;
    private MasterExoPlayerHelper masterExoPlayerHelper;

    int pastVisibleItemCount = 0;
    int totalItemCount = 0;
    int visibleItemCount = 0;
    int pagePerRecord = 20;
    int pageno = 1;
    boolean Loading = true;
    int totalCount = 0;
    public static boolean isLastPage = false;


    public TrendPostFragment(String type, Context context, boolean isTrending) {
        this.type = type;
        this.context = context;
        this.isTrending = isTrending;
    }

    LinearLayoutManager manager = new LinearLayoutManager(context);

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


    @Override
    public void onSignUpClicked(List<AdMediaData> mediaList, int position, String id) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trend_post, container, false);
        recyclerTrendPost = view.findViewById(R.id.recyclerTrendPost);
        loading = view.findViewById(R.id.loading);

        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);

        setFeedsRecycler();

        fetchData(String.valueOf(pageno));

        recyclerTrendPost.setLayoutManager(new LinearLayoutManager(context));
        recyclerTrendPost.setHasFixedSize(true);

        recyclerTrendPost.addOnScrollListener(new PaginationListener((LinearLayoutManager) recyclerTrendPost.getLayoutManager()) {
            @Override
            public void loadMoreItems() {
                System.out.println("==PAGINATION STARTED " + pageno);
                if (Loading) {
                    Loading = false;
                    pageno = pageno + 1;
                    fetchData(String.valueOf(pageno));
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

        return view;
    }

    private void setFeedsRecycler() {
        try {
            recyclerTrendPost.setNestedScrollingEnabled(false);
            masterExoPlayerHelper = new MasterExoPlayerHelper(getActivity(), R.id.frame, true, PlayStrategy.DEFAULT,
                    MuteStrategy.ALL, false, false, 0, Integer.MAX_VALUE);
            masterExoPlayerHelper.makeLifeCycleAware(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchData(String pageno) {

        loading.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("Accept", "application/json");
        map.put("Content-Type", "application/json");
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<FeedResponse> call;

        HashMap<String, Object> body = new HashMap<>();
        body.put("pageNumber", pageno);
        body.put("pageSize", String.valueOf(pagePerRecord));

        if (type.equals("post")) {
            if (isTrending) {
                body.put("key", "days");
                body.put("value", 30);
                call = webApi.trend_post(map, body);
            } else {
                body.put("key", "months");
                body.put("value", 10);
                call = webApi.top_post(map, body);
            }
        } else if (type.equals("image")) {
            if (isTrending) {
                body.put("key", "days");
                body.put("value", 30);
                call = webApi.trend_images(map, body);
            } else {
                body.put("key", "months");
                body.put("value", 10);
                call = webApi.top_images(map, body);
            }
        } else {
            if (isTrending) {
                body.put("key", "days");
                body.put("value", 30);

                call = webApi.trend_videos(map, body);
            } else {
                body.put("key", "months");
                body.put("value", 10);
                call = webApi.top_videos(map, body);
            }
        }

        call.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                loading.setVisibility(View.GONE);
                try {
                    if (response.code() == 200 && response.body().getSuccess() && response.body().getData() != null) {
                        if (response.body().getData().getRows().size() == 0) {
                            Toast.makeText(getContext(), getString(R.string.no_data_available), Toast.LENGTH_SHORT).show();
                        }
                        feedArrayList.addAll(response.body().getData().getRows());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        recyclerTrendPost.setLayoutManager(layoutManager);
                        recyclerTrendPost.setHasFixedSize(false);
//                        ArrayList<HomeModel> list = new ArrayList();
//                        for(FeedResponse.Row i :feedArrayList){
//                            HomeModel homeModel= new HomeModel();
//                            homeModel.setAD(false);
//                            homeModel.setFeedResponse(i);
//                            list.add(homeModel);
//                        }
                        userPostAdapter = new OtherPostAdapter(feedArrayList, masterExoPlayerHelper, getContext(), null);
                        recyclerTrendPost.setAdapter(userPostAdapter);
                    } else {
                        Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                    }

                } catch (Exception e) {
                    loading.setVisibility(View.GONE);
                    Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Log.w("error", t);
                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                //Utilss.showToast(context,"Server Error", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void commentClicked(int position, String id, String caption, boolean isCommentAllowed, Boolean isAD, int count) {
        openedPosition = position;

        Intent intent = new Intent(context, VideoCommentActivityNew.class);
        intent.putExtra("videoId", id);
        intent.putExtra("msg", caption);
        intent.putExtra("commentCount", feedArrayList.get(position).getCommentCounts());
        intent.putExtra("isCommentAllowed", isCommentAllowed);
        intent.putExtra("svs", false);
        startActivityForResult(intent, COMMENT_OPEN_CODE);
    }

    @Override
    public void optionsClicked(int position) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

        final GetReportResponse[] userSettings = new GetReportResponse[1];

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = view.findViewById(android.R.id.content);

        final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_home_feed, viewGroup, false);

        TextView txt_unfollow = dialogView.findViewById(R.id.txt_unfollow);
        TextView txt_mute = dialogView.findViewById(R.id.txt_mute);
        TextView txt_report = dialogView.findViewById(R.id.txt_report);
        TextView txt_notification = dialogView.findViewById(R.id.txt_notification);
        TextView txt_copyLink = dialogView.findViewById(R.id.txt_copyLink);
        TextView txt_shareTo = dialogView.findViewById(R.id.txt_shareTo);
        txt_notification.setVisibility(View.GONE);
        txt_unfollow.setVisibility(View.GONE);
        txt_mute.setVisibility(View.GONE);

        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(getActivity(), SharedPrefreances.AUTH_TOKEN));

        WebApi webApi1 = ApiUtils.getClient().create(WebApi.class);

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();

        progressDialog.show();
        Call<GetReportResponse> call = webApi1.postusersetting(map,
                feedArrayList.get(position).getId(),
                feedArrayList.get(position).getUserId());
        call.enqueue(new Callback<GetReportResponse>() {
            @Override
            public void onResponse(Call<GetReportResponse> call, Response<GetReportResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                    if (response.body().getData() == null || response.body().getData().getIsMute() == null) {
                        // adding post settings
                        Call<ApiResponse> call2 = webApi1.updateuserpost(map,
                                feedArrayList.get(position).getId(),
                                feedArrayList.get(position).getUserId(),
                                true, false, "", false);
                        call2.enqueue(new Callback<ApiResponse>() {
                            @Override
                            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                    progressDialog.dismiss();
                                    optionsClicked(position);
                                } else {
                                    progressDialog.dismiss();
                                    Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiResponse> call, Throwable t) {
                                progressDialog.dismiss();
                                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                            }
                        });
                        return;
                    }
                    userSettings[0] = response.body();
                    progressDialog.dismiss();
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
                progressDialog.dismiss();
                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
            }
        });

        txt_copyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = context.getPackageName();
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("", "https://play.google.com/store/apps/details?id=" + appPackageName);
                clipboard.setPrimaryClip(clip);

                alertDialog.dismiss();
                Utilss.showToast(getContext(), getString(R.string.Link_copied), R.color.green);
            }
        });

        txt_shareTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = context.getPackageName();

                alertDialog.dismiss();

                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Checkout this awesome app");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
                startActivity(Intent.createChooser(intent, "Share link"));
            }
        });

        txt_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnFollowPeoplesParam unFollowPeoplesParam = new UnFollowPeoplesParam(feedArrayList.get(position).getUserId(), false);
                Call<FollowsPeoplesResponse> call1 = webApi1.Unfollow_peoples(map, unFollowPeoplesParam);
                call1.enqueue(new Callback<FollowsPeoplesResponse>() {
                    @Override
                    public void onResponse(Call<FollowsPeoplesResponse> call, Response<FollowsPeoplesResponse> response) {
                        // image.setVisibility(View.GONE);
                        try {
                            if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                                Utilss.showToast(getContext(), getString(R.string.unfollow), R.color.green);
                                alertDialog.dismiss();
                            } else {
                                alertDialog.dismiss();
                                Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            alertDialog.dismiss();
                            Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<FollowsPeoplesResponse> call, Throwable t) {
                        Log.w("error", t);
                        alertDialog.dismiss();
                        Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });

        txt_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ApiResponse> call = webApi1.updateuserpost(map,
                        feedArrayList.get(position).getId(),
                        feedArrayList.get(position).getUserId(), userSettings[0].getData().getPushNotification(),
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
                            Utilss.showToast(getContext(), messageText, R.color.green);
                            alertDialog.dismiss();
                        } else {
                            alertDialog.dismiss();
                            Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        alertDialog.dismiss();
                        Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                    }
                });
            }
        });

        txt_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ApiResponse> call = webApi1.updateuserpost(map,
                        feedArrayList.get(position).getId(),
                        feedArrayList.get(position).getUserId(), !userSettings[0].getData().getPushNotification(),
                        !userSettings[0].getData().getIsMute(), userSettings[0].getData().getReport(),
                        userSettings[0].getData().getIsReportedByme());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.code() == 200 && response.body() != null && response.body().getSuccess()) {
                            String messageText = getString(R.string.Post_notification_turned);
                            if (userSettings[0].getData().getPushNotification()) {
                                messageText += getString(R.string.off);
                            } else {
                                messageText += getString(R.string.on);
                            }
                            Utilss.showToast(getContext(), messageText, R.color.green);
                            alertDialog.dismiss();
                        } else {
                            alertDialog.dismiss();
                            Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Utilss.showToast(getContext(), getString(R.string.error_msg), R.color.grey);
                        alertDialog.dismiss();
                    }
                });
            }
        });

        txt_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new
                        androidx.appcompat.app.AlertDialog.Builder(context, R.style.CustomAlertDialog);
                final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_svs, null, false);

                builder.setView(dialogView);

                androidx.appcompat.app.AlertDialog alertDialog = builder.create();

                TextView report_option = dialogView.findViewById(R.id.report_option);
                ProgressBar progressBar = dialogView.findViewById(R.id.progress_bar);
                RecyclerView report_recycler = dialogView.findViewById(R.id.report_recycler);

                report_option.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Map<String, String> myHeader = new HashMap<>();
                myHeader.put("Accept", "application/json");
                myHeader.put("Content-Type", "application/json");
                myHeader.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));

                // body
                String type = "Post";
                HashMap<String, Object> body = new HashMap<>();
                body.put("reportType", type);
                body.put("reportedData", false);

                Call<ReportTypeResponse> call = webApi.getReportTypes(myHeader, body);
                call.enqueue(new Callback<ReportTypeResponse>() {
                    @Override
                    public void onResponse(Call<ReportTypeResponse> call, Response<ReportTypeResponse> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.code() == 200 && response.body().getSuccess()) {
                            ReportAdapter reportAdapter = new ReportAdapter(context,
                                    response.body(), alertDialog, progressBar,
                                    feedArrayList.get(position).getId(), type);
                            report_recycler.setLayoutManager(new LinearLayoutManager(context));
                            report_recycler.setAdapter(reportAdapter);

                            report_recycler.setVisibility(View.VISIBLE);
                        } else {
                            Utilss.showToast(context, getString(R.string.error_msg), R.color.grey);
                            alertDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportTypeResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);

                        Utilss.showToast(context, getString(R.string.error_msg), R.color.grey);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    public void storyClicked(int position) {

    }

    @Override
    public void myStoryClicked(int position) {

    }

    @Override
    public void storyAddClicked(int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMMENT_OPEN_CODE && userPostAdapter != null && data != null) {
            int commentCount = data.getExtras().getInt("commentCount", 0);
            feedArrayList.get(openedPosition).setCommentCounts(commentCount);
            userPostAdapter.notifyItemChanged(openedPosition);
        }
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