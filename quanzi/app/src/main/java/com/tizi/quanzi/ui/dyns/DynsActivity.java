package com.tizi.quanzi.ui.dyns;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.ui.BaseActivity;

public class DynsActivity extends BaseActivity {

    DynsActivityFragment dynsActivityFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyns);
        dynsActivityFragment = new DynsActivityFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, dynsActivityFragment)
                .commit();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
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
        getMenuInflater().inflate(R.menu.menu_dyns, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_dyn) {
            SendDynFragment sendDynFragment = SendDynFragment.newInstance(null, null);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, sendDynFragment)
                    .addToBackStack("SendDynFragment").commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
