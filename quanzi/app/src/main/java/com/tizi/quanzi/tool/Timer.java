package com.tizi.quanzi.tool;

import android.os.AsyncTask;


/**
 * Created by qixingchen on 15/9/22.
 * 定时器
 */
public class Timer extends AsyncTask<Integer, Integer, Integer> {
    private OnResult onResult;

    public Timer setOnResult(OnResult onResult) {
        this.onResult = onResult;
        return this;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        try {
            Thread.sleep(params[0]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer a) {
        if (onResult != null) {
            onResult.OK();
        }
    }

    public interface OnResult {
        void OK();
    }
}
