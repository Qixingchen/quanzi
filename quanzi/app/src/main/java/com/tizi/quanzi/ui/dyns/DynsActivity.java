package com.tizi.quanzi.ui.dyns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.otto.FragmentResume;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseActivity;

public class DynsActivity extends BaseActivity {

    private DynsActivityFragment dynsActivityFragment;
    private SendDynFragment sendDynFragment;
    private Menu menu;

    private String groupID;
    private String themeID;
    private String themeString;

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
        Intent intent = getIntent();
        groupID = intent.getStringExtra("groupID");
        themeID = intent.getStringExtra("themeID");
        if (themeID == null) {
            return;
        }
        //获取主题的描述
        ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Theme theme = (Theme) ts;
                for (Theme.ActsEntity actsEntity : theme.acts) {
                    if (actsEntity.id.compareTo(themeID) == 0) {
                        themeString = actsEntity.content;
                        break;
                    }
                }
            }

            @Override
            public void onError(String Message) {

            }
        }).getThemes();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_dyns, menu);
        menu.findItem(R.id.action_send).setVisible(false);
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
            if (Tool.isGuest()) {
                Tool.GuestAction(this);
                return true;
            }
            sendDynFragment = SendDynFragment.newInstance(themeString, groupID, themeID);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, sendDynFragment)
                    .addToBackStack("SendDynFragment").commit();
            return true;
        }
        if (id == R.id.action_send) {
            if (sendDynFragment.SendDyn()) {
                getSupportFragmentManager().popBackStack();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*Fragment 启动与停止*/
    @Subscribe
    public void onFragmentResume(FragmentResume fragmentResume) {
        if (fragmentResume.FramgentName.compareTo(SendDynFragment.class.getSimpleName()) == 0) {
            if (fragmentResume.resumeOrPause) {
                menu.findItem(R.id.action_send_dyn).setVisible(false);
                menu.findItem(R.id.action_send).setVisible(true);
            } else {
                menu.findItem(R.id.action_send_dyn).setVisible(true);
                menu.findItem(R.id.action_send).setVisible(false);
            }
        }
    }
}
