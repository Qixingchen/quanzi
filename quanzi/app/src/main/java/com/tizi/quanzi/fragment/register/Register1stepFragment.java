package com.tizi.quanzi.fragment.register;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.tizi.quanzi.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class Register1stepFragment extends Fragment {

    private Button getSignButton, nextStepButton;
    private TextInputLayout phoneNumberInputLayout, signInputLayout;
    private Activity mActivity;
    private NextStep nextStep;

    public void setNextStep(NextStep nextStep) {
        this.nextStep = nextStep;
    }

    public Register1stepFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        return inflater.inflate(R.layout.fragment_register_1step, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getSignButton = (Button) mActivity.findViewById(R.id.get_sign_button);
        nextStepButton = (Button) mActivity.findViewById(R.id.next_button);
        Button getVoiceSignButton = (Button) mActivity.findViewById(R.id.get_voice_sign_button);
        phoneNumberInputLayout = (TextInputLayout) mActivity.findViewById(R.id.phoneNumberInputLayout);
        signInputLayout = (TextInputLayout) mActivity.findViewById(R.id.signInputLayout);
        phoneNumberInputLayout.setError(mActivity.getString(R.string.phone_number_error));

        getSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberInputLayout.getEditText().getText().toString();
                AVOSCloud.requestSMSCodeInBackground(phoneNumber, "圈子", "注册", 10,
                        new RequestMobileCodeCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Snackbar.make(mActivity.findViewById(R.id.fragment),
                                            "验证码发送成功", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getActivity().findViewById(R.id.fragment),
                                            e.getCode() + e.getMessage(), Snackbar.LENGTH_SHORT).show();

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
                                    Snackbar.make(mActivity.findViewById(R.id.fragment),
                                            "请等待接听电话", Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(getActivity().findViewById(R.id.fragment),
                                            e.getCode() + e.getMessage(), Snackbar.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sign = signInputLayout.getEditText().getText().toString();
                final String phoneNumber = phoneNumberInputLayout.getEditText().getText().toString();
                AVOSCloud.verifySMSCodeInBackground(sign, phoneNumber, new AVMobilePhoneVerifyCallback() {

                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Snackbar.make(mActivity.findViewById(R.id.fragment),
                                    e.getCode() + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        Snackbar.make(mActivity.findViewById(R.id.fragment),
                                "验证成功", Snackbar.LENGTH_SHORT).show();
                        nextStep.register1stepOK(phoneNumber);
                    }
                });
            }
        });
    }

    public interface NextStep {
        void register1stepOK(String phoneNumber);
    }
}
