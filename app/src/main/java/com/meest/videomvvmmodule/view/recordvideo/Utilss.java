package com.meest.videomvvmmodule.view.recordvideo;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.meest.R;
import com.meest.videomvvmmodule.view.home.LoginSignUpBottomSheet;

import java.util.regex.Pattern;

public class Utilss {

    public static String postId = "";

    public static boolean isValidMail(String email) {

        String EMAIL_STRING = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(EMAIL_STRING).matcher(email).matches();

    }


    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 10;
        }
        return false;
    }


    public static void showToast(Context context, String msg, int color) {
        new CToast(context).simpleToast(msg, Toast.LENGTH_LONG)
                .setBackgroundColor(color)
                .show();
    }

    public static void showErrorToast(Context context) {
        new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_LONG)
                .setBackgroundColor(R.color.msg_fail)
                .show();
    }

    public static void callLoginSign(Context context) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager manager = activity.getSupportFragmentManager();
        LoginSignUpBottomSheet bottomSheet = new LoginSignUpBottomSheet();
        bottomSheet.show(manager, bottomSheet.getClass().getSimpleName());

    }

    public static long enqueue = 0L;
}
