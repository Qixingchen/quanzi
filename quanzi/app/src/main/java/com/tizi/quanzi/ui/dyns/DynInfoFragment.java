package com.tizi.quanzi.ui.dyns;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.Intent.StartGalleryActivity;
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
import com.tizi.quanzi.tool.Tool;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.widget.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 动态详情界面
 */
public class DynInfoFragment extends BaseFragment {

    private ImageView weibo_avatar_ImageView;
    private TextView userNameTextView, contentTextView, dateTextView,
            attitudesTextView, commentsTextView;
    private ImageView[] weibo_pics_ImageView = new ImageView[3];
    private LinearLayout weibo_pics_linearLayout;

    private ImageView plusOne, addCommentImageView;

    private DynCommentAdapter dynCommentAdapter;
    private RecyclerView commentRecyclerView;

    private Dyns.DynsEntity dyn;
    private boolean iszan;

    public DynInfoFragment() {
        // Required empty public constructor
    }

    public void setDyn(Dyns.DynsEntity dyn) {
        this.dyn = dyn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dyn_info, container, false);
    }

    @Override
    protected void findViews(View view) {
        weibo_avatar_ImageView = (ImageView) view.findViewById(R.id.weibo_avatar);
        userNameTextView = (TextView) view.findViewById(R.id.weibo_name);
        contentTextView = (TextView) view.findViewById(R.id.weibo_content);
        dateTextView = (TextView) view.findViewById(R.id.weibo_date);
        attitudesTextView = (TextView) view.findViewById(R.id.weibo_attitudes);
        commentsTextView = (TextView) view.findViewById(R.id.weibo_comments);
        weibo_pics_ImageView[0] = (ImageView) view.findViewById(R.id.weibo_pic0);
        weibo_pics_ImageView[1] = (ImageView) view.findViewById(R.id.weibo_pic1);
        weibo_pics_ImageView[2] = (ImageView) view.findViewById(R.id.weibo_pic2);
        //        weibo_pics_NetworkImageView[3] = (NetworkImageView) view.findViewById(R.id.weibo_pic3);
        //        weibo_pics_NetworkImageView[4] = (NetworkImageView) view.findViewById(R.id.weibo_pic4);
        //        weibo_pics_NetworkImageView[5] = (NetworkImageView) view.findViewById(R.id.weibo_pic5);
        //        weibo_pics_NetworkImageView[6] = (NetworkImageView) view.findViewById(R.id.weibo_pic6);
        //        weibo_pics_NetworkImageView[7] = (NetworkImageView) view.findViewById(R.id.weibo_pic7);
        //        weibo_pics_NetworkImageView[8] = (NetworkImageView) view.findViewById(R.id.weibo_pic8);
        weibo_pics_linearLayout = (LinearLayout) view.findViewById(R.id.weibo_pics);
        commentRecyclerView = (RecyclerView) view.findViewById(R.id.dyn_comment_item_recycler_view);
        plusOne = (ImageView) view.findViewById(R.id.plus_one);
        addCommentImageView = (ImageView) view.findViewById(R.id.comment);
    }

    @Override
    protected void initViewsAndSetEvent() {
        Picasso.with(mContext).load(dyn.icon)
                .resizeDimen(R.dimen.dyn_user_icon, R.dimen.dyn_user_icon)
                .into(weibo_avatar_ImageView);

        userNameTextView.setText(dyn.nickName);
        contentTextView.setText(dyn.content);
        dateTextView.setText(dyn.createTime);
        attitudesTextView.setText(String.valueOf(dyn.zan));
        commentsTextView.setText(String.valueOf(dyn.commentNum));
        DynamicAct.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                IsZan isZan = (IsZan) ts;
                if (isZan.cnt == 0) {
                    iszan = false;
                    plusOne.setBackground(getResources().getDrawable(R.drawable.ic_plus_one_48dp));
                } else {
                    iszan = true;
                    plusOne.setBackground(getResources().getDrawable(R.drawable.ic_plus_one_red_48dp));
                }
            }

            @Override
            public void onError(String Message) {
                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
            }
        }).isZan(dyn.dynid);
        int picsNum = dyn.pics.size();
        if (picsNum > 3) {
            picsNum = 3;
        }
        for (int i = 0; i < picsNum; i++) {
            String thumUri = dyn.pics.get(i).url;
            Picasso.with(mContext).load(thumUri)
                    .resizeDimen(R.dimen.weibo_pic_hei, R.dimen.weibo_pic_wei)
                    .into(weibo_pics_ImageView[i]);
            final int finalI = i;
            weibo_pics_ImageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartGalleryActivity.startByStringList(getPicsInfo(), finalI, mContext);
                }
            });
        }
        setPicVisbility(picsNum);
        getComment();
        plusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                    return;
                }
                plusOne.setEnabled(false);
                DynamicAct.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                    @Override
                    public void onOK(Object ts) {
                        AddZan addZan = (AddZan) ts;
                        iszan = !iszan;
                        plusOne.setEnabled(true);
                        if (iszan) {
                            plusOne.setBackground(getResources().getDrawable(R.drawable.ic_plus_one_red_48dp));
                        } else {
                            plusOne.setBackground(getResources().getDrawable(R.drawable.ic_plus_one_48dp));
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
                    DynActSendNotify.getNewInstance().plusOne(dyn);
                }
            }
        });
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
    }

    /**
     * 获取图片uri List
     *
     * @return pic uri List
     */
    private ArrayList<String> getPicsInfo() {
        ArrayList<String> pics = new ArrayList<>();
        for (Dyns.DynsEntity.PicsEntity picsEntity : dyn.pics) {
            pics.add(picsEntity.url);
        }
        return pics;
    }

    /**
     * 将需要的图片设置为可见
     * 将多余的图片设置成不可见
     * 如果没有图片，则将 weibo_pics_linearLayout 也设置为不可见
     *
     * @param picsNum 图片数量
     */
    private void setPicVisbility(int picsNum) {
        if (picsNum == 0) {
            weibo_pics_linearLayout.setVisibility(View.GONE);
            return;
        }
        for (int i = 0; i < picsNum; i++) {
            weibo_pics_ImageView[i].setVisibility(View.VISIBLE);
        }
        for (int i = picsNum; i < 3; i++) {
            weibo_pics_ImageView[i].setVisibility(View.GONE);
            weibo_pics_ImageView[i].setOnClickListener(null);
        }
    }

    private void getComment() {
        dynCommentAdapter = new DynCommentAdapter(mActivity);
        commentRecyclerView.setAdapter(dynCommentAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        commentRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mContext));
        //commentRecyclerView.addOnScrollListener(new HideExtraOnScroll(mActivity.findViewById(R.id.need_scroll_out)));

        dynCommentAdapter.setOnCommentClick(new DynCommentAdapter.onCommentClick() {
            @Override
            public void Onclick(Comments.CommentsEntity comment, int position) {
                if (Tool.isGuest()) {
                    Tool.GuestAction(mActivity);
                    return;
                }
                addComment(comment);
            }
        });

        DynamicAct.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Comments comments = (Comments) ts;
                dynCommentAdapter.setCommentses(comments.comments);
            }

            @Override
            public void onError(String Message) {
                Toast.makeText(mActivity, Message, Toast.LENGTH_LONG).show();
            }
        }).getComment(dyn.dynid, 0, 100);
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
        final TextView title = (TextView) layout.findViewById(R.id.dialog_title);

        title.setVisibility(View.GONE);
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
                                newComment.createTime = String.valueOf(Calendar.getInstance().getTimeInMillis());
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
                                dynCommentAdapter.addComment(newComment, 0);
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
                        DynamicAct.getNewInstance().setNetworkListener(addCommentListener)
                                .addComment(dyn.dynid, commentString);
                    } else {
                        DynamicAct.getNewInstance().setNetworkListener(addCommentListener)
                                .addComment(dyn.dynid, commentString, comment.id, comment.createUser);
                        DynActSendNotify.getNewInstance().atUser(commentString, comment.createUser, comment, dyn);
                    }
                    DynActSendNotify.getNewInstance().replayComment(commentString, comment, dyn);
                }
            }
        }).setNegativeButton("取消", null).show();
    }
}
