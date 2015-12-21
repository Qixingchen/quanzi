package com.tizi.quanzi.ui.chat_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatListAdapter;
import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.tool.GetMutipieImage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;

    private int messType;
    private String text;
    private List<String> filePathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
    }

    /**
     * 获取布局控件
     */
    @Override
    protected void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.chat_list_recyclerView);

    }

    /**
     * 初始化View的一些数据
     */
    @Override
    protected void initView() {
        Intent intent = getIntent();
        String type = intent.getType();
        if ("text/plain".equals(type)) {
            messType = StaticField.ChatContantType.TEXT;
            text = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        if (type.startsWith("image/")) {
            messType = StaticField.ChatContantType.IMAGE;
            new GetMutipieImage().setOnImageGet(new GetMutipieImage.OnImageGet() {
                @Override
                public void OK(String filePath) {
                    filePathList.add(filePath);
                }

                @Override
                public void Error(String errorMessage) {
                    Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                }
            }).getMutipieImage(intent);
        }


        chatListAdapter = new ChatListAdapter();
        recyclerView.setAdapter(chatListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    /**
     * 设置点击监听
     */
    @Override
    protected void setViewEvent() {
        chatListAdapter.setOnClick(new ChatListAdapter.OnClick() {
            @Override
            public void onClick(ConvGroupAbs group, int chatType) {
                if (messType == StaticField.ChatContantType.IMAGE) {
                    for (String filePath : filePathList) {
                        SendMessage.getNewInstance()
                                .setChatViewSendOK(new SendMessage.ChatViewSendOK() {
                                    @Override
                                    public void sendOK(ChatMessage Message, String CONVERSATION_ID, String tempID) {

                                    }

                                    @Override
                                    public void preSend(ChatMessage Message, String CONVERSATION_ID) {

                                    }

                                    @Override
                                    public void sendError(String errorMessage, String CONVERSATION_ID, String tempID, ChatMessage Message) {

                                    }
                                })
                                .sendImageMesage(group.convId, filePath, setAttrs(chatType, group.ID));
                    }
                } else if (messType == StaticField.ChatContantType.TEXT) {
                    SendMessage.getNewInstance()
                            .setChatViewSendOK(new SendMessage.ChatViewSendOK() {
                                @Override
                                public void sendOK(ChatMessage Message, String CONVERSATION_ID, String tempID) {

                                }

                                @Override
                                public void preSend(ChatMessage Message, String CONVERSATION_ID) {

                                }

                                @Override
                                public void sendError(String errorMessage, String CONVERSATION_ID, String tempID, ChatMessage Message) {

                                }
                            })
                            .sendTextMessage(group.convId, text, setAttrs(chatType, group.ID));
                }
                finish();
            }
        });
    }

    /**
     * 设置attrs
     */
    private Map<String, Object> setAttrs(int ChatType, String groupID) {

        Map<String, Object> attr;
        attr = SendMessage.setMessAttr(groupID, ChatType);
        return attr;
    }
}
