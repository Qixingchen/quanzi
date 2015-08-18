package com.tizi.quanzi.fragment.main;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tizi.quanzi.R;
import com.tizi.quanzi.network.QuaryDynamic;

/**
 * A simple {@link Fragment} subclass.
 */
public class LockLock extends Fragment {

    private Context mContext;


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
        View view = inflater.inflate(R.layout.fragment_lock_lock, container, false);
        Button getDynBytton = (Button) view.findViewById(R.id.getDyn);
        getDynBytton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuaryDynamic.getInstance().getDynamic();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
