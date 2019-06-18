package com.vansh.resellerprofit.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static void setPrefs(String key, String value, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Consts.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPrefs(String key, Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(Consts.SP_NAME, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key, "notfound");
    }
}
