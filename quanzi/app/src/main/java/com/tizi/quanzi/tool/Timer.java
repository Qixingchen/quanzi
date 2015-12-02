package com.tizi.quanzi.tool;

import android.os.CountDownTimer;


/**
 * Created by qixingchen on 15/9/22.
 * 定时器 睡眠ms
 * 如果大于5000ms,则会有每秒的提示
 */
public class Timer {
    private OnResult onResult;
    private CountDownTimer countDownTimer;

    public Timer setTimer(final long millisInFuture, long countDownInterval) {
        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (onResult != null) {
                    onResult.countdown(millisUntilFinished / 1000, (millisInFuture - millisUntilFinished) / 1000);
                }
            }

            @Override
            public void onFinish() {
                if (onResult != null) {
                    onResult.OK();
                }
            }
        };
        return this;
    }

    public Timer setTimer(long millisInFuture) {
        return setTimer(millisInFuture, 1000L);
    }

    public void start() {
        countDownTimer.start();
    }

    public void cancel() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public Timer setOnResult(OnResult onResult) {
        this.onResult = onResult;
        return this;
    }

    public interface OnResult {
        void OK();

        void countdown(long remainingS, long goneS);
    }
}
