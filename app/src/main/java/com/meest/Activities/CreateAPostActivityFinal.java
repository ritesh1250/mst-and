package com.meest.Activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.meest.Fragments.CreatePhotosFragment;
import com.meest.Fragments.GalleryFragment;
import com.meest.Fragments.LiveFragment;
import com.meest.R;
import com.meest.meestbhart.utilss.SharedPrefreances;

public class CreateAPostActivityFinal extends AppCompatActivity {

    private TextView txt_gallery, txt_photo, txt_video, txt_live;
    private String fragmentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_final_post_activity);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if(getIntent().getExtras() == null) {
            finish();
        }
        fragmentType = getIntent().getExtras().getString("fragmentType");

        findIds();


        if(fragmentType.equalsIgnoreCase("camera")) {
            txt_photo.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_blue));
            txt_live.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
            txt_video.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
            txt_gallery.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));

            SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "back_post", "1");
            pushFragment(new CreatePhotosFragment(this, false), "photo");
        } else if (fragmentType.equalsIgnoreCase("remove")) {
            txt_photo.setVisibility(View.GONE);
            txt_live.setVisibility(View.GONE);
            txt_video.setVisibility(View.GONE);
            txt_gallery.setVisibility(View.GONE);

            SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "back_post", "1");
            pushFragment(new CreatePhotosFragment(this, true), "photo");
        } else {
            txt_photo.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
            txt_live.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_blue));
            txt_video.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
            txt_gallery.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));

            SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "back_post", "1");
            pushFragment(new LiveFragment(), "photo");
        }

        txt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_photo.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_blue));
                txt_live.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_video.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_gallery.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "back_post", "1");
                pushFragment(new CreatePhotosFragment(CreateAPostActivityFinal.this, false), "photo");
            }
        });

        txt_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_photo.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_live.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_blue));
                txt_video.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_gallery.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "back_post", "1");
                pushFragment(new LiveFragment(), "photo");
            }
        });

        txt_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_photo.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_live.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_video.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_blue));
                txt_gallery.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "back_post", "1");

            }
        });

        txt_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_photo.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_live.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_video.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_gray));
                txt_gallery.setTextColor(getApplicationContext().getResources().getColor(R.color.menu_blue));
                SharedPrefreances.setSharedPreferenceString(getApplicationContext(), "back_post", "1");
                pushFragment(new GalleryFragment(), "photo");
            }
        });

//        if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "main_data").equalsIgnoreCase("1")) {
//            pushFragment(new GalleryFragment(), "photo");
//        } else if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "main_data").equalsIgnoreCase("2")) {
//            pushFragment(new CreatePhotosFragment(CreateAPostActivityFinal.this), "photo");
//        } else if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "main_data").equalsIgnoreCase("3")) {
//            pushFragment(new PhotoFragment(), "photo");
//        } else if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(), "main_data").equalsIgnoreCase("4")) {
//            pushFragment(new LiveFragment(), "photo");
//        }
    }


    public void findIds() {
        txt_gallery = findViewById(R.id.txt_gallery);
        txt_photo = findViewById(R.id.txt_photo);
        txt_video = findViewById(R.id.txt_video);
        txt_live = findViewById(R.id.txt_live);
    }


    public boolean pushFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment, tag)
                    .addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
        /*if (SharedPrefreances.getSharedPreferenceString(getApplicationContext(),"back_post").equalsIgnoreCase("1")){
            pushFragment(new GalleryFragment(),"photo");
            SharedPrefreances.setSharedPreferenceString(getApplicationContext(),"back_post","0");
        }else {
            finish();
        }
*/
    }





}
