package com.meest.metme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.google.android.material.resources.TextAppearance;
import com.meest.R;
import com.meest.databinding.ActivityTextAppearanceBinding;
import com.meest.databinding.ColorThemeLayoutBinding;
import com.meest.databinding.FontLayoutBinding;
import com.meest.databinding.GradientThemeLayoutBinding;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.metme.adapter.FontAdapter;
import com.meest.metme.adapter.GradientColorAdapter;
import com.meest.metme.adapter.SolidColorAdapter;
import com.meest.metme.model.FontResponse;
import com.meest.metme.model.GradientColor;
import com.meest.metme.model.SaveTextAppearance;
import com.meest.metme.model.SolidColor;
import com.meest.metme.viewmodels.TextAppearanceViewModel;
import com.meest.utils.Helper;

import java.io.File;

public class TextAppearanceActivity extends AppCompatActivity {
    ActivityTextAppearanceBinding binding;
    TextAppearanceViewModel viewModel;
    GradientColorAdapter gradientColorAdapter;
    private String chatHeadId, userId;
    SaveTextAppearance saveTextAppreance;
    String firstColor, secondColor, bgColor, fontUrl, gradient;
    private String themeSecondColor;
    private String fontId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_appearance);
        viewModel = new TextAppearanceViewModel();
        binding.setViewModel(viewModel);
        if (getIntent() != null) {
            userId = getIntent().getStringExtra("otherUserId");
            chatHeadId = getIntent().getStringExtra("chatHeadId");
            themeSecondColor = getIntent().getStringExtra("secondColor");
        }
        if (themeSecondColor == null) {
            themeSecondColor = "#143988";
        }
        viewModel.secondColor = themeSecondColor;
        saveTextAppreance = new SaveTextAppearance(firstColor, secondColor, bgColor, fontUrl, gradient, userId, "", "", "",fontId);
        saveTextAppreance.setToUserId(userId);
        resetBgColor();
        initView();
        initObserver();
        initListener();
        changeButtonColor(themeSecondColor);
        changeHeaderColor(themeSecondColor);
        viewModel.fontName = SharedPrefreances.getSharedPreferenceString(this, chatHeadId);
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

    private void changeButtonColor(String secondColor) {
        if (secondColor != null && !secondColor.equals("")) {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(secondColor), Color.parseColor(secondColor)});
            gd.setCornerRadius(11f);
            binding.submitTheme.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{getResources().getColor(R.color.first_color), getResources().getColor(R.color.first_color)});
            gd.setCornerRadius(11f);
            binding.submitTheme.setBackground(gd);
        }
    }

    private void resetBgColor() {
        Drawable buttonDrawable0 = this.binding.dummyMainLl.getBackground();
        buttonDrawable0 = DrawableCompat.wrap(buttonDrawable0);
        DrawableCompat.setTint(buttonDrawable0, Color.parseColor("#F4F4F4"));
        this.binding.dummyMainLl.setBackground(buttonDrawable0);
    }

    private void initView() {
        viewModel.context = TextAppearanceActivity.this;
        viewModel.getSolidColor(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        viewModel.getGradientColor(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        viewModel.getFont(SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        binding.colorThemeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.gradientThemeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.fontThemeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.colorThemeRecycler);
//        viewModel.Temp(binding);
    }

    private void initObserver() {
        viewModel.solidColorAdapter = new SolidColorAdapter();
        viewModel.gradientColorAdapter = new GradientColorAdapter();
        viewModel.fontAdapter = new FontAdapter(this,themeSecondColor);
        viewModel.isloading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    Toast.makeText(TextAppearanceActivity.this, "Theme Successfully changed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("return", "true");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetBgColor();
    }

    private void initListener() {
        binding.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewModel.solidColorAdapter.setOnRecyclerViewItemClick(new SolidColorAdapter.OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(SolidColor.Row data, int position, ColorThemeLayoutBinding binding, SolidColorAdapter.ViewHolder holder) {

                firstColor = data.getRecievercolor();
                secondColor = data.getSendercolor();
                bgColor = data.getBgColor();
                gradient = "false";

                Drawable buttonDrawable0 = TextAppearanceActivity.this.binding.dummyMainLl.getBackground();
                buttonDrawable0 = DrawableCompat.wrap(buttonDrawable0);
                DrawableCompat.setTint(buttonDrawable0, Color.parseColor(data.getBgColor()));
                TextAppearanceActivity.this.binding.dummyMainLl.setBackground(buttonDrawable0);

                Drawable buttonDrawable1 = TextAppearanceActivity.this.binding.tempChat1.getBackground();
                buttonDrawable1 = DrawableCompat.wrap(buttonDrawable1);
                DrawableCompat.setTint(buttonDrawable1, Color.parseColor(data.getRecievercolor()));
                TextAppearanceActivity.this.binding.tempChat1.setBackground(buttonDrawable1);

                Drawable buttonDrawable2 = TextAppearanceActivity.this.binding.tempChat2.getBackground();
                buttonDrawable2 = DrawableCompat.wrap(buttonDrawable2);
                DrawableCompat.setTint(buttonDrawable2, Color.parseColor(data.getSendercolor()));
                TextAppearanceActivity.this.binding.tempChat2.setBackground(buttonDrawable2);

                Drawable buttonDrawable3 = TextAppearanceActivity.this.binding.tempChat3.getBackground();
                buttonDrawable3 = DrawableCompat.wrap(buttonDrawable3);
                DrawableCompat.setTint(buttonDrawable3, Color.parseColor(data.getRecievercolor()));
                TextAppearanceActivity.this.binding.tempChat3.setBackground(buttonDrawable3);

                Drawable buttonDrawable4 = TextAppearanceActivity.this.binding.tempChat4.getBackground();
                buttonDrawable4 = DrawableCompat.wrap(buttonDrawable4);
                DrawableCompat.setTint(buttonDrawable4, Color.parseColor(data.getSendercolor()));
                TextAppearanceActivity.this.binding.tempChat4.setBackground(buttonDrawable4);

                Drawable buttonDrawable5 = TextAppearanceActivity.this.binding.tempChat5.getBackground();
                buttonDrawable5 = DrawableCompat.wrap(buttonDrawable5);
                DrawableCompat.setTint(buttonDrawable5, Color.parseColor(data.getRecievercolor()));
                TextAppearanceActivity.this.binding.tempChat5.setBackground(buttonDrawable5);

                String opacityColor = data.getSendercolor().substring(0, 1) + "60" + data.getSendercolor().substring(1);

                TextAppearanceActivity.this.binding.coverRl.setBackgroundColor(Color.parseColor(opacityColor));

                TextAppearanceActivity.this.binding.fabLayoutCamera.setColorFilter(Color.parseColor(data.getSendercolor()));

                TextAppearanceActivity.this.binding.micro.setColorFilter(Color.parseColor(data.getSendercolor()));

                TextAppearanceActivity.this.binding.gallery.setColorFilter(Color.parseColor(data.getSendercolor()));

            }
        });
        viewModel.fontAdapter.setOnRecyclerViewItemClick(new FontAdapter.OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(FontResponse.FontName data, int position, FontLayoutBinding binding, FontAdapter.ViewHolder holder) {
                copyAssets(data);
                if (data.getFonturl() != null) {
                    fontUrl = data.getFonturl();
                    fontId=String.valueOf(data.getId());
                } else {
                    fontUrl = "";
                }
            }
        });
        viewModel.gradientColorAdapter.setOnRecyclerViewItemClick(new GradientColorAdapter.OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(GradientColor.Data data, int position, GradientThemeLayoutBinding binding, GradientColorAdapter.ViewHolder holder) {
                firstColor = data.getFirstcolor();
                secondColor = data.getSecondcolor();
                bgColor = "";
                gradient = "true";
                resetBG_and_IncomingBGColor();
//==============================================
                String firstColor = data.getFirstcolor().substring(0, 1) + "60" + data.getFirstcolor().substring(1);
                String secondColor = data.getSecondcolor().substring(0, 1) + "60" + data.getSecondcolor().substring(1);
                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor(firstColor), Color.parseColor(secondColor)});
                gd.setCornerRadius(0f);
                TextAppearanceActivity.this.binding.coverRl.setBackground(gd);
//==============================================

                TextAppearanceActivity.this.binding.fabLayoutCamera.setColorFilter(Color.parseColor(data.getFirstcolor()));

                TextAppearanceActivity.this.binding.micro.setColorFilter(Color.parseColor(data.getFirstcolor()));

                TextAppearanceActivity.this.binding.gallery.setColorFilter(Color.parseColor(data.getFirstcolor()));
                //==============================================

                //==============================================
                int middle = blendColors(Color.parseColor(data.getFirstcolor()), Color.parseColor(data.getSecondcolor()), 0.5f);
                GradientDrawable gd1 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Color.parseColor(data.getFirstcolor()), middle});
                gd1.setCornerRadius(10f);
                TextAppearanceActivity.this.binding.tempChat2.setBackground(gd1);

                GradientDrawable gd2 = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{middle, Color.parseColor(data.getSecondcolor())});
                gd2.setCornerRadius(10f);
                TextAppearanceActivity.this.binding.tempChat4.setBackground(gd2);
            }
        });
        binding.submitTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Helper.isNetworkAvailable(getApplicationContext())) {
                    viewModel.saveTextAppearance(new SaveTextAppearance(firstColor, secondColor, bgColor, fontUrl, gradient, userId, "", "", "",fontId), SharedPrefreances.getSharedPreferenceString(TextAppearanceActivity.this, SharedPrefreances.AUTH_TOKEN), TextAppearanceActivity.this);
                }else{
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
                Log.e("click", "save");
            }
        });
    }

    private static int blendColors(int color1, int color2, float ratio) {
        final float inverseRation = 1f - ratio;
        float r = (Color.red(color1) * ratio) + (Color.red(color2) * inverseRation);
        float g = (Color.green(color1) * ratio) + (Color.green(color2) * inverseRation);
        float b = (Color.blue(color1) * ratio) + (Color.blue(color2) * inverseRation);
        return Color.rgb((int) r, (int) g, (int) b);
    }

    private void copyAssets(FontResponse.FontName data) {
        File fontFile = new File(getExternalCacheDir() + "/userid_font_" + data.getId() + ".ttf");
        if (!fontFile.exists()) {
            fontFile.delete();
            PRDownloader.download(data.getFonturl(), getExternalCacheDir().getPath(), "/userid_font_" + data.getId() + ".ttf")
                    .build()
                    .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                        @Override
                        public void onStartOrResume() {
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            Typeface typeface = Typeface.createFromFile(getExternalCacheDir() + "/userid_font_" + data.getId() + ".ttf");
                            binding.tvUserChat1.setTypeface(typeface);
                            binding.tvUserChat2.setTypeface(typeface);
                            binding.tvUserChat3.setTypeface(typeface);
                            binding.tvUserChat4.setTypeface(typeface);
                            binding.tvUserChat5.setTypeface(typeface);
                            binding.tvUserChatTime1.setTypeface(typeface);
                            binding.tvUserChatTime2.setTypeface(typeface);
                            binding.tvUserChatTime3.setTypeface(typeface);
                            binding.tvUserChatTime4.setTypeface(typeface);
                            binding.tvUserChatTime5.setTypeface(typeface);
                            binding.chatTime.setTypeface(typeface);
                            binding.messageInput.setTypeface(typeface);
                        }

                        @Override
                        public void onError(Error error) {

                        }
                    });
        } else {
//            fontFile.delete();
            Typeface typeface = Typeface.createFromFile(getExternalCacheDir() + "/userid_font_" + data.getId() + ".ttf");
            binding.tvUserChat1.setTypeface(typeface);
            binding.tvUserChat2.setTypeface(typeface);
            binding.tvUserChat3.setTypeface(typeface);
            binding.tvUserChat4.setTypeface(typeface);
            binding.tvUserChat5.setTypeface(typeface);
            binding.tvUserChatTime1.setTypeface(typeface);
            binding.tvUserChatTime2.setTypeface(typeface);
            binding.tvUserChatTime3.setTypeface(typeface);
            binding.tvUserChatTime4.setTypeface(typeface);
            binding.tvUserChatTime5.setTypeface(typeface);
            binding.chatTime.setTypeface(typeface);
            binding.messageInput.setTypeface(typeface);
        }

    }

    private void resetBG_and_IncomingBGColor() {

        Drawable buttonDrawable0 = this.binding.dummyMainLl.getBackground();
        buttonDrawable0 = DrawableCompat.wrap(buttonDrawable0);
        DrawableCompat.setTint(buttonDrawable0, Color.parseColor("#F4F4F4"));

        Drawable buttonDrawable1 = TextAppearanceActivity.this.binding.tempChat1.getBackground();
        buttonDrawable1 = DrawableCompat.wrap(buttonDrawable1);
        DrawableCompat.setTint(buttonDrawable1, Color.parseColor("#DEDEDE"));

        Drawable buttonDrawable3 = TextAppearanceActivity.this.binding.tempChat3.getBackground();
        buttonDrawable3 = DrawableCompat.wrap(buttonDrawable3);
        DrawableCompat.setTint(buttonDrawable3, Color.parseColor("#DEDEDE"));

        Drawable buttonDrawable5 = TextAppearanceActivity.this.binding.tempChat5.getBackground();
        buttonDrawable5 = DrawableCompat.wrap(buttonDrawable5);
        DrawableCompat.setTint(buttonDrawable5, Color.parseColor("#DEDEDE"));

        this.binding.dummyMainLl.setBackground(buttonDrawable0);
        this.binding.tempChat1.setBackground(buttonDrawable1);
        this.binding.tempChat3.setBackground(buttonDrawable3);
        this.binding.tempChat5.setBackground(buttonDrawable5);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("return", "false");
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}
