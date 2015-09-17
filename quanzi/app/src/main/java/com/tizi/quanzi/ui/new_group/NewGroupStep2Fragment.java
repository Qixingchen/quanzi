package com.tizi.quanzi.ui.new_group;


import android.app.Activity;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ShareAdapter;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetShareIntent;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGroupStep2Fragment extends Fragment {

    private NewGroupStep1Fragment.NewGroupStep1Ans ans;
    private Activity mActivity;

    private RecyclerView mShareItemsRecyclerView;
    private ShareAdapter shareAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public NewGroupStep2Fragment() {
        // Required empty public constructor
    }

    public void setAns(NewGroupStep1Fragment.NewGroupStep1Ans ans) {
        this.ans = ans;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_group_step2, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((NetworkImageView) mActivity.findViewById(R.id.group_face_image_view)).setImageUrl(
                ans.groupFaceUri, GetVolley.getmInstance().getImageLoader());
        ((TextView) mActivity.findViewById(R.id.group_name_text_view)).setText(ans.groupName);
        List<LabeledIntent> intents = GetShareIntent.onShareClick(mActivity);

        mShareItemsRecyclerView = (RecyclerView) mActivity.findViewById(R.id.shareRecyclerView);
        shareAdapter = new ShareAdapter(mActivity, intents);
        mShareItemsRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(mActivity, 5);
        mShareItemsRecyclerView.setLayoutManager(mLayoutManager);
        mShareItemsRecyclerView.setAdapter(shareAdapter);
    }
}
