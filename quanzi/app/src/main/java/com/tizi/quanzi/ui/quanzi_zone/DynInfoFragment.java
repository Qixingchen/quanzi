package com.tizi.quanzi.ui.quanzi_zone;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.Intent.StartGalleryActivity;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.DynCommentAdapter;
import com.tizi.quanzi.gson.Comments;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.network.GetVolley;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.ui.BaseFragment;

import java.util.ArrayList;

/**
 * 动态详情界面
 */
public class DynInfoFragment extends BaseFragment {

    private NetworkImageView weibo_avatar_NetworkImageView;
    private TextView userNameTextView, contentTextView, dateTextView,
            attitudesTextView, commentsTextView;
    private NetworkImageView[] weibo_pics_NetworkImageView = new NetworkImageView[9];
    private LinearLayout weibo_pics_linearLayout;

    private DynCommentAdapter dynCommentAdapter;
    private RecyclerView commentRecyclerView;

    private Dyns.DynsEntity dyns;

    public DynInfoFragment() {
        // Required empty public constructor
    }

    public void setDyn(Dyns.DynsEntity dyn) {
        this.dyns = dyn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dyn_info, container, false);
    }

    @Override
    protected void findViews(View view) {
        weibo_avatar_NetworkImageView = (NetworkImageView) view.findViewById(R.id.weibo_avatar);
        userNameTextView = (TextView) view.findViewById(R.id.weibo_name);
        contentTextView = (TextView) view.findViewById(R.id.weibo_content);
        dateTextView = (TextView) view.findViewById(R.id.weibo_date);
        attitudesTextView = (TextView) view.findViewById(R.id.weibo_attitudes);
        commentsTextView = (TextView) view.findViewById(R.id.weibo_comments);
        weibo_pics_NetworkImageView[0] = (NetworkImageView) view.findViewById(R.id.weibo_pic0);
        weibo_pics_NetworkImageView[1] = (NetworkImageView) view.findViewById(R.id.weibo_pic1);
        weibo_pics_NetworkImageView[2] = (NetworkImageView) view.findViewById(R.id.weibo_pic2);
        weibo_pics_NetworkImageView[3] = (NetworkImageView) view.findViewById(R.id.weibo_pic3);
        weibo_pics_NetworkImageView[4] = (NetworkImageView) view.findViewById(R.id.weibo_pic4);
        weibo_pics_NetworkImageView[5] = (NetworkImageView) view.findViewById(R.id.weibo_pic5);
        weibo_pics_NetworkImageView[6] = (NetworkImageView) view.findViewById(R.id.weibo_pic6);
        weibo_pics_NetworkImageView[7] = (NetworkImageView) view.findViewById(R.id.weibo_pic7);
        weibo_pics_NetworkImageView[8] = (NetworkImageView) view.findViewById(R.id.weibo_pic8);
        weibo_pics_linearLayout = (LinearLayout) view.findViewById(R.id.weibo_pics);
        commentRecyclerView = (RecyclerView) view.findViewById(R.id.dyn_comment_item_recycler_view);
    }

    @Override
    protected void initViewsAndSetEvent() {
        weibo_avatar_NetworkImageView.setImageUrl(dyns.icon,
                GetVolley.getmInstance().getImageLoader());
        userNameTextView.setText(dyns.nickName);
        contentTextView.setText(dyns.content);
        dateTextView.setText(dyns.createTime);
        attitudesTextView.setText(String.valueOf(dyns.zan));
        commentsTextView.setText(String.valueOf(dyns.commentNum));
        int picsNum = dyns.pics.size();
        if (picsNum > 9) {
            picsNum = 9;
        }
        for (int i = 0; i < picsNum; i++) {
            weibo_pics_NetworkImageView[i].setImageUrl(dyns.pics.get(i).url,
                    GetVolley.getmInstance().getImageLoader());
            final int finalI = i;
            weibo_pics_NetworkImageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartGalleryActivity.startByStringList(getPicsInfo(), finalI, mContext);

                }
            });
        }
        setPicVisbility(picsNum);
        getComment();
    }

    /**
     * 获取图片uri List
     *
     * @return pic uri List
     */
    private ArrayList<String> getPicsInfo() {
        ArrayList<String> pics = new ArrayList<>();
        for (Dyns.DynsEntity.PicsEntity picsEntity : dyns.pics) {
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
            weibo_pics_NetworkImageView[i].setVisibility(View.VISIBLE);
        }
        for (int i = picsNum; i < 9; i++) {
            weibo_pics_NetworkImageView[i].setVisibility(View.GONE);
            weibo_pics_NetworkImageView[i].setOnClickListener(null);
        }
    }

    private void getComment() {
        dynCommentAdapter = new DynCommentAdapter(mActivity);
        commentRecyclerView.setAdapter(dynCommentAdapter);
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        com.tizi.quanzi.network.Dyns.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Comments comments = (Comments) ts;
                dynCommentAdapter.setCommentses(comments.comments);
            }

            @Override
            public void onError(String Message) {
                Toast.makeText(mActivity, Message, Toast.LENGTH_LONG).show();
            }
        }).getComment(dyns.dynid, 0, 100);
    }
}
