package com.tizi.quanzi.tool;

import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaRecorder;
import android.support.annotation.Nullable;

import com.tizi.quanzi.app.App;
import com.tizi.quanzi.log.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by qixingchen on 15/8/17.
 * 录音
 * 必须调用 release
 */
public class RecodeAudio {

    private MediaRecorder recorder;

    private Activity mContext;

    private String FileName;
    private File file;

    public RecodeAudio(Activity context) {
        mContext = context;
        recorder = new MediaRecorder();
    }

    /**
     * 开始录音
     */
    public boolean start() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        FileName = String.valueOf(new Date().getTime() / 1000) + ".aac";

        file = new File(mContext.getCacheDir().getAbsolutePath() + "/audio/" + App.getUserID(),
                FileName);
        Log.d("录音", file.getAbsolutePath());
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();
            recorder.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 停止录音并返回录音地址
     *
     * @return 录音地址
     */
    @Nullable
    public String stopAndReturnFilePath() {
        try {
            recorder.stop();
            recorder.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        recorder.release();
    }

}
