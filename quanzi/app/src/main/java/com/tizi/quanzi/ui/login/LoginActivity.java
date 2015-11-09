package com.tizi.quanzi.ui.login;

import android.content.Intent;
import android.os.Bundle;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.network.LoginAndUserAccount;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.otto.OttoLoginActivity;
import com.tizi.quanzi.tool.GetGMSStatue;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.main.MainActivity;

/**
 * 登陆界面
 */
public class LoginActivity extends BaseActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //分析GCM分布
        GetGMSStatue.haveGMS(this);
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

            }
        }).loginFromPrefer();
        Tool.flushTimeDifference();
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        getFragmentManager().beginTransaction().add(R.id.fragment, new LoginFragment()).commit();

    }

    @Override
    protected void setViewEvent() {

    }

    @Subscribe
    public void ForgetPassword(OttoLoginActivity ottoLoginActivity) {
        if (ottoLoginActivity.event == OttoLoginActivity.FORGET_PASSWORD) {
            getFragmentManager().beginTransaction().replace(R.id.fragment, new ForgetPasswordFragment())
                    .addToBackStack("ForgetPasswordFragment").commit();
        }
    }
}
