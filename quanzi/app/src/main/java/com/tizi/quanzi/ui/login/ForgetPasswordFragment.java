package com.tizi.quanzi.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.network.LoginAndUserAccount;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.main.MainActivity;

public class ForgetPasswordFragment extends BaseFragment {


    private android.support.v7.widget.Toolbar toolbar;
    private android.widget.EditText phoneNumberEditText;
    private android.support.design.widget.TextInputLayout phoneNumberInputLayout;
    private android.widget.Button getSignButton;
    private android.widget.Button getVoiceSignButton;
    private android.widget.EditText signEditText;
    private android.support.design.widget.TextInputLayout signInputLayout;
    private android.support.design.widget.TextInputLayout passwrodInputLayout;
    private android.widget.Button okbtn;


    public ForgetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        return view;
    }


    @Override
    protected void findViews(View view) {
        this.okbtn = (Button) view.findViewById(R.id.ok_btn);
        this.passwrodInputLayout = (TextInputLayout) view.findViewById(R.id.passwordInputLayout);
        this.signInputLayout = (TextInputLayout) view.findViewById(R.id.signInputLayout);
        this.signEditText = (EditText) view.findViewById(R.id.signEditText);
        this.getVoiceSignButton = (Button) view.findViewById(R.id.get_voice_sign_button);
        this.getSignButton = (Button) view.findViewById(R.id.get_sign_button);
        this.phoneNumberInputLayout = (TextInputLayout) view.findViewById(R.id.phoneNumberInputLayout);
        this.phoneNumberEditText = (EditText) view.findViewById(R.id.phoneNumberEditText);
        this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    }

    @Override
    protected void initViewsAndSetEvent() {

        getSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberInputLayout.getEditText().getText().toString();
                AVOSCloud.requestSMSCodeInBackground(phoneNumber, "圈子", "重置密码", 10,
                        new RequestMobileCodeCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    countdownToSignCode();
                                    Snackbar.make(view, "验证码发送成功", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(view,
                                            e.getCode() + e.getMessage(), Snackbar.LENGTH_LONG).show();

                                }
                            }
                        });

            }
        });

        getVoiceSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberInputLayout.getEditText().getText().toString();
                AVOSCloud.requestVoiceCodeInBackground(phoneNumber,
                        new RequestMobileCodeCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    countdownToSignCode();
                                    Snackbar.make(view, "请等待接听电话", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(view,
                                            e.getCode() + e.getMessage(), Snackbar.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sign = signInputLayout.getEditText().getText().toString();
                final String phoneNumber = phoneNumberInputLayout.getEditText().getText().toString();
                final String password = passwrodInputLayout.getEditText().getText().toString();


                if (phoneNumber.compareTo("") == 0 || Tool.getPhoneNum(phoneNumber) == null) {
                    Snackbar.make(view, "手机号为空或不合法", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (password.compareTo("") == 0) {
                    Snackbar.make(view, "密码为空", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (sign.compareTo("") == 0) {
                    if (BuildConfig.BUILD_TYPE.equals("debug")) {
                        verifyCodeOK(phoneNumber, password);
                    } else {
                        Snackbar.make(view, "验证码为空", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }

                AVOSCloud.verifySMSCodeInBackground(sign, phoneNumber, new AVMobilePhoneVerifyCallback() {

                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Snackbar.make(view,
                                    e.getCode() + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        verifyCodeOK(phoneNumber, password);
                    }
                });
            }
        });
    }

    private void verifyCodeOK(final String phoneNumber, final String password) {
        if (!isAttached) {
            return;
        }
        Snackbar.make(view, "验证成功", Snackbar.LENGTH_LONG).show();
        LoginAndUserAccount.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                AppStaticValue.setUserPhone(phoneNumber);
                LoginAndUserAccount.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        //启动主界面
                        Intent intent = new Intent(mActivity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String Message) {
                        Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                    }
                }).loginNoHash(password);
            }

            @Override
            public void onError(String Message) {
                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
            }
        }).changePassword(phoneNumber, password);
    }

    /*获取验证码的倒计时*/
    private void countdownToSignCode() {
        final String signButtonText = getSignButton.getText().toString();
        final String voiceSignButtonText = getVoiceSignButton.getText().toString();
        getSignButton.setEnabled(false);
        getVoiceSignButton.setEnabled(false);
        new Timer().setOnResult(new Timer.OnResult() {
            @Override
            public void OK() {
                getSignButton.setEnabled(true);
                getVoiceSignButton.setEnabled(true);
                getVoiceSignButton.setText(voiceSignButtonText);
                getSignButton.setText(signButtonText);
            }

            @Override
            public void countdown(long remainingS, long goneS) {
                String countDown = remainingS + "秒后重新获取";
                getVoiceSignButton.setText(countDown);
                getSignButton.setText(countDown);
            }
        }).setTimer(1000 * StaticField.Limit.SIGN_CODE_COUNTDOWN).start();
    }
}
