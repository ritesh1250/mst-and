package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.CollectionAdpter;
import com.meest.Interfaces.CollectionInterface;
import com.meest.meestbhart.view.notification.NotificationSocialFeedActivity;
import com.meest.R;
import com.meest.responses.SavedDataResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meest.Fragments.HomeFragments.PAGE_SIZE;

public class ColletionActivity extends AppCompatActivity implements CollectionInterface {
    private ImageView img_back_arroe_help;
    RecyclerView recyclerView;
    CollectionAdpter adapter;
    ImageView backArrow;
    TextView noRecordsTV;
    SavedDataResponse userResponse;
    private LottieAnimationView loading;

    private static final int PAGE_SIZE = 20;
    int pagePerRecord = 10;
    int pageno = 1;
    boolean Loading = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_activity);

        initView();

        loading.setAnimation("loading.json");
        loading.playAnimation();
        loading.loop(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        showHide(false);
        getDataFromServer(String.valueOf(pageno));

        recyclerView.addOnScrollListener(new PaginationListener1((LinearLayoutManager) recyclerView.getLayoutManager()) {
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
    }

    private void initView() {
        loading =findViewById(R.id.loading);
        backArrow = findViewById(R.id.img_back_arroe_help);
        recyclerView = findViewById(R.id.recyclerView);
        noRecordsTV = findViewById(R.id.noRecordsTV);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setAdapter(List<SavedDataResponse.Row> rows) {
        if (rows.size() > 0) {
            showHide(true);
            adapter = new CollectionAdpter(rows, this, this);
            recyclerView.setAdapter(adapter);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            showHide(false);

        }

    }

    private void showHide(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.VISIBLE);
            noRecordsTV.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noRecordsTV.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
        }
    }

    public void getDataFromServer(String pageno) {
        try {
            final WebApi webApi = ApiUtils.getClientHeader(this);
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));


            HashMap<String, Object> body = new HashMap<>();
            body.put("pageNumber", pageno);
            body.put("pageSize", String.valueOf(pagePerRecord));

            Call<SavedDataResponse> call = webApi.getSavedData(map, body);
            call.enqueue(new Callback<SavedDataResponse>() {
                @Override
                public void onResponse(Call<SavedDataResponse> call, Response<SavedDataResponse> response) {
                    if (response.code()==200 && response.body().getSuccess() && response.body()!=null) {

                        userResponse = response.body();
                        setAdapter(userResponse.getData().getRows());
                        loading.setVisibility(View.GONE);
                    }
                    else{
                        loading.setVisibility(View.GONE);
                        noRecordsTV.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<SavedDataResponse> call, Throwable t) {
                    System.out.println(t.getStackTrace());
                    loading.setVisibility(View.GONE);
                    noRecordsTV.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            getDataFromServer(String.valueOf(pageno));
        }
    }

    @Override
    public void collectionClicked(int position) {
        Intent intent = new Intent(ColletionActivity.this, NotificationSocialFeedActivity.class);
        intent.putExtra("userId", userResponse.getData().getRows().get(position).getUserId());
        intent.putExtra("postId", userResponse.getData().getRows().get(position).getPostId());
        intent.putExtra("position", position);
        startActivityForResult(intent, 12);
    }
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
