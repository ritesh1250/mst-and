package com.meest.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pc on 10/7/2016.
 */
public class Preferences {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    private static Preferences instance;

    private static final String login = "login";
    private static final String token = "AUTH_TOKEN";


    private Preferences(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("Meest", 0);
        editor = pref.edit();
    }


    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }


    public void setToken(String cname) {
        editor.putString(token, cname);
        editor.commit();
    }

    public String getToken() {
        return pref.getString(token, "");
    }


    public void setLogin(Boolean cname) {
        editor.putBoolean(login, cname);
        editor.commit();
    }

    public boolean isLogin() {
        return pref.getBoolean(login, false);
    }


}