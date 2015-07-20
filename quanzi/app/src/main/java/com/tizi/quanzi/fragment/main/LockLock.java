package com.tizi.quanzi.fragment.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LockLock extends Fragment {


    public LockLock() {
        // Required empty public constructor
    }

    public static LockLock newInstance() {
        LockLock fragment = new LockLock();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lock_lock, container, false);
    }


}
