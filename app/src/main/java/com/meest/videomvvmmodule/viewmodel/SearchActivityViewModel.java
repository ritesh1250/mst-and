package com.meest.videomvvmmodule.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.meest.R;
import com.meest.databinding.ItemSearchUserBinding;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.videomvvmmodule.adapter.ExploreHashTagAdapter;
import com.meest.videomvvmmodule.adapter.HashTagNameAdapter;
import com.meest.videomvvmmodule.adapter.SearchUserAdapter;
import com.meest.videomvvmmodule.adapter.VideoListAdapter;
import com.meest.videomvvmmodule.model.elasticsearch.ElasticSearchRespone;
import com.meest.videomvvmmodule.model.user.RestResponse;
import com.meest.videomvvmmodule.model.user.SearchUser;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.utils.SessionManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivityViewModel extends ViewModel {
    public SessionManager sessionManager;
    public ObservableInt searchtype = new ObservableInt(0);
    //    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<String> toast = new MutableLiveData<>();
    public MutableLiveData<Boolean> onLoadMoreComplete = new MutableLiveData<>();
    public MutableLiveData<RestResponse> followApi = new MutableLiveData<>();
    public String search_text;
    public int userStart = 0;
    public int videoStart = 0;
    public int hashtagStart = 0;
    public int exploreStart = 0;
    public int count_view = 0;
    public ObservableBoolean noUserData = new ObservableBoolean(false);
    public ObservableBoolean noVideoData = new ObservableBoolean(false);
    public SearchUserAdapter searchUseradapter = new SearchUserAdapter();
    //    public VideoListAdapter videoListAdapter = new VideoListAdapter();
    public VideoListAdapter videoListAdapter ;
    public HashTagNameAdapter adapter = new HashTagNameAdapter();
    public ExploreHashTagAdapter exploreHagTagAdapter = new ExploreHashTagAdapter();
    //    public HashTagNameAdapter adapter = new HashTagNameAdapter();
//    public ObservableBoolean isloading = new ObservableBoolean(true);
    int count = 26;
    private CompositeDisposable disposable = new CompositeDisposable();
    public ObservableBoolean isFollowLoading = new ObservableBoolean(false);
    public MutableLiveData<Boolean> isloading = new MutableLiveData<>();
    public CustomDialogBuilder customDialogBuilder;


    public void searchForUser(boolean isLoadMore) {
        disposable.add(Global.initRetrofit().searchUser(Global.apikey, Global.userId, search_text, count, userStart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isloading.setValue(false);
                })
                .subscribe((searchUser, throwable) -> {
                    if (searchUser != null && searchUser.getData() != null) {
                        if (isLoadMore) {
                            searchUseradapter.loadMore(searchUser.getData());
                        } else {
                            searchUseradapter.updateData(searchUser.getData());
                        }
                        userStart = userStart + count;
                    }
                    noUserData.set(searchUseradapter.getmList().isEmpty());
                }));
    }

    public void searchForVideos(boolean isLoadMore) {

        disposable.add(Global.initRetrofit().searchVideo(Global.apikey, search_text, count, videoStart, Global.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnTerminate(() -> onLoadMoreComplete.setValue(true))
                .subscribe((searchVideo, throwable) -> {
                    if (searchVideo != null && searchVideo.getData() != null) {
                        if (isLoadMore) {
                            videoListAdapter.loadMore(searchVideo.getData());
                        } else {
                            videoListAdapter.updateData(searchVideo.getData());
                        }
                        isloading.setValue(false);
                        videoStart = videoStart + count;
                    }
                    noVideoData.set(videoListAdapter.getmList().isEmpty());
                }));
    }
//    public void fetchExploreItems(boolean isLoadMore) {

    public void fetchHashTagName(boolean isLoadMore) {
        Log.e("Name Hashtag========", "true");
        disposable.add(Global.initRetrofit().getExploreVideos(Global.apikey, 20, hashtagStart, Global.userId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isloading.setValue(false);
                })
                .subscribe((explore, throwable) -> {
                    if (explore != null && explore.getData() != null) {
                        if (isLoadMore) {
                            adapter.loadMore(explore.getData());
                        } else {
                            adapter.updateData(explore.getData());
                        }
                        hashtagStart = hashtagStart + count;
                    }
                }));
    }

    public void fetchExploreItems(boolean isLoadMore) {
        Log.e("Explore Hashtag========", "true");
        disposable.add(Global.initRetrofit().getExploreVideos(Global.apikey, count, exploreStart, Global.userId, search_text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable1 -> isloading.setValue(true))
                .doOnTerminate(() -> {
                    onLoadMoreComplete.setValue(true);
                    isloading.setValue(false);
                })
                .subscribe((explore, throwable) -> {
                    if (explore != null && explore.getData() != null) {
                        if (isLoadMore) {
                            exploreHagTagAdapter.loadMore(explore.getData());
                        } else {
                            if (!new Gson().toJson(explore.getData()).equals(new Gson().toJson(exploreHagTagAdapter.getmList()))) {
                                exploreHagTagAdapter.updateData(explore.getData());
                            }
                        }
                        exploreStart = exploreStart + count;
                    }
                }));
    }

    public void afterTextChanged(CharSequence s) {
        search_text = s.toString();
        if(search_text.contains("#")){
            search_text = search_text.replace("#","");
        }
        switch (searchtype.get()) {
            case 0:
                userStart = 0;
                searchForUser(false);
                break;
            case 1:
                exploreStart = 0;
                fetchExploreItems(false);
                break;
            case 2:
                videoStart = 0;
                searchForVideos(false);
                break;
        }
    }


    public void onUserLoadMore() {
        searchForUser(true);
    }

    public void onVideoLoadMore() {
        searchForVideos(true);
    }

    public void onHashtagLoadMore() {
        fetchExploreItems(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void followUnfollow(String userId, Context context, int position, List<SearchUser.User> mList, int type, ItemSearchUserBinding binding) {
        customDialogBuilder = new CustomDialogBuilder(context);
        customDialogBuilder.showLoadingDialog();
        disposable.add(Global.initRetrofit().followUnFollow(Global.apikey, Global.userId, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe((followRequest, throwable) -> {
                    if (followRequest != null && followRequest.getStatus() != null) {
                        customDialogBuilder.hideLoadingDialog();
                        //Toast.makeText(context, followRequest.getMessage(), Toast.LENGTH_SHORT).show();
                        if (followRequest.getMessage().equals("Follow Successfully")) {
                            Toast.makeText(context, context.getResources().getString(R.string.Follow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, true);
                        } else if (followRequest.getMessage().equals("Unfollow Successfully")) {
                            Toast.makeText(context, context.getResources().getString(R.string.Unfollow_Successfully), Toast.LENGTH_SHORT).show();
                            Global.followUnfollow.put(userId, false);
                        }

                        Log.e("@@@@@@@@@@@@@", "Folllo_unflow : " + followRequest.getMessage());
                        if (type == 1) {
                            mList.get(position).setFriend_or_not("friend");
                            binding.follow.setVisibility(View.GONE);
                            binding.unFollow.setVisibility(View.VISIBLE);
                        } else {
                            mList.get(position).setFriend_or_not("notfriend");
                            binding.follow.setVisibility(View.VISIBLE);
                            binding.unFollow.setVisibility(View.GONE);
                        }
                        followApi.setValue(followRequest);
                    }
                }));
    }

    //    ========================elastic search area========================================
    private void searchUserElasticSearch(boolean isLoadMore) {

//      String url = Const.BASE_URL_ELASTIC_SEARCH + "/medlyusers/_search?q="+search_text+"*";
        String url = "http://search:meest123@13.127.143.209:9200/medlyusers/_search?q=" + search_text + "*";
        Log.e("TAG", "searchUserElasticSearch: " + url);


        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<ElasticSearchRespone> call = webApi.userElasticSearch(url);

        call.enqueue(new Callback<ElasticSearchRespone>() {
            @Override
            public void onResponse(Call<ElasticSearchRespone> call, Response<ElasticSearchRespone> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getHits() != null && response.body().getHits().getHits() != null && response.body().getHits().getHits().size() > 0) {
                            Log.e("TAG", "===============onResponse: " + response.body().getHits().getHits().get(0).getSource().getFirstName());
                        }
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ElasticSearchRespone> call, Throwable t) {

            }
        });
    }
}
