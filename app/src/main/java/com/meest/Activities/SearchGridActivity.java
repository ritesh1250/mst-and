package com.meest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.meest.Adapters.SearchPhotoGridAdapter;
import com.meest.R;
import com.meest.responses.HashtagSearchResponse;
import com.meest.social.socialViewModel.model.TopResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.networkcheck.ConnectionUtils;
import com.meest.networkcheck.OnNoInternetRetry;
import com.meest.social.socialViewModel.view.SearchActivityNew;
import com.meest.svs.adapters.HashtagNameAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchGridActivity extends AppCompatActivity implements OnNoInternetRetry {

    private static final String TAG = "SearchGridActivity";

    private String currentFragName;
    int pageno = 1;
    ImageView back_arrow;
    private EditText edit_data_find;
    private RecyclerView recyclerView, recyclerViewname;
    private TextView tvNoData;
    private SearchPhotoGridAdapter adapter;
    private StaggeredGridLayoutManager mLayoutManager;
    private HashtagNameAdapter hashtagNameAdapter;
    private static List<HashtagSearchResponse.Datum> datahashTagList;
    private List<TopResponse.Datum> rowsFeedMain;
    private int currentPage = 1;
    public static boolean isLastPage = false;
    private static int totalCount = -1;
    private boolean isLoading = false;
    public static final int PAGE_SIZE = 100;
    private String data;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_grid_fragment);

        initViews();
    }

    private void initViews() {
        back_arrow = findViewById(R.id.back_arrow);
        edit_data_find = findViewById(R.id.edit_data_find);
        tvNoData = findViewById(R.id.tvNoData);
        recyclerView = findViewById(R.id.recyclerViewSearchGrid);
        //   scrollListener=new RecylerViewLodeMoreScroll(mLayoutManager);
        rowsFeedMain = new ArrayList<>();
        datahashTagList = new ArrayList<>();
        recyclerViewname = findViewById(R.id.hashtag_recycleView_horizental);
        hashtagNameAdapter = new HashtagNameAdapter(this, datahashTagList);
        recyclerViewname.setHasFixedSize(true);
        recyclerViewname.setAdapter(hashtagNameAdapter);

        //     homeFragmentFeedListingService(pageno);
        tvNoData.setVisibility(View.GONE);
        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(mLayoutManager);


//        recyclerView.addOnScrollListener(new EndlessRecyclerGridOnScrollListener(mLayoutManager) {
//            @Override
//            public void onLoadMore(int current_page) {
//                if (isLoading) {
//                    if (totalCount > rowsFeedMain.size()) {
//                        pageno = pageno + 1;
//                        homeFragmentFeedListingService(pageno);
//                    }
//                }
//            }
//        });

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        edit_data_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchGridActivity.this, SearchActivityNew.class));
            }
        });


    }

//    // region Listeners
//    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//        }
//
//        @Override
//        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
//            int totalItemCount = recyclerView.getLayoutManager().getItemCount();
//            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();
//
//            if (!isLoading && !isLastPage) {
//                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                        && firstVisibleItemPosition >= 0
//                        && totalItemCount >= PAGE_SIZE) {
//                    pageno++;
//                    homeFragmentFeedListingService(pageno);
//                }
//            }
//        }
//    };

    public boolean pushFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fm_search_container, fragment, tag)
                    .addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }

    //    public void loadFragment(@NonNull Fragment fragment, boolean clearTillHome) {
//        String fragmentName = fragment.getClass().getSimpleName();
//        currentFragName = fragmentName;
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        if (isFragmentInBackStack(fragmentManager, fragment.getClass().getSimpleName())) {
//            // Fragment exists, go back to that fragment
//            fragmentManager.popBackStack(fragment.getClass().getSimpleName(), 0);
//        } else {
//            if (clearTillHome)
//                fragmentManager.popBackStack(SearchGridFragment.class.getSimpleName(), 0);
//            fragmentManager
//                    .beginTransaction()
//                    .replace(R.id.fm_search_container, fragment, fragmentName)
//                    .addToBackStack(fragmentName)
//                    .commit();
//        }
//    }
    private void fetchData(int pageno) {
        // String searchText = search.getText().toString();
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(SearchGridActivity.this, SharedPrefreances.AUTH_TOKEN));
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        HashMap<String, String> body = new HashMap<>();
        body.put("key", "");
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<HashtagSearchResponse> searchCall = webApi.searchHashTag(header, body);
        searchCall.enqueue(new Callback<HashtagSearchResponse>() {
            @Override
            public void onResponse(Call<HashtagSearchResponse> call, Response<HashtagSearchResponse> response) {

                if (response.code() == 200 && response.body().getSuccess()) {
                    if (datahashTagList.isEmpty()) {
                        datahashTagList = response.body().getData();

                        hashtagNameAdapter.updateList(datahashTagList);
                    }


                } else {
                    // Utilss.showToast(context, "there is no Hashtag", R.color.msg_fail);
                }
            }

            @Override
            public void onFailure(Call<HashtagSearchResponse> call, Throwable t) {

            }
        });
    }

    private void homeFragmentFeedListingService(int pageNo) {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> map = new HashMap<>();
        map.put("x-token", SharedPrefreances.getSharedPreferenceString(SearchGridActivity.this, SharedPrefreances.AUTH_TOKEN));

        HashMap<String, Object> body = new HashMap<>();
        body.put("pageNumber", pageno);
        body.put("pageSize", PAGE_SIZE);

//        body.put("pageSize", String.valueOf("1000"));
        final Call<TopResponse> call = webApi.hashTagTop(map, body);
        call.enqueue(new Callback<TopResponse>() {
            @Override
            public void onResponse(Call<TopResponse> call, Response<TopResponse> response) {
                isLoading = false;
                Log.d("response====", String.valueOf(response));
                try {
                    if (response.code() == 200 && response.body().getSuccess()) {
                        rowsFeedMain.addAll(response.body().getData());
                        totalCount = response.body().getCount();
                        updateAdapter(response.body().getData());
                    } else {
                        tvNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        Utilss.showToast(SearchGridActivity.this, getString(R.string.error_msg), R.color.grey);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TopResponse> call, Throwable t) {
                Utilss.showToast(SearchGridActivity.this, getString(R.string.error_msg), R.color.grey);

            }
        });
    }

    private void updateAdapter(List<TopResponse.Datum> rowsFeed) {
        if (adapter == null) {
            if (rowsFeed != null && rowsFeed.size() > 0) {
                tvNoData.setVisibility(View.GONE);
                adapter = new SearchPhotoGridAdapter(SearchGridActivity.this, rowsFeedMain);
                recyclerView.setAdapter(adapter);

            } else {
                tvNoData.setVisibility(View.VISIBLE);

            }
        } else {

            if (rowsFeed != null && rowsFeed.size() > 0) {
                adapter.notifyDataSetChanged();
            } else {
                Utilss.showToast(SearchGridActivity.this, getString(R.string.No_more_data), R.color.grey);
            }

        }

    }

    @Override
    public void onRetry() {
        if (ConnectionUtils.isConnected(SearchGridActivity.this)) {
            if (rowsFeedMain.size() > 0) {
                rowsFeedMain.clear();
            }
            pageno = 1;
            homeFragmentFeedListingService(pageno);
        } else {
            ConnectionUtils.showNoConnectionDialog(SearchGridActivity.this, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConnectionUtils.isConnected(SearchGridActivity.this)) {
            if (rowsFeedMain.size() > 0) {
                rowsFeedMain.clear();
            }
            pageno = 1;
            homeFragmentFeedListingService(pageno);

        } else {
            ConnectionUtils.showNoConnectionDialog(SearchGridActivity.this, this);
        }
    }
}
