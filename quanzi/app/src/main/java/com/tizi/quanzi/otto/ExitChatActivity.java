package com.tizi.quanzi.otto;

/**
 * Created by qixingchen on 15/10/30.
 * 聊天已经结束
 */
public class ExitChatActivity {
    public String convID;
    public int TYPE;

    /**
     * @param convID 需要退出的convID会话
     * @param TYPE   需要退出的对话类型,9不进行退出
     */
    public ExitChatActivity(String convID, int TYPE) {
        this.convID = convID;
        this.TYPE = TYPE;
    }
}
