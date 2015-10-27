package com.tizi.quanzi.widget;

/**
 * Created by qixingchen on 15/10/26.
 */
public class HideExtraOnScrollHelper {
    public final static int UNKNOWN = -1;
    public final static int TOP = 0;
    public final static int BOTTOM = 1;
    int mMinFlingDistance = 100;
    int mDraggedAmount;
    int mOldDirection;
    int mDragDirection;


    public HideExtraOnScrollHelper(int minFlingDistance) {
        mOldDirection =
                mDragDirection =
                        mDraggedAmount = UNKNOWN;


        mMinFlingDistance = minFlingDistance * 2;
    }


    /**
     * Checks need to hide extra objects on scroll or not
     *
     * @param dy scrolled distance y
     *
     * @return true if need to hide extra objects on screen
     */
    public boolean needsHideExtraObjects(int dy) {
        boolean needHide = false;
        mDragDirection = dy > 0 ? BOTTOM : TOP;


        if (mDragDirection != mOldDirection) {
            mDraggedAmount = 0;
        }


        mDraggedAmount += dy;


        if (mDragDirection == TOP && Math.abs(mDraggedAmount) > mMinFlingDistance / 2) {
            needHide = false;
        } else if (mDragDirection == BOTTOM && mDraggedAmount > mMinFlingDistance) {
            needHide = true;
        }


        if (mOldDirection != mDragDirection) {
            mOldDirection = mDragDirection;
        }


        return needHide;
    }
}

