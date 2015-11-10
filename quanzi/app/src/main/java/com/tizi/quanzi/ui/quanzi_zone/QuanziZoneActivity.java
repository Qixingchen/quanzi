package com.tizi.quanzi.ui.quanzi_zone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.network.GroupSetting;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.new_group.GroupTagFragment;

import java.util.ArrayList;

/**
 * 群空间
 */
public class QuanziZoneActivity extends BaseActivity {

    private QuanziIntroduceFragment quanziIntroduceFragment;
    private QuanziSetFragment quanziSetFragment;
    private GroupAllInfo mGroupAllInfo;
    private GroupTagFragment groupTagFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanzi_zone);


    }

    @Override
    protected void findView() {

    }

    @Override
    protected void initView() {
        quanziIntroduceFragment = new QuanziIntroduceFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment, quanziIntroduceFragment).commit();

        Intent intent = getIntent();
        final String convID = intent.getStringExtra("conversation");
        final String GroupID;
        if (convID == null || convID.equals("")) {
            GroupID = intent.getStringExtra("groupID");
        } else {
            GroupID = GroupList.getInstance().getGroupIDByConvID(convID);
        }
        GroupSetting.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                GroupAllInfo groupAllInfo = (GroupAllInfo) ts;
                mGroupAllInfo = groupAllInfo;
                if (quanziIntroduceFragment != null) {
                    mGroupAllInfo.group.groupNo = GroupID;
                    quanziIntroduceFragment.setGroupInfo(groupAllInfo);
                }
            }

            @Override
            public void onError(String Message) {

            }
        }).queryGroup(GroupID);
    }

    @Override
    protected void setViewEvent() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quanzi_zone, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            quanziSetFragment = QuanziSetFragment.newInstance(mGroupAllInfo);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, quanziSetFragment)
                    .addToBackStack("quanziSetFragment").commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        quanziIntroduceFragment.onActivityResult(requestCode, resultCode, data);
    }

    public void showSetting() {
        mMenu.findItem(R.id.action_settings).setVisible(true);
    }

    public void callForTagFragment(ArrayList<AllTags.TagsEntity> tags) {
        groupTagFragment = GroupTagFragment.newInstance(tags, true);
        getSupportFragmentManager().beginTransaction().hide(quanziSetFragment)
                .add(R.id.fragment, groupTagFragment).addToBackStack("GroupTagFragment").commit();
    }

    public void OnTagsSelectOk(ArrayList<AllTags.TagsEntity> tags) {
        quanziSetFragment.setTags(tags);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().show(quanziSetFragment).commit();
    }
}
