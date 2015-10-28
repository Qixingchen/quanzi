package com.tizi.quanzi.tool;

import android.os.AsyncTask;


/**
 * Created by qixingchen on 15/9/22.
 * 定时器 睡眠ms
 * 如果大于5000ms,则会有每秒的提示
 */
public class Timer extends AsyncTask<Integer, Integer, Integer> {
    private OnResult onResult;

    public Timer setOnResult(OnResult onResult) {
        this.onResult = onResult;
        return this;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        if (params[0] <= 5000) {
            try {
                Thread.sleep(params[0]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0;
        } else {
            int times = params[0] / 1000;
            for (int i = 0; i < times; i++) {
                try {
                    Thread.sleep(1000);
                    publishProgress(times - i - 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return 0;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (onResult != null) {
            onResult.countdown(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Integer a) {
        if (onResult != null) {
            onResult.OK();
        }
    }

    public interface OnResult {
        void OK();

        void countdown(int s);
    }
}
