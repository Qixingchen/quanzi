package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.user_zone.UserZoneActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/9/25.
 */
public class DynCommentAdapter extends RecyclerView.Adapter<DynCommentAdapter.CommentViewHolder> {

    private List<Comments.CommentsEntity> commentses;
    private Activity activity;
    private onCommentClick onCommentClick;

    public DynCommentAdapter(Activity activity) {
        this.activity = activity;
    }

    public DynCommentAdapter setOnCommentClick(DynCommentAdapter.onCommentClick onCommentClick) {
        this.onCommentClick = onCommentClick;
        return this;
    }

    public void addComment(Comments.CommentsEntity commentsEntity, int postion) {
        if (commentses == null) {
            commentses = new ArrayList<>();
        }
        commentses.add(postion, commentsEntity);
        notifyItemInserted(postion);
    }

    public void setCommentses(List<Comments.CommentsEntity> commentses) {
        this.commentses = commentses;
        notifyDataSetChanged();
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
            SpannableString atUser = makeLinkSpan("@" + comment.atUserName, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {
                            Intent userZone = new Intent(activity, UserZoneActivity.class);
                            userZone.putExtra(StaticField.IntentName.OtherUserInfo, (OtherUserInfo) ts);
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
            makeLinksFocusable(holder.comment);
        } else {
            holder.comment.setText(comment.content);
        }
        holder.userName.setText(comment.createUserName);
        holder.createTime.setText(comment.createTime);
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

    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    private void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    @Override
    public int getItemCount() {
        return commentses == null ? 0 : commentses.size();
    }

    public interface onCommentClick {
        void Onclick(Comments.CommentsEntity comment, int postio);
    }

    /* ClickableString class*/
    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;

        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
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
