package com.tizi.quanzi.adapter;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.ui.dyns.DynsActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qixingchen on 15/9/11.
 * 热门动态
 */
public class HotDynsAdapter extends PagerAdapter {
    ArrayList<Dyns.DynsEntity> dyns;
    private int LastCount;
    private String themeID;

    public HotDynsAdapter(ArrayList<Dyns.DynsEntity> dyns, String themeID) {
        this.dyns = dyns;
        this.themeID = themeID;
    }


    public HotDynsAdapter(List<Dyns.DynsEntity> dyns, String themeID) {
        this.dyns = new ArrayList<>(dyns);
        this.themeID = themeID;
    }

    @Override
    public int getCount() {
        return dyns == null ? 0 : dyns.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_topic_hot_dyns, container, false);
        ImageView[] hotdynimage = new ImageView[3];
        hotdynimage[2] = (ImageView) v.findViewById(R.id.weibo_pic2);
        hotdynimage[1] = (ImageView) v.findViewById(R.id.weibo_pic1);
        hotdynimage[0] = (ImageView) v.findViewById(R.id.weibo_pic0);
        TextView dynText = (TextView) v.findViewById(R.id.weibo_content);
        TextView groupnametextview = (TextView) v.findViewById(R.id.weibo_name);
        CircleImageView userfaceimage = (CircleImageView) v.findViewById(R.id.weibo_avatar);
        Dyns.DynsEntity dyn = dyns.get(position);
        LinearLayout pointGroup = (LinearLayout) v.findViewById(R.id.point_group);

        TextView attitudesTextView = (TextView) v.findViewById(R.id.weibo_attitudes);
        TextView commentsTextView = (TextView) v.findViewById(R.id.weibo_comments);
        attitudesTextView.setText(String.valueOf(dyn.zan));
        commentsTextView.setText(String.valueOf(dyn.commentNum));

        for (int i = 0; i < getCount(); i++) {
            View view = new View(container.getContext());
            view.setBackgroundResource(R.drawable.point_background);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
            params.leftMargin = 10;
            if (position != i) {
                view.setEnabled(false);
            } else {
                view.setEnabled(true);
            }
            view.setLayoutParams(params);
            pointGroup.addView(view);// 向线性布局中添加点
        }


        for (int i = 0; i < Math.min(3, dyn.pics.size()); i++) {
            Picasso.with(v.getContext()).load(dyn.pics.get(i).url)
                    .resizeDimen(R.dimen.dyn_image, R.dimen.dyn_image)
                    .into(hotdynimage[i]);
            hotdynimage[i].setVisibility(View.VISIBLE);
        }
        for (int i = dyn.pics.size(); i < 3; i++) {
            hotdynimage[i].setVisibility(View.GONE);
        }


        dynText.setText(dyn.content);
        groupnametextview.setText(dyn.nickName);
        Picasso.with(v.getContext()).load(dyn.icon)
                .resizeDimen(R.dimen.dyn_user_icon, R.dimen.dyn_user_icon)
                .into(userfaceimage);

        container.addView(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dynsIntent = new Intent(App.getApplication(), DynsActivity.class);
                dynsIntent.putExtra("themeID", themeID);
                dynsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getApplication().startActivity(dynsIntent);
            }
        });

        return v;
    }
}
