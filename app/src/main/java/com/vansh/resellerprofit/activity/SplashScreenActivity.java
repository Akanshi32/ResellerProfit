package com.vansh.resellerprofit.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.vansh.resellerprofit.Customs.RevealActivity;
import com.vansh.resellerprofit.R;

public class SplashScreenActivity extends RevealActivity {


    private final int SPLASH_DISPLAY_DURATION = 2000;
    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        showRevealEffect(mSavedInstanceState, findViewById(R.id.activity_splash_screen));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                destroyActivity(findViewById(R.id.activity_splash_screen));
            }
        }, 3000);

          }


    @Override
    public void destroyAnimationFinished() {
        super.destroyAnimationFinished();
        onFinish();
    }

    public void onFinish() {
        Boolean isFirstRun=getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isFirstRun",true);

        if(isFirstRun)
        {
            //show start activity

            startActivity(new Intent(SplashScreenActivity.this, IntroSliderActivity.class));
            Toast.makeText(SplashScreenActivity.this, "Welcome!", Toast.LENGTH_LONG)
                    .show();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).commit();
        }

        else{

            Intent i = new Intent(SplashScreenActivity.this, Glogin.class);
            startActivity(i);
            finish();}
    }
}

