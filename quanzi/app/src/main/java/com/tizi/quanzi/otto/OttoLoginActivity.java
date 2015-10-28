package com.tizi.quanzi.otto;

import android.support.annotation.IntDef;

/**
 * Created by qixingchen on 15/10/28.
 */
public class OttoLoginActivity {

    /*忘记密码*/
    public static final int FORGET_PASSWORD = -2;
    public int event;

    public OttoLoginActivity(@Event int event) {
        this.event = event;
    }

    @IntDef({FORGET_PASSWORD})
    public @interface Event {
    }
}
