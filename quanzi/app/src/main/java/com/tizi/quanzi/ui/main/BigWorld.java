package com.tizi.quanzi.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;

/**
 */
public class BigWorld extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView userName, userSign;
    private NetworkImageView userFace;
    private ImageView userSex;
    private View Share, Setting, userInfoLayout;


    public BigWorld() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment BigWorld.
     */
    public static BigWorld newInstance(String param1, String param2) {
        BigWorld fragment = new BigWorld();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_big_world, container,
                false);
        return rootView;
    }


    @Override
    protected void findViews(View view) {
        userFace = (NetworkImageView) view.findViewById(R.id.userFace);
        userName = (TextView) view.findViewById(R.id.userName);
        userSex = (ImageView) view.findViewById(R.id.userSex);
        userSign = (TextView) view.findViewById(R.id.userSign);
        Share = view.findViewById(R.id.share);
        Setting = view.findViewById(R.id.setting);
        userInfoLayout = view.findViewById(R.id.userInfoLayout);
    }

    @Override
    protected void initViewsAndSetEvent() {
        Login.UserEntity userInfo = MyUserInfo.getInstance().getUserInfo();
        userFace.setImageUrl(userInfo.getIcon(),
                GetVolley.getmInstance().getImageLoader());
        userName.setText(userInfo.getUserName());

        if (userInfo.getSex() == 1) {
            Picasso.with(mActivity).load(R.drawable.man).into(userSex);
        } else {
            Picasso.with(mActivity).load(R.drawable.girl).into(userSex);
        }
        userSign.setText(userInfo.getSignature());
        userInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                } else {
                    ((MainActivity) AppStaticValue.getActivity(MainActivity.class.getSimpleName()))
                            .StartUserInfoSet();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
