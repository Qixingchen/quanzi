package com.tizi.quanzi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.chat.AVMessageHandler;
import com.tizi.quanzi.chat.ChatMessFormatFromAVIM;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.tool.RecodeAudio;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.StaticField;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    private static final int QueryLimit = 10;
    private static final String TAG = ChatActivity.class.getSimpleName();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        context = this;
        this.SendButton = (ImageButton) findViewById(R.id.SendButton);
        this.InputMessage = (EditText) findViewById(R.id.InputMessage);
        this.ChatSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.ChatSwipeToRefresh);
        final ImageButton insertImageButton = (ImageButton) findViewById(R.id.insertImageButton);
        final ImageButton insertVoiceButton = (ImageButton) findViewById(R.id.insertVoiceButton);
        recodeAudio = new RecodeAudio(this);

        insertVoiceButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (recodeAudio.start()) {
                            Toast.makeText(context, "录音中", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "录音初始化失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        recodeAudio.stop();
                        Toast.makeText(context, "录音结束", Toast.LENGTH_SHORT).show();
                        // TODO: 15/8/17 upload
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
                requreForImage.showDialogAndCallIntent();
            }
        });

        //send
        SendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InputMessage.getText().toString().compareTo("") == 0) {
                            return;
                        }
                        final AVIMTextMessage message = new AVIMTextMessage();
                        message.setText(InputMessage.getText().toString());
                        message.setAttrs(getMessAttr());

                        conversation.sendMessage(message, new

                                        AVIMConversationCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (null != e) {
                                                    // todo 出错了。。。
                                                    e.printStackTrace();
                                                } else {
                                                    ChatMessage chatMessage =
                                                            ChatMessFormatFromAVIM.ChatMessageFromAVMessage(message);
                                                    Log.d("发送成功", chatMessage.toString());
                                                    InputMessage.setText("");
                                                    DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
                                                    chatMessageAdapter.addOrUpdateMessage(chatMessage);
                                                    chatmessagerecyclerView.smoothScrollToPosition(chatMessageAdapter.chatMessageList.size());
                                                }
                                            }
                                        }

                        );
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CONVERSATION_ID = getIntent().getStringExtra("conversation");
        // TODO: 15/8/12 testID
        CONVERSATION_ID = "55d163f860b24927fc93795e";
        App.UI_CONVERSATION_ID = CONVERSATION_ID;
        conversation = App.getImClient().getConversation(CONVERSATION_ID);


        //adapt
        conversation.queryMessages(QueryLimit, avimMessagesQueryCallback);
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
        LastPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.UI_CONVERSATION_ID = "";
        AVMessageHandler.getInstance().setOnMessage(null);
        MutiTypeMsgHandler.getInstance().setOnMessage(null);
        recodeAudio.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AVIMImageMessage message = null;
        try {
            message = new AVIMImageMessage(requreForImage.FilePathFromIntent(resultCode, data));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        message.setAttrs(getMessAttr());

        final AVIMImageMessage finalMessage = message;
        conversation.sendMessage(message, new

                        AVIMConversationCallback() {
                            @Override
                            public void done(AVException e) {
                                if (null != e) {
                                    // todo 出错了。。。
                                    e.printStackTrace();
                                } else {
                                    ChatMessage chatMessage =
                                            ChatMessFormatFromAVIM.ChatMessageFromAVMessage(finalMessage);
                                    Log.d("发送成功", chatMessage.toString());
                                    DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
                                    chatMessageAdapter.addOrUpdateMessage(chatMessage);
                                    chatmessagerecyclerView.smoothScrollToPosition(chatMessageAdapter.chatMessageList.size());
                                }
                            }
                        }

        );

    }

    private void setMessageCallback() {
        //聊天消息回调接口
        AVMessageHandler.getInstance().setOnMessage(new AVMessageHandler.OnMessage() {
            @Override
            public void OnMessageGet(ChatMessage chatMessage) {
                chatMessageAdapter.addOrUpdateMessage(chatMessage);
                int LastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager)
                        .findLastVisibleItemPosition();
                int chatSize = chatMessageAdapter.chatMessageList.size();
                Log.w(TAG, "lastPos=" + LastVisibleItemPosition + ",chatsize=" + chatSize);
                if (LastVisibleItemPosition - chatSize <= 2) {
                    Log.w(TAG, "聊天末尾");

                    chatmessagerecyclerView.smoothScrollToPosition(chatMessageAdapter.chatMessageList.size());
                }

            }

            @Override
            public void OnMyMessageSent(ChatMessage chatMessage) {
                chatMessageAdapter.addOrUpdateMessage(chatMessage);
            }
        });
        //复制一遍
        MutiTypeMsgHandler.getInstance().setOnMessage(new MutiTypeMsgHandler.OnMessage() {
            @Override
            public void OnMessageGet(ChatMessage chatMessage) {
                chatMessageAdapter.addOrUpdateMessage(chatMessage);
                int LastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager)
                        .findLastVisibleItemPosition();
                int chatSize = chatMessageAdapter.chatMessageList.size();
                Log.w(TAG, "lastPos=" + LastVisibleItemPosition + ",chatsize=" + chatSize);
                if (LastVisibleItemPosition - chatSize <= 2) {
                    Log.w(TAG, "聊天末尾");

                    chatmessagerecyclerView.smoothScrollToPosition(chatMessageAdapter.chatMessageList.size());
                }

            }

            @Override
            public void OnMyMessageSent(ChatMessage chatMessage) {
                chatMessageAdapter.addOrUpdateMessage(chatMessage);
            }
        });

        //聊天消息获取
        avimMessagesQueryCallback = new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                    ChatSwipeToRefresh.setRefreshing(false);
                    return;
                }
                for (AVIMMessage avimMessage : list) {
                    ChatMessage chatMessage = ChatMessFormatFromAVIM.ChatMessageFromAVMessage((AVIMTypedMessage) avimMessage);
                    DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
                    chatMessageAdapter.addOrUpdateMessage(chatMessage);
                }
                ChatSwipeToRefresh.setRefreshing(false);
            }
        };
    }

    private Map<String, Object> getMessAttr() {
        Map<String, Object> attr = new TreeMap<>();
        // TODO: 15/8/14 add username userIcon
        attr.put(StaticField.ChatMessAttrName.userName, "todo Name");
        attr.put(StaticField.ChatMessAttrName.userIcon, "http://ac-iz9otzx1.clouddn.com/lo73gXLe1hsXP93fGs0m4TMibivViSLY6qN4Pt3A.jpg");
        attr.put(StaticField.ChatMessAttrName.userID, App.getUserID());
        // TODO: 15/8/17 groupID
        attr.put(StaticField.ChatMessAttrName.groupID, "");
        attr.put(StaticField.ChatMessAttrName.type, StaticField.ChatBothUserType.twoPerson);
        return attr;
    }
}
