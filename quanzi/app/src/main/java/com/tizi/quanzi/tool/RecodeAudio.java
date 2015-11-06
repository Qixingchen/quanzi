package com.tizi.quanzi.tool;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.log.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by qixingchen on 15/8/17.
 * 录音
 * 必须调用 release
 */
public class RecodeAudio {

    private MediaRecorder recorder;

    private Activity mActivity;

    private String FileName;
    private File file;

    private long startTime, endTime;

    private RecodeAudio(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public static RecodeAudio getInstance(Activity mActivity) {
        return new RecodeAudio(mActivity);
    }

    /**
     * 开始录音
     */
    public boolean start() {

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestAllPermission();
            return false;
        }
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestAllPermission();
            return false;
        }
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            requestAllPermission();
            return false;
        }
        recorder = new MediaRecorder();
        recorder.reset();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        FileName = String.valueOf(new Date().getTime() / 1000) + ".aac";

        file = new File(mActivity.getCacheDir().getAbsolutePath() + "/audio/" + AppStaticValue.getUserID(),
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
            startTime = Calendar.getInstance().getTimeInMillis();
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
        boolean flag = true;//已经被释放,说明是取消的
        try {
            recorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        try {
            recorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recorder = null;
        endTime = Calendar.getInstance().getTimeInMillis();
        if (endTime - startTime < 1000) {
            return "less";
        }
        if (!flag) {
            return null;
        }
        if (file != null && file.exists()) {
            return file.getAbsolutePath();
        } else {
            return null;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        try {
            recorder.release();
        } catch (Exception ignore) {
        }
    }

    /**
     * 取得所有需要的授权
     */
    private void requestAllPermission() {

        List<String> permissionList = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        if (permissionList.size() == 0) {
            Log.w(RecodeAudio.class.getSimpleName(), "permissionList.size == 0");
            return;
        }

        String[] permission = permissionList.toArray(new String[permissionList.size()]);
        int code = StaticField.PermissionRequestCode.CHAT_RECORD_AUDIO;
        ActivityCompat.requestPermissions(mActivity, permission, code);
    }

    public boolean AllPermissionGrant() {
        List<String> permissionList = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECORD_AUDIO);
        }
        return permissionList.size() == 0;
    }
}
