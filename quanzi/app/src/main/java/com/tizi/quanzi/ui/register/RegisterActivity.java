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
import com.tizi.quanzi.network.LoginAndUserAccount;
import com.tizi.quanzi.network.RetrofitAPI;
import com.tizi.quanzi.network.RetrofitNetwork;
import com.tizi.quanzi.tool.GetGMSStatue;
import com.tizi.quanzi.tool.GetPassword;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends BaseActivity implements Register1stepFragment.NextStep,
        CompleteUesrInfo.AllDone {

    private final static String TAG = RegisterActivity.class.getSimpleName();
    private String phoneNumber;
    private String password;
    private CompleteUesrInfo completeUesrInfo;
    private Context context = this;
    private boolean isRegistering = false;

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
                .add(R.id.fragment, register1StepFragment).commit();
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
                .replace(R.id.fragment, completeUesrInfo).commit();
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

        if (isRegistering) {
            return;
        }
        isRegistering = true;
        AppStaticValue.setUserToken(AppStaticValue.getUserID());
        RetrofitNetwork.retrofit.create(RetrofitAPI.UserAccount.class).register(phoneNumber, userName,
                GetPassword.fullHash(password), String.valueOf(sex), faceUri, "2",
                String.valueOf(Build.VERSION.SDK_INT) + " GMS:" + GetGMSStatue.haveGMS(this)
                , Build.MODEL + "  " + Build.DEVICE).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                isRegistering = false;
                if (response.isSuccessful() && response.body().success) {
                    Login login = response.body();
                    LoginAndUserAccount.setUserInfo(AppStaticValue.getUserPhone(), login.getUser().getId(),
                            login.getUser().getToken());
                    MyUserInfo.getInstance().setUserInfo(login.getUser());
                    PrivateMessPairList.getInstance().getGroupsFromDataBase();
                    List<Login.GroupEntity> groups = new ArrayList<>();
                    groups.addAll(login.group);
                    ((GroupList) GroupList.getInstance()).setGroupListByLoginGroup(groups);
                    //start intent
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                } else {
                    String mess = response.isSuccessful() ? response.body().msg : response.message();
                    Log.w(TAG, mess);
                    Snackbar.make(view, mess, Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                isRegistering = false;
                Log.w(TAG, t.getMessage());
                Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

}
