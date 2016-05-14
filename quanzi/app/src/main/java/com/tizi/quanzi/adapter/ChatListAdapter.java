package com.tizi.quanzi.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.dataStatic.ConvGroupAbsList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.databinding.ItemChatListBinding;
import com.tizi.quanzi.otto.BusProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by qixingchen on 15/12/21.
 */
public class ChatListAdapter extends RecyclerViewAdapterAbs {

    private SortedList<ConvGroupAbs> convGroupAbsSortedList = new SortedList<>(ConvGroupAbs.class, new SortedList.Callback<ConvGroupAbs>() {
        @Override
        public int compare(ConvGroupAbs o1, ConvGroupAbs o2) {
            return (int) (o2.getLastMessTime() / 1000L - o1.getLastMessTime() / 1000L);
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
            if (item1.getID() == null) {
                return false;
            }
            return item1.getID().equals(item2.getID());
        }
    });
    private OnClick onClick;


    public ChatListAdapter() throws NullPointerException {
        try {
            BusProvider.getInstance().register(this);
        } catch (Exception ignored) {
        }
        convGroupAbsSortedList.clear();
        convGroupAbsSortedList.beginBatchedUpdates();
        try {
            List<ConvGroupAbs> groups = DBAct.getInstance().quaryAllChatGroup();
            convGroupAbsSortedList.addAll(groups);
            convGroupAbsSortedList.endBatchedUpdates();
        } catch (NullPointerException ex) {
            throw ex;
        }
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
        vh.bind(group);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick != null) {
                    onClick.onClick(convGroupAbsSortedList.get(position));
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

    /**
     * 判断是不是自己的群
     *
     * @param GroupID 群号
     *
     * @return true：是自己的圈子
     */
    private boolean isMyGroup(String GroupID) {
        if (GroupList.getInstance().getGroup(GroupID) != null) {
            return true;
        } else {
            return false;
        }
    }

    public interface OnClick {
        void onClick(ConvGroupAbs group);
    }

    class ChatListViewHolder extends RecyclerView.ViewHolder {

        private ItemChatListBinding binding;

        public ChatListViewHolder(View itemView) {
            super(itemView);
            binding = ItemChatListBinding.bind(itemView);
        }

        private void bind(@NotNull ConvGroupAbs chat) {
            binding.setChat(chat);
        }
    }
}
