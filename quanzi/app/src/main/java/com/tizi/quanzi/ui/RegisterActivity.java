package com.tizi.quanzi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.fragment.register.CompleteUesrInfo;
import com.tizi.quanzi.fragment.register.Register1stepFragment;
import com.tizi.quanzi.fragment.register.Register2stepFragment;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetPassword;

import java.util.Map;
import java.util.TreeMap;


public class RegisterActivity extends AppCompatActivity implements Register1stepFragment.NextStep,
        Register2stepFragment.NextStep, CompleteUesrInfo.AllDone {

    private String phoneNumber;
    private String password;

    private CompleteUesrInfo completeUesrInfo;

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

    @Override
    public void register1stepOK(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        App.setUserID(phoneNumber);
        Register2stepFragment register2stepFragment = new Register2stepFragment();
        register2stepFragment.setNextStep(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.register_fragment, register2stepFragment).commit();
    }

    @Override
    public void regi2StepOK(String password) {
        this.password = password;
        completeUesrInfo = new CompleteUesrInfo();
        completeUesrInfo.setAllDone(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.register_fragment, completeUesrInfo).commit();
    }

    @Override
    public void CompUserInfoOK(String userName, int sex, String faceUri) {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
            }
        };
        String baseuri = getString(R.string.testbaseuri) + "/applogin/regF";
        Map<String, String> para = new TreeMap();
        para.put("account", phoneNumber);
        para.put("password", GetPassword.fullHash(password));
        para.put("username", userName);
        //todo 判断性别
        para.put("sex", "0");
        para.put("icon", faceUri);
        App.setUserToken(App.getUserID());
        GetVolley.getmInstance(this, listener).addPostRequestWithSign(Request.Method.POST, baseuri, para);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        completeUesrInfo.onIntentResult(requestCode, resultCode, data);

    }
}
