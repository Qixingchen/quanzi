package com.tizi.quanzi.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.model.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatmessagerecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChatMessageAdapter chatMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //RecyclerView
        this.chatmessagerecyclerView = (RecyclerView) findViewById(R.id.chat_message_recyclerView);
        chatmessagerecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        chatmessagerecyclerView.setLayoutManager(mLayoutManager);

        //adapt
        chatMessageAdapter = new ChatMessageAdapter(ChatMessage.getChatMess(), this);
        chatmessagerecyclerView.setAdapter(chatMessageAdapter);
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
}
