package com.meest.videomvvmmodule.view.share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.meest.R;
import com.meest.videomvvmmodule.model.user.User;
import com.meest.videomvvmmodule.model.videos.Video;
import com.meest.videomvvmmodule.utils.CustomDialogBuilder;
import com.meest.videomvvmmodule.utils.Global;
import com.meest.videomvvmmodule.view.base.BaseActivity;
import com.meest.videomvvmmodule.view.home.MainVideoActivity;
import com.meest.videomvvmmodule.view.search.FetchUserActivity;
import com.meest.videomvvmmodule.view.video.PlayerActivity;

import org.json.JSONException;

import java.util.ArrayList;

import io.branch.referral.Branch;

public class ShareHandleActivity extends BaseActivity {

    private CustomDialogBuilder customDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_handle);


        customDialogBuilder = new CustomDialogBuilder(this);
        customDialogBuilder.showLoadingDialog();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Global.apikey = sessionManager.getStringValue("ApiKey");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Global.apikey = (String) snapshot.getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        database.getReference().child("ApiKey").addValueEventListener(valueEventListener);
        getDataFromBranch();
    }

    private void getDataFromBranch() {

        Branch branch = Branch.getInstance();

        // Branch init
        branch.initSession((referringParams, error) -> {
            if (error == null) {
                if (referringParams != null) {
                    Log.i("BRANCH SDK", referringParams.toString());
                }

                try {
                    if (referringParams != null && referringParams.has("data")) {
                        customDialogBuilder.hideLoadingDialog();

                        String data = referringParams.getString("data");

                        User user = new Gson().fromJson(data, User.class);
                        Video.Data video = new Gson().fromJson(data, Video.Data.class);
                        if (user != null && user.getData() != null) {
                            Intent intent = new Intent(ShareHandleActivity.this, FetchUserActivity.class);
                            intent.putExtra("userid", user.getData().getUserId());
                            startActivity(intent);
                        } else if (video != null) {
                            Intent intent = new Intent(ShareHandleActivity.this, PlayerActivity.class);
                            ArrayList<Video.Data> mList = new ArrayList<>();
                            mList.add(video);
                            intent.putExtra("video_list", new Gson().toJson(mList));
                            intent.putExtra("position", 0);
                            intent.putExtra("type", 5);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(ShareHandleActivity.this, MainVideoActivity.class));
                            finish();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.i("BRANCH SDK", error.getMessage());
            }
        }, this.getIntent().getData(), this);
    }
}