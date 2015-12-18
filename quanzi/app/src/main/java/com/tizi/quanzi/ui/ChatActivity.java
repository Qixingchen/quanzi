package com.tizi.quanzi.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.darsh.multipleimageselect.helpers.Constants;
import com.squareup.otto.Subscribe;
import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.ChatMessageAdapter;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.chat.MutiTypeMsgHandler;
import com.tizi.quanzi.chat.MyAVIMClientEventHandler;
import com.tizi.quanzi.chat.SendMessage;
import com.tizi.quanzi.dataStatic.BoomGroupList;
import com.tizi.quanzi.dataStatic.GroupList;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.gson.OtherUserInfo;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.model.ChatMessage;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.network.FindUser;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.notification.AddNotification;
import com.tizi.quanzi.otto.AVIMNetworkEvents;
import com.tizi.quanzi.otto.ExitChatActivity;
import com.tizi.quanzi.tool.GetMutipieImage;
import com.tizi.quanzi.tool.RecodeAudio;
import com.tizi.quanzi.tool.RequreForImage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.tool.Timer;
import com.tizi.quanzi.ui.quanzi_zone.QuanziZoneActivity;
import com.tizi.quanzi.ui.user_zone.UserZoneActivity;
import com.tizi.quanzi.widget.swipe_to_cancel.ViewProxy;

import java.util.List;
import java.util.Map;

/**
 * 聊天界面
 */
public class ChatActivity extends BaseActivity {

    private static final int QueryLimit = StaticField.Limit.MessageLimit;
    private static final String TAG = ChatActivity.class.getSimpleName();
    private Context context;
    private RecyclerView chatmessagerecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ChatMessageAdapter chatMessageAdapter;
    private android.support.v4.widget.SwipeRefreshLayout ChatSwipeToRefresh;
    private android.widget.EditText InputMessage;
    private android.widget.ImageButton SendButton;
    private String CONVERSATION_ID = "";
    private RequreForImage requreForImage;
    private RecodeAudio recodeAudio;
    private int ChatType;
    private ImageButton insertImageButton, insertVoiceButton;
    private int LastPosition = -1;
    //toolbar
    private String toolbarTitle;
    //swipe to cancel
    private TextView recordTimeText;
    private View recordPanel;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = 200;
    private Timer timer;
    //下方数量
    private View scrollToEndView;
    private TextView underMessSumTextView;
    //发送回调
    private SendMessage.ChatViewSendOK sendOK = new SendMessage.ChatViewSendOK() {
        @Override
        public void sendOK(ChatMessage Message, String convID, String tempID) {
            if (convID.equals(CONVERSATION_ID)) {
                chatMessageAdapter.updateTempMess(tempID, Message);
            }
        }

        /**
         * 消息预发送时的处理
         * 加入列表并跳转到最后
         *
         * @param Message 预发送的消息
         */
        @Override
        public void preSend(ChatMessage Message, String convID) {
            if (convID.equals(CONVERSATION_ID)) {
                chatmessagerecyclerView.scrollToPosition(
                        chatMessageAdapter.chatMessageList.size() - 1);
                chatmessagerecyclerView.scrollToPosition(
                        chatMessageAdapter.chatMessageList.size());
                chatMessageAdapter.addOrUpdateMessage(Message);
            }
            setScrollToEnd();
        }

        @Override
        public void sendError(String errorMessage, String convID, String tempID, ChatMessage chatMessage) {
            Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
            chatMessage.status = AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed.getStatusCode();
            chatMessageAdapter.updateTempMess(tempID, chatMessage);
        }

    };
    //重发回调
    private ChatMessageAdapter.OnResend onResend = new ChatMessageAdapter.OnResend() {
        @Override
        public boolean onResend(ChatMessage chatMessage) {
            switch (chatMessage.type) {
                case StaticField.ChatContantType.TEXT:
                    SendMessage.getNewInstance().setChatViewSendOK(sendOK)
                            .sendTextMessage(CONVERSATION_ID, chatMessage.text, setAttrs());
                    break;
                case StaticField.ChatContantType.IMAGE:
                    SendMessage.getNewInstance().setChatViewSendOK(sendOK)
                            .sendImageMesage(CONVERSATION_ID, chatMessage.local_path, setAttrs());
                    break;
                case StaticField.ChatContantType.VOICE:
                    SendMessage.getNewInstance().setChatViewSendOK(sendOK)
                            .sendAudioMessage(CONVERSATION_ID, chatMessage.local_path, setAttrs());
                    break;
                default:
                    return false;

            }
            return true;
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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
        this.SendButton = (ImageButton) findViewById(R.id.SendButton);
        SendButton.setOnTouchListener(null);
        this.InputMessage = (EditText) findViewById(R.id.InputMessage);
        this.ChatSwipeToRefresh = (SwipeRefreshLayout) findViewById(R.id.ChatSwipeToRefresh);
        insertImageButton = (ImageButton) findViewById(R.id.insertImageButton);
        insertVoiceButton = (ImageButton) findViewById(R.id.insertVoiceButton);
        this.chatmessagerecyclerView = (RecyclerView) findViewById(R.id.chat_message_recyclerView);

        recordPanel = findViewById(R.id.record_panel);
        recordTimeText = (TextView) findViewById(R.id.recording_time_text);
        slideText = findViewById(R.id.slideText);

        scrollToEndView = findViewById(R.id.scroll_to_end_view);
        underMessSumTextView = (TextView) findViewById(R.id.under_mess_sum);
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
                                    SendMessage.getNewInstance().setChatViewSendOK(sendOK)
                                            .sendAudioMessage(CONVERSATION_ID, Filepath,
                                                    setAttrs());
                                }
                            }

                            @Override
                            public void countdown(long remainingS, long goneS) {
                                recordTimeText.setText(String.format("%ds  /  -%ds", goneS, remainingS));
                            }
                        }).setTimer(60 * 1000).start();
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
                        timer.cancel();
                    }
                    if (Filepath != null && Filepath.compareTo("less") != 0) {
                        Toast.makeText(context, "录音结束", Toast.LENGTH_SHORT).show();
                        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(200);
                        SendMessage.getNewInstance().setChatViewSendOK(sendOK)
                                .sendAudioMessage(CONVERSATION_ID, Filepath,
                                        setAttrs());
                    } else if (recodeAudio.AllPermissionGrant() && Filepath != null
                            && Filepath.compareTo("less") == 0) {
                        Toast.makeText(context, "按住录音,放开发送", Toast.LENGTH_SHORT).show();
                    }
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float x = motionEvent.getX();
                    Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                    if (x < -distCanMove) {
                        if (isRecoeding[0]) {
                            v.vibrate(100);
                        }
                        if (timer != null) {
                            timer.cancel();
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

                List<ChatMessage> chatMessageList =
                        DBAct.getInstance().queryMessage(CONVERSATION_ID, chatMessageAdapter.getItemCount());

                chatMessageAdapter.addOrUpdateMessages(chatMessageList);
                ChatSwipeToRefresh.setRefreshing(false);
            }
        });

        //RecyclerView
        chatmessagerecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        chatmessagerecyclerView.setLayoutManager(mLayoutManager);

        //insertImage
        insertImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requreForImage = RequreForImage.getInstance(mActivity);
                requreForImage.showDialogAndCallIntent("选择图片", StaticField.PermissionRequestCode.chat_insert_photo,
                        true, Integer.MAX_VALUE);
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

                        SendMessage.getNewInstance().setChatViewSendOK(sendOK)
                                .sendTextMessage(CONVERSATION_ID, text, setAttrs());
                        InputMessage.setText("");

                    }
                }
        );

        //滑到底
        scrollToEndView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatmessagerecyclerView.smoothScrollToPosition(chatMessageAdapter.getItemCount());
                scrollToEndView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        //更换静音按钮的文字
        if (!AppStaticValue.getNeedNotifi(CONVERSATION_ID)) {
            menu.findItem(R.id.action_mute_notifications).setTitle("启用通知");
        } else {
            menu.findItem(R.id.action_mute_notifications).setTitle("静音");
        }
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
                    FindUser.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
                        @Override
                        public void onOK(Object ts) {
                            OtherUserInfo otherUserInfo = (OtherUserInfo) ts;
                            Intent otherUser = new Intent(mContext, UserZoneActivity.class);
                            otherUser.putExtra(StaticField.IntentName.OtherUserInfo, (Parcelable) otherUserInfo);
                            mContext.startActivity(otherUser);
                        }

                        @Override
                        public void onError(String Message) {
                            Toast.makeText(mContext, "此用户已不存在", Toast.LENGTH_LONG).show();
                        }
                    }).findUserByID(PrivateMessPairList.getInstance().getGroupIDByConvID(CONVERSATION_ID));
                    return true;
                case StaticField.ConvType.BoomGroup:
                    // TODO: 15/10/13 boom group zone
                    return true;
                default:
                    return true;
            }
        }

        if (id == R.id.action_clear_history) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("确认删除聊天记录么？").setMessage("删除后无法恢复");
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DBAct.getInstance().deleteAllMessage(CONVERSATION_ID);
                    chatMessageAdapter.chatMessageList.clear();
                    switch (ChatType) {
                        case StaticField.ConvType.GROUP:
                            GroupList.getInstance().getGroupByConvID(CONVERSATION_ID).lastMess = "";
                            GroupList.getInstance().getGroupByConvID(CONVERSATION_ID).lastMessTime = 0;
                            GroupList.getInstance().removeAllUnread(CONVERSATION_ID);
                            break;

                        case StaticField.ConvType.BoomGroup:
                            BoomGroupList.getInstance().getGroupByConvID(CONVERSATION_ID).lastMess = "";
                            BoomGroupList.getInstance().getGroupByConvID(CONVERSATION_ID).lastMessTime = 0;
                            BoomGroupList.getInstance().removeAllUnread(CONVERSATION_ID);
                            break;

                        case StaticField.ConvType.twoPerson:
                            PrivateMessPairList.getInstance().getGroupByConvID(CONVERSATION_ID).lastMess = "";
                            PrivateMessPairList.getInstance().getGroupByConvID(CONVERSATION_ID).lastMessTime = 0;
                            PrivateMessPairList.getInstance().removeAllUnread(CONVERSATION_ID);
                            break;
                    }
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
        }

        if (id == R.id.action_mute_notifications) {
            boolean needNotifi = AppStaticValue.getNeedNotifi(CONVERSATION_ID);
            AppStaticValue.setNeedNotifi(CONVERSATION_ID, !needNotifi);
            if (needNotifi) {
                item.setTitle("静音");
            } else {
                item.setTitle("启用通知");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CONVERSATION_ID = getIntent().getStringExtra("conversation");
        AppStaticValue.UI_CONVERSATION_ID = CONVERSATION_ID;
        AddNotification.getInstance().chatActivityOpened(CONVERSATION_ID);

        //更换静音按钮的文字
        if (!AppStaticValue.getNeedNotifi(CONVERSATION_ID)) {
            try {
                mMenu.findItem(R.id.action_mute_notifications).setTitle("启用通知");
            } catch (Exception ignore) {
            }
        } else {
            try {
                mMenu.findItem(R.id.action_mute_notifications).setTitle("静音");
            } catch (Exception ignore) {
            }
        }

        ChatType = getIntent().getIntExtra("chatType", 9);
        //adapt
        List<ChatMessage> chatMessageList =
                DBAct.getInstance().queryMessage(CONVERSATION_ID, 0);
        chatMessageAdapter = new ChatMessageAdapter(chatMessageList, this);
        chatMessageAdapter.setOnResend(onResend);
        chatmessagerecyclerView.setAdapter(chatMessageAdapter);
        if (LastPosition != -1) {
            mLayoutManager.scrollToPosition(LastPosition);
        } else {
            mLayoutManager.scrollToPosition(chatMessageAdapter.lastReadPosition() + 1);
        }
        setTitle();
        toolbar.setTitle(MyAVIMClientEventHandler.getInstance().isNetworkAvailable ? toolbarTitle : "等待网络");

        ((LinearLayoutManager) mLayoutManager).setStackFromEnd(true);

        /*设置滑到底,需要延时*/
        new Timer().setOnResult(new Timer.OnResult() {
            @Override
            public void OK() {
                setScrollToEnd();
            }

            @Override
            public void countdown(long remainingS, long goneS) {

            }
        }).setTimer(500).start();


        chatmessagerecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                setScrollToEnd();
            }
        });

    }

    private void setScrollToEnd() {
        int below = chatMessageAdapter.getItemCount() - ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() - 1;
        if (below != 0) {
            scrollToEndView.setVisibility(View.VISIBLE);
            underMessSumTextView.setText(String.valueOf(below));
        } else {
            scrollToEndView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LastPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        if (timer != null) {
            timer.cancel();
        }
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
            if (data == null || (data.getData() == null && data
                    .getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES) == null)
                    && data.getClipData() == null) {
                sendImageMessage(requreForImage.getFilePathFromIntentMaybeCamera(null));
            } else {
                new GetMutipieImage().setOnImageGet(new GetMutipieImage.OnImageGet() {
                    @Override
                    public void OK(String FilePath) {
                        sendImageMessage(FilePath);
                    }

                    @Override
                    public void Error(String errorMessage) {
                        Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
                    }
                }).getMutipieImage(data);
            }

        }
    }

    private void sendImageMessage(String filePath) {
        SendMessage.getNewInstance()
                .setChatViewSendOK(sendOK)
                .sendImageMesage(CONVERSATION_ID, filePath, setAttrs());
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
                setScrollToEnd();

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

    private void setTitle() {
        switch (ChatType) {
            case StaticField.ConvType.GROUP:
                GroupClass group = (GroupClass) GroupList.getInstance().getGroupByConvID(CONVERSATION_ID);
                if (group != null) {
                    toolbarTitle = group.Name;
                }
                break;

            case StaticField.ConvType.BoomGroup:
                BoomGroupClass boom = BoomGroupList.getInstance().getGroupByConvID(CONVERSATION_ID);
                if (boom != null) {
                    toolbarTitle = boom.Name;
                }
                break;

            case StaticField.ConvType.twoPerson:
                PrivateMessPair pair = PrivateMessPairList.getInstance().getGroupByConvID(CONVERSATION_ID);
                if (pair != null) {
                    toolbarTitle = pair.Name;
                }
                break;
        }
    }
}
