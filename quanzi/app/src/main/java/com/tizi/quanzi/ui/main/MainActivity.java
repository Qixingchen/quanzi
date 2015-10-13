package com.tizi.quanzi.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.otto.AVIMNetworkEvents;
import com.tizi.quanzi.otto.PrivateMessFragmentResume;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseActivity;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String toolbarTitle = "圈子";
    private MainFragment mainFragment;
    private PrivateMessageFragment privateMessageFragment;
    private UserInfoSetFragment userInfoSetFragment;
    //toolbar
    private Toolbar toolbar;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 15/10/13 play政策不允许做这个
        //GetAppVersion.doit(this);
    }

    @Override
    protected void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainFragment = new MainFragment();
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, mainFragment).commit();
    }

    @Override
    protected void setViewEvent() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        mViewPager.removeOnPageChangeListener(getOnPageChangeListener());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        PrivateMessPairList.getInstance().addOnChangeCallBack(new PrivateMessPairList.OnChangeCallBack() {
            @Override
            public void changed() {
                int num = PrivateMessPairList.getInstance().getAllUnreadCount();
                if (num != 0) {
                    menu.findItem(R.id.action_private_message).setTitle("私信（" + num + "）条");
                } else {
                    menu.findItem(R.id.action_private_message).setTitle("私信");
                }
            }
        });
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_private_message) {
            privateMessageFragment = new PrivateMessageFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, privateMessageFragment)
                    .addToBackStack("privateMessageFragment").commit();
        }

        return super.onOptionsItemSelected(item);
    }

    /*订阅私信界面的恢复和暂停*/
    @Subscribe
    public void onPriMessFragChange(PrivateMessFragmentResume privateMessFragmentResume) {
        if (privateMessFragmentResume.resumeOrPause) {
            menu.findItem(R.id.action_private_message).setVisible(false);
        } else {
            menu.findItem(R.id.action_private_message).setVisible(true);
        }
    }

    /*LC网络状态更改*/
    @Subscribe
    public void onNetworkChange(AVIMNetworkEvents avimNetworkEvents) {
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(avimNetworkEvents.isNetWorkAvailable ? toolbarTitle : "等待网络");
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle(MyAVIMClientEventHandler.getInstance().isNetworkAvailable ? toolbarTitle : "等待网络");
    }

    public void StartUserInfoSet() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "没有位置权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    StaticField.PermissionRequestCode.userInfoSetFragment);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.w(TAG, "没有位置权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    StaticField.PermissionRequestCode.userInfoSetFragment);
            return;
        }
        userInfoSetFragment = new UserInfoSetFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, userInfoSetFragment).addToBackStack("userInfoSetFragment").commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (userInfoSetFragment == null) {
            return;
        }
        userInfoSetFragment.onActivityResult(requestCode, resultCode, data);
    }
}
