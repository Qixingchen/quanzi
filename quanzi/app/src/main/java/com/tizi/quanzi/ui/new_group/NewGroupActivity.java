package com.tizi.quanzi.ui.new_group;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.NewAVIMConversation;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.GroupSetting;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.otto.FragmentResume;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.main.MainActivity;

import java.util.ArrayList;

public class NewGroupActivity extends BaseActivity {
    NewGroupStep1Fragment newGroupStep1Fragment;
    NewGroupStep2Fragment newGroupStep2Fragment;
    NewGroupStep1Fragment.NewGroupStep1Ans ans;
    private String convID = "0";
    private Menu mMenu;

    private GroupTagFragment groupTagFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
    }

    @Override
    protected void findView() {
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
        NewAVIMConversation.getNewInstance().setConversationCallBack(
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
        int id = item.getItemId();

        if (id == R.id.action_next_step) {
            NewGroupStep1Fragment.NewGroupStep1Ans temp = newGroupStep1Fragment.getNewGroupAns();
            /*下一步*/
            if (temp.complete) {

                /*ConvID*/
                if (convID.compareTo("0") == 0) {
                    Snackbar.make(view, "LC ConvID 为空,请稍等,正在重试", Snackbar.LENGTH_LONG).show();
                    NewAVIMConversation.getNewInstance().setConversationCallBack(
                            new NewAVIMConversation.ConversationCallBack() {
                                @Override
                                public void setConversationID(String conversationID) {
                                    convID = conversationID;
                                }
                            }
                    ).newAChatGroup();
                    return super.onOptionsItemSelected(item);
                }

                ans = temp;
                mMenu.findItem(R.id.action_next_step).setVisible(false);
                mMenu.findItem(R.id.action_complete).setVisible(true);
                toolbar.setTitle("邀请好友");
                newGroupStep2Fragment = new NewGroupStep2Fragment();
                newGroupStep2Fragment.setAns(ans);
                String GroupName = ans.groupName;
                String icon = ans.groupFaceUri;
                String notice = ans.groupSign;

                GroupSetting.getNewInstance().setNetworkListener(
                        new RetrofitNetworkAbs.NetworkListener() {
                            @Override
                            public void onOK(Object ts) {
                                Group group = (Group) ts;

                                newGroupStep2Fragment.setGroupID(group.getGroupId());
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment, newGroupStep2Fragment).commit();
                                GroupClass groupClass = new GroupClass();
                                groupClass.ID = group.getGroupId();
                                groupClass.Name = ans.groupName;
                                groupClass.Face = ans.groupFaceUri;
                                groupClass.Type = StaticField.ConvType.GROUP;
                                groupClass.Notice = ans.groupSign;
                                groupClass.convId = convID;
                                groupClass.createUser = AppStaticValue.getUserID();
                                groupClass.memlist = new ArrayList<>();
                                GroupList.getInstance().addGroup(groupClass);
                            }

                            @Override
                            public void onError(String Message) {
                                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .NewAGroup(GroupName, icon, notice, ans.tags, convID);


            } else {
                Snackbar.make(view, "头像和群名是必须的哦~", Snackbar.LENGTH_LONG).show();
            }
            return super.onOptionsItemSelected(item);
        }

        if (id == R.id.action_complete) {
            startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

        if (id == R.id.action_tag_complete) {
            newGroupStep1Fragment.setTags(groupTagFragment.OnOK());
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction().show(newGroupStep1Fragment).commit();
            mMenu.findItem(R.id.action_next_step).setVisible(true);
            mMenu.findItem(R.id.action_tag_complete).setVisible(false);
            mMenu.findItem(R.id.action_complete).setVisible(false);
        }

        return super.onOptionsItemSelected(item);
    }

    public void CallForTagFragment(ArrayList<AllTags.TagsEntity> tags) {
        mMenu.findItem(R.id.action_next_step).setVisible(false);
        mMenu.findItem(R.id.action_tag_complete).setVisible(true);
        mMenu.findItem(R.id.action_complete).setVisible(false);
        groupTagFragment = GroupTagFragment.newInstance(tags, false, true);
        getSupportFragmentManager().beginTransaction().hide(newGroupStep1Fragment)
                .add(R.id.fragment, groupTagFragment).addToBackStack("GroupTagFragment").commit();
    }

    @Subscribe
    public void onFragementResumeAndPause(FragmentResume resume) {
        if (!resume.resumeOrPause && resume.FramgentName.equals(GroupTagFragment.class.getSimpleName())) {
            mMenu.findItem(R.id.action_next_step).setVisible(true);
            mMenu.findItem(R.id.action_tag_complete).setVisible(false);
            mMenu.findItem(R.id.action_complete).setVisible(false);
        }
    }

}
