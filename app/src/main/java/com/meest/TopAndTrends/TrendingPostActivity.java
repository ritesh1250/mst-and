package com.meest.TopAndTrends;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.meest.Adapters.TrendingTimeAdapter;
import com.meest.model.TimeTrendingModel;
import com.meest.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class TrendingPostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener,
        HowManyTrendCallback, ViewPager.OnPageChangeListener {


    RadioButton radioTop, radioTrending;

    TabLayout tabLayoutTrend;
    FrameLayout viewPagerTrend;
    ImageView img_back_arrow_trending;

    private TextView tvToday, tvAllTime, tvYesterday, tvSelectDate, tvDropTrending;
    private TextView tvPost, tvPeople, tvHashtag, tvImages, tvVideos;
    ArrayList<TimeTrendingModel> trendingTimeModel;
    TrendingTimeAdapter adapter;
    RadioGroup radioGroupTrending;
    NavigationView navigationView;
    // public DrawerLayout drawer;
    public static int selectedTab = 0;
    ImageView ivFilter;
    Fragment fragment;
    Spinner spinTextTrend;
    RecyclerView recyclerTrendHowMany;
    TextView spinHowManyTrend;



    private boolean isTrending = true;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_right_trending_post);
        initList();

        img_back_arrow_trending = findViewById(R.id.img_back_arrow_trending);
        img_back_arrow_trending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayoutTrend = findViewById(R.id.tabLayoutTrend);
        viewPagerTrend = findViewById(R.id.viewPagerTrend);
        tabLayoutTrend.addTab(tabLayoutTrend.newTab().setText(getString(R.string.POST_)));
        tabLayoutTrend.addTab(tabLayoutTrend.newTab().setText(getString(R.string.PEOPLE_)));
        tabLayoutTrend.addTab(tabLayoutTrend.newTab().setText(getString(R.string.TAGS_)));
        tabLayoutTrend.addTab(tabLayoutTrend.newTab().setText(getString(R.string.IMAGES_)));
        tabLayoutTrend.addTab(tabLayoutTrend.newTab().setText(getString(R.string.VIDEOS_)));

        tabLayoutTrend.setTabGravity(TabLayout.GRAVITY_FILL);

        // selecting default fragment
        TrendingPostActivity.selectedTab = 0;
        fragment = new TrendPostFragment("post", TrendingPostActivity.this,isTrending);
        transactFragment();

//        final TrendingMenuAdapter trendAdapter = new TrendingMenuAdapter(this, getSupportFragmentManager(),
//                tabLayoutTrend.getTabCount());
//        viewPagerTrend.setAdapter(trendAdapter);
//        viewPagerTrend.addOnPageChangeListener(this);

        tabLayoutTrend.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                changeTab(position);
                selectedTab = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        tvDropTrending = findViewById(R.id.tvDropTrending);
        tvDropTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TrendingPostActivity.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.custom_dialog_trending_post, viewGroup, false);

                radioGroupTrending = dialogView.findViewById(R.id.radioGroupTrending);
                radioTop = dialogView.findViewById(R.id.radioTop);
                radioTrending = dialogView.findViewById(R.id.radioTrending);

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                if (isTrending) {
                    radioTop.setChecked(false);
                    radioTrending.setChecked(true);
                } else {
                    radioTop.setChecked(true);
                    radioTrending.setChecked(false);
                }

                radioTop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioTop.setChecked(true);
                        radioTrending.setChecked(false);
                        isTrending = false;
                        tvDropTrending.setText(getString(R.string.Top));
                        changeTab(TrendingPostActivity.selectedTab);
//                        viewPagerTrend.setCurrentItem(selectedTab);
                        alertDialog.dismiss();
                    }
                });

                radioTrending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioTop.setChecked(false);
                        radioTrending.setChecked(true);
                        tvDropTrending.setText(getString(R.string.Trending));
                        radioTrending.setText(getString(R.string.Trending));
                        isTrending = true;
                        changeTab(TrendingPostActivity.selectedTab);
//                        viewPagerTrend.setCurrentItem(selectedTab);
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

//        navigationView = (NavigationView) findViewById(R.id.nav_view_trending);
//        ivFilter = findViewById(R.id.ivFilter);
//
//        ivFilter.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onClick(View v) {
//
//                // spinHowManyTrend=findViewById(R.id.spinHowManyTrend);
//                //spinTextTrend = findViewById(R.id.spinTextTrend);
//
//                adapter = new TrendingTimeAdapter(TrendingPostActivity.this, trendingTimeModel);
//                spinTextTrend.setAdapter(adapter);
//                spinTextTrend.setOnItemSelectedListener(
//                        new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> parent,
//                                                       View view, int position, long id) {
//                                // It returns the clicked item.
//                                TimeTrendingModel clickedItem = (TimeTrendingModel)
//                                        parent.getItemAtPosition(position);
//                                String name = clickedItem.getTrendingTime();
//                                if (clickedItem.getTrendingTime().equals("Select Date")) {
//                                    Toast.makeText(TrendingPostActivity.this, "Select date", Toast.LENGTH_SHORT).show();
//
//                                }
//                                // Toast.makeText(MainActivity.this, name + " selected", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//                            }
//                        });
//
////                DrawerLayout navDrawer = findViewById(R.id.drawerLayout);
//                // If the navigation drawer is not open then open it, if its already open then close it.
////                if (!navDrawer.isDrawerOpen(Gravity.RIGHT)) navDrawer.openDrawer(Gravity.RIGHT);
////                else navDrawer.closeDrawer(Gravity.START);
//            }
//        });
    }

    private void changeTab(int position) {
        switch (position) {
            case 1:
                TrendingPostActivity.selectedTab = 1;
                fragment = new TrendPeopleFragment(TrendingPostActivity.this, isTrending);
                transactFragment();
                break;
            case 2:
                TrendingPostActivity.selectedTab = 2;
                fragment = new TrendHashTagFragment(TrendingPostActivity.this, isTrending);
                transactFragment();
                break;
            case 3:
                TrendingPostActivity.selectedTab = 3;
                fragment = new TrendPostFragment("image", TrendingPostActivity.this, isTrending);
                transactFragment();
                break;
            case 4:
                TrendingPostActivity.selectedTab = 4;
                fragment = new TrendPostFragment("video", TrendingPostActivity.this, isTrending);
                transactFragment();
                break;
            case 0:
            default:
                TrendingPostActivity.selectedTab = 0;
                fragment = new TrendPostFragment("post", TrendingPostActivity.this, isTrending);
                transactFragment();
                break;
        }
    }

    private void transactFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.viewPagerTrend, fragment);
        fragmentTransaction.commit();
    }

    private void initList() {
        trendingTimeModel = new ArrayList<>();
        trendingTimeModel.add(new TimeTrendingModel("Today"));
        trendingTimeModel.add(new TimeTrendingModel("Yesterday"));
        trendingTimeModel.add(new TimeTrendingModel("All Time"));
        trendingTimeModel.add(new TimeTrendingModel("Select Date"));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void howManyCallback(int position) {

//    spinHowManyTrend.setText(howManyList.get(position));

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectedTab = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
