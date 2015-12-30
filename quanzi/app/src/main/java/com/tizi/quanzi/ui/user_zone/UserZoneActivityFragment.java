package com.tizi.quanzi.ui.user_zone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.Intent.StartGalleryActivity;
import com.tizi.quanzi.R;
import com.tizi.quanzi.chat.StartPrivateChat;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.gson.GroupAllInfo;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.ChatActivity;
import com.tizi.quanzi.ui.dyns.DynsActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.next.tagview.TagCloudView;

/**
 * A placeholder fragment containing a simple view.
 */
public class UserZoneActivityFragment extends BaseFragment {

    private CoordinatorLayout mainContent;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView zoneBackground;
    private TextView zoneSign, userAge, userXingzuo, userLocation, userSex;
    private CircleImageView userFace;
    private Button sendMessage;
    private OtherUserInfo otherUserInfo;
    private RelativeLayout userFriendZone;
    private TagCloudView userTagView;

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
        zoneBackground = (ImageView) view.findViewById(R.id.zoneBackground);
        zoneSign = (TextView) view.findViewById(R.id.user_sign);
        sendMessage = (Button) view.findViewById(R.id.send_message);
        userFace = (CircleImageView) view.findViewById(R.id.user_face);
        userAge = (TextView) view.findViewById(R.id.user_age);
        userXingzuo = (TextView) view.findViewById(R.id.user_xingzuo);
        userLocation = (TextView) view.findViewById(R.id.user_location);
        userSex = (TextView) view.findViewById(R.id.user_sex);
        userFriendZone = (RelativeLayout) view.findViewById(R.id.user_friend_zone);
        userTagView = (TagCloudView) view.findViewById(R.id.user_tag_view);
    }

    @Override
    protected void initViewsAndSetEvent() {
        if (otherUserInfo == null) {
            return;
        }
        collapsingToolbar.setTitle(otherUserInfo.userName);
        Picasso.with(mContext).load(otherUserInfo.bg).fit().into(zoneBackground);
        zoneSign.setText(otherUserInfo.signatrue);
        if (otherUserInfo.birthday != null) {
            userAge.setText(String.valueOf(FriendTime.getAge(otherUserInfo.birthday)));
            userXingzuo.setText(FriendTime.getXingzuo(otherUserInfo.birthday));
        } else {
            userAge.setText("不知道哦");
            userXingzuo.setText("不知道哦");
        }
        if (otherUserInfo.sex == 1) {
            userSex.setText("女");
        } else {
            userSex.setText("男");
        }
        userLocation.setText(otherUserInfo.area);
        int px = GetThumbnailsUri.getPXs(mContext, 60);
        Picasso.with(mContext).load(otherUserInfo.icon).resize(px, px)
                .into(userFace);

        userFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> pic = new ArrayList<>();
                pic.add(otherUserInfo.icon);
                StartGalleryActivity.startByStringList(pic, 0, mContext);
            }
        });

        userTagView.setTags(otherUserInfo.getTags());

        boolean isFriend = false;
        //在自己圈内查找
        List<GroupClass> AllGroup = GroupList.getInstance().getGroupList();
        for (GroupClass groupClass : AllGroup) {
            if (groupClass.memlist == null) {
                continue;
            }
            for (GroupAllInfo.MemberEntity member : groupClass.memlist) {
                if (otherUserInfo.id.compareTo(member.id) == 0) {
                    isFriend = true;
                    break;
                }
            }
            if (isFriend) {
                break;
            }
        }
        //在碰撞圈查找
        List<BoomGroupClass> allBoomGroup = BoomGroupList.getInstance().getGroupList();
        for (BoomGroupClass boom : allBoomGroup) {
            if (isFriend) {
                break;
            }
            for (GroupAllInfo.MemberEntity member : boom.groupMenber1) {
                if (otherUserInfo.id.compareTo(member.id) == 0) {
                    isFriend = true;
                    break;
                }
            }
            if (isFriend) {
                break;
            }
            for (GroupAllInfo.MemberEntity member : boom.groupMenber2) {
                if (otherUserInfo.id.compareTo(member.id) == 0) {
                    isFriend = true;
                    break;
                }
            }
            if (isFriend) {
                break;
            }
        }
        if (isFriend) {
            sendMessage.setVisibility(View.VISIBLE);
        } else {
            sendMessage.setVisibility(View.GONE);
        }
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartPrivateChat.getNewInstance(new StartPrivateChat.GetConvID() {
                    @Override
                    public void onConvID(String convID) {
                        startChat(convID);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                    }
                }).getPrivateChatConvID(otherUserInfo.id);

            }
        });

        userFriendZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friend = new Intent(mContext, DynsActivity.class);
                friend.putExtra("isUser", true);
                friend.putExtra("userID", otherUserInfo.id);
                startActivity(friend);
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
        chat.putExtra("chatType", StaticField.ConvType.TWO_PERSON);
        startActivity(chat);
    }
}
