package com.meest.social.socialViewModel.view.home.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.meest.Interfaces.CollectionInterface;
import com.meest.R;
import com.meest.databinding.SocialMyBookmarksBinding;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.responses.SavedDataResponse;
import com.meest.social.socialViewModel.viewModel.navigationViewModel.MyBookmarksviewModel;

import java.util.ArrayList;
import java.util.List;


public class MyBookmarks extends AppCompatActivity implements CollectionInterface {

    MyBookmarksviewModel myBookmarksviewModel;
    SocialMyBookmarksBinding socialMyBookmarksBinding;
    List<SavedDataResponse.Row> collectionList= new ArrayList<>();;
    private static final int PAGE_SIZE = 20;
    int pagePerRecord = 10;
    int pageno = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socialMyBookmarksBinding= DataBindingUtil.setContentView(this, R.layout.social_my_bookmarks);
        myBookmarksviewModel=new ViewModelProvider(this).get(MyBookmarksviewModel.class);
        socialMyBookmarksBinding.setSocialMyBookmarksBinding(myBookmarksviewModel);
        initView();
        initListeners();
        initObserve();
    }



    private void initView() {
        socialMyBookmarksBinding.loading.setAnimation("loading.json");
        socialMyBookmarksBinding.loading.playAnimation();
        socialMyBookmarksBinding.loading.loop(true);
        myBookmarksviewModel.getDataFromServer(String.valueOf(pageno),MyBookmarks.this);
        socialMyBookmarksBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        socialMyBookmarksBinding.recyclerView.setHasFixedSize(false);
    }
    private void initListeners() {
        socialMyBookmarksBinding.recyclerView.addOnScrollListener(new PaginationListener1((LinearLayoutManager) socialMyBookmarksBinding.recyclerView.getLayoutManager()) {
            @Override
            public void loadMoreItems() {
                System.out.println("==PAGINATION STARTED " + pageno);
                if (myBookmarksviewModel.isLoading.get()) {
                    myBookmarksviewModel.isLoading.set(false);
                    Log.e("PAGINATION STARTED", "under if");
                    myBookmarksviewModel.getDataFromServer(String.valueOf(pageno),MyBookmarks.this);
                }
            }

            @Override
            public boolean isLastPage() {
                myBookmarksviewModel.isLastPage.set(false);
                return  myBookmarksviewModel.isLastPage.get();
            }

            @Override
            public boolean isLoading() {
                myBookmarksviewModel.isLoading.set(false);
                return myBookmarksviewModel.isLoading.get();
            }
        });

        socialMyBookmarksBinding.imgBackArroeHelp.setOnClickListener(v -> {finish();});
    }
    private void initObserve() {
       myBookmarksviewModel.saveDataresponse.observe(this,rows -> {
           collectionList.addAll(rows);
           RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
           socialMyBookmarksBinding.recyclerView.setHasFixedSize(false);
           socialMyBookmarksBinding.recyclerView.setLayoutManager(mLayoutManager);
           myBookmarksviewModel.adapterModel.addLoading(rows,this::collectionClicked);
       } );


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            int pageno = 1;
            myBookmarksviewModel.getDataFromServer(String.valueOf(pageno),MyBookmarks.this);
        }
    }

    @Override
    public void collectionClicked(int position) {
        Intent intent = new Intent(MyBookmarks.this, NotificationSocialFeedActivity.class);
        intent.putExtra("userId", collectionList.get(position).getUserId());
        intent.putExtra("postId", collectionList.get(position).getPostId());
        intent.putExtra("position", position);
        startActivity(intent);

    }

    abstract class PaginationListener1 extends RecyclerView.OnScrollListener {

        public static final int PAGE_START = 1;

        @NonNull
        private LinearLayoutManager layoutManager;
        /**
         * Set scrolling threshold here (for now i'm assuming 10 item in one page)
         */

        private Long timeStamp = 0L;
        /**
         * Supporting only LinearLayoutManager for now.
         */
        public PaginationListener1(@NonNull LinearLayoutManager layoutManager) {
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
}