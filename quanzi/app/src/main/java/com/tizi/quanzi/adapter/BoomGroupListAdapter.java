package com.tizi.quanzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.gson.GroupIDs;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.dyns.DynsActivity;
import com.tizi.quanzi.ui.main.MainActivity;
import com.tizi.quanzi.ui.theme.ThemeSignUpFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qixingchen on 15/10/9.
 * 碰撞圈子列表 适配器
 */
public class BoomGroupListAdapter extends RecyclerViewAdapterAbs {

    private final int BOOM_GROUP = 1;
    private final int NO_BOOM_GROUP = 2;
    private final int NO_SIGN_UP = 3;
    private List<BoomGroupClass> boomGroups;
    private Context context;
    private String themeID;
    private OnClick onClick;
    private Boolean signed;

    public BoomGroupListAdapter(Context context, String themeID) {
        this.boomGroups = BoomGroupList.getInstance().getGroupList();
        this.themeID = themeID;
        this.context = context;
        BusProvider.getInstance().register(this);
    }

    @Override
    public int getItemViewType(int position) {
        if (boomGroups == null || boomGroups.size() == 0) {
            if (signed == null || signed) {
                return NO_BOOM_GROUP;
            } else {
                return NO_SIGN_UP;
            }
        } else {
            return BOOM_GROUP;
        }
    }

    @Subscribe
    public void onChanged(BoomGroupList ignore) {
        notifyDataSetChanged();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        BusProvider.getInstance().unregister(this);
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    /**
     * 创建 ViewHolder
     *
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 记录类型
     *
     * @return ViewHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BOOM_GROUP:
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_boom_group, parent, false);
                return new BoomGroupItemViewHolder(v);
            case NO_SIGN_UP:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_not_sign_up, parent, false);
                return new NoSignUpItemViewHolder(v);
            case NO_BOOM_GROUP:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_no_boom_group, parent, false);
                return new NoBoomGroupItemViewHolder(v);
            default:
                return null;
        }


    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (BoomGroupItemViewHolder.class.isInstance(holder)) {
            BoomGroupItemViewHolder vh = (BoomGroupItemViewHolder) holder;
            final BoomGroupClass boomGroup = boomGroups.get(position);
            /*1*/
            vh.firstGroupName.setText(boomGroup.groupName1);
            Picasso.with(context).load(boomGroup.icon1)
                    .resizeDimen(R.dimen.group_face, R.dimen.group_face).into(vh.firstGroupFace);

            /*2*/
            vh.secondGroupName.setText(boomGroup.groupName2);
            Picasso.with(context).load(boomGroup.icon2)
                    .resizeDimen(R.dimen.group_face, R.dimen.group_face).into(vh.secondGroupFace);

            /*both*/
            if (boomGroup.isGroup1MyGroup) {
                vh.firstGroupDescription.setText(R.string.my_group);
                vh.secondGroupDescription.setText(R.string.boom_group);
            } else {
                vh.firstGroupDescription.setText(R.string.boom_group);
                vh.secondGroupDescription.setText(R.string.my_group);
            }

            /*unread*/
            Integer unreadCount = BoomGroupList.getInstance().getUnreadCount(boomGroup.convId, boomGroup.ID);
            vh.unreadCount.setText(String.valueOf(unreadCount));
            if (unreadCount == 0) {
                vh.unreadCount.setVisibility(View.INVISIBLE);
            } else {
                vh.unreadCount.setVisibility(View.VISIBLE);
            }

            /*onCLick*/
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClick == null) {
                        Log.w(TAG, "onclick is null");
                        return;
                    }
                    onClick.clickBoomGroup(boomGroup);
                }
            });
        }
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        if (boomGroups == null || boomGroups.size() == 0) {
            if (signed == null) {
                ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        GroupIDs groupIDs = (GroupIDs) ts;
                        if (groupIDs.grpids.size() == 0) {
                            signed = false;
                            notifyDataSetChanged();
                        } else {
                            signed = true;
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String Message) {

                    }
                }).getMySignedGroups(themeID);
            }
            return 1;
        }
        return boomGroups.size();
    }


    public interface OnClick {
        void clickBoomGroup(BoomGroupClass boomGroup);
    }

    class BoomGroupItemViewHolder extends RecyclerView.ViewHolder {
        private de.hdodenhof.circleimageview.CircleImageView firstGroupFace;
        private android.widget.TextView firstGroupName;
        private android.widget.TextView firstGroupDescription;
        private android.widget.TextView unreadCount;
        private de.hdodenhof.circleimageview.CircleImageView secondGroupFace;
        private android.widget.TextView secondGroupName;
        private android.widget.TextView secondGroupDescription;

        public BoomGroupItemViewHolder(View v) {
            super(v);
            this.secondGroupDescription = (TextView) v.findViewById(R.id.second_group_description);
            this.secondGroupName = (TextView) v.findViewById(R.id.second_group_name);
            this.secondGroupFace = (CircleImageView) v.findViewById(R.id.second_group_face);
            this.unreadCount = (TextView) v.findViewById(R.id.unread_count);
            this.firstGroupDescription = (TextView) v.findViewById(R.id.first_group_description);
            this.firstGroupName = (TextView) v.findViewById(R.id.first_group_name);
            this.firstGroupFace = (CircleImageView) v.findViewById(R.id.first_group_face);
        }
    }

    class NoBoomGroupItemViewHolder extends RecyclerView.ViewHolder {
        private Button dynsButton;

        public NoBoomGroupItemViewHolder(View v) {
            super(v);
            dynsButton = (Button) v.findViewById(R.id.dyns_button);
            dynsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent dynsIntent = new Intent(App.getApplication(), DynsActivity.class);
                    dynsIntent.putExtra("themeID", themeID);
                    dynsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getApplication().startActivity(dynsIntent);
                }
            });
        }
    }

    class NoSignUpItemViewHolder extends RecyclerView.ViewHolder {
        private Button signUpButton;

        public NoSignUpItemViewHolder(View v) {
            super(v);
            signUpButton = (Button) v.findViewById(R.id.sign_up_button);
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Tool.isGuest()) {
                        Tool.GuestAction(AppStaticValue.getActivity(MainActivity.class.getSimpleName()));
                        return;
                    }
                    ((FragmentActivity) AppStaticValue.getActivity(MainActivity.class.getSimpleName()))
                            .getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, ThemeSignUpFragment.newInstance(themeID))
                            .addToBackStack("ThemeSignUpFragment").commit();
                }
            });
        }
    }
}
