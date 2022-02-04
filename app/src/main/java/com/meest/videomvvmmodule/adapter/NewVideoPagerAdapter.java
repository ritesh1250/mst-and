package com.meest.videomvvmmodule.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.view.home.AdsFragment;
import com.meest.videomvvmmodule.view.home.PlayerFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;

public class NewVideoPagerAdapter extends FragmentStateAdapter {


    private List<Video.Data> dataList = new ArrayList<>();

    VideoPagerAdapter.OnCallApi onCallApi;

    VideoFullAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick;

    public List<Video.Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Video.Data> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return this.dataList.size();
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (this.dataList.get(position) == null) {
            return new AdsFragment();
        } else {
            if (position + 3 > this.dataList.size()) {
                VideoPagerAdapter.OnCallApi var10000 = this.onCallApi;
                if (var10000 != null) {
                    var10000.callApi();
                }
            }

//            Intrinsics.checkNotNullExpressionValue(var2, "PlayerFragment.newInstan…erViewItemClick,position)");
            return PlayerFragment.newInstance(this.dataList.get(position), this.onRecyclerViewItemClick, position);
        }
    }

    public final void updateData(@NotNull ArrayList dataList) {
        Intrinsics.checkNotNullParameter(dataList, "dataList");
        this.dataList.clear();
        this.dataList = dataList;
        this.notifyDataSetChanged();
    }

    public final void loadMore(@NotNull List data) {
        Intrinsics.checkNotNullParameter(data, "data");
        this.dataList.addAll((Collection) data);
        this.notifyDataSetChanged();
    }

    @Nullable
    public final VideoPagerAdapter.OnCallApi getOnCallApi() {
        return this.onCallApi;
    }

    public void setOnCallApi(VideoPagerAdapter.OnCallApi onCallApi) {
        this.onCallApi = onCallApi;
    }

    @Nullable
    public final VideoFullAdapter.OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return this.onRecyclerViewItemClick;
    }

    public interface OnCallApi {
        void callApi();
    }

    public NewVideoPagerAdapter(@NotNull FragmentActivity fragment, @Nullable VideoPagerAdapter.OnCallApi onCallApi, @Nullable VideoFullAdapter.OnRecyclerViewItemClick onRecyclerViewItemClick) {
        super(fragment);
        this.onCallApi = onCallApi;
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
        this.dataList = new ArrayList();
    }
}
