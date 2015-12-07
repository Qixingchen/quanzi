package com.tizi.quanzi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.R;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.gson.Theme;
import com.tizi.quanzi.tool.FriendTime;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.ui.dyns.DynsActivity;

/**
 * Created by qixingchen on 15/11/18.
 */
public class ThemesPagerAdapter extends PagerAdapter {

    private final static String TAG = ThemesPagerAdapter.class.getSimpleName();

    private Theme theme;
    private Activity mActivity;
    private Timer[] timer;

    public ThemesPagerAdapter(Theme theme, Activity mActivity) {
        this.theme = theme;
        timer = new Timer[theme.acts.size()];
        this.mActivity = mActivity;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        for (Timer t : timer) {
            if (t != null) {
                t.cancel();
            }
        }
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return theme.acts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        timer[position].cancel();
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View rootView = themeView(container, theme.acts.get(position), position);

        container.addView(rootView);
        return rootView;
    }

    private View themeView(final ViewGroup container, final Theme.ActsEntity act, int position) {
        final View rootView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_theme, container, false);

        /*动态*/
        final ImageView themeImageView;
        final TextView countDownTextView, themeContentTextView, themeTitleTextView;

        themeTitleTextView = (TextView) rootView.findViewById(R.id.theme_title);
        themeImageView = (ImageView) rootView.findViewById(R.id.theme_pic);
        countDownTextView = (TextView) rootView.findViewById(R.id.countdown_time);
        themeContentTextView = (TextView) rootView.findViewById(R.id.theme_content);

        themeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dynsIntent = new Intent(App.getApplication(), DynsActivity.class);
                dynsIntent.putExtra("themeID", act.id);
                dynsIntent.putExtra("themeAD", act.adUrl);
                dynsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getApplication().startActivity(dynsIntent);
            }
        });

        themeTitleTextView.setText(act.title);
        Picasso.with(mActivity).load(act.icon).into(themeImageView);
        themeContentTextView.setText(act.content);

        /*倒计时*/
        int countDown = FriendTime.getThemeCountDown(act.beginTime, act.endTime);
        assert timer != null;
        timer[position] = new Timer();
        timer[position].setOnResult(new Timer.OnResult() {
            @Override
            public void OK() {

            }

            @Override
            public void countdown(long s, long goneS) {
                countDownTextView.setText(String.format("%d:%02d:%02d", s / 3600, (s % 3600) / 60,
                        s % 60));
            }
        }).setTimer(countDown * 1000).start();

        return rootView;
    }
}
