package com.tizi.quanzi.ui.login;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.network.LoginAndUserAccount;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.otto.OttoLoginActivity;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.main.MainActivity;
import com.tizi.quanzi.ui.register.RegisterActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {

    private android.widget.EditText phoneNumberEditText;
    private android.widget.EditText passwordEditText;
    private android.widget.Button LoginButton;
    private android.support.design.widget.TextInputLayout phoneNumberInputLayout;
    private android.support.design.widget.TextInputLayout passwordInputLayout;
    private Button newAccount;
    private View rootView;
    private Timer timer;
    private TextView infoText;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    protected void findViews(View view) {
        this.newAccount = (Button) view.findViewById(R.id.new_account);
        this.passwordInputLayout = (TextInputLayout) view.findViewById(R.id.passwordInputLayout);
        this.phoneNumberInputLayout = (TextInputLayout) view.findViewById(R.id.phoneNumberInputLayout);
        this.LoginButton = (Button) view.findViewById(R.id.LoginButton);
        this.passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        this.phoneNumberEditText = (EditText) view.findViewById(R.id.phoneNumberEditText);
        rootView = view.findViewById(R.id.login_root_view);
        infoText = (TextView) view.findViewById(R.id.login_info);
    }

    @Override
    protected void initViewsAndSetEvent() {

        timer = new Timer().setOnResult(new Timer.OnResult() {
            @Override
            public void OK() {
                rootView.setVisibility(View.VISIBLE);
                infoText.setVisibility(View.GONE);
            }

            @Override
            public void countdown(long remainingS, long goneS) {
                infoText.setText("登入时间超过预期");
                infoText.setVisibility(View.VISIBLE);
            }
        }).setTimer(10 * 1000, 5000);

        if (!LoginAndUserAccount.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                //启动主界面
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                timer.cancel();
            }

            @Override
            public void onError(String Message) {
                rootView.setVisibility(View.VISIBLE);
                infoText.setVisibility(View.GONE);
                timer.cancel();
            }
        }).loginFromPrefer()) {
            rootView.setVisibility(View.VISIBLE);
            infoText.setVisibility(View.GONE);
            timer.cancel();
        }


        phoneNumberInputLayout.setError(getString(R.string.phone_number_error));
        phoneNumberInputLayout.setErrorEnabled(false);
        /*游客登陆*/
        mActivity.findViewById(R.id.guest_user_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除偏好的密码
                App.getApplication().getSharedPreferences(
                        StaticField.Preferences.TOKENFILE, Context.MODE_PRIVATE).edit()
                        .putString(StaticField.Preferences.PASSWORD, "").apply();
                LoginAndUserAccount.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
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
                login();
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

        /*忘记密码*/
        mActivity.findViewById(R.id.forgetPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusProvider.getInstance().post(new OttoLoginActivity(OttoLoginActivity.FORGET_PASSWORD));
            }
        });

        /*监听密码的Enter*/
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == KeyEvent.KEYCODE_ENTER || actionId == KeyEvent.KEYCODE_ENDCALL) {
                    login();
                }
                return false;
            }
        });
    }

    private void login() {
        String phoneNumber = phoneNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (Tool.getPhoneNum(phoneNumber) == null || password.compareTo("") == 0) {
            Snackbar.make(view, "手机号未填写或不合法或密码未填写",
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        AppStaticValue.setUserPhone(phoneNumber);
        LoginAndUserAccount.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Login login = (Login) ts;

                //启动主界面
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
