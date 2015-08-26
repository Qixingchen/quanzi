package com.tizi.quanzi.ui.QuanziZone;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class QuanziIntroduceFragment extends Fragment {

    private Activity mActivity;
    private ImageView zoneBackgroundImageView, groupFaceImageView, userface[];


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
        zoneBackgroundImageView = (ImageView) mActivity.findViewById(R.id.zoneBackground);
        groupFaceImageView = (ImageView) mActivity.findViewById(R.id.gruop_face);
        Picasso.with(mActivity).load(R.drawable.face)
                .into(zoneBackgroundImageView);
        Picasso.with(mActivity).load(R.drawable.face)
                .into(groupFaceImageView);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
}
