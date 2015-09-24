package com.tizi.quanzi.ui.quanzi_zone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.SaveCallback;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.DynsAdapter;
import com.tizi.quanzi.adapter.GroupUserAdapter;
import com.tizi.quanzi.adapter.GroupZonePagerAdapter;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.DynamicAct;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.GroupSetting;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;

import java.io.IOException;

/**
 * A placeholder fragment containing a simple view.
 * 群空间，介绍页
 */
public class QuanziIntroduceFragment extends BaseFragment {

    private Toolbar toolbar;
    private ImageView groupFaceImageView;
    private NetworkImageView zoneBackgroundImageView, userface[];
    private TextView zoneSignTextview;
    private CollapsingToolbarLayout collapsingtoolbar;
    private ViewPager viewPager;
    private GroupZonePagerAdapter groupZonePagerAdapter;
    private RecyclerView groupUsersRecyclerView, groupDynsRecyclerView;
    private GroupUserAdapter groupUserAdapter;
    private DynsAdapter dynsAdapter;
    private RecyclerView.LayoutManager groupUsersLayoutManager;
    private RecyclerView.LayoutManager groupDynsLayoutManager;
    private FloatingActionButton changeGroupImageFAB;
    private GroupAllInfo groupAllInfo;
    private GroupClass groupClass;
    private boolean hasMoreToGet = true;
    private int lastIndex = 0;

    private RequreForImage requreForImage;


    public QuanziIntroduceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quanzi_introduce, container, false);
    }

    @Override
    protected void findViews(View view) {
        zoneBackgroundImageView = (NetworkImageView) view.findViewById(R.id.zoneBackground);
        groupFaceImageView = (ImageView) view.findViewById(R.id.gruop_face);
        zoneSignTextview = (TextView) view.findViewById(R.id.zoneSign);

        collapsingtoolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        //crash is know issue at https://code.google.com/p/android/issues/detail?id=183166
        //        viewPager = (ViewPager) view.findViewById(R.id.detail_viewpager);
        //        groupZonePagerAdapter = new GroupZonePagerAdapter(mActivity);
        //        viewPager.setAdapter(groupZonePagerAdapter);
        groupUsersRecyclerView = (RecyclerView) view.findViewById(R.id.group_users_item_recycler_view);
        groupDynsRecyclerView = (RecyclerView) view.findViewById(R.id.group_dyns_item_recycler_view);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        changeGroupImageFAB = (FloatingActionButton) view.findViewById(R.id.change_image_fab);
    }

    @Override
    protected void initViewsAndSetEvent() {

        ((AppCompatActivity) mActivity).setSupportActionBar(toolbar);

        groupDynsRecyclerView.setHasFixedSize(true);
        dynsAdapter = new DynsAdapter(null, mActivity);
        lastIndex = 0;
        dynsAdapter.setNeedMore(new DynsAdapter.NeedMore() {
            @Override
            public void needMore() {
                if (hasMoreToGet) {
                    quaryMore(groupClass.ID, lastIndex);
                    lastIndex += StaticField.MessageQueryLimit.DynamicLimit;
                }
            }
        });
        groupDynsLayoutManager = new LinearLayoutManager(mActivity);
        groupDynsRecyclerView.setLayoutManager(groupDynsLayoutManager);
        groupDynsRecyclerView.setAdapter(dynsAdapter);
        groupDynsRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        groupUserAdapter = new GroupUserAdapter(mActivity,
                groupAllInfo == null ? null : groupAllInfo.memlist,
                groupClass != null && groupClass.createUser.compareTo(AppStaticValue.getUserID()) == 0);
        groupUsersLayoutManager = new GridLayoutManager(mActivity, 6);
        Log.i(TAG, "groupUsersLayoutManager Heigh:" + groupUsersLayoutManager.getHeight());

        groupUsersRecyclerView.setAdapter(groupUserAdapter);
        groupUsersRecyclerView.setLayoutManager(groupUsersLayoutManager);
        changeGroupImageFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage = new RequreForImage(mActivity);
                requreForImage.showDialogAndCallIntent("选择圈子照片");
            }
        });
        showGroupInfo();
    }

    /**
     * 设置群的信息
     */
    public void setGroupInfo(GroupAllInfo groupAllInfo, final GroupClass groupClass) {
        this.groupAllInfo = groupAllInfo;
        this.groupClass = groupClass;
        boolean isCreate = groupClass.createUser.compareTo(AppStaticValue.getUserID()) == 0;
        if (groupUserAdapter != null) {
            groupUserAdapter.setMemlist(groupAllInfo.memlist);
            groupUserAdapter.setIsCreater(isCreate);
            groupUserAdapter.setGroupID(groupClass.ID);
        }
        showGroupInfo();

    }

    private void showGroupInfo() {
        if (groupClass != null) {
            collapsingtoolbar.setTitle(groupClass.Name);
            Picasso.with(mActivity).load(groupClass.Face)
                    .resize(GetThumbnailsUri.getPXs(mActivity, 120),
                            GetThumbnailsUri.getPXs(mActivity, 120))
                    .into(groupFaceImageView);
            zoneBackgroundImageView.setImageUrl(groupClass.background,
                    GetVolley.getmInstance().getImageLoader());
            zoneSignTextview.setText("签名是：" + groupClass.Notice);
            quaryMore(groupClass.ID, lastIndex);
            lastIndex += StaticField.MessageQueryLimit.DynamicLimit;
        }
        if (groupAllInfo != null) {
            groupUsersRecyclerView.getLayoutParams().height = (int) (105 * GetThumbnailsUri.getDpi(mActivity)
                    * ((groupAllInfo.memlist.size() / 6) + 1));
        }

    }

    private void quaryMore(String groupID, int lastIndex) {
        Log.i(TAG, "查询群动态 lastIndex=" + lastIndex);
        DynamicAct.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Dyns dyns = (Dyns) ts;
                dynsAdapter.addItems(dyns.dyns);
                if (dyns.dyns.size() != StaticField.MessageQueryLimit.DynamicLimit) {
                    hasMoreToGet = false;
                }
                Log.i(TAG, "groupDynsLayoutManager Heigh:" + groupDynsLayoutManager.getHeight());
            }

            @Override
            public void onError(String Message) {

            }
        }).getGroupDynamic("", groupID, lastIndex);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case StaticField.PermissionRequestCode.requreForImage:
                    String filepath = requreForImage.ZipedFilePathFromIntent(data);
                    savePhotoToLC(filepath);
                    break;
            }
        }
    }

    /**
     * 将图片储存到LeanCloud
     *
     * @param filepath 图片地址
     */
    private void savePhotoToLC(String filepath) {
        AVFile file = null;
        try {
            file = AVFile.withAbsoluteLocalPath(AppStaticValue.getUserID() + "face.jpg",
                    filepath);
            final AVFile finalFile = file;
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        //上传失败
                    } else {
                        String photoUri = finalFile.getUrl();
                        zoneBackgroundImageView.setImageUrl(photoUri, GetVolley.getmInstance().getImageLoader());
                        GroupSetting.getInstance().changeIcon(groupClass.ID, photoUri);
                        groupClass.Face = photoUri;
                        GroupList.getInstance().updateGroup(groupClass);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
