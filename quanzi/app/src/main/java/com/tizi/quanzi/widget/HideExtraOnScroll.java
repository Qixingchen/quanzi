package com.tizi.quanzi.widget;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.lang.ref.WeakReference;

/**
 * Created by qixingchen on 15/10/26.
 */
public class HideExtraOnScroll extends RecyclerView.OnScrollListener {


    final static Interpolator ACCELERATE = new AccelerateInterpolator();
    final static Interpolator DECELERATE = new DecelerateInterpolator();


    WeakReference<View> mTarget;
    HideExtraOnScrollHelper mScrollHelper;


    boolean isExtraObjectsOutside;


    public HideExtraOnScroll(View target) {
        int minimumFlingVelocity = ViewConfiguration.get(target.getContext())
                .getScaledMinimumFlingVelocity();


        mScrollHelper = new HideExtraOnScrollHelper(minimumFlingVelocity);
        mTarget = new WeakReference<>(target);
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


        final View target = mTarget.get();


        if (target == null) {
            return;
        }

        if (recyclerView.getLayoutManager().findFirstCompletelyVisibleItemPosition() == 0) {
            if (!isShowBottom) {

            }
            isShowBottom = true;
        } else {
            isShowBottom = false;
        }


        boolean needsToHideExtraObjects = mScrollHelper.needsHideExtraObjects(dy);


        if (!isVisible(target) && !needsToHideExtraObjects) {
            show(target);
            isExtraObjectsOutside = false;
        } else if (isVisible(target) && needsToHideExtraObjects) {
            hide(target, -target.getHeight());
            isExtraObjectsOutside = true;
        }
    }


    public boolean isVisible(View target) {
        return !isExtraObjectsOutside;
    }


    public void hide(final View target, float distance) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationY",
                target.getTranslationY(), distance);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            animator.setAutoCancel(true);
        }
        animator.setInterpolator(DECELERATE);
        animator.start();
        target.setVisibility(View.GONE);
    }


    public void show(final View target) {
        target.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "translationY",
                target.getTranslationY(), 0f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            animator.setAutoCancel(true);
        }
        animator.setInterpolator(ACCELERATE);
        animator.start();
    }


}

