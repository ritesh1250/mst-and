package com.meest.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.UserActivityAdapter;
import com.meest.R;
import com.meest.responses.UserActivityResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.meest.Activities.PaginationListener.PAGE_START;

public class ActivityOption extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserActivityAdapter adapter;
    private RelativeLayout backArrowIV;
    TextView noRecordsTV;
    LottieAnimationView image;
    int pagePerRecord = 10;
    int pageno = 1;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private int totalPage = 0;
    private boolean isLoading = false;
    LinearLayoutManager manager ;
    List<UserActivityResponse.Row> userResponseList ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        initViews();
        manager=  new LinearLayoutManager(this);
        userResponseList=new ArrayList<>();
        showHide(false);

//        adapter = new UserActivityAdapter(userResponseList, this);
//        recyclerView.setAdapter(adapter);
        getDataFromServer(String.valueOf(pageno));

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);


        recyclerView.addOnScrollListener(new PaginationListener(manager)
        {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;

                if (userResponseList.size()<=totalPage)
                getDataFromServer(String.valueOf(currentPage));
            }
            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
            @Override
            public boolean isLoading() {
                return isLoading;
            }
        }
        );

    }

    private void showHide(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.VISIBLE);
            noRecordsTV.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            noRecordsTV.setVisibility(View.VISIBLE);
        }
    }

    private void showHideLoader(boolean showLoader) {
        if (showLoader) {
            if (image.getVisibility() != View.VISIBLE) {
                image.setVisibility(View.VISIBLE);
            }
        } else {
            image.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        image = findViewById(R.id.loading);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);
        recyclerView = findViewById(R.id.recyclerView);
        noRecordsTV = findViewById(R.id.noRecordsTV);
        backArrowIV = findViewById(R.id.img_back_arroe_help);
        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setAdapter(List<UserActivityResponse.Row> rows) {
        if (rows.size() > 0) {
            showHide(true);

        } else {
            showHide(false);
        }
    }

    private void getDataFromServer(String pageno) {
        try {
            final WebApi webApi = ApiUtils.getClientHeader(this);
            Map<String, String> map = new HashMap<>();
            map.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
            map.put("Accept", "application/json");
            map.put("Content-Type", "application/json");

            HashMap<String, Object> body = new HashMap<>();
            body.put("pageNumber", pageno);
            body.put("pageSize", String.valueOf(pagePerRecord));

            Call<UserActivityResponse> call = webApi.getActivityHistory(map, body);
            showHideLoader(true);
            call.enqueue(new Callback<UserActivityResponse>() {
                @Override
                public void onResponse(Call<UserActivityResponse> call, Response<UserActivityResponse> response) {
                    if (response.code() == 200 && response.body().getSuccess() && response.body() != null) {

                        showHideLoader(false);
                        UserActivityResponse userResponse = response.body();
                        totalPage=response.body().getData().getCount();
                        adapter.addLoading(userResponse.getData().getRows());
                        adapter.notifyDataSetChanged();
                        showHide(true);
                        if (userResponseList.size()== totalPage) {
                            isLastPage = true;
                        } else {
                            isLastPage = false;
                        }
                        isLoading = false;

                    } else {
                        showHideLoader(false);
                    }

                }

                @Override
                public void onFailure(Call<UserActivityResponse> call, Throwable t) {
                    showHideLoader(false);
                    System.out.println(t.getStackTrace());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
    private static final int PAGE_SIZE = 10;
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

abstract class PaginationGridListener extends RecyclerView.OnScrollListener {
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

