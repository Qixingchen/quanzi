package com.tizi.quanzi.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tizi.quanzi.R;

import java.util.List;

/**
 * Created by qixingchen on 15/8/28.
 * 分享适配器
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.MyViewHolder> {

    private Context context;
    private List<LabeledIntent> intents;

    public ShareAdapter(Context context, List<LabeledIntent> intents) {
        this.context = context;
        this.intents = intents;
    }

    /**
     * 创建 ViewHolder
     *
     * @param parent   需要创建ViewHolder的 ViewGroup
     * @param viewType 记录类型
     *
     * @return PriavteMessAbsViewHolder {@link com.tizi.quanzi.adapter.ShareAdapter.MyViewHolder}
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * 发生绑定时，为viewHolder的元素赋值
     *
     * @param holder   被绑定的ViewHolder
     * @param position 列表位置
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(intents.get(position).getSourcePackage());
            holder.appIcon.setBackground(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        holder.share_text.setText(intents.get(position).getNonLocalizedLabel());
        holder.share_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openInChooser = new Intent(Intent.ACTION_SEND);
                openInChooser.setType("text/plain");
                openInChooser.setPackage(intents.get(position).getSourcePackage());
                openInChooser.putExtra(Intent.EXTRA_TEXT, "The text you want to share");
                context.startActivity(openInChooser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return intents.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView appIcon;
        public TextView share_text;
        public View share_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            appIcon = (ImageView) itemView.findViewById(R.id.share_pic);
            share_text = (TextView) itemView.findViewById(R.id.share_text);
            share_item = itemView.findViewById(R.id.share_item);
        }
    }
}
