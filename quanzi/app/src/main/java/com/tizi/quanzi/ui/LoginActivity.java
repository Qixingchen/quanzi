package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tizi.quanzi.Intent.StartMainActivity;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.network.AutoLogin;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.tool.StaticField;


public class LoginActivity extends AppCompatActivity {

    private android.widget.EditText phoneNumberEditText;
    private android.widget.EditText passwordEditText;
    private android.widget.Button LoginButton;
    private Activity mActivity;
    private android.support.design.widget.TextInputLayout phoneNumberInputLayout;
    private android.support.design.widget.TextInputLayout passwordInputLayout;
    private TextView newaccount;

    private static final String TAG = LoginActivity.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AutoLogin.getInstance().makeOKListener().makeErrorListener().login();
        this.newaccount = (TextView) findViewById(R.id.new_account);
        this.passwordInputLayout = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        this.phoneNumberInputLayout = (TextInputLayout) findViewById(R.id.phoneNumberInputLayout);
        phoneNumberInputLayout.setError(getString(R.string.phone_number_error));
        this.LoginButton = (Button) findViewById(R.id.LoginButton);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);

        mActivity = this;

        newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(mActivity, RegisterActivity.class);
                startActivity(register);
            }
        });

        final Response.Listener<String> mOKListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Login login = gson.fromJson(response, Login.class);
                if (login.isSuccess()) {
                    AutoLogin.setUserInfo(phoneNumberEditText.getText().toString(),
                            login.getUser().getId(), login.getUser().getToken());
                    SharedPreferences preferences = getSharedPreferences(StaticField.TokenPreferences.TOKENFILE,
                            MODE_PRIVATE);
                    preferences.edit().putString(StaticField.TokenPreferences.PASSWORD,
                            GetPassword.preHASH(passwordEditText.getText().toString())).apply();

                    StartMainActivity.startByLoginGroup(login.getGroup(), mActivity);
                }
            }
        };
        final Response.ErrorListener mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(findViewById(R.id.LoginLayout), "网络错误", Snackbar.LENGTH_LONG).show();
            }
        };

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.setUserPhone(phoneNumberEditText.getText().toString());
                String password = passwordEditText.getText().toString();
                AutoLogin.getInstance().setmOKListener(mOKListener).
                        setmErrorListener(mErrorListener).
                        login(GetPassword.preHASH(password));
            }
        });
        phoneNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPhoneNumber();
            }
        });
    }

    private boolean checkPhoneNumber() {
        String phoneNumber = phoneNumberEditText.getText().toString();

        if (phoneNumber.length() != 11) {
            phoneNumberInputLayout.setErrorEnabled(true);
            return false;
        } else {
            phoneNumberInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void toMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
