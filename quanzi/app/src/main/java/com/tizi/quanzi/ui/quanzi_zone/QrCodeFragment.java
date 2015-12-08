package com.tizi.quanzi.ui.quanzi_zone;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.ShareImage;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QrCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QrCodeFragment extends BaseFragment {
    private static final String GROUP_ID = "groupId";
    private static final String GROUP_FACE = "groupFace";
    private static final String GROUP_NAME = "groupName";

    private String groupID;
    private String groupName;
    private String groupFace;
    private android.widget.ImageView groupfaceimageview;
    private android.widget.ImageView qrcodeimageview;
    private TextView groupNameTextView;


    public QrCodeFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param groupID   群ID
     * @param groupName 群名
     * @param groupFace 群头像
     *
     * @return A new instance of fragment QrCodeFragment.
     */
    public static QrCodeFragment newInstance(String groupID, String groupName, String groupFace) {
        QrCodeFragment fragment = new QrCodeFragment();
        Bundle args = new Bundle();
        args.putString(GROUP_ID, groupID);
        args.putString(GROUP_NAME, groupName);
        args.putString(GROUP_FACE, groupFace);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupID = getArguments().getString(GROUP_ID);
            groupName = getArguments().getString(GROUP_NAME);
            groupFace = getArguments().getString(GROUP_FACE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_code, container, false);
    }

    @Override
    protected void findViews(View view) {
        this.qrcodeimageview = (ImageView) view.findViewById(R.id.qr_code_image_view);
        this.groupfaceimageview = (ImageView) view.findViewById(R.id.group_face_image_view);
        groupNameTextView = (TextView) view.findViewById(R.id.group_name_text_view);
    }

    @Override
    protected void initViewsAndSetEvent() {
        String ApiUri = "https://api.qrserver.com/v1/create-qr-code/?size=200x200&ecc=Q&format=png&data=";
        final String MyUri = "http://www.tizi-tech.com/sys:joinGroup=" + groupID;
        Log.i(TAG + "QrCode", ApiUri + MyUri);
        Picasso.with(mContext).load(ApiUri + MyUri).placeholder(R.drawable.ic_photo_loading).into(qrcodeimageview);
        Picasso.with(mContext).load(groupFace).into(groupfaceimageview);
        groupNameTextView.setText(groupName);

        qrcodeimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareImage.getInstance().shareImage(mActivity, ((BitmapDrawable) qrcodeimageview
                        .getDrawable()).getBitmap(), "qrcode.jpg");
            }
        });
        view.findViewById(R.id.share_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(android.content.Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(android.content.Intent.EXTRA_TEXT, "我邀请您加入我的圈子~:  " + MyUri);
                if (Tool.isIntentSafe(mActivity, i)) {
                    startActivity(i);
                }
            }
        });
    }


}
