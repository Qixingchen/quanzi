package com.tizi.quanzi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.fragment.Register1stepFragment;
import com.tizi.quanzi.fragment.Register2stepFragment;


public class RegisterActivity extends AppCompatActivity implements Register1stepFragment.NextStep {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Register1stepFragment register1StepFragment = new Register1stepFragment();
        register1StepFragment.setNextStep(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, register1StepFragment).commit();

        //todo: 测试用，跳过了验证码
        Register2stepFragment register2stepFragment = new Register2stepFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, register2stepFragment).commit();
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
        Register2stepFragment register2stepFragment = new Register2stepFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, register2stepFragment).commit();
    }
}
