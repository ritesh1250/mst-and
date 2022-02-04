//package com.meest.Activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//
//import com.meest.Fragments.MyAccountFragmentVideo;
//import com.meest.R;
//import com.meest.meestbhart.utilss.SharedPrefreances;
//import com.meest.meestbhart.splash.SplashScreenActivity;
//import com.google.android.material.navigation.NavigationView;
//import com.google.android.material.shape.CornerFamily;
//import com.google.android.material.shape.MaterialShapeDrawable;
//
//public class VideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    NavigationView navigationView;
//    public DrawerLayout drawer;
//    RelativeLayout layout_notification,layout_search,layout_my_account_new,layout_home__new;
//    RelativeLayout layout_my_account,layout_home,layout_setting,layout_help,layout_manage,layout_invite_friends,layout_post,layout_collection;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.video_activity);
//        overridePendingTransition(R.anim.zoom_out,R.anim.zoom_in);
////        pushFragment(new VideoPostFragment(),"video");
//        layout_my_account_new = findViewById(R.id.layout_my_account);
//        layout_search = findViewById(R.id.layout_search);
//        layout_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pushFragment(new SearchFragmentNew(),"");
//            }
//        });
//        layout_notification = findViewById(R.id.layout_notification);
//        layout_notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //pushFragment(new NotificationFragment(),"");
//            }
//        });
//
//        layout_my_account_new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pushFragment(new MyAccountFragmentVideo(),"");
//            }
//        });
//
//        layout_home__new = findViewById(R.id.layout_home);
//        layout_home__new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                pushFragment(new VideoPostFragment(),"");
//            }
//        });
//
//
//
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        // drawer = findViewById(R.id.drawer_layout);
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//
//        MaterialShapeDrawable navViewBackground = (MaterialShapeDrawable) navigationView.getBackground();
//        navViewBackground.setShapeAppearanceModel(
//                navViewBackground.getShapeAppearanceModel()
//                        .toBuilder()
//                        .setBottomLeftCorner(CornerFamily.ROUNDED,100)
//                        .setTopLeftCorner(CornerFamily.ROUNDED,100)
//                        .build());
//
//
//
//
//        drawer = findViewById(R.id.drawer_layout);
//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//
//        // NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
//
//        navigationView.setNavigationItemSelectedListener(VideoActivity.this);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                VideoActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//
//        View headerView = navigationView.getHeaderView(0);
////        ImageView imageView = (ImageView) headerView.findViewById(R.id.imageView);
//        //      Switch toggleButton1 = (Switch) headerView.findViewById(R.id.toggleButton1);
// /*       txt_name = (TextView) headerView.findViewById(R.id.txt_name);
//        txt_email = (TextView) headerView.findViewById(R.id.txt_email);
//        txt_mobile = (TextView) headerView.findViewById(R.id.txt_mobile);
//        imageview = (CircleImageView) headerView.findViewById(R.id.profile);*/
//
//        RelativeLayout layout_logout = headerView.findViewById(R.id.layout_logout);
//        ImageView img_close = headerView.findViewById(R.id.img_close);
//
//        layout_setting = headerView.findViewById(R.id.layout_setting);
//        layout_help = headerView.findViewById(R.id.layout_help);
//        layout_manage = headerView.findViewById(R.id.layout_manage);
//        layout_invite_friends = headerView.findViewById(R.id.layout_invite_friends);
//        layout_setting = headerView.findViewById(R.id.layout_setting);
//        layout_collection = headerView.findViewById(R.id.layout_collection);
//        TextView txt_name = headerView.findViewById(R.id.txt_name);
//        txt_name.setText(SharedPrefreances.getSharedPreferenceString(getApplicationContext(),SharedPrefreances.USERNAME));
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer.closeDrawer(Gravity.RIGHT);
//            }
//        });
//        layout_collection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(VideoActivity.this, ColletionActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        layout_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPrefreances.setSharedPreferenceString(getApplicationContext(),SharedPrefreances.PROFILE,"");
//                SharedPrefreances.setSharedPreferenceString(getApplicationContext(),"login","0");
//                Intent intent = new Intent(VideoActivity.this, SplashScreenActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });
//
//
//        layout_invite_friends.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(VideoActivity.this, InviteFriendActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//
//
//        layout_setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        layout_help.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(VideoActivity.this, HelpActivity.class);
//                startActivity(intent);
//            }
//        });
//        layout_manage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(VideoActivity.this, ManagmentActivity.class);
//                startActivity(intent);
//            }
//        });
//        layout_setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(VideoActivity.this, SettingActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//
//    }
//
//
//    private boolean pushFragment(Fragment fragment, String tag) {
//        if (fragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, fragment, tag)
//                    .addToBackStack("fragment")
//                    .commit();
//            return true;
//        }
//        return false;
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//
//        finish();
//        overridePendingTransition(R.anim.zoom_out,R.anim.zoom_in);
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        return false;
//    }
//
//}
