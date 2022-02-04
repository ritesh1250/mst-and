package com.meest.meestbhart.utilss;

import android.content.Context;
import android.widget.Toast;

import com.meest.R;
import com.meest.utils.CToast;

import java.util.regex.Pattern;

public class Utilss {


    public static String notificationTypeFollowRequest = "FollowerRequest";


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


    public static void showToast(Context context, String msg, int color) throws IllegalStateException {
        try {
            new CToast(context).simpleToast(msg, Toast.LENGTH_LONG)
                    .setBackgroundColor(R.color.social_background_blue)
                    .show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void showToastSuccess(Context context, String msg) {
        new CToast(context).simpleToast(msg, Toast.LENGTH_LONG)
                .setBackgroundColor(R.color.green)
                .show();
    }

    public static void showErrorToast(Context context) {
        new CToast(context).simpleToast("Something went wrong ! Please try again.", Toast.LENGTH_LONG)
                .setBackgroundColor(R.color.social_background_blue)
                .show();
    }
}
