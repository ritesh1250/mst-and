package com.meest.social.socialViewModel.viewModel.otherUserAccountViewModel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.social.socialViewModel.adapter.OtherUserCameraAdapter1;
import com.meest.social.socialViewModel.model.ProfilesVideoResponse1;
import com.meest.videomvvmmodule.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OtherUserCameraFragViewModel extends ViewModel {
    public ArrayList<ProfilesVideoResponse1.Row> arrayList = new ArrayList<>();
    public OtherUserCameraAdapter1 adapter = new OtherUserCameraAdapter1();
    Context context;
    FragmentActivity activity;
    public String userId;
    public boolean setDataFlag = false;
    public CompositeDisposable disposable = new CompositeDisposable();
    private static final String TAG = "OtherUserCameraFragView";
    public MutableLiveData<Boolean> loaderVisibility = new MutableLiveData<>();
    public MutableLiveData<Boolean> tvNoDataVis = new MutableLiveData<>();
    public MutableLiveData<Boolean> recyV = new MutableLiveData<>();
    public List<ProfilesVideoResponse1.Row> feedArrayList = new ArrayList<>();


    public OtherUserCameraFragViewModel(Context context, FragmentActivity activity, String userId) {
        this.context = context;
        this.activity = activity;
        this.userId = userId;

    }

    public void fetchData(boolean loadMore, int pageNo) {
        arrayList.clear();
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("pageNumber", String.valueOf(pageNo));
        body.put("pageSize", "27");
        body.put("userId", userId);
        disposable.add(Global.initSocialRetrofit().tagUserMedia(map, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> loaderVisibility.setValue(true))
                .doOnTerminate(() -> loaderVisibility.setValue(false))
                .subscribe((ProfilesVideoResponse1 response, Throwable throwable) -> {
                    loaderVisibility.setValue(false);
//                    if (response.getCode() == 1) {
//                        if (isLoadMore)
//                            adapter.loadMore(response.getData().getRows());
//                        else {
//                            if (response.getData().getRows().size() == 0) {
//                                tvNoDataVis.setValue(true);
//                                recyV.setValue(false);
//                            } else
//                                adapter.setData(response.getData().getRows());
//                        }
//                      /*  if (response.getData().getRows().size() == 0) {
//                            tvNoDataVis.setValue(true);
//                        }*/
//                        // arrayList.addAll(response.getData().getRows());
//                        //adapter.notifyDataSetChanged();
//                    } else {
//                        Utilss.showToast(context, context.getString(R.string.SOME_ERROR), R.color.grey);
//                    }
                    if (response.getCode() == 1) {
                        if (response.getData().getRows().size() > 0) {
                            feedArrayList.addAll(response.getData().getRows());
                            if (!loadMore) {
                                adapter.update(response.getData().getRows());
                            } else {
                                adapter.loadMore(response.getData().getRows());
                            }
                        } else {
                            if (feedArrayList.size() == 0) {
//                            binding.tvNoData.setVisibility(View.VISIBLE);
                                tvNoDataVis.setValue(true);
                            }
                        }
                    }
                    else {
                        Utilss.showToast(context, context.getString(R.string.SOME_ERROR), R.color.grey);
                    }
                }));
    }

    @SuppressLint("WrongConstant")
    public void adapterFunc() {
        adapter.setOnItemClickListener(position -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
            ViewGroup viewGroup = activity.findViewById(android.R.id.content);
            final View dialogView = LayoutInflater.from(activity).inflate(R.layout.video_player_layout, viewGroup, false);
            TextView txt_usernsme = dialogView.findViewById(R.id.txt_usernsme);
            CircleImageView img_profile = dialogView.findViewById(R.id.img_profile);
            ImageView tagged_post_image = dialogView.findViewById(R.id.iv_tagDialog);
            PlayerView playerView = dialogView.findViewById(R.id.videoView);
            String str = arrayList.get(position).getPosts().get(0).getPost();
            if (!(str.substring(str.length() - 4).equalsIgnoreCase(".mp4"))) {
                playerView.setVisibility(View.GONE);
                tagged_post_image.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(arrayList.get(position).getPosts().get(0).getPost())
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(tagged_post_image);
            } else {
                playerView.setVisibility(View.VISIBLE);
                tagged_post_image.setVisibility(View.GONE);
            }
            txt_usernsme.setText(SharedPrefreances.getSharedPreferenceString(context, SharedPrefreances.Reg_USERNAME));
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "tunemist"));
            final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            // for mp4 media
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(arrayList.get(position).getPosts().get(0).getPost()));
            // for hls media
//        MediaSource mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(item.getVideoURL()));
            player.prepare(mediaSource);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            //player.addListener(getActivity());
            playerView.setPlayer(player);
            playerView.setUseController(false);
            player.setPlayWhenReady(true);
            //  playerView.set
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH | AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
            builder.setView(dialogView);
            final AlertDialog alertDialog = builder.create();
            alertDialog.setOnDismissListener(dialogInterface -> {
                player.stop();
                alertDialog.dismiss();
            });
            alertDialog.show();
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
