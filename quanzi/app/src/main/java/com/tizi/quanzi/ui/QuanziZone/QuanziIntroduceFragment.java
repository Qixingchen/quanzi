package com.tizi.quanzi.ui.QuanziZone;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GroupZonePagerAdapter;
import com.tizi.quanzi.gson.GroupInfo;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuanziIntroduceFragment extends BaseFragment {

    private Toolbar toolbar;
    private ImageView groupFaceImageView;
    private NetworkImageView zoneBackgroundImageView, userface[];
    private TextView zoneSignTextview;
    private CollapsingToolbarLayout collapsingtoolbar;
    private ViewPager viewPager;
    private GroupZonePagerAdapter groupZonePagerAdapter;


    public QuanziIntroduceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quanzi_introduce, container, false);
    }

    @Override
    protected void findViews() {
        zoneBackgroundImageView = (NetworkImageView) mActivity.findViewById(R.id.zoneBackground);
        groupFaceImageView = (ImageView) mActivity.findViewById(R.id.gruop_face);
        zoneSignTextview = (TextView) mActivity.findViewById(R.id.zoneSign);

        collapsingtoolbar =
                (CollapsingToolbarLayout) mActivity.findViewById(R.id.collapsing_toolbar);
        collapsingtoolbar.setTitle("圈名");
        //crash is know issue at https://code.google.com/p/android/issues/detail?id=183166
        viewPager = (ViewPager) mActivity.findViewById(R.id.detail_viewpager);
        groupZonePagerAdapter = new GroupZonePagerAdapter(mActivity);
        viewPager.setAdapter(groupZonePagerAdapter);
        toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        userface = new NetworkImageView[12];
        userface[0] = (NetworkImageView) mActivity.findViewById(R.id.Group_user1);
        userface[1] = (NetworkImageView) mActivity.findViewById(R.id.Group_user2);
        userface[2] = (NetworkImageView) mActivity.findViewById(R.id.Group_user3);
        userface[3] = (NetworkImageView) mActivity.findViewById(R.id.Group_user4);
        userface[4] = (NetworkImageView) mActivity.findViewById(R.id.Group_user5);
        userface[5] = (NetworkImageView) mActivity.findViewById(R.id.Group_user6);
        userface[6] = (NetworkImageView) mActivity.findViewById(R.id.Group_user7);
        userface[7] = (NetworkImageView) mActivity.findViewById(R.id.Group_user8);
        userface[8] = (NetworkImageView) mActivity.findViewById(R.id.Group_user9);
        userface[9] = (NetworkImageView) mActivity.findViewById(R.id.Group_user10);
        userface[10] = (NetworkImageView) mActivity.findViewById(R.id.Group_user11);
        userface[11] = (NetworkImageView) mActivity.findViewById(R.id.Group_user12);
    }

    @Override
    protected void initViewsAndSetEvent() {

        ((AppCompatActivity) mActivity).setSupportActionBar(toolbar);
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        Picasso.with(mActivity).load(groupInfo.icon)
                .resize(GetThumbnailsUri.getPXs(mActivity, 120),
                        GetThumbnailsUri.getPXs(mActivity, 120))
                .into(groupFaceImageView);
        zoneBackgroundImageView.setImageUrl(groupInfo.icon, GetVolley.getmInstance(mActivity).getImageLoader());
        // TODO: 15/9/1 sign
        zoneSignTextview.setText("签名是：");
        for (int i = 0; i < groupInfo.memlist.size(); i++) {
            userface[i].setImageUrl(groupInfo.memlist.get(i).icon, GetVolley.getmInstance(mActivity).getImageLoader());
        }
        deleteUserfaces(groupInfo.memlist.size());
    }

    private void deleteUserfaces(int userCount) {
        if (userCount == 12) {
            return;
        }
        for (int i = userCount; i < 12; i++) {
            userface[i].setVisibility(View.GONE);
        }
    }
}
