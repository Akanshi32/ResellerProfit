package com.vansh.resellerprofit.activity;

/**
 * Created by vansh on 05-Dec-16.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.scwang.wave.MultiWaveHeader;
import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.model.LoginResponse;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;
import com.vansh.resellerprofit.utility.Consts;
import com.vansh.resellerprofit.utility.DialogUtil;
import com.vansh.resellerprofit.utility.Preferences;

import java.util.Arrays;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Glogin extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = Glogin.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private LinearLayout btnSignIn;
    private Button btnSignOut, btnRevokeAccess;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail,mIdTokenTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnSignIn =  findViewById(R.id.btn_sign_in);

//        MultiWaveHeader waveHeader = findViewById(R.id.waveHeader);
//        String[] waves = new String[]{
//                "5,4,1.9,0.2,50"
//        };
//        waveHeader.setWaves(TextUtils.join(" ", Arrays.asList(waves)));
//        waveHeader.setWaveHeight(350);
//        waveHeader.setGradientAngle(170);
//        waveHeader.setProgress(.8f);


        View decorView = getWindow().getDecorView();

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


     /*   btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        mIdTokenTextView = (TextView) findViewById(R.id.mIdTokenTextView);*/

        btnSignIn.setOnClickListener(this);
//        btnSignOut.setOnClickListener(this);
 //       btnRevokeAccess.setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        //setGooglePlusButtonText(btnSignIn,"LOGIN USING GOOGLE");

        // Customizing G+ button
      //  btnSignIn.setSize(SignInButton.SIZE_STANDARD);
      //  btnSignIn.setScopes(gso.getScopeArray());



    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTextColor(Color.BLACK);
                return;
            }
        }
    }



    private void signIn() {
        showProgressDialog();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }



    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            final String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            final String email = acct.getEmail();


            final String idToken = acct.getIdToken();
            // TODO(user): send token to server and validate server-side


           /* txtName.setText(personName);

            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);*/

            Preferences.setPrefs(Consts.TOKEN_SP_KEY,idToken ,Glogin.this);



            final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);
            Call<LoginResponse> loginResponseCall = apiInterface.getResponse(new HashMap<String, String>());
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.body().getMessage().equals("100"))
                    {
                        DialogUtil.createDialog("Please call at +917354273542 to activate the Application", Glogin.this, new DialogUtil.OnPositiveButtonClick() {
                            @Override
                            public void onClick() {
                                finish();
                            }
                        });
                    }
                       else {

                        if (response.body().getSuccess()) {
                            hideProgressDialog();
                            Toast.makeText(getBaseContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            onFinish();


                        } else

                        {
                            Toast.makeText(getBaseContext(), "Welcome", Toast.LENGTH_LONG).show();


                            {
                                Intent it = new Intent(Glogin.this, SignupActivity.class);
                                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                it.putExtra("name", personName);
                                it.putExtra("ema", email);
                                startActivity(it);
                            }

                        }

                    }

                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                }
            });


           // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            Log.e(TAG, "error");

            // updateUI(false);
        }
    }

    public void onFinish() {
        Boolean isFirstRun1=getSharedPreferences("PREFERENCE1",MODE_PRIVATE).getBoolean("isFirstRun1",true);

        if(isFirstRun1)
        {
            //show start activity

            startActivity(new Intent(Glogin.this, CompanyProfile.class));
            Toast.makeText(Glogin.this, "Welcome!", Toast.LENGTH_LONG)
                    .show();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            getSharedPreferences("PREFERENCE1", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun1", false).commit();
        }

        else{

            Intent i = new Intent(Glogin.this, MainActivity.class);
            startActivity(i);
            finish();}
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_revoke_access:
                revokeAccess();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

      /*  OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
    }
}