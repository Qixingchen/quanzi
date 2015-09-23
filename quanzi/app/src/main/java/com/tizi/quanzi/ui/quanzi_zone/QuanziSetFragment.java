package com.tizi.quanzi.ui.quanzi_zone;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.GroupSetting;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * 群的设置
 */
public class QuanziSetFragment extends BaseFragment {

    private TextView quanziName, quanziTag, quanziSign, deleteMess;
    private Switch messNotifiSwitch;
    private Button exitQuanzi;
    private GroupAllInfo groupAllInfo;


    public QuanziSetFragment() {
        // Required empty public constructor
    }

    public void setGroupAllInfo(GroupAllInfo groupAllInfo) {
        this.groupAllInfo = groupAllInfo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quanzi_set, container, false);
    }

    @Override
    public void findViews(View view) {
        quanziName = (TextView) view.findViewById(R.id.quanzi_name);
        quanziTag = (TextView) view.findViewById(R.id.quanzi_tag);
        quanziSign = (TextView) view.findViewById(R.id.quanzi_sign);
        deleteMess = (TextView) view.findViewById(R.id.delete_mess);
        messNotifiSwitch = (Switch) view.findViewById(R.id.mess_notifi_switch);
        exitQuanzi = (Button) view.findViewById(R.id.exit_quanzi);

    }

    @Override
    public void initViewsAndSetEvent() {
        final GroupClass group = (GroupClass) GroupList.getInstance().getGroup(groupAllInfo.group.groupNo);
        if (group == null) {
            Log.e(TAG, "group 获取失败");
            return;
        }


        if (groupAllInfo != null) {
            quanziName.setText(group.Name);
            quanziName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = mActivity.getLayoutInflater();
                    final View layout = inflater.inflate(R.layout.dialog_one_line,
                            (ViewGroup) mActivity.findViewById(R.id.one_line_dialog));
                    final EditText input = (EditText) layout.findViewById(R.id.dialog_edit_text);
                    final TextView title = (TextView) layout.findViewById(R.id.dialog_title);

                    title.setText("输入新的圈子名称");
                    input.setHint("圈子名称");
                    input.setText(group.Name);
                    builder.setTitle("更改圈子名称").setView(layout)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String name = input.getText().toString();
                                    //后台

                                    //LC发送广播
                                    SendMessage.getInstance().sendTextMessage(
                                            group.convId,
                                            name,
                                            SendMessage.setGroupManageSysMessAttr(
                                                    SendMessage.setMessAttr(group.ID,
                                                            StaticField.ChatBothUserType.GROUP),
                                                    group.convId,
                                                    StaticField.SystemMessAttrName.systemFlag.group_change_name,
                                                    ""));

                                    //GroupList更新
                                    group.Name = name;
                                    GroupList.getInstance().updateGroup(group);
                                    quanziName.setText(name);

                                    //后台更新
                                    GroupSetting.getInstance().ChangeName(group.ID, group.Name);

                                }
                            }).setNegativeButton("取消", null).show();
                }
            });
            quanziSign.setText(groupAllInfo.group.groupName);

            if (group.createUser.compareTo(AppStaticValue.getUserID()) == 0) {
                exitQuanzi.setText("解散圈子");
            } else {
                exitQuanzi.setText("退出圈子");
            }
            deleteMess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("确认删除聊天记录么？").setMessage("删除后无法恢复");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBAct.getInstance().deleteAllMessage(groupAllInfo.group.convId);
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            });
            messNotifiSwitch.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            GroupClass groupClass = (GroupClass) GroupList.getInstance().getGroup(group.ID);
                            if (groupClass != null) {
                                if (isChecked) {

                                    groupClass.setNeedNotifi(false, true);
                                } else {
                                    groupClass.setNeedNotifi(true, true);

                                }
                            }
                        }

                    }

            );
            exitQuanzi.setOnClickListener(
                    new View.OnClickListener()

                    {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            final GroupClass groupClass = (GroupClass) GroupList.getInstance()
                                    .getGroup(groupAllInfo.group.groupNo);
                            if (groupClass.createUser.compareTo(AppStaticValue.getUserID()) == 0) {
                                builder.setTitle("确认解散这个圈子么？");
                                builder.setPositiveButton("解散", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        GroupUserAdmin.getInstance(mActivity).setOnResult(
                                                new GroupUserAdmin.OnResult() {
                                                    @Override
                                                    public void OK() {
                                                        GroupList.getInstance().deleteGroup(groupClass.ID);
                                                        Intent mainActivity = new Intent(mActivity, MainActivity.class);
                                                        startActivity(mainActivity);
                                                    }

                                                    @Override
                                                    public void error(String errorMessage) {

                                                    }
                                                }
                                        ).deleteGroup(groupClass.convId, groupClass.ID);
                                    }
                                });
                                builder.setNegativeButton("取消", null);
                                builder.create().show();
                            } else {
                                builder.setTitle("确认退出这个圈子么？");
                                builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        GroupUserAdmin.getInstance(mActivity).setOnResult(
                                                new GroupUserAdmin.OnResult() {
                                                    @Override
                                                    public void OK() {
                                                        GroupList.getInstance().deleteGroup(groupClass.ID);
                                                        Intent mainActivity = new Intent(mActivity, MainActivity.class);
                                                        startActivity(mainActivity);
                                                    }

                                                    @Override
                                                    public void error(String errorMessage) {

                                                    }
                                                }
                                        ).deleteMember(groupClass.convId, groupClass.ID, AppStaticValue.getUserID());

                                    }
                                });
                                builder.setNegativeButton("取消", null);
                                builder.create().show();
                            }
                        }
                    }

            );
        }
    }

}
