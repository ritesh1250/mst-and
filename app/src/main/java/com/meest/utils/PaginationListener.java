package com.meest.utils;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class PaginationListener extends RecyclerView.OnScrollListener {

    public static final int PAGE_START = 1;

    @NonNull
    private RecyclerView.LayoutManager layoutManager;

    /**
     * Set scrolling threshold here (for now i'm assuming 10 item in one page)
     */
    public static final int PAGE_SIZE_40 = 40;
    public static final int PAGE_SIZE_30 = 30;
    public static final int PAGE_SIZE_20 = 20;
    public static final int PAGE_SIZE_10 = 10;
    public static final int PAGE_SIZE_15 = 15;
    public static final int PAGE_SIZE_5 = 5;
    private int PAGE_SIZE = 0;
    private Long timeStamp = 0L;

    /**
     * Supporting only LinearLayoutManager for now.
     */
    public PaginationListener(@NonNull LinearLayoutManager layoutManager, int PAGE_SIZE) {
        this.layoutManager = layoutManager;
        this.PAGE_SIZE = PAGE_SIZE;
    }

    public PaginationListener(@NonNull GridLayoutManager layoutManager, int PAGE_SIZE) {
        this.layoutManager = layoutManager;
        this.PAGE_SIZE = PAGE_SIZE;
    }

    public PaginationListener(@NonNull StaggeredGridLayoutManager layoutManager, int PAGE_SIZE) {
        this.layoutManager = layoutManager;
        this.PAGE_SIZE = PAGE_SIZE;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = 0;

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] firstVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                // get maximum element within the list
                firstVisibleItemPosition = firstVisibleItemPositions[0];

            } else if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();

            } else if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
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


    public abstract class PaginationGridListener extends RecyclerView.OnScrollListener {
        public static final int PAGE_START = 1;
        @NonNull
        private GridLayoutManager layoutManager;
        /**
         * Set scrolling threshold here (for now i'm assuming 10 item in one page)
         */
        private static final int PAGE_SIZE = 10;

        /**
         * Supporting only LinearLayoutManager for now.
         */
        public PaginationGridListener(@NonNull GridLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    loadMoreItems();
                }
            }
        }

        protected abstract void loadMoreItems();

        public abstract boolean isLastPage();

        public abstract boolean isLoading();
    }
}