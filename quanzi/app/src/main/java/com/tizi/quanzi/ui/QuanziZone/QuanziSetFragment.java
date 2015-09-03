package com.tizi.quanzi.ui.QuanziZone;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.GroupUserInfo;
import com.tizi.quanzi.network.AddOrQuitGroup;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuanziSetFragment extends BaseFragment {

    private TextView quanziName, quanziTag, quanziSign, deleteMess;
    private Switch messNotifiSwitch;
    private Button exitQuanzi;
    private GroupUserInfo groupUserInfo;


    public QuanziSetFragment() {
        // Required empty public constructor
    }

    public void setGroupUserInfo(GroupUserInfo groupUserInfo) {
        this.groupUserInfo = groupUserInfo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quanzi_set, container, false);
    }

    @Override
    public void findViews() {
        quanziName = (TextView) mActivity.findViewById(R.id.quanzi_name);
        quanziTag = (TextView) mActivity.findViewById(R.id.quanzi_tag);
        quanziSign = (TextView) mActivity.findViewById(R.id.quanzi_sign);
        deleteMess = (TextView) mActivity.findViewById(R.id.delete_mess);
        messNotifiSwitch = (Switch) mActivity.findViewById(R.id.mess_notifi_switch);
        exitQuanzi = (Button) mActivity.findViewById(R.id.exit_quanzi);

    }

    @Override
    public void initViewsAndSetEvent() {
        if (groupUserInfo != null) {
            quanziName.setText(groupUserInfo.groupName);
            quanziSign.setText(groupUserInfo.groupName);
            deleteMess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("确认删除聊天记录么？").setMessage("删除后无法恢复");
                    builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO: 15/8/31 delete
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            });
            messNotifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // TODO: 15/8/31 ignore
                    } else {
                        // TODO: 15/8/31 not ignore
                    }
                }
            });
            exitQuanzi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("确认退出这个圈子么？");
                    builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddOrQuitGroup.getInstance()
                                    .setQuitGroupListener(new AddOrQuitGroup.QuitGroupListener() {
                                        @Override
                                        public void onOK() {
                                            // TODO: 15/8/31 ok
                                        }

                                        @Override
                                        public void onError() {
                                            // TODO: 15/8/31 error
                                        }
                                    })
                                    .exitGroup(groupUserInfo.groupNo);
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            });
        }
    }

}