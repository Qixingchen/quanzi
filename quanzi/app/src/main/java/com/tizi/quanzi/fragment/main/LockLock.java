package com.tizi.quanzi.fragment.main;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.DynsAdapter;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.network.QuaryDynamic;

/**
 * A simple {@link Fragment} subclass.
 */
public class LockLock extends Fragment {

    private Activity mActivity;
    private RecyclerView mDynsItemsRecyclerView;
    private DynsAdapter dynsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static LockLock mInstance;


    public LockLock() {
        // Required empty public constructor
    }

    public static LockLock newInstance() {
        mInstance = new LockLock();
        return mInstance;
    }

    public static LockLock getInstance() {
        if (mInstance == null) {
            synchronized (LockLock.class) {
                if (mInstance == null) {
                    mInstance = new LockLock();
                }
            }
        }
        return mInstance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_lock, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();

        mDynsItemsRecyclerView = (RecyclerView) mActivity.findViewById(R.id.dyns_item_recycler_view);
        mDynsItemsRecyclerView.setHasFixedSize(true);
        dynsAdapter = new DynsAdapter(null, mActivity);
        mLayoutManager = new LinearLayoutManager(mActivity);
        mDynsItemsRecyclerView.setLayoutManager(mLayoutManager);
        mDynsItemsRecyclerView.setAdapter(dynsAdapter);

        QuaryDynamic.getInstance().setQuaryDynamicListener(new QuaryDynamic.QuaryDynamicListener() {
            @Override
            public void onOK(Dyns dyns) {
                dynsAdapter.addItems(dyns.dyns);
            }

            @Override
            public void onError() {

            }
        }).getQuanZiDynamic();
    }
}
