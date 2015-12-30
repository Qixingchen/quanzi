package com.tizi.quanzi.ui.chat_list;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatListAdapter;
import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.tool.GetMutipieImage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseActivity;
import com.tizi.quanzi.ui.ChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatListActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;

    private int messType;
    private String text;
    private List<String> filePathList = new ArrayList<>();

    private ProgressDialog progressDialog;
    private SendMessage.ChatViewSendOK sendOK = new SendMessage.ChatViewSendOK() {

        /**
         * @param Message         AVIM 消息
         * @param CONVERSATION_ID 消息对应的CONVID
         * @param tempID          此消息对应的临时ID
         */
        @Override
        public void sendOK(ChatMessage Message, String CONVERSATION_ID, String tempID) {
            Toast.makeText(mContext, "发送成功", Toast.LENGTH_LONG).show();
            progressDialog.cancel();
            finish();
        }

        @Override
        public void preSend(ChatMessage Message, String CONVERSATION_ID) {

        }

        @Override
        public void sendError(String errorMessage, final String CONVERSATION_ID, String tempID, ChatMessage Message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("发送失败").setMessage(errorMessage)
                    .setPositiveButton("去重试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent chatActivity = new Intent(mContext, ChatActivity.class);
                            chatActivity.putExtra("conversation", CONVERSATION_ID);
                        }
                    }).show();
            progressDialog.cancel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Log.i(TAG, "onStart");
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
        ConvGroupAbs group = (ConvGroupAbs) intent.getSerializableExtra("group");

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

        if (group != null) {
            sendMessage(group);
            return;
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
            public void onClick(ConvGroupAbs group) {
                sendMessage(group);
            }
        });
    }

    private void sendMessage(ConvGroupAbs group) {
        if (messType == StaticField.ChatContantType.IMAGE) {
            for (String filePath : filePathList) {
                SendMessage.getNewInstance()
                        .setChatViewSendOK(sendOK)
                        .sendImageMesage(group.getConvId(), filePath, setAttrs(group.getType(), group.getID()));
            }
        } else if (messType == StaticField.ChatContantType.TEXT) {
            SendMessage.getNewInstance()
                    .setChatViewSendOK(sendOK)
                    .sendTextMessage(group.getConvId(), text, setAttrs(group.getType(), group.getID()));
        }
        progressDialog = showDialog();
    }

    /**
     * 进度条
     */
    private ProgressDialog showDialog() {
        ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("发送中");

        progressDialog.show();

        return progressDialog;
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
