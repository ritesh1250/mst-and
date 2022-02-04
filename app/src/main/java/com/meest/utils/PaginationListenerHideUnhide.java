package com.meest.utils;


import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meest.Fragments.HomeFragments;

public abstract class PaginationListenerHideUnhide extends RecyclerView.OnScrollListener {

    public static final int PAGE_START = 1;

    @NonNull
    private LinearLayoutManager layoutManager;
    public static final int PAGE_SIZE_40 = 40;
    public static final int PAGE_SIZE_30 = 30;
    public static final int PAGE_SIZE_20 = 20;
    public static final int PAGE_SIZE_10 = 10;
    public static final int PAGE_SIZE_15 = 15;
    public static final int PAGE_SIZE_5 = 5;
    View view;
    private int PAGE_SIZE = 0;
    private Long timeStamp = 0L;

    static int y;

    private int lastFirstVisibleItem;

    /**
     * Supporting only LinearLayoutManager for now.
     */
    public PaginationListenerHideUnhide(@NonNull LinearLayoutManager layoutManager, int PAGE_SIZE, View view) {
        this.layoutManager = layoutManager;
        this.PAGE_SIZE = PAGE_SIZE;
        this.view = view;
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
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                if (System.currentTimeMillis() - timeStamp > 1000) {
                    timeStamp = System.currentTimeMillis();
                    loadMoreItems();

                }
            }
        }
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        System.out.println("click_position dx "+ dx +"  dy  "+dy+"  position "+firstVisiblePosition);
      //  recyclerView.getLayoutManager().getPosition(view);
        if (firstVisiblePosition  == 0) {
                view.setVisibility(View.VISIBLE);
        }else if (firstVisibleItemPosition>1){
                view.setVisibility(View.GONE);
        }
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
            // Do something
        } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            // Do something
        } else {
            // Do something
        }
    }

    public abstract void loadMoreItems();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}