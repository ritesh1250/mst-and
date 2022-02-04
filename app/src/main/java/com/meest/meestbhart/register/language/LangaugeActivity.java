package com.meest.meestbhart.register.language;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.meest.Activities.SettingActivity;

import com.meest.social.socialViewModel.view.login.LoginSignUp;
import com.meest.utils.ItemOffsetDecoration;
import com.meest.chat_calling.utils.SharedPreferencesManager;
import com.meest.R;
import com.meest.responses.LanguagesResponse;
import com.meest.meestbhart.register.language.adapter.LangAdapter;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Utilss;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.meest.meestbhart.utilss.WebApi;
import java.util.ArrayList;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LangaugeActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView img_user_continue;
    TextView text_user_continue;
    LinearLayout bottom;
    RecyclerView recyclerView;
    LangAdapter langAdapter;
    ImageView back;
    ArrayList<LanguagesResponse.Row> arrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    int test = 1;
    TextView welcome_to_;
    Context context;
    Resources resources;
    boolean isFromSettings = false;

    private void updateFields() {
        if (getIntent().hasExtra("lang")) {
            isFromSettings = true;
            // welcome_to_.setText(getResources().getString(R.string.change_language));
        } else {
            isFromSettings = false;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);
        progressDialog = new ProgressDialog(LangaugeActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        back = findViewById(R.id.back);
        collapsingToolbarLayout = findViewById(R.id.htab_collapse_toolbar);
        text_user_continue = findViewById(R.id.text_user_continue);
        img_user_continue = findViewById(R.id.img_user_continue);
        bottom = findViewById(R.id.bottom);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        langAdapter = new LangAdapter(this, arrayList, 0);
        updateFields();

        //language API is calling here..........
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<LanguagesResponse> call = webApi.languages();
        call.enqueue(new Callback<LanguagesResponse>() {
            @Override
            public void onResponse(Call<LanguagesResponse> call, Response<LanguagesResponse> response) {
                progressDialog.dismiss();
                try {
                    if (response.body().getCode() == 1) {
                        Log.e("========= ","==========="+response.body());
                        for (int i = 0; i < response.body().getData().getRows().size(); i++) {
                            arrayList.add(response.body().getData().getRows().get(i));
                            if (response.body().getData().getRows().get(i).getLanguageNameEnglish().equalsIgnoreCase("English")) {
                                test = i;
                            }
                        }
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(LangaugeActivity.this, 2);
                        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(LangaugeActivity.this, R.dimen._4sdp);
                        recyclerView.addItemDecoration(itemDecoration);
                        recyclerView.setLayoutManager(gridLayoutManager);
                        //recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(langAdapter);
                        langAdapter.notifyDataSetChanged();
                        changeColours(true);
                    } else {
                        Utilss.showToast(getApplicationContext(), response.body().getSuccess().toString(), R.color.msg_fail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LanguagesResponse> call, Throwable t) {
                Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);

            }
        });


        // language adapter is calling here............
        langAdapter.setOnItemClickListener(new LangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isFromSettings) {
                    Intent it = new Intent();
                    it.putExtra("language", arrayList.get(position).getLanguageNameEnglish());
                    setResult(RESULT_OK, it);
                    finish();
                } else {
                   /* String lang_id = SharedPreferencesManager.getLanguage();
                    setLanguage(lang_id);*/
                    String lang_id = SharedPreferencesManager.getLanguage();
                    Log.e("====Language ID","======="+lang_id);
                    //setLanguage(lang_id);

                }
            }
        });



        // nextLL=findViewById(R.id.nextLL1);
        text_user_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFromSettings) {
                   /* Intent it = new Intent();
                    it.putExtra("language", arrayList.get(langAdapter.selectedPosition).getLanguageNameEnglish());
                    setResult(RESULT_OK, it);
                    finish();*/
                    Intent it = new Intent(LangaugeActivity.this, SettingActivity.class);
                    it.putExtra("language", arrayList.get(langAdapter.selectedPosition).getLanguageNameEnglish());
                    setResult(RESULT_OK, it);
                    it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(it);
                    finish();
                } else {
                    String lang_id = SharedPreferencesManager.getLanguage();
                    Log.e("====Language ID","======="+lang_id);
                    //setLanguage(lang_id);

                    /*Intent intent = new Intent(LangaugeActivity.this, RegisterProcessScreen.class);
                    startActivity(intent);*/

                }
            }
        });

        bottom.setClickable(false);
        bottom.setEnabled(false);
        changeColours(false);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.cghhk);
    }

    public void changeColours(boolean isComplete) {
        Log.e("iscomplete", "iscomple: " + isComplete);
        if (isComplete) {
            bottom.setClickable(true);
            bottom.setEnabled(true);
            text_user_continue.setClickable(true);
            text_user_continue.setEnabled(true);
            img_user_continue.setClickable(true);
            img_user_continue.setEnabled(true);
            text_user_continue.setTextColor(getResources().getColor(R.color.continueColor));
            img_user_continue.setBackground(getResources().getDrawable(R.drawable.continue_pink, null));
        } else {
            bottom.setClickable(false);
            bottom.setEnabled(false);
            text_user_continue.setClickable(false);
            text_user_continue.setEnabled(false);
            img_user_continue.setClickable(false);
            img_user_continue.setEnabled(false);
            text_user_continue.setTextColor(getResources().getColor(R.color.grayColor));
            img_user_continue.setBackground(getResources().getDrawable(R.drawable.continue_grey, null));
        }
    }
    @Override
    public void onBackPressed() {
        if (isFromSettings)
        {
            finish();
        }
        else
        {
            startActivity(new Intent(this, LoginSignUp.class));
            finish();
        }

    }
 /*   public void setLanguage(String languageID) {
        switch (languageID) {
            case "7254beda-a1d7-4ac2-a870-53e87da8e1ac": //English
                setAppLocale("en");
                *//*Intent intent = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent);*//*
                break;

            case "b0847af4-5f16-4bee-bb9a-1f6bdc79a5d1": //Hindi
                setAppLocale("hi");
                *//*Intent intent2 = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent2);*//*
               *//* context  = LocaleHelper.setLocale(LangaugeActivity.this, "en");
                resources = context.getResources();
                Intent intent = new Intent(LangaugeActivity.this, RegisterProcessScreen.class);
                startActivity(intent);*//*
                Locale locale_en = new Locale("en");
                Locale.setDefault(locale_en);
                Configuration config_en = new Configuration();
                config_en.locale = locale_en;
                context().getResources().updateConfiguration(config_en, context().getResources().getDisplayMetrics());

//                Intent intent = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
//                startActivity(intent);
                Toast.makeText(LangaugeActivity.this, "Locale in English !", Toast.LENGTH_LONG).show();
                break;

            case "b0847af4-5f16-4bee-bb9a-1f6bdc79a5d1": //Hindi
               *//* context  = LocaleHelper.setLocale(LangaugeActivity.this, "hi");
                resources = context.getResources();
                Intent intent2 = new Intent(LangaugeActivity.this, RegisterProcessScreen.class);
                startActivity(intent2);*//*
                Locale locale_hin = new Locale("hi");
                Locale.setDefault(locale_hin);
                Configuration config_hin = new Configuration();
                config_hin.locale = locale_hin;
                context().getResources().updateConfiguration(config_hin, context().getResources().getDisplayMetrics());
                Toast.makeText(LangaugeActivity.this, "Locale in Hindi !", Toast.LENGTH_LONG).show();

//                Intent intent2 = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
//                startActivity(intent2);
                break;

            case "6adc98c2-9b04-414c-9cde-683139ed34e1": //Punjabi
                setAppLocale("pa");
                Intent intent_pa = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_pa);
                break;

            case "568c0b05-23cb-4921-80e4-727878a7f6c2": //Bengali
                setAppLocale("bn");
                Intent intent_bn = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_bn);
                break;

            case "7b6381e1-e21b-49af-a9f6-ba03f1173d88": //Malayalam
                setAppLocale("ml");
                Intent intent_ml = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_ml);
                break;

            case "7bf31330-c8c2-4ece-9d76-5357c463e290": //Urdu
                setAppLocale("ur");
                Intent intent_ur = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_ur);
                break;


            case "bf267bbf-ea14-4005-8fa1-ba74251f24c8": //Tamil
                setAppLocale("ta");
                Intent intent_ta = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_ta);
                break;

            case "d2b8f570-b7e5-4691-ad48-2082db576350": //odia

                setAppLocale("or");
                Intent intent_od = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_od);
                break;


            case "d5943bdb-b8dd-41fc-9123-b46a8d79596f": //kannada
                setAppLocale("kn");
                Intent intent_kn = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_kn);
                break;

            case "e4de2084-7d3b-46fc-9b2c-3a8b140f210c": //assamese
                setAppLocale("as");
                Intent intent_as = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_as);
                break;

            case "ec2ab884-c65e-43fc-9a3a-830e08b37803": //Marathi

                setAppLocale("mr");
                Intent intent_mr = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_mr);
                break;

            case "ec44519d-d7ca-4317-95cb-6c708b9b3d16": //Telegu
                setAppLocale("te");
                Intent intent_te = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_te);
                break;

            case "f96662f6-c67a-4be4-98a6-398daf0128ed": //Gujarati
                setAppLocale("gu");
                Intent intent_gu = new Intent(LangaugeActivity.this, RegisterNameActivity.class);
                startActivity(intent_gu);
                break;

            default:
                //This code is executed when value of variable 'Language'
                //doesn't match with any of case above
                break;
        }

    }*/
    public void setAppLocale(String lang) {
        Locale locale = new Locale(lang);
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, displayMetrics);
    }

}
