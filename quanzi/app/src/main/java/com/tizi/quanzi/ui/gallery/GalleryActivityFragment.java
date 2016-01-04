package com.tizi.quanzi.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.GalleryAdapter;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.BaseFragment;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class GalleryActivityFragment extends BaseFragment {

    private List<String> pics;

    private ViewPager picsViewPager;
    private TextView picsNum;
    private GalleryAdapter galleryAdapter;

    public GalleryActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    protected void findViews(View view) {
        picsViewPager = (ViewPager) view.findViewById(R.id.picsViewPager);
        picsNum = (TextView) view.findViewById(R.id.pics_num);
    }

    @Override
    protected void initViewsAndSetEvent() {
        Intent intent = getActivity().getIntent();
        pics = intent.getStringArrayListExtra(StaticField.GalleryPara.pics);
        int nowPosition = intent.getIntExtra(StaticField.GalleryPara.nowPosition, 0);
        picsNum.setText(getPicsNum(nowPosition + 1));
        galleryAdapter = new GalleryAdapter(pics, (BaseActivity) getActivity());
        picsViewPager.setAdapter(galleryAdapter);
        picsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                picsNum.setText(getPicsNum(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        picsViewPager.setCurrentItem(nowPosition);
    }

    private String getPicsNum(int nowPosition) {
        return String.valueOf(nowPosition + "/" + pics.size());
    }
}
