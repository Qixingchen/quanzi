package com.tizi.quanzi.ui.main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.GroupIDs;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.GetShareIntent;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.dyns.DynsActivity;
import com.tizi.quanzi.ui.theme.ThemeSignUpFragment;

/**
 * 倒计时界面
 */
public class CountdownFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String startTime;
    private String themeID;
    private Button shareapp;
    private Button hotdyns;
    private View countDown;
    private Button signUpButton;


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
        this.hotdyns = (Button) view.findViewById(R.id.hot_dyns);
        this.shareapp = (Button) view.findViewById(R.id.share_app);
        countDown = view.findViewById(R.id.no_boom_group);
        signUpButton = (Button) view.findViewById(R.id.sign_up_button);
        countDown.setVisibility(View.GONE);
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
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(AppStaticValue.getActivity(MainActivity.class.getSimpleName()));
                    return;
                }
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment, ThemeSignUpFragment.newInstance(themeID))
                        .addToBackStack("ThemeSignUpFragment").commit();
            }
        });
        ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                GroupIDs ids = (GroupIDs) ts;
                if (ids.grpids.size() == 0) {
                    countDown.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String Message) {

            }
        }).getMySignedGroups(themeID);
    }


}
