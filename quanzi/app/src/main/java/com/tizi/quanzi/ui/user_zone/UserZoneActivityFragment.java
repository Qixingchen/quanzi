package com.tizi.quanzi.ui.user_zone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.NewAVIMConversation;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.ChatActivity;

import java.util.ArrayList;
import java.util.List;

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
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> members = new ArrayList<>();
                members.add(AppStaticValue.getUserID());
                members.add(otherUserInfo.id);

                AVIMConversationQuery query = AppStaticValue.getImClient().getQuery();
                query.whereEqualTo("attr.type", StaticField.ChatBothUserType.twoPerson);
                query.withMembers(members);
                query.whereSizeEqual("m", 2);
                query.findInBackground(new AVIMConversationQueryCallback() {
                    @Override
                    public void done(List<AVIMConversation> list, AVException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(), "出现错误：" + e.getMessage(), Toast.LENGTH_LONG).show();
                            return;
                        }

                        switch (list.size()) {
                            case 0:
                                NewAVIMConversation.getInstance().setConversationCallBack(
                                        new NewAVIMConversation.ConversationCallBack() {
                                            @Override
                                            public void setConversationID(String conversationID) {
                                                startChat(conversationID);
                                            }
                                        }).newAPrivateChat(otherUserInfo.id);
                                break;
                            case 1:
                                startChat(list.get(0).getConversationId());
                                break;
                            default:
                                startChat(list.get(0).getConversationId());
                                StringBuilder convIDs = new StringBuilder();
                                for (AVIMConversation conversation : list) {
                                    convIDs.append(conversation.getConversationId()).append("\n");
                                }
                                Log.e(TAG, "发现多个私聊ConvID：" + convIDs);
                        }
                    }
                });
            }
        });
    }

    public void setOtherUserInfo(OtherUserInfo otherUserInfo) {
        this.otherUserInfo = otherUserInfo;
    }

    private void startChat(String convID) {
        if (PrivateMessPairList.getInstance().getGroup(otherUserInfo.id) == null) {
            PrivateMessPairList.getInstance().addGroup(PrivateMessPair.newPrivatePair(otherUserInfo, convID));
        }
        Intent chat = new Intent(getActivity(), ChatActivity.class);
        chat.putExtra("conversation", convID);
        chat.putExtra("chatType", StaticField.ChatBothUserType.twoPerson);
        startActivity(chat);
    }
}
