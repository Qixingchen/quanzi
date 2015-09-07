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
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.Login;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.ui.BaseFragment;

/**
 */
public class BigWorld extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView userName, userSign;
    private NetworkImageView userFace;
    private ImageView userSex;
    private View Share, Setting;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     *
     * @return A new instance of fragment BigWorld.
     */
    // TODO: Rename and change types and number of parameters
    public static BigWorld newInstance(String param1, String param2) {
        BigWorld fragment = new BigWorld();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public BigWorld() {
        // Required empty public constructor
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
    protected void findViews() {
        userFace = (NetworkImageView) mActivity.findViewById(R.id.userFace);
        userName = (TextView) mActivity.findViewById(R.id.userName);
        userSex = (ImageView) mActivity.findViewById(R.id.userSex);
        userSign = (TextView) mActivity.findViewById(R.id.userSign);
        Share = mActivity.findViewById(R.id.share);
        Setting = mActivity.findViewById(R.id.setting);
    }

    @Override
    protected void initViewsAndSetEvent() {
        Login.UserEntity userInfo = MyUserInfo.getInstance().getUserInfo();
        userFace.setImageUrl(userInfo.getIcon(),
                GetVolley.getmInstance(mActivity).getImageLoader());
        userName.setText(userInfo.getUserName());

        if (userInfo.getSex() == 1) {
            Picasso.with(mActivity).load(R.drawable.man).into(userSex);
        } else {
            Picasso.with(mActivity).load(R.drawable.girl).into(userSex);
        }
        userSign.setText(userInfo.getSignature());

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
