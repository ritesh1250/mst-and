package com.meest.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.meest.Activities.SettingActivity;
import com.meest.MainActivity;
import com.meest.R;
import com.meest.social.socialViewModel.utils.SocialPrefrences;
import com.meest.social.socialViewModel.view.home.navigation.MySetting;

import androidx.annotation.Nullable;

import java.util.Locale;

import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_AS;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_BN;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_EN;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_HI;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_MR;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_OD;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_PA;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_TA;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_TE;
import static com.meest.social.socialViewModel.utils.SocialPrefrences.SocialKeyPrams.SOCIAL_LAN_UR;

public class LanguageBottomSheet extends BottomSheetDialogFragment {

    RadioGroup rg_language_group;
    RadioButton rb_eng, rb_hind, rb_punj, rb_bang, rb_urd, rb_marathi, rb_odia, rb_asm, rb_telgu, rb_tamil;
    ImageView close_arrow;
    SharedPreferences sharedPreferences;
    public MySetting settingActivity;
    private SocialPrefrences prefrences;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.language_bottom_sheet,
                container, false);

        sharedPreferences = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        settingActivity = (MySetting) getActivity();

        TextView textview = (TextView) getActivity().findViewById(R.id.txt_launguage);

        rb_eng = v.findViewById(R.id.rb_eng);
        rb_hind = v.findViewById(R.id.rb_hind);
        rb_punj = v.findViewById(R.id.rb_punj);
        rb_bang = v.findViewById(R.id.rb_bang);
        rb_urd = v.findViewById(R.id.rb_urd);
        rb_marathi = v.findViewById(R.id.rb_marathi);
        rb_odia = v.findViewById(R.id.rb_odia);
        rb_asm = v.findViewById(R.id.rb_asm);
        rb_telgu = v.findViewById(R.id.rb_telgu);
        rb_tamil = v.findViewById(R.id.rb_tamil);
        close_arrow = v.findViewById(R.id.close_arrow);
//        Button algo_button = v.findViewById(R.id.algo_button);
//        Button course_button = v.findViewById(R.id.course_button);

//        String lang_eng = SharedPreferencesManager.getLanguage();

        String lang_eng = textview.getText().toString();
        if (lang_eng.equals(SOCIAL_LAN_EN)) {
            rb_eng.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_HI)) {
            rb_hind.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_PA)) {
            rb_punj.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_BN)) {
            rb_bang.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_UR)) {
            rb_urd.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_MR)) {
            rb_marathi.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_OD)) {
            rb_odia.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_AS)) {
            rb_asm.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_TE)) {
            rb_telgu.setChecked(true);
        } else if (lang_eng.equals(SOCIAL_LAN_TA)) {
            rb_tamil.setChecked(true);
        } else {

        }
        rg_language_group = v.findViewById(R.id.rg_language_group);
        rg_language_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = rg_language_group.getCheckedRadioButtonId();
                switch (id) {
                    case R.id.rb_eng:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_EN);
                        String lang_eng = SocialPrefrences.getLang(getActivity());
                        setAppLocale(SOCIAL_LAN_EN);
//                        Intent intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        //  recreateActivity();
                        // Your code
                        break;
                    case R.id.rb_hind:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_HI);
                        String lang_hi = SocialPrefrences.getLang(getActivity());
                        setAppLocale(SOCIAL_LAN_HI);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        // recreateActivity();
                        // Your code
                        break;
                    case R.id.rb_punj:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_PA);
                        String lang_pub = SocialPrefrences.getLang(getActivity());
                        setAppLocale(SOCIAL_LAN_PA);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        //  recreateActivity();
                        // Your code
                        break;
                    case R.id.rb_bang:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_BN);
                        String lang_ban = SocialPrefrences.getLang(getActivity());
                        setAppLocale(SOCIAL_LAN_BN);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        //   recreateActivity();
                        // Your code
                        break;
                    case R.id.rb_urd:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_UR);
                        String lang_ur = SocialPrefrences.getLang(getActivity());
                        setAppLocale(SOCIAL_LAN_UR);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        // Your code
                        break;
                    case R.id.rb_marathi:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_MR);
                        String lang_mr = SocialPrefrences.getLang(getActivity());
                        setAppLocale(SOCIAL_LAN_MR);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        // Your code
                        break;
                    case R.id.rb_odia:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_OD);
                        String lang_od = SocialPrefrences.getLang(getActivity());
                        Log.e("=====lang_name", "" + lang_od);
                        setAppLocale(SOCIAL_LAN_OD);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        // Your code
                        break;
                    case R.id.rb_asm:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_AS);
                        String lang_as = SocialPrefrences.getLang(getActivity());
                        Log.e("=====lang_name", "" + lang_as);
                        setAppLocale(SOCIAL_LAN_AS);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        // Your code
                        break;
                    case R.id.rb_telgu:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_TE);
                        String lang_te = SocialPrefrences.getLang(getActivity());
                        Log.e("=====lang_name", "" + lang_te);
                        setAppLocale(SOCIAL_LAN_TE);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        // Your code
                        break;
                    case R.id.rb_tamil:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_TA);
                        String lang_ta = SocialPrefrences.getLang(getActivity());
                        Log.e("=====lang_name", "" + lang_ta);
                        setAppLocale(SOCIAL_LAN_TA);
//                        intent = getActivity().getIntent();
//                        getActivity().finish();
//                        startActivity(intent);
                        BackMethod();
                        dismiss();
                        // Your code
                        break;
                    default:
                        SocialPrefrences.setSaveLang(getActivity(), SOCIAL_LAN_EN);
                        String lang_den = SocialPrefrences.getLang(getActivity());
                        Log.e("=====lang_name", "" + lang_den);
                        setAppLocale(SOCIAL_LAN_EN);
                        dismiss();
                        // Your code
                        break;
                }
            }
        });

        return v;
    }

    public void BackMethod() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(getActivity(), MainActivity.class);
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

}
