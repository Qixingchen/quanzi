package com.tizi.quanzi.ui.dyns;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.DynCommentAdapter;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.gson.AddComment;
import com.tizi.quanzi.gson.AddZan;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.gson.IsZan;
import com.tizi.quanzi.network.DynamicAct;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.widget.DynItem;
import com.tizi.quanzi.widget.SimpleDividerItemDecoration;

/**
 * 动态详情界面
 */
public class DynInfoFragment extends BaseFragment {

    private ImageView plusOne, addCommentImageView;
    private TextView attitudesTextView, commentsTextView;
    private DynCommentAdapter dynCommentAdapter;
    private RecyclerView commentRecyclerView;

    private Dyns.DynsEntity dyn;
    private boolean iszan;
    private boolean showUser;
    private boolean isUser = false;
    private OnDeleteDyn onDeleteDyn;

    public DynInfoFragment() {
        // Required empty public constructor
    }

    public void setDyn(Dyns.DynsEntity dyn) {
        this.dyn = dyn;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

    public void setShowUser(boolean showUser) {
        this.showUser = showUser;
    }

    public void setOnDeleteDyn(OnDeleteDyn onDeleteDyn) {
        this.onDeleteDyn = onDeleteDyn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dyn_info, container, false);
    }

    @Override
    protected void findViews(View view) {
        new DynItem(dyn, view, showUser, isUser, mContext);
        attitudesTextView = (TextView) view.findViewById(R.id.weibo_attitudes);
        commentsTextView = (TextView) view.findViewById(R.id.weibo_comments);
        commentRecyclerView = (RecyclerView) view.findViewById(R.id.dyn_comment_item_recycler_view);
        plusOne = (ImageView) view.findViewById(R.id.weibo_attitude_image_view);
        addCommentImageView = (ImageView) view.findViewById(R.id.comment);
    }

    @Override
    protected void initViewsAndSetEvent() {

        //        DynamicAct.getNewInstance(isUser).setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
        //            @Override
        //            public void onOK(Object ts) {
        //                IsZan isZan = (IsZan) ts;
        //                if (isZan.cnt == 0) {
        //                    iszan = false;
        //                    Picasso.with(mContext).load(R.drawable.ic_like_grey).into(plusOne);
        //                } else {
        //                    iszan = true;
        //                    Picasso.with(mContext).load(R.drawable.ic_like_red).into(plusOne);
        //                }
        //            }
        //
        //            @Override
        //            public void onError(String Message) {
        //                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
        //            }
        //        }).isZan(dyn.dynid);

        if (dyn.createUser.equals(AppStaticValue.getUserID())) {
            ((DynsActivity) (mActivity)).needDeleteIcon(new DynsActivity.OnDelete() {
                @Override
                public void onDelete() {
                    DynamicAct.getNewInstance(isUser).setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {

                            if (getFragmentManager().getFragments().size() == 1) {
                                Intent intent = new Intent();
                                intent.putExtra("dynID", dyn.dynid);
                                mActivity.setResult(Activity.RESULT_OK, intent);
                                mActivity.finish();
                            } else {
                                if (onDeleteDyn != null) {
                                    onDeleteDyn.onDelete(dyn);
                                }
                                getFragmentManager().popBackStack();
                            }
                        }

                        @Override
                        public void onError(String Message) {
                            Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                        }
                    }).deleteDyn(dyn.dynid);
                }
            });
        }
        getComment();
        addCommentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                    return;
                }
                addComment(null);
            }
        });
        /*以下给非User动态增加点击操作*/
        if (isUser) {
            return;
        }
        //判断是否已点赞
        DynamicAct.getNewInstance(isUser).setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                IsZan isZan = (IsZan) ts;
                if (isZan.cnt == 0) {
                    iszan = false;
                    Picasso.with(mContext).load(R.drawable.ic_like_grey).into(plusOne);
                } else {
                    iszan = true;
                    Picasso.with(mContext).load(R.drawable.ic_like_red).into(plusOne);
                }
            }

            @Override
            public void onError(String Message) {
                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
            }
        }).isZan(dyn.dynid);
        plusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                    return;
                }
                plusOne.setEnabled(false);
                DynamicAct.getNewInstance(isUser).setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        AddZan addZan = (AddZan) ts;
                        iszan = !iszan;
                        plusOne.setEnabled(true);
                        if (iszan) {
                            Picasso.with(mContext).load(R.drawable.ic_like_red).into(plusOne);
                        } else {
                            Picasso.with(mContext).load(R.drawable.ic_like_grey).into(plusOne);
                        }
                        attitudesTextView.setText(String.valueOf(addZan.zan));
                        dyn.zan = addZan.zan;
                    }

                    @Override
                    public void onError(String Message) {
                        Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                        plusOne.setEnabled(true);
                    }
                }).addZan(dyn.dynid, !iszan);
                if (!iszan) {
                    DynActSendNotify.getNewInstance().plusOne(dyn, isUser);
                }
            }
        });
    }

    private void getComment() {
        dynCommentAdapter = new DynCommentAdapter(mActivity, isUser);
        commentRecyclerView.setAdapter(dynCommentAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        commentRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mContext));

        dynCommentAdapter.setOnCommentClick(new DynCommentAdapter.onCommentClick() {
            @Override
            public void Onclick(Comments.CommentsEntity comment, int position) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                    return;
                }
                addComment(comment);
            }

            @Override
            public void OnDelete(final Comments.CommentsEntity comment, int position) {
                DynamicAct.getNewInstance(isUser).setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        dyn.commentNum--;
                        commentsTextView.setText(String.valueOf(dyn.commentNum));
                        dynCommentAdapter.deleteComment(comment);
                    }

                    @Override
                    public void onError(String Message) {
                        Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                    }
                }).deleteComment(comment.id);
            }
        });

        RetrofitNetworkAbs.NetworkListener listener = new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Comments comments = (Comments) ts;
                dynCommentAdapter.setCommentses(comments.comments);
                commentsTextView.setText(String.valueOf(comments.comments.size()));
            }

            @Override
            public void onError(String Message) {
                Toast.makeText(mActivity, Message, Toast.LENGTH_LONG).show();
            }
        };

        DynamicAct.getNewInstance(isUser).setNetworkListener(listener).getComment(dyn.dynid, 0, 100);

    }

    /**
     * 添加回复
     *
     * @param comment 可能为空 回复针对的评论
     */
    private void addComment(final Comments.CommentsEntity comment) {
        String hit = "回复：";
        if (comment != null) {
            hit += comment.createUserName;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_one_line,
                (ViewGroup) mActivity.findViewById(R.id.dialog_one_line));
        final EditText input = (EditText) layout.findViewById(R.id.dialog_edit_text);
        input.setHint(hit);
        builder.setTitle("回复消息").setView(layout).setPositiveButton("发送", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String commentString = input.getText().toString();

                RetrofitNetworkAbs.NetworkListener addCommentListener =
                        new RetrofitNetworkAbs.NetworkListener() {
                            @Override
                            public void onOK(Object ts) {
                                AddComment addComment = (AddComment) ts;
                                Comments.CommentsEntity newComment = new Comments.CommentsEntity();
                                newComment.id = addComment.cid;
                                newComment.content = commentString;
                                newComment.createTime = FriendTime.getServerTime();
                                newComment.createUser = AppStaticValue.getUserID();
                                newComment.createUserName = MyUserInfo.getInstance().getUserInfo().getUserName();
                                newComment.dynamicId = dyn.dynid;
                                newComment.userIcon = MyUserInfo.getInstance().getUserInfo().getIcon();
                                newComment.senderId = AppStaticValue.getUserID();
                                if (comment != null) {
                                    newComment.atUserName = comment.createUserName;
                                    newComment.atUserId = comment.createUser;
                                    newComment.replyId = comment.id;
                                }
                                dynCommentAdapter.addComment(newComment);
                                dyn.commentNum++;
                                commentsTextView.setText(String.valueOf(dyn.commentNum));
                            }

                            @Override
                            public void onError(String Message) {
                                Toast.makeText(mActivity, Message, Toast.LENGTH_LONG).show();
                            }
                        };

                if (commentString.compareTo("") == 0) {
                    Toast.makeText(mActivity, "内容为空", Toast.LENGTH_LONG).show();
                } else {
                    if (comment == null) {
                        DynamicAct.getNewInstance(isUser).setNetworkListener(addCommentListener)
                                .addComment(dyn.dynid, commentString);
                    } else {
                        DynamicAct.getNewInstance(isUser).setNetworkListener(addCommentListener)
                                .addComment(dyn.dynid, commentString, comment.id, comment.createUser);
                        DynActSendNotify.getNewInstance().atUser(commentString, comment.createUser, comment, dyn, isUser);
                    }
                    DynActSendNotify.getNewInstance().replayComment(commentString, comment, dyn, isUser);
                }
            }
        }).setNegativeButton("取消", null).show();
    }

    public interface OnDeleteDyn {
        void onDelete(Dyns.DynsEntity dyn);
    }
}
