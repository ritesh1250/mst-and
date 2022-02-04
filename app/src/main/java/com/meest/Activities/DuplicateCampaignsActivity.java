package com.meest.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.meest.Fragments.DraftFragment;
import com.meest.Fragments.DublicateFragment;
import com.meest.R;

public class DuplicateCampaignsActivity extends AppCompatActivity {


    private RelativeLayout duplicate,draft,create_new;
    private ImageView img_Duplicate,img_draft;
    private TextView txt_Duplicate,txt_draft,header_duplicate,header_draft;
    private View view_Duplicate,view_draft;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duplicate_campaigns);

        header_draft = findViewById(R.id.txt_draft_header);
        header_duplicate = findViewById(R.id.txt_doublicate_header);

        final Fragment fragment_doublicate = new DublicateFragment();
        final Fragment fragment_draft = new DraftFragment();

        duplicate = findViewById(R.id.layout_dublicate_campaign);
        draft = findViewById(R.id.layout_draft_campaign);
        create_new = findViewById(R.id.layout_create_campaign);


        img_Duplicate = findViewById(R.id.img_dublicate_campaign);
        img_draft = findViewById(R.id.img_draft_campaign);

        view_Duplicate = findViewById(R.id.View_dublicate_Campaign);
        view_draft = findViewById(R.id.View_draft_Campaign);

        txt_draft=findViewById(R.id.txt_draft);
        txt_Duplicate=findViewById(R.id.txt_dublicate);


        create_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getApplicationContext(),CreateNewCampaignActivity.class);
                startActivity(intent);
            }
        });

        duplicate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.framlayout_doublicate_fraft_campaign, fragment_doublicate);
                fragmentTransaction.commit();

                txt_Duplicate.setTextColor(getColor(R.color.blue));
                txt_draft.setTextColor(getColor(R.color.gray));


                header_duplicate.setVisibility(View.VISIBLE);
                header_draft.setVisibility(View.INVISIBLE);


                view_Duplicate.setVisibility(View.VISIBLE);
                view_draft.setVisibility(View.INVISIBLE);

                img_Duplicate.setImageResource(R.drawable.ic_duplicate_select);
                img_draft.setImageResource(R.drawable.ic_draft_campain_icon);

            }
        });


        draft.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.framlayout_doublicate_fraft_campaign, fragment_draft);
                fragmentTransaction.commit();

                txt_draft.setTextColor(getColor(R.color.blue));
                txt_Duplicate.setTextColor(getColor(R.color.gray));

                header_duplicate.setVisibility(View.INVISIBLE);
                header_draft.setVisibility(View.VISIBLE);


                view_Duplicate.setVisibility(View.INVISIBLE);
                view_draft.setVisibility(View.VISIBLE);

                img_Duplicate.setImageResource(R.drawable.ic_duplicate_campaign_icon);
                img_draft.setImageResource(R.drawable.ic_draft_blue_icon);

            }
        });


        if (savedInstanceState == null) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.framlayout_doublicate_fraft_campaign, fragment_doublicate);
            fragmentTransaction.commit();

            txt_Duplicate.setTextColor(getColor(R.color.blue));
            txt_draft.setTextColor(getColor(R.color.gray));

            header_duplicate.setVisibility(View.VISIBLE);
            header_draft.setVisibility(View.INVISIBLE);

            view_Duplicate.setVisibility(View.VISIBLE);
            view_draft.setVisibility(View.INVISIBLE);

            img_Duplicate.setImageResource(R.drawable.ic_duplicate_select);
            img_draft.setImageResource(R.drawable.ic_draft_campain_icon);
        }


    }
}
