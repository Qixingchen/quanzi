package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.chat.ChatMessFormatFromAVIM;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.RecodeAudio;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.QuanziZone.QuanziZoneActivity;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 聊天界面
 */
public class ChatActivity extends AppCompatActivity {

    private Context context;
    private RecyclerView chatmessagerecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChatMessageAdapter chatMessageAdapter;
    private android.support.v4.widget.SwipeRefreshLayout ChatSwipeToRefresh;
    private android.widget.EditText InputMessage;
    private android.widget.ImageButton SendButton;
    private String CONVERSATION_ID = "";
    private AVIMConversation conversation;
    private AVIMMessagesQueryCallback avimMessagesQueryCallback;
    private RequreForImage requreForImage;
    private RecodeAudio recodeAudio;

    private int LastPosition = -1;

    private static final int QueryLimit = StaticField.MessageQueryLimit.Limit;
    private static final String TAG = ChatActivity.class.getSimpleName();

    //toolbar
    private Toolbar toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        this.SendButton = (ImageButton) findViewById(R.id.SendButton);
        this.InputMessage = (EditText) findViewById(R.id.InputMessage);
        this.ChatSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.ChatSwipeToRefresh);
        final ImageButton insertImageButton = (ImageButton) findViewById(R.id.insertImageButton);
        final ImageButton insertVoiceButton = (ImageButton) findViewById(R.id.insertVoiceButton);
        recodeAudio = new RecodeAudio(this);

        //录音
        insertVoiceButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (recodeAudio.start()) {
                            Toast.makeText(context, "录音中", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "录音初始化失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        String Filepath = recodeAudio.stopAndReturnFilePath();
                        if (Filepath != null) {
                            Toast.makeText(context, "录音结束", Toast.LENGTH_SHORT).show();
                            sendAudioMessage(Filepath);
                        }
                }
                return false;
            }
        });

        //键入时隐藏显示按钮
        InputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    insertImageButton.setVisibility(View.VISIBLE);
                    insertVoiceButton.setVisibility(View.VISIBLE);
                    SendButton.setVisibility(View.GONE);
                } else {
                    insertImageButton.setVisibility(View.GONE);
                    insertVoiceButton.setVisibility(View.GONE);
                    SendButton.setVisibility(View.VISIBLE);
                }
            }
        });


        //SwipeRefresh
        ChatSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ChatMessage oldestChatMess = DBAct.getInstance().queryOldestMessage(CONVERSATION_ID);
                // TODO: 15/8/19 保持位置或者自动加在旧消息
                if (oldestChatMess != null) {
                    conversation.queryMessages(oldestChatMess.messID, oldestChatMess.create_time,
                            QueryLimit, avimMessagesQueryCallback);
                } else {
                    conversation.queryMessages(QueryLimit, avimMessagesQueryCallback);
                }
            }
        });

        //RecyclerView
        this.chatmessagerecyclerView = (RecyclerView) findViewById(R.id.chat_message_recyclerView);
        chatmessagerecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        chatmessagerecyclerView.setLayoutManager(mLayoutManager);

        //insertImage
        requreForImage = new RequreForImage(this);
        insertImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage.showDialogAndCallIntent("选择图片");
            }
        });

        //send
        SendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = InputMessage.getText().toString();
                        if (text.compareTo("") == 0) {
                            return;
                        }
                        sendTextMessage(text);
                    }
                }
        );


        setMessageCallback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_zone) {
            Intent intent = new Intent(this, QuanziZoneActivity.class);
            intent.putExtra("conversation", CONVERSATION_ID);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CONVERSATION_ID = getIntent().getStringExtra("conversation");
        App.UI_CONVERSATION_ID = CONVERSATION_ID;
        conversation = App.getImClient().getConversation(CONVERSATION_ID);

        //adapt
        //不再检索消息，不知道会不会有问题
        //conversation.queryMessages(QueryLimit, avimMessagesQueryCallback);
        List<ChatMessage> chatMessageList =
                DBAct.getInstance().queryMessage(CONVERSATION_ID);
        chatMessageAdapter = new ChatMessageAdapter(chatMessageList, this);
        chatmessagerecyclerView.setAdapter(chatMessageAdapter);
        if (LastPosition != -1) {
            chatmessagerecyclerView.scrollToPosition(LastPosition);
        } else {
            chatmessagerecyclerView.scrollToPosition(chatMessageAdapter.lastReadPosition());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LastPosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.UI_CONVERSATION_ID = "";
        MutiTypeMsgHandler.getInstance().setOnMessage(null);
        recodeAudio.release();
    }

    /**
     * 图片回调
     *
     * @see RequreForImage
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            sendImageMesage(requreForImage.ZipedFilePathFromIntent(data));
        }

    }

    /**
     * 授权回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        requreForImage.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /*聊天消息回调接口*/
    private void setMessageCallback() {

        //富文本简体
        MutiTypeMsgHandler.getInstance().setOnMessage(new MutiTypeMsgHandler.OnMessage() {
            /**
             * 收到消息，加入列表
             *
             * @param chatMessage 收到的消息
             * */
            @Override
            public void OnMessageGet(ChatMessage chatMessage) {
                chatMessageAdapter.addOrUpdateMessage(chatMessage);
                int LastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager)
                        .findLastVisibleItemPosition();
                int chatSize = chatMessageAdapter.chatMessageList.size();
                Log.w(TAG, "lastPos=" + LastVisibleItemPosition + ",chatsize=" + chatSize);
                if (chatSize - LastVisibleItemPosition <= 2) {
                    Log.w(TAG, "聊天末尾");
                    chatmessagerecyclerView.smoothScrollToPosition(
                            chatMessageAdapter.chatMessageList.size());
                }

            }

            @Override
            public void OnMyMessageSent(ChatMessage chatMessage) {
                chatMessageAdapter.addOrUpdateMessage(chatMessage);
            }
        });

        //聊天消息获取回调
        avimMessagesQueryCallback = new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                    ChatSwipeToRefresh.setRefreshing(false);
                    return;
                }
                for (AVIMMessage avimMessage : list) {
                    try {
                        ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage((AVIMTypedMessage) avimMessage);
                        DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
                        chatMessageAdapter.addOrUpdateMessage(chatMessage);
                    } catch (ClassFormatError formatError) {
                        SystemMessage systemMessage = ChatMessFormatFromAVIM.SysMessFromAVMess((AVIMTypedMessage) avimMessage);
                        Log.w(TAG, systemMessage.toString());
                        DBAct.getInstance().addOrReplaceSysMess(systemMessage);
                    }
                }
                ChatSwipeToRefresh.setRefreshing(false);
            }
        };
    }

    /**
     * 设置附加参数
     *
     * @return 聊天时需要附加的参数
     */
    private Map<String, Object> setMessAttr() {
        Map<String, Object> attr = new TreeMap<>();
        // TODO: 15/8/14 add username userIcon
        attr.put(StaticField.ChatMessAttrName.userName, "todo Name");
        attr.put(StaticField.ChatMessAttrName.userIcon, "http://ac-iz9otzx1.clouddn.com/lo73gXLe1hsXP93fGs0m4TMibivViSLY6qN4Pt3A.jpg");
        attr.put(StaticField.ChatMessAttrName.userID, App.getUserID());
        // TODO: 15/8/17 groupID
        attr.put(StaticField.ChatMessAttrName.groupID, "");
        attr.put(StaticField.ChatMessAttrName.type, StaticField.ChatBothUserType.twoPerson);
        attr.put(StaticField.ChatMessAttrName.IS_SYS_MESS,
                StaticField.SystemMessAttrName.MessTypeCode.normal_mess);
        return attr;
    }

    /**
     * 设置系统消息的附加参数
     *
     * @param attr 原有阐述列
     *
     * @return 添加了系统参数的列
     */
    private Map<String, Object> setSysMessAttr(Map<String, Object> attr) {
        attr.put(StaticField.ChatMessAttrName.IS_SYS_MESS,
                StaticField.SystemMessAttrName.MessTypeCode.System_mess);
        attr.put(StaticField.SystemMessAttrName.REMARK, "嗨 你好！");
        attr.put(StaticField.SystemMessAttrName.JOIN_CONV_ID, CONVERSATION_ID);
        attr.put(StaticField.SystemMessAttrName.LINK_URL, "");
        attr.put(StaticField.SystemMessAttrName.SYS_MSG_FLAG,
                StaticField.SystemMessAttrName.systemFlag.invitation);
        return attr;
    }

    /**
     * 发送图片消息
     *
     * @param Filepath 图片地址
     */
    private void sendImageMesage(String Filepath) {
        AVIMImageMessage message;
        try {
            message = new AVIMImageMessage(Filepath);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        message.setAttrs(setMessAttr());

        final AVIMImageMessage finalMessage = message;
        conversation.sendMessage(message,
                new AVIMConversationCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null != e) {
                            // todo 出错了。。。
                            e.printStackTrace();
                        } else {
                            onMessageSendOK(finalMessage);
                        }
                    }
                }

        );
    }

    /**
     * 发送音频消息
     *
     * @param Filepath 音频地址
     */
    private void sendAudioMessage(String Filepath) {
        try {
            AVIMAudioMessage message = new AVIMAudioMessage(Filepath);
            message.setAttrs(setMessAttr());
            final AVIMAudioMessage finalMessage = message;
            conversation.sendMessage(message, new AVIMConversationCallback() {
                @Override
                public void done(AVException e) {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        onMessageSendOK(finalMessage);
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 发送文本消息
     *
     * @param text 发送的文本
     */
    private void sendTextMessage(String text) {
        final AVIMTextMessage message = new AVIMTextMessage();
        message.setText(text);
        message.setAttrs(setMessAttr());

        conversation.sendMessage(message,
                new AVIMConversationCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null != e) {
                            // todo 出错了。。。
                            e.printStackTrace();
                        } else {
                            onMessageSendOK(message);
                            InputMessage.getText().clear();
                        }
                    }
                }

        );
    }

    /**
     * 消息发送成功时的处理
     * 加入列表并跳转到最后
     *
     * @param Message 发送成功的消息
     */
    private void onMessageSendOK(AVIMTypedMessage Message) {
        ChatMessage chatMessage =
                ChatMessFormatFromAVIM.ChatMessageFromAVMessage(Message);
        Log.d("发送成功", chatMessage.toString());
        DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
        GroupList.getInstance().updateGroupLastMess(CONVERSATION_ID, chatMessage.text, chatMessage.create_time);
        chatMessageAdapter.addOrUpdateMessage(chatMessage);
        chatmessagerecyclerView.scrollToPosition(
                chatMessageAdapter.chatMessageList.size() - 1);

    }
}
