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
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.app.App;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatmessagerecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChatMessageAdapter chatMessageAdapter;
    private android.support.v4.widget.SwipeRefreshLayout ChatSwipeToRefresh;
    private android.widget.EditText InputMessage;
    private android.widget.ImageButton SendButton;
    private String CONVERSATION_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.SendButton = (ImageButton) findViewById(R.id.SendButton);
        this.InputMessage = (EditText) findViewById(R.id.InputMessage);
        this.ChatSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.ChatSwipeToRefresh);

        //RecyclerView
        this.chatmessagerecyclerView = (RecyclerView) findViewById(R.id.chat_message_recyclerView);
        chatmessagerecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        chatmessagerecyclerView.setLayoutManager(mLayoutManager);

        //adapt
        chatMessageAdapter = new ChatMessageAdapter(ChatMessage.getChatMess(), this);
        chatmessagerecyclerView.setAdapter(chatMessageAdapter);


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
                        AVIMConversation conversation =
                                App.getImClient().getConversation(CONVERSATION_ID);


                        conversation.sendMessage(message, new

                                        AVIMConversationCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (null != e) {
                                                    // 出错了。。。
                                                    e.printStackTrace();
                                                } else {
                                                    Log.d("发送成功，msgId=", message.getMessageId());
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
    }
}
