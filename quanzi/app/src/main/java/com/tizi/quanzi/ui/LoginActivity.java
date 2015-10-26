package com.tizi.quanzi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.network.AutoLogin;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.GetGMSStatue;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.main.MainActivity;
import com.tizi.quanzi.ui.register.RegisterActivity;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity {

    private android.widget.EditText phoneNumberEditText;
    private android.widget.EditText passwordEditText;
    private android.widget.Button LoginButton;
    private android.support.design.widget.TextInputLayout phoneNumberInputLayout;
    private android.support.design.widget.TextInputLayout passwordInputLayout;
    private Button newAccount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //分析GCM分布
        GetGMSStatue.haveGMS(this);
        AutoLogin.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                //启动主界面
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onError(String Message) {

            }
        }).loginFromPrefer();
        Tool.flushTimeDifference();
    }

    @Override
    protected void findView() {
        this.newAccount = (Button) findViewById(R.id.new_account);
        this.passwordInputLayout = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        this.phoneNumberInputLayout = (TextInputLayout) findViewById(R.id.phoneNumberInputLayout);
        this.LoginButton = (Button) findViewById(R.id.LoginButton);
        this.passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        this.phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
    }

    @Override
    protected void initView() {
        phoneNumberInputLayout.setError(getString(R.string.phone_number_error));
    }

    @Override
    protected void setViewEvent() {
        /*游客登陆*/
        findViewById(R.id.guest_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除偏好的密码
                App.getApplication().getSharedPreferences(
                        StaticField.Preferences.TOKENFILE, Context.MODE_PRIVATE).edit()
                        .putString(StaticField.Preferences.PASSWORD, "").apply();
                AutoLogin.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        //启动主界面
                        //start intent
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String Message) {
                        Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                    }
                }).loginRaw(StaticField.GuestUser.Account, StaticField.GuestUser.PassWord);
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(mContext, RegisterActivity.class);
                startActivity(register);
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = phoneNumberEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (Tool.getPhoneNum(phoneNumber) == null || password.compareTo("") == 0) {
                    Snackbar.make(view, "手机号未填写或不合法或密码未填写",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }

                AppStaticValue.setUserPhone(phoneNumber);
                AutoLogin.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        Login login = (Login) ts;

                        //储存密码
                        SharedPreferences preferences =
                                getSharedPreferences(StaticField.Preferences.TOKENFILE,
                                        MODE_PRIVATE);
                        preferences.edit().putString(StaticField.Preferences.PASSWORD,
                                GetPassword.preHASH(passwordEditText.getText().toString())).apply();
                        //启动主界面
                        //start intent
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String Message) {
                        Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                    }
                }).loginFromPrePassword(GetPassword.preHASH(password));
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
}
