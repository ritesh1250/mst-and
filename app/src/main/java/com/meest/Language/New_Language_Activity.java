package com.meest.Language;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.meest.Activities.SettingActivity;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.chat_calling.utils.SharedPreferencesManager;

import java.util.Locale;

public class New_Language_Activity extends AppCompatActivity {
    RadioGroup rg_language_group;
    RadioButton rb_eng, rb_hind, rb_punj, rb_bang, rb_urd, rb_marathi, rb_odia, rb_asm, rb_telgu, rb_tamil;
    ImageView back_arrow_lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__language);
        rb_eng = findViewById(R.id.rb_eng);
        rb_hind = findViewById(R.id.rb_hind);
        rb_punj = findViewById(R.id.rb_punj);
        rb_bang = findViewById(R.id.rb_bang);
        rb_urd = findViewById(R.id.rb_urd);
        rb_marathi = findViewById(R.id.rb_marathi);
        rb_odia = findViewById(R.id.rb_odia);
        rb_asm = findViewById(R.id.rb_asm);
        rb_telgu = findViewById(R.id.rb_telgu);
        rb_tamil = findViewById(R.id.rb_tamil);
        back_arrow_lang = findViewById(R.id.back_arrow_lang);

        String lang_eng = SharedPreferencesManager.getLanguage();
        if (lang_eng.equals("en")) {
            rb_eng.setChecked(true);
        } else if (lang_eng.equals("hi")) {
            rb_hind.setChecked(true);
        } else if (lang_eng.equals("pa")) {
            rb_punj.setChecked(true);
        } else if (lang_eng.equals("bn")) {
            rb_bang.setChecked(true);
        } else if (lang_eng.equals("ur")) {
            rb_urd.setChecked(true);
        } else if (lang_eng.equals("ma")) {
            rb_marathi.setChecked(true);
        } else if (lang_eng.equals("or")) {
            rb_odia.setChecked(true);
        } else if (lang_eng.equals("as")) {
            rb_asm.setChecked(true);
        } else if (lang_eng.equals("te")) {
            rb_telgu.setChecked(true);
        } else if (lang_eng.equals("ta")) {
            rb_tamil.setChecked(true);
        } else {
            Log.e("==========", "Not Language");
        }

        rg_language_group = (RadioGroup) findViewById(R.id.rg_language_group);
        rg_language_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = rg_language_group.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.rb_eng:
                        SharedPreferencesManager.save_Language("en");
                        String lang_eng = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_eng);
                        setAppLocale("en");
                        BackMethod();
                        //  recreateActivity();
                        // Your code
                        break;
                    case R.id.rb_hind:
                        SharedPreferencesManager.save_Language("hi");
                        String lang_hi = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_hi);
                        setAppLocale("hi");
                        BackMethod();
                        // recreateActivity();
                        // Your code
                        break;
                    case R.id.rb_punj:
                        SharedPreferencesManager.save_Language("pa");
                        String lang_pub = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_pub);
                        setAppLocale("pa");
                        BackMethod();
                        //  recreateActivity();
                        // Your code
                        break;
                    case R.id.rb_bang:
                        SharedPreferencesManager.save_Language("bn");
                        String lang_ban = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_ban);
                        setAppLocale("bn");
                        BackMethod();
                        //   recreateActivity();
                        // Your code
                        break;
                    case R.id.rb_urd:
                        SharedPreferencesManager.save_Language("ur");
                        String lang_ur = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_ur);
                        setAppLocale("ur");
                        BackMethod();
                        // Your code
                        break;
                    case R.id.rb_marathi:
                        SharedPreferencesManager.save_Language("mr");
                        String lang_mr = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_mr);
                        setAppLocale("mr");
                        BackMethod();
                        // Your code
                        break;
                    case R.id.rb_odia:
                        SharedPreferencesManager.save_Language("or");
                        String lang_od = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_od);
                        setAppLocale("or");
                        BackMethod();
                        // Your code
                        break;
                    case R.id.rb_asm:
                        SharedPreferencesManager.save_Language("as");
                        String lang_as = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_as);
                        setAppLocale("as");
                        BackMethod();
                        // Your code
                        break;
                    case R.id.rb_telgu:
                        SharedPreferencesManager.save_Language("te");
                        String lang_te = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_te);
                        setAppLocale("te");
                        BackMethod();
                        // Your code
                        break;
                    case R.id.rb_tamil:
                        SharedPreferencesManager.save_Language("ta");
                        String lang_ta = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_ta);
                        setAppLocale("ta");
                        BackMethod();
                        // Your code
                        break;
                    default:
                        SharedPreferencesManager.save_Language("en");
                        String lang_den = SharedPreferencesManager.getLanguage();
                        Log.e("=====lang_name", "" + lang_den);
                        setAppLocale("en");
                        BackMethod();
                        // Your code
                        break;
                }
            }
        });
        back_arrow_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /*   @Override
       public void onBackPressed() {
           Log.d("CDA", "onBackPressed Called");
           Intent intent = new Intent(New_Language_Activity.this, MainActivity.class);
           startActivity(intent);
           finish();
       }*/
    public void BackMethod() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(New_Language_Activity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setAppLocale(String localeCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public void recreateActivity() {
        Intent intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}