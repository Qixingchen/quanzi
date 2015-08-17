package com.tizi.quanzi.tool;

import android.app.Activity;
import android.media.MediaRecorder;

import com.tizi.quanzi.log.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by qixingchen on 15/8/17.
 */
public class RecodeAudio {

    private MediaRecorder recorder;

    private Activity mContext;

    public RecodeAudio(Activity context) {
        mContext = context;
        recorder = new MediaRecorder();
    }

    public boolean start() {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        new File(mContext.getCacheDir(), "audio/");

        File file = new File(mContext.getCacheDir().getAbsolutePath() + "/audio", "3.aac");
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
        // 开始录音
        return false;
    }

    public void stop() {
        try {
            recorder.stop();
            recorder.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // You can reuse the object by going back to setAudioSource() step
    }

    public void release() {
        recorder.release(); // Now the object cannot be reused
    }


}
