package com.meest.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.meest.Adapters.SliderPagerAdapter;
import com.meest.Adapters.ViewPagerAdapter;
import com.meest.R;
import com.meest.responses.OnBoardingResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.view.login.LoginSignUp;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Button button;
    private SliderPagerAdapter adapter;
    List<OnBoardingResponse.Row> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    private int dotscount;
    private ImageView[] dots;
    LinearLayout sliderDotspanel;
    int demo = 0;
    TextView txt_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.ad_activity);
//        getSupportActionBar().hide();
        // bind views
        sliderImg = new ArrayList<>();
        viewPager = findViewById(R.id.pagerIntroSlider);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        txt_next = findViewById(R.id.txt_next);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_not_select));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_select));

                //Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_LONG).show();

                if (position + 1 == demo) {
                    txt_next.setVisibility(View.VISIBLE);
                } else {
                    txt_next.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "ad_status", "1");
                Intent intent = new Intent(AdActivity.this, LoginSignUp.class);
                startActivity(intent);
            }
        });

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<OnBoardingResponse> call = webApi.afterSignup();
        call.enqueue(new Callback<OnBoardingResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<OnBoardingResponse> call, Response<OnBoardingResponse> response) {
                for (int i = 0; i < response.body().getData().getRows().size(); i++) {


                    try {
                        sliderImg.add(response.body().getData().getRows().get(i));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                viewPagerAdapter = new ViewPagerAdapter(sliderImg, AdActivity.this);

                viewPager.setAdapter(viewPagerAdapter);

                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];


                demo = response.body().getData().getCount();

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(AdActivity.this);
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_not_select));

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.slider_select));


            }

            @Override
            public void onFailure(Call<OnBoardingResponse> call, Throwable t) {

            }
        });

    }
}
