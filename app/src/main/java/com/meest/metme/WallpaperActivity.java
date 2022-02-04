package com.meest.metme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.meest.R;
import com.meest.databinding.ActivityWallpaperBinding;
import com.meest.databinding.GradientThemeLayoutBinding;
import com.meest.databinding.WallpaperGridItemBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.adapter.GradientWallpaperAdapter;
import com.meest.metme.adapter.WallpaperAdapter;
import com.meest.metme.model.GradientColor;
import com.meest.metme.model.WallpaperItemModel;
import com.meest.metme.model.WallpaperNewModel;
import com.meest.metme.viewmodels.WallpaperViewModel;

import java.util.ArrayList;
import java.util.List;

public class WallpaperActivity extends AppCompatActivity {

    ActivityWallpaperBinding binding;
    List<WallpaperItemModel> wallpaperItemModelList;
    WallpaperItemModel wallpaperItemModel;
    WallpaperViewModel viewModel;
    String wallpaper_id;
    String toUserId,chatHeadId;
    private String secondColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallpaper);
        if (getIntent() != null) {
            toUserId = getIntent().getStringExtra("otherUserId");
            chatHeadId = getIntent().getStringExtra("chatHeadId");
            secondColor = getIntent().getStringExtra("secondColor");
        }
        viewModel = new WallpaperViewModel();
        binding.setWallpaperViewModel(viewModel);
        loadData();
        //   loadThemesData();
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.recyclerView);
        snapHelper.attachToRecyclerView(binding.gradientThemeRecycler);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,  2, GridLayoutManager.HORIZONTAL, false));
//        binding.recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.gradientThemeRecycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
//        binding.gradientThemeRecycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        binding.gradientThemeRecycler.setItemAnimator(new DefaultItemAnimator());

        initListener();
        viewModel.getGradientColor(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        viewModel.getWallpaper(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        changeHeaderColor(secondColor);
        viewModel.fontName=SharedPrefreances.getSharedPreferenceString(this,chatHeadId);
    }
    private void changeHeaderColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(0f);
            binding.mainContainer.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getColor(R.color.first_color), getColor(R.color.first_color)});
            gd.setCornerRadius(0f);
            binding.mainContainer.setBackground(gd);
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void loadData() {
        wallpaperItemModelList = new ArrayList<>();
        wallpaperItemModel = new WallpaperItemModel("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        wallpaperItemModelList.add(wallpaperItemModel);
        wallpaperItemModel = new WallpaperItemModel("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        wallpaperItemModelList.add(wallpaperItemModel);
        wallpaperItemModel = new WallpaperItemModel("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        wallpaperItemModelList.add(wallpaperItemModel);
        wallpaperItemModel = new WallpaperItemModel("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        wallpaperItemModelList.add(wallpaperItemModel);
        wallpaperItemModel = new WallpaperItemModel("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        wallpaperItemModelList.add(wallpaperItemModel);
        wallpaperItemModel = new WallpaperItemModel("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
        wallpaperItemModelList.add(wallpaperItemModel);

    }

    private void initListener() {
        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewModel.gradientWallpaperAdapter.setOnRecyclerViewItemClick(new GradientWallpaperAdapter.OnRecyclerViewItemClick() {
            @Override
            public void onGradientClick(GradientColor.Data data, int position, GradientThemeLayoutBinding binding, GradientWallpaperAdapter.ViewHolder holder) {
                Intent intent=new Intent(WallpaperActivity.this, WallpaperAppearance.class);
                intent.putExtra("bg_first_color",data.getFirstcolor());
                intent.putExtra("bg_second_color",data.getSecondcolor());
                intent.putExtra("toUserId",toUserId);
                intent.putExtra("chatHeadId",chatHeadId);
                intent.putExtra("where","gradient");
                intent.putExtra("secondColor",secondColor);
                startActivityForResult(intent,100);
            }
        });
        viewModel.wallpaperAdapter.setOnRecyclerViewItemClick(new WallpaperAdapter.OnRecyclerViewItemClick() {
            @Override
            public void onWallpaperClick(WallpaperNewModel.Row data, int position, WallpaperGridItemBinding binding, WallpaperAdapter.ViewHolder holder) {
                Log.e("TAG", "onWallpaperClick: "+data.getId());
                Intent intent=new Intent(WallpaperActivity.this, WallpaperAppearance.class);
                intent.putExtra("original_wallpaper",data.getBackgroundImage());
                intent.putExtra("id",String.valueOf(data.getId()));
                intent.putExtra("where","wallpaper");
                intent.putExtra("toUserId",toUserId);
                intent.putExtra("chatHeadId",chatHeadId);
                intent.putExtra("secondColor",secondColor);
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data.getStringExtra("return").equals("true")) {
            Intent intent=new Intent();
            intent.putExtra("return","true");
            setResult(Activity.RESULT_OK,intent);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("return","false");
        setResult(Activity.RESULT_OK,intent);
        super.onBackPressed();
    }

}