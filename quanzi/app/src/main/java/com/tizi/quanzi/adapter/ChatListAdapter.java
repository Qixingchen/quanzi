package com.tizi.quanzi.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.dataStatic.ConvGroupAbsList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.otto.BusProvider;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.GetThumbnailsUri;
import com.tizi.quanzi.tool.StaticField;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by qixingchen on 15/12/21.
 */
public class ChatListAdapter extends RecyclerViewAdapterAbs {

    private SortedList<ConvGroupAbs> convGroupAbsSortedList = new SortedList<>(ConvGroupAbs.class, new SortedList.Callback<ConvGroupAbs>() {
        @Override
        public int compare(ConvGroupAbs o1, ConvGroupAbs o2) {
            return (int) (o2.lastMessTime / 1000L - o1.lastMessTime / 1000L);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(ConvGroupAbs oldItem, ConvGroupAbs newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areItemsTheSame(ConvGroupAbs item1, ConvGroupAbs item2) {
            if (item1.ID == null) {
                return false;
            }
            return item1.ID.equals(item2.ID);
        }
    });
    private OnClick onClick;


    public ChatListAdapter() {
        try {
            BusProvider.getInstance().register(this);
        } catch (Exception ignored) {
        }
        convGroupAbsSortedList.clear();
        convGroupAbsSortedList.beginBatchedUpdates();

        for (Object group : GroupList.getInstance().getGroupList()) {
            convGroupAbsSortedList.add((ConvGroupAbs) group);
        }

        for (Object group : PrivateMessPairList.getInstance().getGroupList()) {
            convGroupAbsSortedList.add((ConvGroupAbs) group);
        }

        for (Object group : BoomGroupList.getInstance().getGroupList()) {
            convGroupAbsSortedList.add((ConvGroupAbs) group);
        }
        convGroupAbsSortedList.endBatchedUpdates();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            BusProvider.getInstance().unregister(this);
        } catch (Exception ignored) {
        }
    }

    @Subscribe
    public void onGroupChange(ConvGroupAbsList list) {
        convGroupAbsSortedList.beginBatchedUpdates();
        convGroupAbsSortedList.addAll(list.getGroupList());
        convGroupAbsSortedList.endBatchedUpdates();
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ChatListViewHolder(v);
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ChatListViewHolder vh = (ChatListViewHolder) holder;
        final ConvGroupAbs group = convGroupAbsSortedList.get(position);

        int chatType = 0;
        if (group instanceof GroupClass) {
            vh.groupType.setImageResource(R.drawable.ic_group_black_24dp);
            chatType = StaticField.ConvType.GROUP;
        } else if (group instanceof PrivateMessPair) {
            vh.groupType.setImageResource(R.drawable.ic_person_black_24dp);
            chatType = StaticField.ConvType.twoPerson;
        } else if (group instanceof BoomGroupClass) {
            vh.groupType.setImageResource(R.drawable.ic_whatshot_black_24dp);
            chatType = StaticField.ConvType.BoomGroup;
        }
        if (chatType != StaticField.ConvType.BoomGroup) {
            Picasso.with(vh.groupFace.getContext())
                    .load(GetThumbnailsUri.getUriLink(group.Face, 64, 64, vh.groupFace.getContext()))
                    .fit().into(vh.groupFace);
        } else {
            Picasso.with(vh.groupFace.getContext())
                    .load(R.drawable.ic_whatshot_black_24dp)
                    .fit().into(vh.groupFace);
        }

        vh.groupName.setText(group.Name);
        if (group.lastMessTime == 0) {
            vh.lastChatTime.setVisibility(View.GONE);
            vh.LastChatMess.setVisibility(View.GONE);
        } else {
            vh.lastChatTime.setVisibility(View.VISIBLE);
            vh.LastChatMess.setVisibility(View.VISIBLE);
        }
        vh.lastChatTime.setText(FriendTime.FriendlyDate(group.lastMessTime));
        vh.LastChatMess.setText(group.lastMess);
        if (group.getUnreadCount() == 0) {
            vh.unreadMessCount.setVisibility(View.GONE);
        } else {
            vh.unreadMessCount.setVisibility(View.VISIBLE);
        }
        vh.unreadMessCount.setText(String.valueOf(group.getUnreadCount()));


        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean needNotifi = AppStaticValue.getNeedNotifi(group.convId);
                subscriber.onNext(needNotifi);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean needNotify) {
                        if (needNotify) {
                            vh.disallowNotify.setVisibility(View.GONE);
                        } else {
                            vh.disallowNotify.setVisibility(View.VISIBLE);
                        }
                    }
                });
        final int finalChatType = chatType;
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.onClick(convGroupAbsSortedList.get(position), finalChatType);
                }
            }
        });
    }

    /**
     * @return 记录数
     */
    @Override
    public int getItemCount() {
        return convGroupAbsSortedList.size();
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public interface OnClick {
        void onClick(ConvGroupAbs group, int chatType);
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView groupFace;
        private TextView groupName, lastChatTime, LastChatMess, unreadMessCount;
        private ImageView groupType, disallowNotify;

        public ChatListViewHolder(View itemView) {
            super(itemView);
            findViews(itemView);
        }

        private void findViews(View v) {
            groupFace = (CircleImageView) v.findViewById(R.id.group_face_image_view);
            groupName = (TextView) v.findViewById(R.id.group_name_text_view);
            lastChatTime = (TextView) v.findViewById(R.id.last_mess_time_text_view);
            LastChatMess = (TextView) v.findViewById(R.id.last_mess_text_view);
            unreadMessCount = (TextView) v.findViewById(R.id.unread_count);
            groupType = (ImageView) v.findViewById(R.id.group_type);
            disallowNotify = (ImageView) v.findViewById(R.id.disallow_notify);
        }
    }
}
