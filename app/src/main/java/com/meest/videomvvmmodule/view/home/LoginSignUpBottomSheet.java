package com.meest.videomvvmmodule.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.meest.R;
import com.meest.social.socialViewModel.view.login.LoginSignUp;


public class LoginSignUpBottomSheet extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_or_sign_up_sheet,
                container, false);

        TextView signup = v.findViewById(R.id.signup_btn);
        Button login = v.findViewById(R.id.login_btn);

        signup.setOnClickListener(v1 -> {
            getContext().startActivity(new Intent(getContext(), LoginSignUp.class));
            dismiss();
        });

        login.setOnClickListener(v12 -> {
            getContext().startActivity(new Intent(getContext(), LoginSignUp.class));
            dismiss();
        });

        v.findViewById(R.id.img_close).setOnClickListener(v12 -> {
            dismiss();
        });
        return v;
    }
}