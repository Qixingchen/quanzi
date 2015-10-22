package com.tizi.quanzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.dataStatic.PrivateMessPairList;
import com.tizi.quanzi.dataStatic.SystemMessageList;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.model.PrivateMessPair;
import com.tizi.quanzi.model.SystemMessage;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.ChatActivity;

/**
 * Created by qixingchen on 15/10/22.
 * 私聊 系统消息 PagerAdapter
 */
public class NotifiPagerAdapter extends PagerAdapter {

    private final static String TAG = NotifiPagerAdapter.class.getSimpleName();
    private final String tabTitles[] = new String[]{"私信", "系统"};
    private View Views[] = new View[tabTitles.length];
    private Context mContext;
    private RecyclerView privateMessRecyclerView, systemMessRecyclerView;
    private PrivateMessageAdapter privateMessageAdapter;
    private SystemMessageAdapter systemMessageAdapter;

    public NotifiPagerAdapter(Context context) {
        mContext = context;
        LayoutInflater Inflater = LayoutInflater.from(context);
        Views[0] = Inflater.inflate(R.layout.item_only_recycler_view, null);
        Views[1] = Inflater.inflate(R.layout.item_only_recycler_view, null);
    }

    /**
     * 获取标签数量
     */
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    /**
     * 获取PageTitle
     * 第一屏不显示 Know issue & fixed on next relese
     * https://code.google.com/p/android/issues/detail?id=183127
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(Views[position]);
    }

    /**
     * 新增项目
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        switch (position) {
            case 0:
                privateMessRecyclerView = (RecyclerView) Views[0].findViewById(R.id.item_recycler_view);
                privateMessRecyclerView.setHasFixedSize(true);
                privateMessageAdapter = new PrivateMessageAdapter(PrivateMessPairList.getInstance().getGroupList(),
                        mContext, new PrivateMessageAdapter.Onclick() {
                    @Override
                    public void priMessClick(PrivateMessPair privateMessPair) {
                        Intent chat = new Intent(mContext, ChatActivity.class);
                        chat.putExtra("conversation", privateMessPair.convId);
                        chat.putExtra("chatType", StaticField.ConvType.twoPerson);
                        mContext.startActivity(chat);
                    }
                });
                privateMessRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                privateMessRecyclerView.setAdapter(privateMessageAdapter);
                container.addView(Views[position], 0);
                break;

            case 1:
                systemMessRecyclerView = (RecyclerView) Views[1].findViewById(R.id.item_recycler_view);
                systemMessRecyclerView.setHasFixedSize(true);
                systemMessageAdapter = new SystemMessageAdapter(SystemMessageList.getInstance().getGroupList(),
                        mContext, new SystemMessageAdapter.Onclick() {
                    @Override
                    public void systemMessClick(SystemMessage systemMessage) {
                        //ignore
                    }
                });
                systemMessRecyclerView.setAdapter(systemMessageAdapter);
                systemMessRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                container.addView(Views[position], 1);

                break;

            default:
                Log.e(TAG, "error view pager positon:" + position);
        }


        return Views[position];
    }
}
