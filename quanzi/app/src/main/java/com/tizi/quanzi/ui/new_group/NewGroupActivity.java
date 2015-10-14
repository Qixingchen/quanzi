package com.tizi.quanzi.ui.new_group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.NewAVIMConversation;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.AddOrQuaryGroup;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.main.MainActivity;

public class NewGroupActivity extends BaseActivity {
    NewGroupStep1Fragment newGroupStep1Fragment;
    NewGroupStep2Fragment newGroupStep2Fragment;
    NewGroupStep1Fragment.NewGroupStep1Ans ans;
    Toolbar toolbar;
    private String convID;
    private Menu mMenu;

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
        NewAVIMConversation.getInstance().setConversationCallBack(
                new NewAVIMConversation.ConversationCallBack() {
                    @Override
                    public void setConversationID(String conversationID) {
                        convID = conversationID;
                    }
                }
        ).newAChatGroup();
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
            /*下一步*/
            if (temp.complete) {
                ans = temp;
                mMenu.findItem(R.id.action_next_step).setVisible(false);
                mMenu.findItem(R.id.action_complete).setVisible(true);
                toolbar.setTitle("邀请好友");
                newGroupStep2Fragment = new NewGroupStep2Fragment();
                newGroupStep2Fragment.setAns(ans);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, newGroupStep2Fragment).commit();
                String GroupName = ans.groupName;
                String icon = ans.groupFaceUri;
                String notice = ans.groupSign;
                String userID = AppStaticValue.getUserID(), tag = "[{}]";

                AddOrQuaryGroup.getNewInstance().setNetworkListener(
                        new RetrofitNetworkAbs.NetworkListener() {
                            @Override
                            public void onOK(Object ts) {
                                Group group = (Group) ts;

                                GroupClass groupClass = new GroupClass();
                                groupClass.ID = group.getGroupId();
                                groupClass.Name = ans.groupName;
                                groupClass.Face = ans.groupFaceUri;
                                groupClass.Type = StaticField.ConvType.GROUP;
                                groupClass.Notice = ans.groupSign;
                                groupClass.convId = convID;
                                groupClass.createUser = AppStaticValue.getUserID();
                                groupClass.UnreadCount = 0;
                                GroupList.getInstance().addGroup(groupClass);
                            }

                            @Override
                            public void onError(String Message) {

                            }
                        })
                        .NewAGroup(GroupName, icon, notice, tag, convID);


            }
            return true;
        }

        if (id == R.id.action_complete) {
            startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        newGroupStep1Fragment.onIntentResult(requestCode, resultCode, data);
    }
}
