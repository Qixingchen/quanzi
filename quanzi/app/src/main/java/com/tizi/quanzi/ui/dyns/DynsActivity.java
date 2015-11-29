package com.tizi.quanzi.ui.dyns;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.network.DynamicAct;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.network.UserDynamicAct;
import com.tizi.quanzi.otto.FragmentResume;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseActivity;

public class DynsActivity extends BaseActivity {

    private DynsActivityFragment dynsActivityFragment;
    private SendDynFragment sendDynFragment;
    private Menu menu;

    private String groupID;
    private String themeID;
    private String themeString;

    private boolean isUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dyns);
        String dynID = getIntent().getStringExtra("dynID");
        isUser = getIntent().getBooleanExtra("isUser", false);
        if (dynID == null) {
            dynsActivityFragment = new DynsActivityFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, dynsActivityFragment)
                    .commit();
        } else {
            RetrofitNetworkAbs.NetworkListener listener = new RetrofitNetworkAbs.NetworkListener() {
                @Override
                public void onOK(Object ts) {
                    Dyns dyns = (Dyns) ts;
                    if (dyns.dyns.size() == 1) {
                        DynInfoFragment dynInfoFragment = new DynInfoFragment();
                        dynInfoFragment.setDyn(dyns.dyns.get(0));
                        dynInfoFragment.setIsUser(isUser);
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment, dynInfoFragment)
                                .commit();
                    } else {
                        Snackbar.make(view, "找不到对应动态 数量:" + dyns.dyns.size(), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(String Message) {
                    Snackbar.make(view, "找不到对应动态 " + Message, Snackbar.LENGTH_LONG).show();
                }
            };

            if (isUser) {
                UserDynamicAct.getNewInstance().setNetworkListener(listener).getDynamicByID(dynID);
            } else {
                DynamicAct.getNewInstance().setNetworkListener(listener).getDynamicByID(dynID);
            }

        }
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

        if (id == R.id.action_send) {
            if (sendDynFragment.SendDyn()) {
                getSupportFragmentManager().popBackStack();
                new Timer().setOnResult(new Timer.OnResult() {
                    @Override
                    public void OK() {
                        dynsActivityFragment.quaryMore(themeID, groupID, 0);
                    }

                    @Override
                    public void countdown(int s) {

                    }
                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 2000);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toSendDyn() {
        if (Tool.isGuest()) {
            Tool.GuestAction(this);
            return;
        }
        sendDynFragment = SendDynFragment.newInstance(themeString, groupID, themeID, isUser);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                        R.anim.no_change, R.anim.slide_out_to_bottom)
                .hide(dynsActivityFragment)
                .add(R.id.fragment, sendDynFragment)
                .addToBackStack("SendDynFragment").commit();
    }

    /*Fragment 启动与停止*/
    @Subscribe
    public void onFragmentResume(FragmentResume fragmentResume) {
        if (fragmentResume.FramgentName.compareTo(SendDynFragment.class.getSimpleName()) == 0) {
            if (fragmentResume.resumeOrPause) {
                menu.findItem(R.id.action_send).setVisible(true);
            } else {
                menu.findItem(R.id.action_send).setVisible(false);
            }
        }
    }
}
