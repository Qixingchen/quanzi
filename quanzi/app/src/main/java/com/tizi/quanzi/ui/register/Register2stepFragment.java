package com.tizi.quanzi.ui.register;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tizi.quanzi.R;

/**
 * 注册界面第2步
 */
public class Register2stepFragment extends Fragment {

    private Activity mActivity;
    private NextStep nextStep;

    /**
     * @param nextStep 设置NextStep
     *
     * @see com.tizi.quanzi.ui.register.Register2stepFragment.NextStep
     */
    public void setNextStep(NextStep nextStep) {
        this.nextStep = nextStep;
    }

    public Register2stepFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_2step, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        final TextInputLayout password1Layout =
                (TextInputLayout) mActivity.findViewById(R.id.password1InputLayout);
        final TextInputLayout password2Layout =
                (TextInputLayout) mActivity.findViewById(R.id.password2InputLayout);

        password2Layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password1Layout.getEditText().getText().toString().
                        compareTo(password2Layout.getEditText().getText().toString()) != 0) {
                    password2Layout.setError("密码不一致");
                    password2Layout.setErrorEnabled(true);
                } else {
                    password2Layout.setErrorEnabled(false);
                }
            }
        });

        Button signInButton = (Button) mActivity.findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password1Layout.getEditText().getText().toString().
                        compareTo(password2Layout.getEditText().getText().toString()) == 0) {
                    nextStep.regi2StepOK(password1Layout.getEditText().getText().toString());
                }
            }
        });
    }

    /*点击下一步的接口*/
    public interface NextStep {
        /**
         * @param password 用户输入的密码
         */
        void regi2StepOK(String password);
    }
}
