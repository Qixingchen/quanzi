package com.tizi.quanzi.ui.NewGroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.network.AddOrQuitGroup;
import com.tizi.quanzi.ui.BaseActivity;

public class NewGroupActivity extends BaseActivity {
    NewGroupStep1Fragment newGroupStep1Fragment;
    NewGroupStep2Fragment newGroupStep2Fragment;
    NewGroupStep1Fragment.NewGroupStep1Ans ans;
    Toolbar toolbar;
    private Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
    }

    @Override
    protected void findView() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        newGroupStep1Fragment = new NewGroupStep1Fragment();
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, newGroupStep1Fragment).commit();
    }

    @Override
    protected void setViewEvent() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_group, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next_step) {
            NewGroupStep1Fragment.NewGroupStep1Ans temp = newGroupStep1Fragment.getNewGroupAns();
            if (temp.complete) {
                ans = temp;
                mMenu.findItem(R.id.action_next_step).setVisible(false);
                mMenu.findItem(R.id.action_complete).setVisible(true);
                toolbar.setTitle("邀请好友");
                newGroupStep2Fragment = new NewGroupStep2Fragment();
                newGroupStep2Fragment.setAns(ans);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, newGroupStep2Fragment).commit();
                // TODO: 15/8/31 send to Server
                String GroupName = ans.groupName;
                String icon = ans.groupFaceUri;
                String notice = ans.groupSign;
                String userID = App.getUserID(), tag = "[{}]";
                AddOrQuitGroup.getInstance().setNewGroupListener(null
                        //todo add Group
                ).NewAGroup(GroupName, icon, notice, userID, tag);

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newGroupStep1Fragment.onIntentResult(requestCode, resultCode, data);
    }
}
