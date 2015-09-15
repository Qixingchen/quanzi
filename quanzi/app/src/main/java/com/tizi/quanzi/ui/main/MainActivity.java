package com.tizi.quanzi.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseActivity;


public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MainFragment mainFragment;
    private PrivateMessageFragment privateMessageFragment;
    private UserInfoSetFragment userInfoSetFragment;
    private static final String toolBarName = "圈子";
    private static final String netWorkChangeName = "MainActivity";

    //toolbar
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        MyAVIMClientEventHandler.getInstance().addChange(netWorkChangeName, new MyAVIMClientEventHandler.OnConnectionChange() {
            @Override
            public void onPaused() {
                toolbar.setTitle("等待网络");
            }

            @Override
            public void onResume() {
                toolbar.setTitle(toolBarName);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        mViewPager.removeOnPageChangeListener(getOnPageChangeListener());
        MyAVIMClientEventHandler.getInstance().removeChange(netWorkChangeName);
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
}
