package com.meest.Faq;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.FAQCategoriesAdapter;
import com.meest.Adapters.FaqQuestionAdapter;
import com.meest.Paramaters.FAQParametr;
import com.meest.R;
import com.meest.responses.UserFAQCategoryResponse;
import com.meest.responses.UserFAQResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqActivity extends AppCompatActivity {

    RecyclerView faqQuesRecyclerView;
    RecyclerView.Adapter FaqAdapter;
    RecyclerView.LayoutManager FaqManager;
    RecyclerView faqCategoriesRecycler;
    FAQCategoriesAdapter faqCategoriesAdapter;
    RecyclerView.LayoutManager faqCategoriesManager;
    private ImageView backArrow;
    LottieAnimationView image;
    private Activity mActivity;

    private void initViews() {
        backArrow = findViewById(R.id.img_back_arroe_faq);
        image = findViewById(R.id.loading);
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);
        faqCategoriesRecycler = findViewById(R.id.faqBtList);
        faqQuesRecyclerView = findViewById(R.id.recylFaqQueList);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        mActivity = this;
        initViews();
        getCategoriesFromServer();
    }

    private void setCategoriesAdapter(List<UserFAQCategoryResponse.Row> rows) {
        try {
            faqCategoriesRecycler.setHasFixedSize(true);
            faqCategoriesAdapter = new FAQCategoriesAdapter(rows);
            faqCategoriesManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            faqCategoriesRecycler.setLayoutManager(faqCategoriesManager);
            faqCategoriesRecycler.setAdapter(faqCategoriesAdapter);
            faqCategoriesAdapter.setItemClickListener(new FAQCategoriesAdapter.onItemClickListener() {
                @Override
                public void onItemClick(int position, UserFAQCategoryResponse.Row rowItem) {
                    getFAQs(rowItem.getId());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFAQs(String faqID) {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
        FAQParametr paramter = new FAQParametr(faqID);
        Call<UserFAQResponse> call = webApi.getFAQbyCategoryID(header, paramter);
        showHideLoader(true);
        call.enqueue(new Callback<UserFAQResponse>() {
            @Override
            public void onResponse(Call<UserFAQResponse> call, Response<UserFAQResponse> response) {
                showHideLoader(false);
                if (response.code() == 200) {
                    setFAQQuestions(response.body().getData().getRows());
                } else {
                }
            }

            @Override
            public void onFailure(Call<UserFAQResponse> call, Throwable t) {
                showHideLoader(false);
            }
        });
    }

    private void setFAQQuestions(List<UserFAQResponse.Row> rows) {
        try {
            faqQuesRecyclerView.setHasFixedSize(true);
            FaqAdapter = new FaqQuestionAdapter(rows);
            FaqManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            faqQuesRecyclerView.setLayoutManager(FaqManager);
            faqQuesRecyclerView.setAdapter(FaqAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategoriesFromServer() {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        Call<UserFAQCategoryResponse> call = webApi.getFAQCategories(header);
        showHideLoader(true);
        call.enqueue(new Callback<UserFAQCategoryResponse>() {
            @Override
            public void onResponse(Call<UserFAQCategoryResponse> call, Response<UserFAQCategoryResponse> response) {
                showHideLoader(false);
                if (response.code() == 200) {
                    setCategoriesAdapter(response.body().getData().getRows());
                    if(response.body().getData().getRows().size()!=0){
                        getFAQs(response.body().getData().getRows().get(0).getId());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<UserFAQCategoryResponse> call, Throwable t) {
                showHideLoader(false);
            }
        });
    }
}