package com.tizi.quanzi.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.chat.AVMessageHandler;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.ChatMessage;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.tool.Tool;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatmessagerecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChatMessageAdapter chatMessageAdapter;
    private android.support.v4.widget.SwipeRefreshLayout ChatSwipeToRefresh;
    private android.widget.EditText InputMessage;
    private android.widget.ImageButton SendButton;
    private String CONVERSATION_ID = "";
    AVIMConversation conversation;

    private static final String TAG = ChatActivity.class.getSimpleName();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.SendButton = (ImageButton) findViewById(R.id.SendButton);
        this.InputMessage = (EditText) findViewById(R.id.InputMessage);
        this.ChatSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.ChatSwipeToRefresh);

        //SwipeRefresh
        ChatSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO: 15/8/13 下拉刷新获取数据
                ChatSwipeToRefresh.setRefreshing(false);
            }
        });

        //RecyclerView
        this.chatmessagerecyclerView = (RecyclerView) findViewById(R.id.chat_message_recyclerView);
        chatmessagerecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        chatmessagerecyclerView.setLayoutManager(mLayoutManager);

        //send
        SendButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (InputMessage.getText().toString().compareTo("") == 0) {
                            return;
                        }
                        final AVIMMessage message = new AVIMMessage();
                        message.setContent(InputMessage.getText().toString());

                        conversation.sendMessage(message, new

                                        AVIMConversationCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (null != e) {
                                                    // todo 出错了。。。
                                                    e.printStackTrace();
                                                } else {
                                                    com.tizi.quanzi.gson.ChatMessage chatMessage =
                                                            Tool.chatMessageFromAVMessage(message);
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
        //CONVERSATION_ID = "55caba1840ac41014f7e78ce";//安静的账号
        CONVERSATION_ID = "55c1b77b00b0cb9ca0a4d664";//喧闹的
        AVMessageHandler.getInstance().UI_CONVERSATION_ID = CONVERSATION_ID;
        conversation = App.getImClient().getConversation(CONVERSATION_ID);

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

        //adapt
        // TODO: 15/8/12 获取聊天记录
        conversation.queryMessages(30, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVException e) {
                if (e != null) {
                    e.printStackTrace();
                    return;
                }
                for (AVIMMessage avimMessage : list) {
                    ChatMessage chatMessage = Tool.chatMessageFromAVMessage(avimMessage);
                    DBAct.getInstance().addOrReplaceChatMessage(chatMessage);
                    chatMessageAdapter.addOrUpdateMessage(chatMessage);

                }
            }
        });
        List<ChatMessage> chatMessageList =
                DBAct.getInstance().queryMessage(CONVERSATION_ID);
        chatMessageAdapter = new ChatMessageAdapter(chatMessageList, this);
        chatmessagerecyclerView.setAdapter(chatMessageAdapter);
        chatmessagerecyclerView.scrollToPosition(chatMessageAdapter.lastReadPosition());
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVMessageHandler.getInstance().UI_CONVERSATION_ID = "";
        AVMessageHandler.getInstance().setOnMessage(null);
    }


}
