package com.tizi.quanzi.ui.login;

import android.os.Bundle;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.otto.OttoLoginActivity;
import com.tizi.quanzi.tool.GetGMSStatue;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseActivity;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //分析GCM分布
        GetGMSStatue.haveGMS(this);
        Tool.flushTimeDifference();
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, new LoginFragment()).commit();

    }

    @Override
    protected void setViewEvent() {

    }

    @Subscribe
    public void ForgetPassword(OttoLoginActivity ottoLoginActivity) {
        if (ottoLoginActivity.event == OttoLoginActivity.FORGET_PASSWORD) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ForgetPasswordFragment())
                    .addToBackStack("ForgetPasswordFragment").commit();
        }
    }
}
