package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.MakeSpannableString;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.user_zone.UserZoneActivity;

import java.util.List;

/**
 * Created by qixingchen on 15/9/25.
 */
public class DynCommentAdapter extends RecyclerView.Adapter<DynCommentAdapter.CommentViewHolder> {

    private SortedList<Comments.CommentsEntity> commentses = new SortedList<Comments.CommentsEntity>(Comments.CommentsEntity.class, new SortedList.Callback<Comments.CommentsEntity>() {
        @Override
        public int compare(Comments.CommentsEntity o1, Comments.CommentsEntity o2) {
            return (int) (FriendTime.getTimeFromServerString(o1.createTime) -
                    FriendTime.getTimeFromServerString(o2.createTime));
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
        public boolean areContentsTheSame(Comments.CommentsEntity oldItem, Comments.CommentsEntity newItem) {
            return oldItem.content != null && oldItem.content.equals(newItem.content);
        }

        @Override
        public boolean areItemsTheSame(Comments.CommentsEntity item1, Comments.CommentsEntity item2) {
            return item1.id.equals(item2.id);
        }
    });
    private Activity activity;
    private onCommentClick onCommentClick;

    public DynCommentAdapter(Activity activity) {
        this.activity = activity;
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

    public void setCommentses(List<Comments.CommentsEntity> commentses) {
        if (commentses == null) {
            return;
        }
        this.commentses.beginBatchedUpdates();
        this.commentses.addAll(commentses);
        this.commentses.endBatchedUpdates();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(v);
    }


    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {
        final Comments.CommentsEntity comment = commentses.get(position);
        holder.comment.setText("");
        holder.userFace.setImageUrl(comment.userIcon, GetVolley.getmInstance().getImageLoader());
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
    }

    @Override
    public int getItemCount() {
        return commentses == null ? 0 : commentses.size();
    }

    public interface onCommentClick {
        void Onclick(Comments.CommentsEntity comment, int position);
    }

    protected static class CommentViewHolder extends RecyclerView.ViewHolder {
        private NetworkImageView userFace;
        private TextView userName, comment, createTime;
        private ImageButton addComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userFace = (NetworkImageView) itemView.findViewById(R.id.user_face);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            comment = (TextView) itemView.findViewById(R.id.comment);
            createTime = (TextView) itemView.findViewById(R.id.creat_time);
            addComment = (ImageButton) itemView.findViewById(R.id.add_comment);
        }
    }
}
