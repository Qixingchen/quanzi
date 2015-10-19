package com.tizi.quanzi.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.GroupList;
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
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class RegisterActivity extends BaseActivity implements Register1stepFragment.NextStep,
        CompleteUesrInfo.AllDone {

    private final static String TAG = RegisterActivity.class.getSimpleName();
    private String phoneNumber;
    private String password;
    private CompleteUesrInfo completeUesrInfo;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    /**
     * 获取布局控件
     */
    @Override
    protected void findView() {

    }

    /**
     * 初始化View的一些数据
     */
    @Override
    protected void initView() {
        Register1stepFragment register1StepFragment = new Register1stepFragment();
        register1StepFragment.setNextStep(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.register_fragment, register1StepFragment).commit();
    }

    /**
     * 设置点击监听
     */
    @Override
    protected void setViewEvent() {

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
     * 跳转信息补全
     *
     * @param phoneNumber 用户手机号
     */
    @Override
    public void register1stepOK(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        AppStaticValue.setUserID(phoneNumber);
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
            public void onResponse(Response<Login> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body().isSuccess()) {
                    Login login = response.body();
                    AutoLogin.setUserInfo(AppStaticValue.getUserPhone(), login.getUser().getId(),
                            login.getUser().getToken());
                    MyUserInfo.getInstance().setUserInfo(login.getUser());
                    PrivateMessPairList.getInstance().getGroupsFromDataBase();
                    List<Login.GroupEntity> groups = new ArrayList<>();
                    ((GroupList) GroupList.getInstance()).setGroupListByLoginGroup(groups);
                    //start intent
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                } else {
                    String mess = response.isSuccess() ? response.body().msg : response.message();
                    Log.w(TAG, mess);
                    Snackbar.make(findViewById(R.id.register_fragment),
                            mess, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, t.getMessage());
                Snackbar.make(findViewById(R.id.register_fragment),
                        t.getMessage(), Snackbar.LENGTH_LONG).show();
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
