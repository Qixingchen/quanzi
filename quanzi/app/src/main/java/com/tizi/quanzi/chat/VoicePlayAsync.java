package com.tizi.quanzi.chat;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.tizi.quanzi.adapter.BaseViewHolder;
import com.tizi.quanzi.model.ChatMessage;

import java.io.IOException;

/**
 * Created by qixingchen on 15/8/18.
 */
public class VoicePlayAsync extends AsyncTask<Integer, Integer, Integer> {

    private ChatMessage chatMessage;
    public BaseViewHolder holder;
    private int voiceSecondMuL10;
    private VoicePlayAsync mInstance = this;
    private Context context;
    private MediaPlayer player;

    @UiThread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        voiceSecondMuL10 = (int) (chatMessage.voice_duration * 10);
        holder.audioProgressBar.setMax(voiceSecondMuL10);
        holder.audioProgressBar.setProgress(0);
        player = new MediaPlayer();
        try {
            player.setDataSource(chatMessage.url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @WorkerThread
    @Override
    protected Integer doInBackground(Integer... params) {

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();
        for (int i = 0; i < voiceSecondMuL10; i++) {
            publishProgress(i);
            if (isCancelled()) {
                player.stop();
                player.release();
                return 1;
            }
            try {
                Thread.currentThread();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        publishProgress(voiceSecondMuL10);

        return 0;
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        holder.audioProgressBar.setProgress(0);
        player.release();
    }

    @UiThread
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        for (int i : values) {
            holder.audioProgressBar.setProgress(i);
        }

    }

    public VoicePlayAsync setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
        return mInstance;
    }

    public VoicePlayAsync setHolder(BaseViewHolder holder) {
        this.holder = holder;
        return mInstance;
    }

    public VoicePlayAsync setContext(Context context) {
        this.context = context;
        return mInstance;
    }
}
