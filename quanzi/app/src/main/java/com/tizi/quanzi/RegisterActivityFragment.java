package com.tizi.quanzi;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.w3c.dom.Text;


/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterActivityFragment extends Fragment {

    private Button getSignButton, nextStepButton;
    private TextInputLayout phoneNumberInputLayout, signInputLayout;
    private Activity mActivity;

    public RegisterActivityFragment() {
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_1step, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getSignButton = (Button) mActivity.findViewById(R.id.get_sign_button);
        nextStepButton = (Button) mActivity.findViewById(R.id.next_button);
        phoneNumberInputLayout = (TextInputLayout) mActivity.findViewById(R.id.phoneNumberInputLayout);
        signInputLayout = (TextInputLayout) mActivity.findViewById(R.id.signInputLayout);
        phoneNumberInputLayout.setError(mActivity.getString(R.string.phone_number_error));

        getSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
