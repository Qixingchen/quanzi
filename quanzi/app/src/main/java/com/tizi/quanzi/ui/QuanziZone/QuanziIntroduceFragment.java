package com.tizi.quanzi.ui.QuanziZone;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GroupZonePagerAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuanziIntroduceFragment extends Fragment {

    private Activity mActivity;
    private ImageView zoneBackgroundImageView, groupFaceImageView, userface[];
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
    public void onStart() {
        super.onStart();
        findViews();

        Picasso.with(mActivity).load(R.drawable.face)
                .into(groupFaceImageView);
        Picasso.with(mActivity).load(R.drawable.face)
                .into(zoneBackgroundImageView);
        zoneSignTextview.setText("这里是签名");

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    private void findViews() {
        zoneBackgroundImageView = (ImageView) mActivity.findViewById(R.id.zoneBackground);
        groupFaceImageView = (ImageView) mActivity.findViewById(R.id.gruop_face);
        zoneSignTextview = (TextView) mActivity.findViewById(R.id.zoneSign);

        collapsingtoolbar =
                (CollapsingToolbarLayout) mActivity.findViewById(R.id.collapsing_toolbar);
        collapsingtoolbar.setTitle("圈名");
        viewPager = (ViewPager) mActivity.findViewById(R.id.detail_viewpager);
        groupZonePagerAdapter = new GroupZonePagerAdapter(mActivity);
        viewPager.setAdapter(groupZonePagerAdapter);

    }
}
