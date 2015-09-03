package com.tizi.quanzi.ui.main;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.PrivateMessageAdapter;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * 私信／系统通知
 */
public class PrivateMessage extends BaseFragment {

    private RecyclerView mPrivateMessRecyclerView;
    private PrivateMessageAdapter privateMessageAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public PrivateMessage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_private_message, container, false);
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void initViewsAndSetEvent() {
        mPrivateMessRecyclerView = (RecyclerView) mActivity.findViewById(R.id.group_item_recycler_view);

        privateMessageAdapter = new PrivateMessageAdapter(groupClasses, mActivity, onclick);
        mPrivateMessRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(mActivity, 2);
        mPrivateMessRecyclerView.setLayoutManager(mLayoutManager);
        mPrivateMessRecyclerView.setAdapter(privateMessageAdapter);
    }


}
