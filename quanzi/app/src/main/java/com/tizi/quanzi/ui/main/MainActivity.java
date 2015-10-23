package com.tizi.quanzi.ui.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.dataStatic.SystemMessageList;
import com.tizi.quanzi.otto.AVIMNetworkEvents;
import com.tizi.quanzi.otto.FragmentResume;
import com.tizi.quanzi.ui.BaseActivity;


public class MainActivity extends BaseActivity {
    private static final String toolbarTitle = "圈子";
    private MainFragment mainFragment;
    private NotifiMessageFragment notifiMessageFragment;
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
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Subscribe
    public void onChanged(SystemMessageList ignore) {
        if (menu == null) {
            return;
        }
        int num = PrivateMessPairList.getInstance().getAllUnreadCount()
                + SystemMessageList.getInstance().getAllUnreadCount();
        if (num != 0) {
            menu.findItem(R.id.action_notifi_message).setTitle("通知（" + num + "）条");
        } else {
            menu.findItem(R.id.action_notifi_message).setTitle("通知");
        }
    }

    @Subscribe
    public void onChanged(PrivateMessPairList ignore) {
        if (menu == null) {
            return;
        }
        int num = PrivateMessPairList.getInstance().getAllUnreadCount()
                + SystemMessageList.getInstance().getAllUnreadCount();
        if (num != 0) {
            menu.findItem(R.id.action_notifi_message).setTitle("通知（" + num + "）条");
        } else {
            menu.findItem(R.id.action_notifi_message).setTitle("通知");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notifi_message) {
            notifiMessageFragment = new NotifiMessageFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, notifiMessageFragment)
                    .addToBackStack("notifiMessageFragment").commit();
        }

        return super.onOptionsItemSelected(item);
    }

    /*订阅通知界面的恢复和暂停*/
    @Subscribe
    public void onFragmentResume(FragmentResume fragmentResume) {
        if (fragmentResume.FramgentName.compareTo(NotifiMessageFragment.class.getSimpleName()) != 0) {
            return;
        }
        if (fragmentResume.resumeOrPause) {
            menu.findItem(R.id.action_notifi_message).setVisible(false);
        } else {
            menu.findItem(R.id.action_notifi_message).setVisible(true);
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
        userInfoSetFragment = new UserInfoSetFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, userInfoSetFragment).addToBackStack("userInfoSetFragment").commit();
    }
}
