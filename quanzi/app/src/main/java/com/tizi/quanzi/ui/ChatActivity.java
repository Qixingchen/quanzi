package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.ChatMessFormatFromAVIM;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.RecodeAudio;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.quanzi_zone.QuanziZoneActivity;

import java.util.List;
import java.util.Map;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseActivity {

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
    private int ChatType;

    private ImageButton insertImageButton, insertVoiceButton;

    private int LastPosition = -1;

    private static final int QueryLimit = StaticField.MessageQueryLimit.Limit;
    private static final String TAG = ChatActivity.class.getSimpleName();

    //toolbar
    private Toolbar toolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        context = this;
        setMessageCallback();
    }

    @Override
    protected void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.SendButton = (ImageButton) findViewById(R.id.SendButton);
        this.InputMessage = (EditText) findViewById(R.id.InputMessage);
        this.ChatSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.ChatSwipeToRefresh);
        insertImageButton = (ImageButton) findViewById(R.id.insertImageButton);
        insertVoiceButton = (ImageButton) findViewById(R.id.insertVoiceButton);
        this.chatmessagerecyclerView = (RecyclerView) findViewById(R.id.chat_message_recyclerView);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setViewEvent() {
        //录音
        insertVoiceButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        recodeAudio = RecodeAudio.getInstance(AppStaticValue.getActivity(ChatActivity.class.getSimpleName()));
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
                            SendMessage.getInstance().setSendOK(new SendMessage.SendOK() {
                                @Override
                                public void sendOK(AVIMTypedMessage Message, String CONVERSATION_ID) {
                                    onMessageSendOK(Message);
                                }

                                @Override
                                public void sendError(String errorMessage, String CONVERSATION_ID) {
                                    onMessageSendError(errorMessage);
                                }
                            })
                                    .sendAudioMessage(CONVERSATION_ID, Filepath,
                                            setAttrs());
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

                        SendMessage.getInstance().setSendOK(
                                new SendMessage.SendOK() {
                                    @Override
                                    public void sendOK(AVIMTypedMessage Message, String CONVERSATION_ID) {
                                        onMessageSendOK(Message);
                                        InputMessage.getText().clear();
                                    }

                                    @Override
                                    public void sendError(String errorMessage, String CONVERSATION_ID) {
                                        onMessageSendError(errorMessage);
                                    }
                                }
                        )
                                .sendTextMessage(CONVERSATION_ID, text, setAttrs());

                    }
                }
        );
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
        AppStaticValue.UI_CONVERSATION_ID = CONVERSATION_ID;
        conversation = AppStaticValue.getImClient().getConversation(CONVERSATION_ID);

        ChatType = getIntent().getIntExtra("chatType", 9);
        //adapt
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
        AppStaticValue.UI_CONVERSATION_ID = "";
        MutiTypeMsgHandler.getInstance().setOnMessage(null);
        if (recodeAudio != null) {
            recodeAudio.release();
        }
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
            SendMessage.getInstance()
                    .setSendOK(new SendMessage.SendOK() {
                        @Override
                        public void sendOK(AVIMTypedMessage Message, String CONVERSATION_ID) {
                            onMessageSendOK(Message);
                        }

                        @Override
                        public void sendError(String errorMessage, String CONVERSATION_ID) {

                        }
                    })
                    .sendImageMesage(CONVERSATION_ID, requreForImage.ZipedFilePathFromIntent(data),
                            setAttrs());

        }

    }

    /**
     * 授权回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == StaticField.PermissionRequestCode.requreForImage) {
            requreForImage.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

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
     * 设置attrs
     */
    private Map<String, Object> setAttrs() {
        Map<String, Object> attr;
        if (ChatType == StaticField.ChatBothUserType.GROUP) {
            attr = SendMessage.setMessAttr(GroupList.getInstance().getGroupIDByConvID(CONVERSATION_ID),
                    ChatType);
        } else {
            attr = SendMessage.setMessAttr("", ChatType);
        }
        return attr;
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
        chatMessageAdapter.addOrUpdateMessage(chatMessage);
        chatmessagerecyclerView.scrollToPosition(
                chatMessageAdapter.chatMessageList.size() - 1);
    }

    private void onMessageSendError(String errorMess) {
        Toast.makeText(context, "发送失败" + errorMess, Toast.LENGTH_LONG).show();
    }
}
