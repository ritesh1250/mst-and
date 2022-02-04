package com.meest.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.meest.Adapters.BlockAccountAdapter;
import com.meest.Paramaters.RemoveBlockedUserParameter;
import com.meest.R;
import com.meest.social.socialViewModel.model.RemoveBlockedUserResponse;
import com.meest.responses.UserBlockListResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BlockActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BlockAccountAdapter adapter;
    RecyclerView.LayoutManager manager;
    LottieAnimationView image;
    ConstraintLayout blockUserLayout;
    ImageView backArrow, iv_block;

    private void initViews() {
        image = findViewById(R.id.loading);
        backArrow = findViewById(R.id.img_back_arrow_block_Account);
        iv_block = findViewById(R.id.iv_block_user);
        blockUserLayout = findViewById(R.id.blockUserLayout);

        recyclerView = findViewById(R.id.recycler_block_account);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        image.setAnimation("loading.json");
        image.playAnimation();
        image.loop(true);
    }

    private void showHideLoader(boolean showLoader) {
        if (showLoader) {
            if (image.getVisibility() != View.VISIBLE) {
                image.setVisibility(View.VISIBLE);
            }
        } else {
            image.setVisibility(View.GONE);
        }
    }

    private void showHide(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.VISIBLE);
            blockUserLayout.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            blockUserLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked__accounts_);
        initViews();
        showHide(false);
        getDataFromServer();
    }

    private void getDataFromServer() {
        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Map<String, String> header = new HashMap<>();
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));

        Call<UserBlockListResponse> call = webApi.getBlockedUserList(header);
        showHideLoader(true);
        call.enqueue(new Callback<UserBlockListResponse>() {
            @Override
            public void onResponse(Call<UserBlockListResponse> call, Response<UserBlockListResponse> response) {
                showHideLoader(false);
                if (response.code() == 200) {
                    setAdapter(response.body().getData().getRows());
                } else {
                    Utilss.showToast(BlockActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<UserBlockListResponse> call, Throwable t) {
                showHideLoader(false);
                Utilss.showToast(BlockActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    private void setAdapter(List<UserBlockListResponse.Row> rows) {
        if (rows.size() > 0) {
            showHide(true);
            recyclerView.setHasFixedSize(true);
            adapter = new BlockAccountAdapter(rows);
            manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
            adapter.setItemClickListener(new BlockAccountAdapter.onItemClickListener() {
                @Override
                public void onItemClick(int position, String userId) {
                    createUnblockPopup(userId);
                }
            });
        } else {
            showHide(false);
        }
    }

    private void unblockUser(String userId) {
        try {
            WebApi webApi = ApiUtils.getClient().create(WebApi.class);
            Map<String, String> header = new HashMap<>();
            header.put("x-token", SharedPrefreances.getSharedPreferenceString(getApplicationContext(), SharedPrefreances.AUTH_TOKEN));
            RemoveBlockedUserParameter parameter = new RemoveBlockedUserParameter(userId);
            Call<RemoveBlockedUserResponse> call = webApi.removeBlockedUser(header, parameter);
            showHideLoader(true);
            call.enqueue(new Callback<RemoveBlockedUserResponse>() {
                @Override
                public void onResponse(Call<RemoveBlockedUserResponse> call, Response<RemoveBlockedUserResponse> response) {
                    showHideLoader(false);
                    getDataFromServer();
                }

                @Override
                public void onFailure(Call<RemoveBlockedUserResponse> call, Throwable t) {
                    showHideLoader(false);
                    Utilss.showToast(BlockActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AlertDialog alertDialog = null;

    private void createUnblockPopup(String userId) {
        try {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            final View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_diloag_unblock_user, viewGroup, false);
            Button okBtn = dialogView.findViewById(R.id.okBtn);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    unblockUser(userId);
                    alertDialog.cancel();
                }
            });
            builder.setView(dialogView);
            alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
