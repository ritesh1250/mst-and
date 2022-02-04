package com.meest.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.meest.meestbhart.register.fragment.username.model.VerifyUsernameParam;
import com.meest.R;
import com.meest.meestbhart.register.fragment.username.model.VerifyUserNameResponse;
import com.meest.meestbhart.utilss.ApiUtils;
import com.meest.meestbhart.utilss.Utilss;
import com.meest.meestbhart.utilss.WebApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateAccountActivity extends AppCompatActivity {
    TextView txt_login, txt_error, txt_msg_title1, txt_msg_title2, txt_msg_title3, txt_msg_title4;
    EditText edit_username, edit_email, edit_mobile, edit_pass;
    View view_1, view_2, view_3, view_4;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account_activity);


        progressDialog = new ProgressDialog(CreateAccountActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));


        txt_login = findViewById(R.id.txt_login);
        txt_error = findViewById(R.id.txt_error);
        edit_username = findViewById(R.id.edit_username);
        edit_email = findViewById(R.id.edit_email);
        edit_mobile = findViewById(R.id.edit_mobile);
        edit_pass = findViewById(R.id.edit_pass);

        txt_msg_title1 = findViewById(R.id.txt_msg_title1);
        txt_msg_title2 = findViewById(R.id.txt_msg_title2);
        txt_msg_title3 = findViewById(R.id.txt_msg_title3);
        txt_msg_title4 = findViewById(R.id.txt_msg_title4);

        view_1 = findViewById(R.id.view_1);
        view_2 = findViewById(R.id.view_2);
        view_3 = findViewById(R.id.view_3);
        view_4 = findViewById(R.id.view_4);


        edit_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    txt_msg_title1.setVisibility(View.VISIBLE);
                    view_1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.blue));
                } else {
                    view_1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.edit_line_gray));
                    txt_msg_title1.setVisibility(View.GONE);
                }
            }
        });


        edit_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    txt_msg_title2.setVisibility(View.VISIBLE);
                    view_2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.blue));
                } else {
                    view_2.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.edit_line_gray));
                    txt_msg_title2.setVisibility(View.GONE);
                }
            }
        });


        edit_mobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    txt_msg_title3.setVisibility(View.VISIBLE);
                    view_3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.blue));
                } else {
                    view_3.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.edit_line_gray));
                    txt_msg_title3.setVisibility(View.GONE);
                }
            }
        });


        edit_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    txt_msg_title4.setVisibility(View.VISIBLE);
                    view_4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.blue));
                } else {
                    view_4.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.edit_line_gray));
                    txt_msg_title4.setVisibility(View.GONE);
                }
            }
        });


        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edit_username.getText().toString().isEmpty()) {
                    edit_username.setError("Enter Username");
                } else if (edit_email.getText().toString().isEmpty()) {
                    edit_email.setError("Enter Email");
                } else if (edit_mobile.getText().toString().isEmpty()) {
                    edit_mobile.setError("Enter Mobile");
                } else if (edit_pass.getText().toString().isEmpty()) {
                    edit_pass.setError("Enter Password");
                } else {


                    progressDialog.show();
                    VerifyUsernameParam verifyParam = new VerifyUsernameParam(edit_username.getText().toString());
                    WebApi webApi = ApiUtils.getClient().create(WebApi.class);
                    Call<VerifyUserNameResponse> call = webApi.verifyUsername(verifyParam);
                    call.enqueue(new Callback<VerifyUserNameResponse>() {
                        @Override
                        public void onResponse(Call<VerifyUserNameResponse> call, Response<VerifyUserNameResponse> response) {
                            progressDialog.dismiss();
                            if (!response.body().getData().getMessage().equalsIgnoreCase("Username does not exist")) {
                                txt_error.setText(response.body().getData().getMessage().toString());
                                // view_1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.error));
                            } else {

                                progressDialog.show();
//                                VerifyParam verifyParam = new VerifyParam(edit_email.getText().toString());
//                                WebApi  webApi = ApiUtils.getClient().create(WebApi.class);
//                                Call<VerifyEmailResponse> call1 = webApi.verifyEmail(verifyParam);
//                                call1.enqueue(new Callback<VerifyEmailResponse>() {
//                                    @Override
//                                    public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
//                                        progressDialog.dismiss();
//                                        if (!response.body().getData().getMessage().equalsIgnoreCase("Email does not exist")){
//                                            txt_error.setText(response.body().getData().getMessage().toString());
//
//                                            //view_1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.error));
//                                        }else {
//
//
//                                            VerifiyMobileParam verifiyMobileParam = new VerifiyMobileParam(edit_mobile.getText().toString());
//                                            WebApi  webApi = ApiUtils.getClient().create(WebApi.class);
//                                            Call<VerifyEmailResponse> call2 = webApi.verifyMobile(verifiyMobileParam);
//                                            call2.enqueue(new Callback<VerifyEmailResponse>() {
//                                                @Override
//                                                public void onResponse(Call<VerifyEmailResponse> call, Response<VerifyEmailResponse> response) {
//                                                    progressDialog.dismiss();
//                                                    if (response.body().getData().getMessage().equalsIgnoreCase("Mobile does not exist")){
//
//                                                        Intent intent = new Intent(CreateAccountActivity.this,GenderActivity.class);
//                                                        intent.putExtra("username",edit_username.getText().toString());
//                                                        intent.putExtra("email",edit_email.getText().toString());
//                                                        intent.putExtra("mob",edit_mobile.getText().toString());
//                                                        intent.putExtra("password",edit_pass.getText().toString());
//                                                        startActivity(intent);
//
//                                                    }else {
//
//                                                        txt_error.setText(response.body().getData().getMessage().toString());
//                                                        view_1.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.error));
//
//                                                    }
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
//                                                    progressDialog.dismiss();
//                                                    Utilss.showToast(getApplicationContext(),"Server Error",R.color.msg_fail);
//                                                }
//                                            });
//


//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<VerifyEmailResponse> call, Throwable t) {
//                                        progressDialog.dismiss();
//                                        Utilss.showToast(getApplicationContext(),"Server Error",R.color.msg_fail);
//                                    }
//                                });


                            }
                        }

                        @Override
                        public void onFailure(Call<VerifyUserNameResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Utilss.showToast(getApplicationContext(), getString(R.string.error_msg), R.color.grey);
                        }
                    });


                }

            }
        });


    }
}
