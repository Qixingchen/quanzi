package com.tizi.quanzi.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.tool.GetShareIntent;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.dyns.DynsActivity;

/**
 * 倒计时界面
 */
public class CountdownFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String startTime;
    private String themeID;
    private android.widget.ImageButton shareapp;
    private android.widget.ImageButton hotdyns;


    public CountdownFragment() {
        // Required empty public constructor
    }

    public static CountdownFragment newInstance(String startTime, String themeID) {
        CountdownFragment fragment = new CountdownFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, startTime);
        args.putString(ARG_PARAM2, themeID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startTime = getArguments().getString(ARG_PARAM1);
            themeID = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_countdown, container, false);

    }

    @Override
    protected void findViews(View view) {
        this.hotdyns = (ImageButton) view.findViewById(R.id.hot_dyns);
        this.shareapp = (ImageButton) view.findViewById(R.id.share_app);
    }

    @Override
    protected void initViewsAndSetEvent() {
        hotdyns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dynsIntent = new Intent(App.getApplication(), DynsActivity.class);
                dynsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dynsIntent.putExtra("themeID", themeID);
                App.getApplication().startActivity(dynsIntent);
            }
        });
        shareapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetShareIntent.startShare(getContext());
            }
        });
    }


}
