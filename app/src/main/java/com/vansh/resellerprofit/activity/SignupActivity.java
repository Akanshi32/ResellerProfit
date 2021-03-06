package com.vansh.resellerprofit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vansh.resellerprofit.R;
import com.vansh.resellerprofit.model.SignUpRequest;
import com.vansh.resellerprofit.rest.ApiClient;
import com.vansh.resellerprofit.rest.ApiInterface;
import com.vansh.resellerprofit.utility.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.username)
    EditText _nameText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;
    @BindView(R.id.email)
    TextView _email;
    @BindView(R.id.mobile)
    TextView _mobile;
    @BindView(R.id.company)
    TextView _company;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        final SignUpRequest signUpRequest = new SignUpRequest();
        final ApiInterface apiInterface = ApiClient.getClient(this).create(ApiInterface.class);

        String s= getIntent().getStringExtra("name");
        String d= getIntent().getStringExtra("ema");

        _nameText.setText(s);
        _email.setText(d);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_nameText.getText().toString().isEmpty() || _email.getText().toString().isEmpty() || _company.getText().toString().isEmpty()|| _mobile.getText().toString().isEmpty()) {
                    DialogUtil.createDialog("Please Fill All the information!", SignupActivity.this, new DialogUtil.OnPositiveButtonClick() {
                        @Override
                        public void onClick() {
                            finish();
                        }
                    });

                }
                else {

                    signUpRequest.setName(_nameText.getText().toString());
                    signUpRequest.setEmail(_email.getText().toString());
                    signUpRequest.setCompanyName(_company.getText().toString());
                    signUpRequest.setPhone(_mobile.getText().toString());
                    Call<SignUpRequest> call = apiInterface.SignUp(signUpRequest);

                    call.enqueue(new Callback<SignUpRequest>() {
                        @Override
                        public void onResponse(Call<SignUpRequest> call, Response<SignUpRequest> response) {
                            // if (response.body().getCode().equals(Consts.SUCCESS)){
                            //   Toast.makeText(getBaseContext(), "Username exists", Toast.LENGTH_LONG).show();
                            signup();
                            Intent intent = new Intent(SignupActivity.this, CompanyProfile.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<SignUpRequest> call, Throwable t) {
                        }
                    });
                }
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void startActivitySplash(){
        Intent splashActivityIntent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(splashActivityIntent);
        finish();
    }







    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = _nameText.getText().toString();


        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        startActivitySplash();

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }


        return valid;
    }
}