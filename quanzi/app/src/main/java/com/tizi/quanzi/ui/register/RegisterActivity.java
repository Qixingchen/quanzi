package com.tizi.quanzi.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.Intent.StartMainActivity;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.AutoLogin;
import com.tizi.quanzi.network.RetrofitAPI;
import com.tizi.quanzi.network.RetrofitNetwork;
import com.tizi.quanzi.tool.GetGMSStatue;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.tool.Tool;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;


public class RegisterActivity extends AppCompatActivity implements Register1stepFragment.NextStep,
        Register2stepFragment.NextStep, CompleteUesrInfo.AllDone {

    private String phoneNumber;
    private String password;

    private CompleteUesrInfo completeUesrInfo;
    private Context context = this;

    private final static String TAG = RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register1stepFragment register1StepFragment = new Register1stepFragment();
        register1StepFragment.setNextStep(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.register_fragment, register1StepFragment).commit();

        //        //todo: 测试用，跳过了验证码
        //        Register2stepFragment register2stepFragment = new Register2stepFragment();
        //        register2stepFragment.setNextStep(this);
        //        getSupportFragmentManager().beginTransaction()
        //                .replace(R.id.register_fragment, register2stepFragment).commit();
        //
        //        completeUesrInfo = new CompleteUesrInfo();
        //        completeUesrInfo.setAllDone(this);
        //        getSupportFragmentManager().beginTransaction()
        //                .replace(R.id.register_fragment, completeUesrInfo).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 注册第一步完成（手机验证）
     * 跳转第二界面
     *
     * @param phoneNumber 用户手机号
     */
    @Override
    public void register1stepOK(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        AppStaticValue.setUserID(phoneNumber);
        Register2stepFragment register2stepFragment = new Register2stepFragment();
        register2stepFragment.setNextStep(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.register_fragment, register2stepFragment).commit();
    }

    /**
     * 注册第2步完成（密码）
     * 跳转补全
     *
     * @param password 用户密码
     */
    @Override
    public void regi2StepOK(String password) {
        this.password = password;
        completeUesrInfo = new CompleteUesrInfo();
        completeUesrInfo.setAllDone(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.register_fragment, completeUesrInfo).commit();
    }

    /**
     * 注册补全完成（用户信息）
     *
     * @param userName 用户昵称
     * @param sex      性别
     * @param faceUri  头像
     */
    @Override
    public void CompUserInfoOK(String userName, int sex, String faceUri) {

        AppStaticValue.setUserToken(AppStaticValue.getUserID());
        RetrofitNetwork.retrofit.create(RetrofitAPI.UserAccount.class).register(phoneNumber, userName,
                GetPassword.fullHash(password), String.valueOf(sex), faceUri, "2",
                String.valueOf(Build.VERSION.SDK_INT) + " GMS:" + GetGMSStatue.haveGMS(this)
                , Build.MODEL + "  " + Build.DEVICE, Tool.getSignMap()).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Response<Login> response) {
                if (response.isSuccess() && response.body().isSuccess()) {
                    Login login = response.body();
                    AutoLogin.setUserInfo(AppStaticValue.getUserPhone(), login.getUser().getId(),
                            login.getUser().getToken());
                    MyUserInfo.getInstance().setUserInfo(login.getUser());
                    PrivateMessPairList.getInstance().getGroupsFromDataBase();
                    List<Login.GroupEntity> groups = new ArrayList<>();
                    StartMainActivity.startByLoginGroup(groups, context);
                } else {
                    String mess = response.isSuccess() ? response.body().msg : response.message();
                    Log.w(TAG, mess);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getMessage());
            }
        });

    }

    /**
     * 选图 拍照返回值
     *
     * @see CompleteUesrInfo
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        completeUesrInfo.onIntentResult(requestCode, resultCode, data);

    }
}
