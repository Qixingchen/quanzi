package com.tizi.quanzi.ui.register;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.tizi.quanzi.R;
import com.tizi.quanzi.tool.Statue;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;


/**
 * 注册界面第1步
 */
public class Register1stepFragment extends BaseFragment {

    private Button getSignButton, nextStepButton, getVoiceSignButton;
    private TextInputLayout phoneNumberInputLayout, signInputLayout, passwrodInputLayout;
    private Activity mActivity;
    private NextStep nextStep;
    private Boolean agree = false;

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

    }

    @Override
    protected void initViewsAndSetEvent() {
        phoneNumberInputLayout.setError(mActivity.getString(R.string.phone_number_error));

        ((CheckBox) view.findViewById(R.id.agree)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        agree = isChecked;
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
                                    Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                                            "验证码发送成功", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(getActivity().findViewById(R.id.register_fragment),
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
                                    Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                                            "请等待接听电话", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(getActivity().findViewById(R.id.register_fragment),
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
                    Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                            "您必须阅读并同意我们的条款", Snackbar.LENGTH_LONG).show();
                    return;
                }

                String sign = signInputLayout.getEditText().getText().toString();
                final String phoneNumber = phoneNumberInputLayout.getEditText().getText().toString();
                final String password = passwrodInputLayout.getEditText().getText().toString();


                if (phoneNumber.compareTo("") == 0 || Tool.getPhoneNum(phoneNumber) == null) {
                    Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                            "手机号为空或不合法", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (password.compareTo("") == 0) {
                    Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                            "密码为空", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (sign.compareTo("") == 0) {
                    if (Statue.IsDev.isDev) {
                        nextStep.register1stepOK(phoneNumber, password);
                    } else {
                        Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                                "验证码为空", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }

                AVOSCloud.verifySMSCodeInBackground(sign, phoneNumber, new AVMobilePhoneVerifyCallback() {

                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                                    e.getCode() + e.getMessage(), Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        Snackbar.make(mActivity.findViewById(R.id.register_fragment),
                                "验证成功", Snackbar.LENGTH_LONG).show();
                        nextStep.register1stepOK(phoneNumber, password);
                    }
                });
            }
        });
    }

    public interface NextStep {
        /**
         * @param phoneNumber 用户手机号
         */
        void register1stepOK(String phoneNumber, String password);
    }
}
