package com.meest.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.R;
import com.meest.responses.RegisterResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;
import com.meest.social.socialViewModel.view.login.LoginSignUp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GenderActivity extends AppCompatActivity {
    ImageView img_2, img_1;
    RelativeLayout layout_2, layout_1;
    TextView txt_login;
    ProgressDialog progressDialog;
    String gentder = getString(R.string.male);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gendera_activity);
        layout_2 = findViewById(R.id.action_camera);
        layout_1 = findViewById(R.id.action_gallery);

        progressDialog = new ProgressDialog(GenderActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));


        img_2 = findViewById(R.id.img_2);
        img_1 = findViewById(R.id.img_1);
        txt_login = findViewById(R.id.txt_login);

        layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_2.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gender_white));
                layout_1.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gender_simple));

                img_1.setVisibility(View.GONE);
                img_2.setVisibility(View.VISIBLE);
                gentder = "Female";
            }
        });

        layout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_1.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gender_white));
                layout_2.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.gender_simple));

                img_2.setVisibility(View.GONE);
                img_1.setVisibility(View.VISIBLE);
                gentder = getString(R.string.male);
            }
        });


        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

//                RegisteP registeP = new RegisteP("testq","tests", getIntent().getStringExtra("password"),
//                        getIntent().getStringExtra("username"),gentder,getIntent().getStringExtra("mob"),getIntent().getStringExtra("email"),"true","1","");

                WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                Call<RegisterResponse> call = webApi.register(null);
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                        if (response.code() == 500) {
                            Utilss.showToast(getApplicationContext(), getString(R.string.Failed), R.color.msg_fail);
                        } else {
                            Intent intent = new Intent(GenderActivity.this, LoginSignUp.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                    }
                });

            }
        });
    }
}
