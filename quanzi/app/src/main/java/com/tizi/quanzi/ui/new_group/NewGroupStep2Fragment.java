package com.tizi.quanzi.ui.new_group;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.InviteListAdapter;
import com.tizi.quanzi.chat.GroupUserAdmin;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.ContantUsers;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.otto.PermissionAnser;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGroupStep2Fragment extends BaseFragment {

    private NewGroupStep1Fragment.NewGroupStep1Ans ans;
    private String groupID;

    private RecyclerView.LayoutManager mLayoutManager;
    private NetworkImageView groupfaceimageview;
    private TextView groupnametextview;
    private RecyclerView shareRecyclerView;

    private InviteListAdapter inviteListAdapter;

    public NewGroupStep2Fragment() {
        // Required empty public constructor
    }

    public void setAns(NewGroupStep1Fragment.NewGroupStep1Ans ans) {
        this.ans = ans;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_group_step2, container, false);

    }

    @Override
    protected void findViews(View view) {
        this.shareRecyclerView = (RecyclerView) view.findViewById(R.id.shareRecyclerView);
        this.groupnametextview = (TextView) view.findViewById(R.id.group_name_text_view);
        this.groupfaceimageview = (NetworkImageView) view.findViewById(R.id.group_face_image_view);
    }

    @Override
    protected void initViewsAndSetEvent() {
        groupfaceimageview.setImageUrl(ans.groupFaceUri, GetVolley.getmInstance().getImageLoader());
        groupnametextview.setText(ans.groupName);
        shareRecyclerView.setHasFixedSize(true);
        shareRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        inviteListAdapter = new InviteListAdapter(null, null, mActivity,
                new InviteListAdapter.OnAddUser() {
                    @Override
                    public void add(String userID) {
                        final String convID = GroupList.getInstance().getGroup(groupID).convId;

                        GroupUserAdmin.getInstance(mContext)
                                .setOnResult(new GroupUserAdmin.OnResult() {
                                    @Override
                                    public void OK() {
                                        Toast.makeText(mContext, "添加请求发送成功", Toast.LENGTH_LONG).show();
                                        Log.i(TAG, "添加请求发送成功");
                                    }

                                    @Override
                                    public void error(String errorMessage) {
                                        Toast.makeText(mContext, "添加失败" + errorMessage
                                                , Toast.LENGTH_LONG).show();
                                        Log.w(TAG, "添加失败" + errorMessage);
                                    }
                                })
                                .addMember(convID, groupID, userID);
                    }
                });

        shareRecyclerView.setAdapter(inviteListAdapter);

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            int code = StaticField.PermissionRequestCode.addContactUsersInNewGroup;
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_CONTACTS}, code);
            return;
        }

        FindContactUsers();

    }

    private void FindContactUsers() {
        FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                ContantUsers users = (ContantUsers) ts;
                if (users.mobiles.size() == 0) {
                    Toast.makeText(mContext, "没有好友", Toast.LENGTH_LONG).show();
                    return;
                }
                inviteListAdapter.addUser(users.mobiles);
            }

            @Override
            public void onError(String Message) {

            }
        }).findContactUsers();
    }

    @Subscribe
    public void onContactPermissionAns(PermissionAnser permissionAnser) {
        if (!permissionAnser.allGreen) {
            return;
        }
        if (StaticField.PermissionRequestCode.addContactUsersInNewGroup == permissionAnser.requestCode) {

            FindContactUsers();
        }
    }

}
