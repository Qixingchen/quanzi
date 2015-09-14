package com.tizi.quanzi.ui.quanzi_zone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.GroupUserInfo;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.AddOrQuaryGroup;
import com.tizi.quanzi.ui.BaseActivity;

/**
 * 群空间
 */
public class QuanziZoneActivity extends BaseActivity {

    private QuanziIntroduceFragment quanziIntroduceFragment;
    private QuanziSetFragment quanziSetFragment;
    private GroupUserInfo mGroupUserInfo;

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
        final String GroupID = GroupList.getInstance().getGroupIDByConvID(convID);
        AddOrQuaryGroup.getInstance().setQueryListener(new AddOrQuaryGroup.QueryGroupListener() {
            @Override
            public void OK(GroupUserInfo groupUserInfo) {
                mGroupUserInfo = groupUserInfo;
                if (quanziIntroduceFragment != null) {
                    mGroupUserInfo.groupNo = GroupID;
                    quanziIntroduceFragment.setGroupInfo(groupUserInfo, (GroupClass) GroupList.getInstance().getGroup(GroupID));
                }
            }

            @Override
            public void Error(String Mess) {

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
            quanziSetFragment = new QuanziSetFragment();
            quanziSetFragment.setGroupUserInfo(mGroupUserInfo);
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
}
