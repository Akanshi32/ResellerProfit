package com.vansh.resellerprofit.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
        Intent intent = new Intent(SplashScreenActivity.this, Glogin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}

