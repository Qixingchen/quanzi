package com.tizi.quanzi.tool;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by qixingchen on 15/12/28.
 */
public class BindingHelper {

    @BindingAdapter({"bind:imageUrl", "bind:error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.with(view.getContext()).load(url).error(error).fit().into(view);
    }

    @BindingAdapter({"bind:imageUrl", "bind:holder"})
    public static void loadImage(ImageView view, String url, @DrawableRes int holder) {
        url = GetThumbnailsUri.getUriLink(url, view.getHeight(), view.getWidth());
        Picasso.with(view.getContext()).load(url).placeholder(holder).fit().into(view);
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).fit().into(view);
    }

    @BindingAdapter({"bind:lastMessTime"})
    public static void setLastTime(TextView textView, long lastMessTine) {
        if (lastMessTine == 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(FriendTime.FriendlyDate(lastMessTine));
        }
    }
}