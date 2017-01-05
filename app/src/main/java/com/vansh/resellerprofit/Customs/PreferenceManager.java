package com.vansh.resellerprofit.Customs;

import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared Preference file name
    private static final String PREF_NAME="intro_slide_date";
    //This is my key value to which we will give corresponding value's boolean value.
    //Think in term of key-value pair
    private static final String IS_FIRST_TIME_LAUNCH="IsFisrstTimeLaunch";

    public PreferenceManager(Context context)
    {
        this._context=context;
        pref=_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,true);
        editor.commit();
    }

    public boolean ifFirstTimeLaunch(){
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }

}
