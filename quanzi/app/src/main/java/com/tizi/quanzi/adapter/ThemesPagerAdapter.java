package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.gson.HotDyns;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.ThemeActs;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.ui.dyns.DynsActivity;
import com.tizi.quanzi.widget.custom_tab.SimpleCustomChromeTabsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/11/18.
 */
public class ThemesPagerAdapter extends PagerAdapter {

    private final static String TAG = ThemesPagerAdapter.class.getSimpleName();

    private Theme theme;
    private Activity mActivity;
    private OnClick onClick;
    private Timer timer;

    public ThemesPagerAdapter(Theme theme, Activity mActivity, OnClick onClick) {
        this.theme = theme;
        this.mActivity = mActivity;
        this.onClick = onClick;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (timer != null) {
            timer.cancel(false);
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return theme.acts.size() + 1;
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
    public Object instantiateItem(final ViewGroup container, int position) {
        if (position == theme.acts.size()) {
            final View vRoot = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.item_hope_for_next, container, false);
            container.addView(vRoot);
            return vRoot;
        }
        View rootView = tuoDanDaZuoZan(container, theme.acts.get(position));

        container.addView(rootView);
        return rootView;
    }

    private View tuoDanDaZuoZan(final ViewGroup container, final Theme.ActsEntity act) {
        final View rootView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_tuo_dan_zuo_zan, container, false);

        /*动态*/
        final ImageView weibo_avatar_ImageView;
        final TextView userNameTextView, contentTextView, dateTextView,
                attitudesTextView, commentsTextView, themeTitleTextView;

        themeTitleTextView = (TextView) rootView.findViewById(R.id.theme_title);
        weibo_avatar_ImageView = (ImageView) rootView.findViewById(R.id.weibo_avatar);
        userNameTextView = (TextView) rootView.findViewById(R.id.weibo_name);
        contentTextView = (TextView) rootView.findViewById(R.id.weibo_content);
        dateTextView = (TextView) rootView.findViewById(R.id.weibo_date);
        attitudesTextView = (TextView) rootView.findViewById(R.id.weibo_attitudes);
        commentsTextView = (TextView) rootView.findViewById(R.id.weibo_comments);

        final ViewPager pics = (ViewPager) rootView.findViewById(R.id.picsViewPager);

        pics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dynsIntent = new Intent(App.getApplication(), DynsActivity.class);
                dynsIntent.putExtra("themeID", act.id);
                dynsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getApplication().startActivity(dynsIntent);
            }
        });
        rootView.findViewById(R.id.hot_dyns).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dynsIntent = new Intent(App.getApplication(), DynsActivity.class);
                dynsIntent.putExtra("themeID", act.id);
                dynsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getApplication().startActivity(dynsIntent);
            }
        });


        ThemeActs.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Dyns.DynsEntity dyn = ((HotDyns) ts).dyns.get(0);
                List<String> picString = new ArrayList<>();
                for (Dyns.DynsEntity.PicsEntity pic : dyn.pics) {
                    picString.add(pic.url);
                }
                pics.setAdapter(new GalleryAdapter(picString, mActivity));
                Picasso.with(mActivity).load(dyn.groupIcon)
                        .resizeDimen(R.dimen.dyn_user_icon, R.dimen.dyn_user_icon)
                        .into(weibo_avatar_ImageView);
                userNameTextView.setText(dyn.groupName);
                contentTextView.setText(dyn.content);
                dateTextView.setText(FriendTime.FriendlyDate(
                        FriendTime.getTimeFromServerString(dyn.createTime)));
                attitudesTextView.setText(String.valueOf(dyn.zan));
                commentsTextView.setText(String.valueOf(dyn.commentNum));
            }

            @Override
            public void onError(String Message) {

            }
        })
                .getHotDyns(act.id);

        themeTitleTextView.setText(act.title);

         /*报名等事件*/
        final Button participateButton, detailButton, boomButton;
        final TextView participantsNum;
        participateButton = (Button) rootView.findViewById(R.id.participate_button);
        detailButton = (Button) rootView.findViewById(R.id.detail_button);
        participantsNum = (TextView) rootView.findViewById(R.id.num_of_participants);
        boomButton = (Button) rootView.findViewById(R.id.boom_button);

        participantsNum.setText(String.format("已报名:%d", act.signNum));
        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick == null) {
                    Log.w(TAG, "报名但没有回调监听:");
                    return;
                }
                onClick.SignUP(act);
            }
        });
        boomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick == null) {
                    Log.w(TAG, "没有回调监听:");
                    return;
                }
                onClick.EnterTheme(act);
            }
        });

        /*详情界面*/
        final SimpleCustomChromeTabsHelper mCustomTabHelper = new SimpleCustomChromeTabsHelper(mActivity);
        mCustomTabHelper.prepareUrl(act.detailUrl);
        SimpleCustomChromeTabsHelper.CustomTabsUiBuilder uiBuilder = mCustomTabHelper.new CustomTabsUiBuilder();
        uiBuilder.setToolbarColor(mActivity.getResources().getColor(R.color.colorPrimary));

        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SimpleCustomChromeTabsHelper.canUseCustomChromeTabs(mActivity)) {
                    mCustomTabHelper.openUrl(act.detailUrl);
                } else {
                    mCustomTabHelper.openUrl(act.detailUrl);
                    // TODO: 15/11/25 webview 不能加载
                    //                    if (onClick == null) {
                    //                        Log.w(TAG, "没有回调监听:");
                    //                        return;
                    //                    }
                    //                    onClick.watchIntro(act);
                }
            }
        });

        /*倒计时*/
        final TextView countDownTextView = (TextView) rootView.findViewById(R.id.countdown_time);
        int countDown = FriendTime.getThemeCountDown(act.beginTime, act.endTime);
        if (timer != null) {
            timer.cancel(false);
        }
        timer = new Timer();
        timer.setOnResult(new Timer.OnResult() {
            @Override
            public void OK() {

            }

            @Override
            public void countdown(int s) {
                countDownTextView.setText(String.format("%d:%2d:%2d", s / 3600, (s % 3600) / 60,
                        s % 60));
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, countDown * 1000);

        return rootView;
    }


    /**
     * 点击监听
     */
    public interface OnClick {
        /*报名*/
        void SignUP(Theme.ActsEntity act);

        /*进入活动*/
        void EnterTheme(Theme.ActsEntity act);

        /*查看详情*/
        void watchIntro(Theme.ActsEntity act);
    }
}
