package com.meest.videomvvmmodule.view.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.meest.R;

public class MusicActivity extends AppCompatActivity {

    MusicFrameFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container_frag_regis_side, fragment,fragment.getClass().getName());
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }
}