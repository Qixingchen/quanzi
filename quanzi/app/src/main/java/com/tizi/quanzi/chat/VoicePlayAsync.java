package com.tizi.quanzi.chat;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.tizi.quanzi.adapter.ChatMessAbsViewHolder;
import com.tizi.quanzi.model.ChatMessage;

import java.io.IOException;

/**
 * Created by qixingchen on 15/8/18.
 * 音频播放 AsyncTask
 */
public class VoicePlayAsync extends AsyncTask<Integer, Integer, Integer> {

    private ChatMessage chatMessage;
    public ChatMessAbsViewHolder holder;
    private int voiceSecondMuL10;
    private VoicePlayAsync mInstance = this;
    private Context context;
    private MediaPlayer player;

    /**
     * 后台前
     * 设置进度，预加载
     */
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

    /**
     * 后台
     * 播放音频，并更新进度
     * 每100ms检查是否已停止，停止并释放资源
     *
     * @param params 无效参数
     */
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

    /**
     * 播放结束 设置进度为0，释放资源
     *
     * @param i 无效参数
     */
    @UiThread
    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        holder.audioProgressBar.setProgress(0);
        player.release();
    }

    /**
     * 更新进度
     *
     * @param values 进度值
     */
    @UiThread
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        for (int i : values) {
            holder.audioProgressBar.setProgress(i);
        }

    }

    /**
     * 设置 ChatMessage
     *
     * @param chatMessage 需要播放的 ChatMessage
     *
     * @return 本class实例
     */
    public VoicePlayAsync setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
        return mInstance;
    }

    /**
     * 设置 ChatMessAbsViewHolder
     *
     * @param holder 播放所在的viewHolder
     *
     * @return 本class实例
     */
    public VoicePlayAsync setHolder(ChatMessAbsViewHolder holder) {
        this.holder = holder;
        return mInstance;
    }

    /**
     * 设置上下文
     *
     * @param context 上下文
     *
     * @return 本class实例
     */
    public VoicePlayAsync setContext(Context context) {
        this.context = context;
        return mInstance;
    }
}
