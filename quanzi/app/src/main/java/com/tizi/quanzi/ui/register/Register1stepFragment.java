package com.tizi.quanzi.ui.register;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.tizi.quanzi.BuildConfig;
import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.MakeSpannableString;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.main.WebViewFragment;
import com.tizi.quanzi.widget.custom_tab.SimpleCustomChromeTabsHelper;


/**
 * 注册界面第1步
 */
public class Register1stepFragment extends BaseFragment {

    private Button getSignButton, nextStepButton, getVoiceSignButton;
    private TextInputLayout phoneNumberInputLayout, signInputLayout, passwrodInputLayout;
    private Activity mActivity;
    private NextStep nextStep;
    private Boolean agree = false;
    private TextView userLicense;

    private CheckBox agreeBox;
    private View agreeView;

    private SimpleCustomChromeTabsHelper mCustomTabHelper;

    public Register1stepFragment() {
    }

    /**
     * 设置下一步监听器
     *
     * @param nextStep
     *
     * @see com.tizi.quanzi.ui.register.Register1stepFragment.NextStep
     */
    public void setNextStep(NextStep nextStep) {
        this.nextStep = nextStep;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        return inflater.inflate(R.layout.fragment_register_1step, container, false);
    }

    @Override
    protected void findViews(View view) {
        passwrodInputLayout = (TextInputLayout) view.findViewById(R.id.passwordInputLayout);
        getSignButton = (Button) view.findViewById(R.id.get_sign_button);
        nextStepButton = (Button) view.findViewById(R.id.next_button);
        getVoiceSignButton = (Button) view.findViewById(R.id.get_voice_sign_button);
        phoneNumberInputLayout = (TextInputLayout) view.findViewById(R.id.phoneNumberInputLayout);
        signInputLayout = (TextInputLayout) view.findViewById(R.id.signInputLayout);
        userLicense = (TextView) view.findViewById(R.id.agree_text_view);
        agreeBox = (CheckBox) view.findViewById(R.id.agree);
        agreeView = view.findViewById(R.id.agree_view);
    }

    @Override
    protected void initViewsAndSetEvent() {

        mCustomTabHelper = new SimpleCustomChromeTabsHelper(mActivity);
        if (SimpleCustomChromeTabsHelper.canUseCustomChromeTabs(mContext)) {

            mCustomTabHelper.prepareUrl(getString(R.string.user_license));
        }


        SpannableString license = MakeSpannableString.makeLinkSpan(String.format("《%s协议》", StaticField.AppName.AppEngName), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SimpleCustomChromeTabsHelper.canUseCustomChromeTabs(mContext)) {
                    mCustomTabHelper.openUrl(getString(R.string.user_license));
                } else {
                    getFragmentManager().beginTransaction().hide(mFragment).add(R.id.fragment,
                            WebViewFragment.newInstance(WebViewFragment.License, getString(R.string.user_license)))
                            .addToBackStack("WebViewFragment").commit();
                }
            }
        });

        userLicense.setText("我已阅读并同意");
        userLicense.append(license);
        MakeSpannableString.makeLinksFocusable(userLicense);

        agreeBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        agree = isChecked;
                    }
                });

        agreeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agree = !agree;
                agreeBox.setChecked(agree);
            }
        });

        userLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agree = !agree;
                agreeBox.setChecked(agree);
            }
        });

        getSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberInputLayout.getEditText().getText().toString();
                AVOSCloud.requestSMSCodeInBackground(phoneNumber, "圈子", "注册", 10,
                        new RequestMobileCodeCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    countdownToSignCode();
                                    Snackbar.make(view,
                                            "验证码发送成功", Snackbar.LENGTH_LONG).show();
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
                                    Snackbar.make(view,
                                            "请等待接听电话", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(view,
                                            e.getCode() + e.getMessage(), Snackbar.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!agree) {
                    Snackbar.make(view,
                            "您必须阅读并同意我们的条款", Snackbar.LENGTH_LONG).show();
                    return;
                }

                String sign = signInputLayout.getEditText().getText().toString();
                final String phoneNumber = phoneNumberInputLayout.getEditText().getText().toString();
                final String password = passwrodInputLayout.getEditText().getText().toString();


                if (phoneNumber.compareTo("") == 0 || Tool.getPhoneNum(phoneNumber) == null) {
                    Snackbar.make(view,
                            "手机号为空或不合法", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (password.compareTo("") == 0) {
                    Snackbar.make(view,
                            "密码为空", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (sign.compareTo("") == 0) {
                    if (BuildConfig.DEBUG) {
                        nextStep.register1stepOK(phoneNumber, password);
                    } else {
                        Snackbar.make(view,
                                "验证码为空", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }

                AVOSCloud.verifySMSCodeInBackground(sign, phoneNumber, new AVMobilePhoneVerifyCallback() {

                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            if (isAttached) {
                                Snackbar.make(view,
                                        e.getCode() + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                            return;
                        }
                        if (isAttached) {
                            Snackbar.make(view,
                                    "验证成功", Snackbar.LENGTH_LONG).show();
                        }
                        nextStep.register1stepOK(phoneNumber, password);
                    }
                });
            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCustomTabHelper.unbindCustomTabsService();
    }

    public interface NextStep {
        /**
         * @param phoneNumber 用户手机号
         */
        void register1stepOK(String phoneNumber, String password);
    }
}
