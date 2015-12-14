package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.MakeSpannableString;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.user_zone.UserZoneActivity;

import java.util.List;

/**
 * Created by qixingchen on 15/9/25.
 */
public class DynCommentAdapter extends RecyclerViewAdapterAbs {

    private static final int COMMENT_VIEW = 1;
    private static final int HEADER_VIEW = 2;
    private Activity activity;
    private onCommentClick onCommentClick;
    private boolean isUser;
    private View headView;
    private SortedList<Comments.CommentsEntity> commentses = new SortedList<>(
            Comments.CommentsEntity.class, new SortedList.Callback<Comments.CommentsEntity>() {
        @Override
        public int compare(Comments.CommentsEntity o1, Comments.CommentsEntity o2) {
            return (int) (FriendTime.getTimeFromServerString(o1.createTime) / 1000L -
                    FriendTime.getTimeFromServerString(o2.createTime) / 1000L);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position + getAddtion(), count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position + getAddtion(), count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition + getAddtion(), toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position + getAddtion(), count);
        }

        @Override
        public boolean areContentsTheSame(Comments.CommentsEntity oldItem, Comments.CommentsEntity newItem) {
            return oldItem.content != null && oldItem.content.equals(newItem.content);
        }

        @Override
        public boolean areItemsTheSame(Comments.CommentsEntity item1, Comments.CommentsEntity item2) {
            return item1.id.equals(item2.id);
        }
    });

    public DynCommentAdapter(Activity activity, boolean isUser) {
        this.activity = activity;
        this.isUser = isUser;
    }

    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public DynCommentAdapter setOnCommentClick(DynCommentAdapter.onCommentClick onCommentClick) {
        this.onCommentClick = onCommentClick;
        return this;
    }

    public void addComment(Comments.CommentsEntity commentsEntity) {
        if (commentsEntity == null) {
            return;
        }
        commentses.add(commentsEntity);
    }

    public void deleteComment(Comments.CommentsEntity commentsEntity) {
        if (commentsEntity != null) {
            commentses.remove(commentsEntity);
        }
    }

    public void setCommentses(List<Comments.CommentsEntity> commentses) {
        if (commentses == null) {
            return;
        }
        this.commentses.beginBatchedUpdates();
        this.commentses.addAll(commentses);
        this.commentses.endBatchedUpdates();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh;
        switch (viewType) {
            case COMMENT_VIEW:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_comment, parent, false);
                vh = new CommentViewHolder(v);
                break;
            case HEADER_VIEW:
                v = headView;
                vh = new HeaderViewHolder(v);
                break;
            default:
                vh = null;
        }

        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getAddtion()) {
            if (position == 0) {
                return HEADER_VIEW;
            }
        }
        return COMMENT_VIEW;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            onBindCommentViewHolder((CommentViewHolder) holder, position - getAddtion());
        }
    }


    private void onBindCommentViewHolder(CommentViewHolder holder, final int position) {
        final Comments.CommentsEntity comment = commentses.get(position);
        holder.comment.setText("");
        Picasso.with(holder.userFace.getContext()).load(comment.userIcon).fit().into(holder.userFace);
        holder.userFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        OtherUserInfo otherUserInfo = (OtherUserInfo) ts;
                        Intent otherUser = new Intent(activity, UserZoneActivity.class);
                        otherUser.putExtra(StaticField.IntentName.OtherUserInfo, (Parcelable) otherUserInfo);
                        activity.startActivity(otherUser);
                    }

                    @Override
                    public void onError(String Message) {
                        Toast.makeText(activity, "此用户已不存在", Toast.LENGTH_LONG).show();
                    }
                }).findUserByID(comment.createUser);
            }
        });
        if (comment.atUserId != null) {
            SpannableString atUser = MakeSpannableString.makeLinkSpan("@" + comment.atUserName, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {
                            Intent userZone = new Intent(activity, UserZoneActivity.class);
                            userZone.putExtra(StaticField.IntentName.OtherUserInfo, (Parcelable) ts);
                            activity.startActivity(userZone);
                        }

                        @Override
                        public void onError(String Message) {
                            Toast.makeText(activity, Message, Toast.LENGTH_LONG).show();
                        }
                    }).findUserByID(comment.atUserId);

                }
            });
            holder.comment.append(atUser);
            holder.comment.append(comment.content);
            MakeSpannableString.makeLinksFocusable(holder.comment);
        } else {
            holder.comment.setText(comment.content);
        }
        holder.userName.setText(comment.createUserName);
        holder.createTime.setText(FriendTime.FriendlyDate(FriendTime.getTimeFromServerString(comment.createTime)));
        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onCommentClick != null) {
                    onCommentClick.Onclick(comment, position);
                } else {
                    Log.w(DynCommentAdapter.class.getSimpleName(), "评论点击,但没有回调函数,位置:" + position);
                    Thread.dumpStack();
                }
            }
        });
        /*长按删除*/
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!comment.senderId.equals(AppStaticValue.getUserID())) {
                    return false;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("删除评论")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (onCommentClick != null) {
                                    onCommentClick.OnDelete(comment, position);
                                }
                            }
                        }).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentses == null ? getAddtion() : commentses.size() + getAddtion();
    }

    private int getAddtion() {
        if (headView != null) {
            return 1;
        } else {
            return 0;
        }
    }

    public interface onCommentClick {
        void Onclick(Comments.CommentsEntity comment, int position);

        void OnDelete(Comments.CommentsEntity comment, int position);
    }

    protected static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView userFace;
        private TextView userName, comment, createTime;
        private ImageButton addComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userFace = (ImageView) itemView.findViewById(R.id.user_face);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            comment = (TextView) itemView.findViewById(R.id.comment);
            createTime = (TextView) itemView.findViewById(R.id.creat_time);
            addComment = (ImageButton) itemView.findViewById(R.id.add_comment);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
