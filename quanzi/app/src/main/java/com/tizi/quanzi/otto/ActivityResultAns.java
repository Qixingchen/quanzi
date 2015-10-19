package com.tizi.quanzi.otto;

import android.content.Intent;

/**
 * Created by qixingchen on 15/10/19.
 */
public class ActivityResultAns {
    public int requestCode;
    public int resultCode;
    public Intent data;

    public ActivityResultAns() {
    }

    public ActivityResultAns(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }
}
