package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.ChatMessFormatFromAVIM;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.notification.AddNotification;
import com.tizi.quanzi.otto.AVIMNetworkEvents;
import com.tizi.quanzi.otto.ExitChatActivity;
import com.tizi.quanzi.tool.RecodeAudio;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.ui.quanzi_zone.QuanziZoneActivity;
import com.tizi.quanzi.widget.swipe_to_cancel.ViewProxy;

import java.util.List;
import java.util.Map;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseActivity {

    private static final int QueryLimit = StaticField.QueryLimit.MessageLimit;
    private static final String TAG = ChatActivity.class.getSimpleName();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Context context;
    private RecyclerView chatmessagerecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChatMessageAdapter chatMessageAdapter;
    private android.support.v4.widget.SwipeRefreshLayout ChatSwipeToRefresh;
    private android.widget.EditText InputMessage;
    private android.widget.ImageButton SendButton;
    private String CONVERSATION_ID = "";
    private AVIMConversation conversation;
    private RequreForImage requreForImage;
    private RecodeAudio recodeAudio;
    private int ChatType;
    private ImageButton insertImageButton, insertVoiceButton;
    private int LastPosition = -1;
    //toolbar
    private Toolbar toolbar;
    private String toolbarTitle;
    //swipe to cancel
    private TextView recordTimeText;
    private View recordPanel;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = 200;
    private Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        toolbarTitle = "聊天";

        context = this;
        setMessageCallback();
        recodeAudio = RecodeAudio.getInstance(AppStaticValue.getActivity(ChatActivity.class.getSimpleName()));
    }

    /*LC网络状态更改*/
    @Subscribe
    public void onNetworkChange(AVIMNetworkEvents avimNetworkEvents) {
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle(avimNetworkEvents.isNetWorkAvailable ? toolbarTitle : "等待网络");
    }

    /*需要退出*/
    @Subscribe
    public void shouldExit(ExitChatActivity exitChatActivity) {
        if (CONVERSATION_ID.compareTo(exitChatActivity.convID) == 0) {
            finish();
        }
        if (ChatType == exitChatActivity.TYPE) {
            finish();
        }
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

        recordPanel = findViewById(R.id.record_panel);
        recordTimeText = (TextView) findViewById(R.id.recording_time_text);
        slideText = findViewById(R.id.slideText);
        TextView textView = (TextView) findViewById(R.id.slideToCancelTextView);
        textView.setText("SlideToCancel");
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void setViewEvent() {
        final boolean[] isRecoeding = new boolean[1];//防止多次震动
        //录音
        insertVoiceButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    params.leftMargin = 30;
                    slideText.setLayoutParams(params);
                    ViewProxy.setAlpha(slideText, 1);
                    startedDraggingX = -1;
                    isRecoeding[0] = true;
                    // startRecording();
                    if (recodeAudio.start()) {
                        InputMessage.setVisibility(View.GONE);
                        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(100);
                        recordTimeText.setText("0s");
                        timer = new Timer();
                        timer.setOnResult(new Timer.OnResult() {
                            @Override
                            public void OK() {
                                String Filepath = recodeAudio.stopAndReturnFilePath();
                                if (Filepath != null && Filepath.compareTo("less") != 0) {
                                    Toast.makeText(context, "录音最长60s", Toast.LENGTH_SHORT).show();
                                    Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(200);
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

                            @Override
                            public void countdown(int s) {
                                recordTimeText.setText(String.format("%ds  /  -%ds", 60 - s, s));
                            }
                        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 60 * 1000);
                    } else if (recodeAudio.AllPermissionGrant()) {
                        Toast.makeText(context, "录音初始化失败", Toast.LENGTH_SHORT).show();
                    }
                    insertVoiceButton.getParent()
                            .requestDisallowInterceptTouchEvent(true);
                    recordPanel.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    startedDraggingX = -1;
                    recordPanel.setVisibility(View.GONE);
                    InputMessage.setVisibility(View.VISIBLE);
                    String Filepath = recodeAudio.stopAndReturnFilePath();
                    if (timer != null) {
                        timer.cancel(false);
                    }
                    if (Filepath != null && Filepath.compareTo("less") != 0) {
                        Toast.makeText(context, "录音结束", Toast.LENGTH_SHORT).show();
                        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(200);
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
                    } else if (recodeAudio.AllPermissionGrant() && Filepath != null
                            && Filepath.compareTo("less") == 0) {
                        Toast.makeText(context, "press to recode,release to send", Toast.LENGTH_SHORT).show();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    if (x < -distCanMove) {
                        if (isRecoeding[0]) {
                            v.vibrate(100);
                        }
                        if (timer != null) {
                            timer.cancel(false);
                        }
                        recodeAudio.stopAndReturnFilePath();
                        recordPanel.setVisibility(View.GONE);
                        InputMessage.setVisibility(View.VISIBLE);
                        isRecoeding[0] = false;
                    }
                    x = x + ViewProxy.getX(insertVoiceButton);
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    if (startedDraggingX != -1) {
                        float dist = (x - startedDraggingX);
                        params.leftMargin = 30 + (int) dist;
                        slideText.setLayoutParams(params);
                        float alpha = 1.0f + dist / distCanMove;
                        if (alpha > 1) {
                            alpha = 1;
                        } else if (alpha < 0) {
                            alpha = 0;
                        }
                        ViewProxy.setAlpha(slideText, alpha);
                    }
                    if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
                            + 30) {
                        if (startedDraggingX == -1) {
                            startedDraggingX = x;
                            distCanMove = (recordPanel.getMeasuredWidth()
                                    - slideText.getMeasuredWidth() - 48) / 2.0f;
                            if (distCanMove <= 0) {
                                distCanMove = 80;
                            } else if (distCanMove > 80) {
                                distCanMove = 80;
                            }
                        }
                    }
                    if (params.leftMargin > 30) {
                        params.leftMargin = 30;
                        slideText.setLayoutParams(params);
                        ViewProxy.setAlpha(slideText, 1);
                        startedDraggingX = -1;
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
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
                ChatSwipeToRefresh.setRefreshing(false);
                // TODO: 15/9/18 fresh from database
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
                requreForImage.showDialogAndCallIntent("选择图片", StaticField.PermissionRequestCode.chat_insert_photo);
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
        int id = item.getItemId();

        if (id == R.id.action_zone) {

            switch (ChatType) {
                case StaticField.ConvType.GROUP:
                    Intent intent = new Intent(this, QuanziZoneActivity.class);
                    intent.putExtra("conversation", CONVERSATION_ID);
                    startActivity(intent);
                    return true;
                case StaticField.ConvType.twoPerson:
                    return true;
                case StaticField.ConvType.BoomGroup:
                    // TODO: 15/10/13 boom group zone
                    return true;
                default:
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CONVERSATION_ID = getIntent().getStringExtra("conversation");
        AppStaticValue.UI_CONVERSATION_ID = CONVERSATION_ID;
        conversation = AppStaticValue.getImClient().getConversation(CONVERSATION_ID);
        AddNotification.getInstance().chatActivityOpened(CONVERSATION_ID);

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
        toolbar.setTitle(MyAVIMClientEventHandler.getInstance().isNetworkAvailable ? toolbarTitle : "等待网络");

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


    /*聊天消息回调接口*/
    private void setMessageCallback() {

        //富文本解析
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
    }

    /**
     * 设置attrs
     */
    private Map<String, Object> setAttrs() {
        Map<String, Object> attr;
        if (ChatType == StaticField.ConvType.GROUP) {
            attr = SendMessage.setMessAttr(GroupList.getInstance().getGroupIDByConvID(CONVERSATION_ID),
                    ChatType);
        } else if (ChatType == StaticField.ConvType.BoomGroup) {
            attr = SendMessage.setMessAttr(BoomGroupList.getInstance().getGroupIDByConvID(CONVERSATION_ID),
                    ChatType);
        } else {
            attr = SendMessage.setMessAttr(PrivateMessPairList.getInstance().getGroupIDByConvID(CONVERSATION_ID), ChatType);
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
