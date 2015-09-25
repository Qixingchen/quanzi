package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.network.Dyns;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.RetrofitNetworkAbs;

import java.util.List;

/**
 * Created by qixingchen on 15/9/25.
 */
public class DynCommentAdapter extends RecyclerView.Adapter<DynCommentAdapter.CommentViewHolder> {

    private List<Comments.CommentsEntity> commentses;
    private Activity activity;

    public DynCommentAdapter(Activity activity) {
        this.activity = activity;
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
        holder.userFace.setImageUrl(comment.userIcon, GetVolley.getmInstance().getImageLoader());
        if (comment.atUserId != null) {
            holder.comment.setText("@" + comment.atUserName + comment.content);
        } else {
            holder.comment.setText(comment.content);
        }
        holder.userName.setText(comment.createUserName);
        holder.createTime.setText(comment.createTime);
        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hit = "回复：" + comment.createUserName;

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                LayoutInflater inflater = activity.getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog_one_line,
                        (ViewGroup) activity.findViewById(R.id.dialog_one_line));
                final EditText input = (EditText) layout.findViewById(R.id.dialog_edit_text);
                final TextView title = (TextView) layout.findViewById(R.id.dialog_title);

                title.setVisibility(View.GONE);
                input.setHint(hit);
                builder.setTitle("回复消息").setView(layout).setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String commentString = input.getText().toString();
                        if (commentString.compareTo("") == 0) {
                            Toast.makeText(activity, "内容为空", Toast.LENGTH_LONG).show();
                        } else {
                            Dyns.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                                @Override
                                public void onOK(Object ts) {

                                }

                                @Override
                                public void onError(String Message) {
                                    Toast.makeText(activity, Message, Toast.LENGTH_LONG).show();
                                }
                            }).addComment(comment.dynamicId, commentString, comment.id, comment.createUser);
                        }
                    }
                }).setNegativeButton("取消", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentses == null ? 0 : commentses.size();
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
