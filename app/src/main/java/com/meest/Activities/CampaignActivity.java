package com.meest.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.meest.Fragments.CampaignCompletedFragment;
import com.meest.Fragments.CampaignDraftedFragment;
import com.meest.Fragments.CampaignRunningFragment;
import com.meest.R;
import com.meest.responses.CampaignAccountOverviewResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.SharedPrefreances;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CampaignActivity extends AppCompatActivity {

    ImageView imvDotCompleted, imvDotRunning, imvDotCompletedGrey, imvDotRunningGrey;
    CampaignAccountOverviewResponse campaignAccountOverviewResponse;
    FrameLayout fragmentCampView;
    TextView txtSpentRs, tvPeopleReachedCount;
    ImageView img_back_campaign, create_new_campaign;
    TextView tvAccountOverview, tvNumComplete, tvNumRunning;
    TextView txt_Inactive, txt_active;
    View viewTabCompleted, viewTabRunning, viewTabDraft;
    LinearLayout tvCompleted, tvRunning, tvDrafted;
    TextView tabCompleted, tabRunning, tabDraft;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);

        tabCompleted = findViewById(R.id.tabCompleted);
        tabRunning = findViewById(R.id.tabRunning);
        tabDraft = findViewById(R.id.tabDraft);
        viewTabCompleted = findViewById(R.id.viewTabCompleted);
        viewTabRunning = findViewById(R.id.viewTabRunning);
        viewTabDraft = findViewById(R.id.viewTabDraft);
        txt_Inactive = findViewById(R.id.txt_Inactive);
        txt_active = findViewById(R.id.txt_active);
        imvDotCompletedGrey = findViewById(R.id.imvDotCompletedGrey);
        imvDotRunningGrey = findViewById(R.id.imvDotRunningGrey);
        tvNumComplete = findViewById(R.id.tvNumComplete);
        tvNumRunning = findViewById(R.id.tvNumRunning);
        imvDotCompleted = findViewById(R.id.imvDotCompleted);
        imvDotRunning = findViewById(R.id.imvDotRunning);

        tvAccountOverview = findViewById(R.id.tvAccountOverview);
        create_new_campaign = (ImageView) findViewById(R.id.img_creatnew_campaign);
        create_new_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///////////////////////////////////
//                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CampaignActivity.this, R.style.CustomAlertDialog);
//                ViewGroup viewGroup = findViewById(android.R.id.content);
//                final View dialogView = LayoutInflater.from(CampaignActivity.this).inflate(R.layout.custom_layout_ad_kind, viewGroup, false);
//                LinearLayout llAdTypeHomeFeed=dialogView.findViewById(R.id.llAdTypeHomeFeed);
//                LinearLayout llAdTypeVideoFeed=dialogView.findViewById(R.id.llAdTypeVideoFeed);
//                builder.setView(dialogView);
//                final android.app.AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//
//                llAdTypeHomeFeed.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                        final android.app.AlertDialog.Builder build = new android.app.AlertDialog.Builder(CampaignActivity.this, R.style.CustomAlertDialog);
//                        ViewGroup viewGroup = findViewById(android.R.id.content);
//                        final View dV = LayoutInflater.from(CampaignActivity.this).inflate(R.layout.custom_ad_type_selection, viewGroup, false);
//                        build.setView(dV);
//                        final android.app.AlertDialog aD = build.create();
//                        aD.show();
//                        LinearLayout llAdTypeCPC=dV.findViewById(R.id.llAdTypeCPC);
//                        LinearLayout llAdTypeCPL=dV.findViewById(R.id.llAdTypeCPL);
//                        LinearLayout llAdTypeCPV=dV.findViewById(R.id.llAdTypeCPV);
//
//                        llAdTypeCPC.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(CampaignActivity.this, CreateNewCampaignActivity.class);
//                                intent.putExtra("type","CPC");
//                                startActivity(intent);
//                                llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.lan_gray));
//                                llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.white));
//                                llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.white));
//                                aD.dismiss();
//                            }
//                        });
//                        llAdTypeCPL.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(CampaignActivity.this,CreateNewCampaignActivity.class);
//                                intent.putExtra("type","CPL");
//                                startActivity(intent);
//                                llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.lan_gray));
//                                llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.white));
//                                llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.white));
//                                aD.dismiss();
//                            }
//                        });
//                        llAdTypeCPV.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(CampaignActivity.this,CreateNewCampaignActivity.class);
//                                intent.putExtra("type","CPV");
//                                startActivity(intent);
//                                llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.lan_gray));
//                                llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.white));
//                                llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.white));
//                                aD.dismiss();
//                            }
//                        });
//                    }
//                });
//                llAdTypeVideoFeed.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                       Intent intent=new Intent(CampaignActivity.this,TikTokAdManagerActivity.class);
////                       startActivity(intent);
//
//                        alertDialog.dismiss();
//                        final android.app.AlertDialog.Builder build = new android.app.AlertDialog.Builder(CampaignActivity.this, R.style.CustomAlertDialog);
//                        ViewGroup viewGroup = findViewById(android.R.id.content);
//                        final View dV = LayoutInflater.from(CampaignActivity.this).inflate(R.layout.custom_ad_type_selection, viewGroup, false);
//                        build.setView(dV);
//                        final android.app.AlertDialog aD = build.create();
//                        aD.show();
//                        LinearLayout llAdTypeCPC=dV.findViewById(R.id.llAdTypeCPC);
//                        LinearLayout llAdTypeCPL=dV.findViewById(R.id.llAdTypeCPL);
//                        LinearLayout llAdTypeCPV=dV.findViewById(R.id.llAdTypeCPV);
//
//                        llAdTypeCPC.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(CampaignActivity.this, CreateCampaignTiktok.class);
//                                intent.putExtra("type","CPC");
//                                startActivity(intent);
//                                llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.lan_gray));
//                                llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.white));
//                                llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.white));
//                                aD.dismiss();
//                            }
//                        });
//                        llAdTypeCPL.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(CampaignActivity.this,CreateCampaignTiktok.class);
//                                intent.putExtra("type","CPL");
//                                startActivity(intent);
//                                llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.lan_gray));
//                                llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.white));
//                                llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.white));
//                                aD.dismiss();
//                            }
//                        });
//                        llAdTypeCPV.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(CampaignActivity.this,CreateCampaignTiktok.class);
//                                intent.putExtra("type","CPV");
//                                startActivity(intent);
//                                llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.lan_gray));
//                                llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.white));
//                                llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.white));
//                                aD.dismiss();
//                            }
//                        });
//
//                    }
//                });


                /////////////////////////////////

                final AlertDialog.Builder build = new AlertDialog.Builder(CampaignActivity.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                final View dV = LayoutInflater.from(CampaignActivity.this).inflate(R.layout.custom_ad_type_selection, viewGroup, false);
                build.setView(dV);
                final AlertDialog aD = build.create();
                aD.show();
                LinearLayout llAdTypeCPC = dV.findViewById(R.id.llAdTypeCPC);
                LinearLayout llAdTypeCPL = dV.findViewById(R.id.llAdTypeCPL);
                LinearLayout llAdTypeCPV = dV.findViewById(R.id.llAdTypeCPV);

                llAdTypeCPC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CampaignActivity.this, CreateNewCampaignActivity.class);
                        intent.putExtra("type", "CPC");
                        startActivity(intent);
                        llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.lan_gray));
                        llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.white));
                        llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.white));
                        aD.dismiss();
                    }
                });
                llAdTypeCPL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CampaignActivity.this, CreateNewCampaignActivity.class);
                        intent.putExtra("type", "CPL");
                        startActivity(intent);
                        llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.lan_gray));
                        llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.white));
                        llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.white));
                        aD.dismiss();
                    }
                });
                llAdTypeCPV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CampaignActivity.this, CreateNewCampaignActivity.class);
                        intent.putExtra("type", "CPV");
                        startActivity(intent);
                        llAdTypeCPV.setBackgroundColor(getResources().getColor(R.color.lan_gray));
                        llAdTypeCPC.setBackgroundColor(getResources().getColor(R.color.white));
                        llAdTypeCPL.setBackgroundColor(getResources().getColor(R.color.white));
                        aD.dismiss();
                    }
                });


            }
        });


//            }
//        });


        tvPeopleReachedCount = findViewById(R.id.tvPeopleReachedCount);
        txtSpentRs = findViewById(R.id.txtSpentRs);

        img_back_campaign = findViewById(R.id.img_back_campaign);

        img_back_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fragmentCampView = findViewById(R.id.fragmentCampView);
        tvCompleted = findViewById(R.id.tvCompleted);
        tvRunning = findViewById(R.id.tvRunning);
        tvDrafted = findViewById(R.id.tvDrafted);

        pushFragment(new CampaignCompletedFragment(), "");

        tvDrafted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragment(new CampaignDraftedFragment(), "");
                tabDraft.setTextColor(getResources().getColor(R.color.blue));
                tabCompleted.setTextColor(getResources().getColor(R.color.lan_gray));
                tabRunning.setTextColor(getResources().getColor(R.color.lan_gray));
                viewTabDraft.setVisibility(View.VISIBLE);
                viewTabRunning.setVisibility(View.GONE);
                viewTabCompleted.setVisibility(View.GONE);

            }
        });

        tvCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragment(new CampaignCompletedFragment(), "");
                tabCompleted.setTextColor(getResources().getColor(R.color.blue));
                tabRunning.setTextColor(getResources().getColor(R.color.lan_gray));
                tabDraft.setTextColor(getResources().getColor(R.color.lan_gray));

                viewTabCompleted.setVisibility(View.VISIBLE);
                viewTabRunning.setVisibility(View.GONE);
                viewTabDraft.setVisibility(View.GONE);
            }
        });

        tvRunning.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pushFragment(new CampaignRunningFragment(), "");
                tabRunning.setTextColor(getResources().getColor(R.color.blue));
                tabCompleted.setTextColor(getResources().getColor(R.color.lan_gray));
                tabDraft.setTextColor(getResources().getColor(R.color.lan_gray));

                viewTabRunning.setVisibility(View.VISIBLE);
                viewTabDraft.setVisibility(View.GONE);
                viewTabCompleted.setVisibility(View.GONE);
            }
        });

        fetchData();
    }

    private void fetchData() {
        Map<String, String> header = new HashMap<>();
        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("x-token", SharedPrefreances.getSharedPreferenceString(this, SharedPrefreances.AUTH_TOKEN));
        HashMap<String, String> body = new HashMap<>();
        body.put("accountOverview", "LIFETIME");

        WebApi webApi = ApiUtils.getClient().create(WebApi.class);
        Call<CampaignAccountOverviewResponse> call = webApi.getCampAccountOverview(header, body);
        call.enqueue(new Callback<CampaignAccountOverviewResponse>() {
            @Override
            public void onResponse(Call<CampaignAccountOverviewResponse> call, Response<CampaignAccountOverviewResponse> response) {

                if (response.code() == 200 && response.body().getSuccess()) {

                    campaignAccountOverviewResponse = response.body();
                    txtSpentRs.setText(campaignAccountOverviewResponse.getData().get(0).getTotalSpent().toString());
                    tvPeopleReachedCount.setText(campaignAccountOverviewResponse.getData().get(0).getPeoplesReached().toString());
                    tvNumComplete.setText(campaignAccountOverviewResponse.getData().get(0).getCompleted().toString());
                    tvNumRunning.setText(campaignAccountOverviewResponse.getData().get(0).getRunnung().toString());

                    if (campaignAccountOverviewResponse.getData().get(0).getCompleted() != 0 && campaignAccountOverviewResponse.getData().get(0).getCompleted() > 0) {
                        txt_active.setText("Completed");
                        imvDotRunningGrey.setVisibility(View.GONE);
                        imvDotCompleted.setVisibility(View.VISIBLE);
                        tvNumComplete.setVisibility(View.VISIBLE);
                    } else {
                        imvDotCompleted.setVisibility(View.GONE);
                        imvDotRunningGrey.setVisibility(View.VISIBLE);
                        tvNumComplete.setVisibility(View.GONE);
                    }

                    if (campaignAccountOverviewResponse.getData().get(0).getRunnung() != 0 && campaignAccountOverviewResponse.getData().get(0).getRunnung() > 0) {
                        txt_Inactive.setText("Running");
                        imvDotRunning.setVisibility(View.VISIBLE);
                        imvDotCompletedGrey.setVisibility(View.GONE);
                        tvNumRunning.setVisibility(View.VISIBLE);

                    } else {
                        imvDotCompletedGrey.setVisibility(View.VISIBLE);
                        imvDotRunning.setVisibility(View.GONE);
                        tvNumRunning.setVisibility(View.GONE);
                    }

                } else {
                    Utilss.showToast(CampaignActivity.this, getString(R.string.error_msg), R.color.grey);
                }
            }

            @Override
            public void onFailure(Call<CampaignAccountOverviewResponse> call, Throwable t) {

                Utilss.showToast(CampaignActivity.this, getString(R.string.error_msg), R.color.grey);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public boolean pushFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentCampView, fragment, tag)
                    .addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }
}