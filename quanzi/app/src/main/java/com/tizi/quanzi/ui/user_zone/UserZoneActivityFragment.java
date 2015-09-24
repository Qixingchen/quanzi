package com.tizi.quanzi.ui.user_zone;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.ui.BaseFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserZoneActivityFragment extends BaseFragment {

    private CoordinatorLayout mainContent;
    private CollapsingToolbarLayout collapsingToolbar;
    private NetworkImageView zoneBackground;
    private TextView zoneSign;
    private CircleImageView userFace;
    private Button sendMessage;
    private OtherUserInfo otherUserInfo;

    public UserZoneActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_zone, container, false);
    }

    @Override
    protected void findViews(View view) {
        mainContent = (CoordinatorLayout) view.findViewById(R.id.main_content);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        zoneBackground = (NetworkImageView) view.findViewById(R.id.zoneBackground);
        zoneSign = (TextView) view.findViewById(R.id.zoneSign);
        sendMessage = (Button) view.findViewById(R.id.send_message);
        userFace = (CircleImageView) view.findViewById(R.id.user_face);
    }

    @Override
    protected void initViewsAndSetEvent() {
        if (otherUserInfo == null) {
            return;
        }
        collapsingToolbar.setTitle(otherUserInfo.userName);
        zoneBackground.setImageUrl(otherUserInfo.bg, GetVolley.getmInstance().getImageLoader());
        zoneSign.setText(otherUserInfo.signatrue);
        int px = GetThumbnailsUri.getPXs(mContext, 60);
        Picasso.with(mContext).load(otherUserInfo.icon).resize(px, px)
                .into(userFace);

    }

    public void setOtherUserInfo(OtherUserInfo otherUserInfo) {
        this.otherUserInfo = otherUserInfo;
    }
}
