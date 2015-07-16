package com.tizi.quanzi.adapter;

import android.content.Context;
import android.graphics.drawable.RippleDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.Group;
import com.tizi.quanzi.network.GetVolley;

/**
 * Created by qixingchen on 15/7/16.
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder> {

    private Group[] groups;
    private Context context;

    public GroupListAdapter(Group[] groups, Context context) {
        this.groups = groups;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_chat_item, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        myViewHolder.unreadTextview.setText("unread commit");
        myViewHolder.groupNameTextview.setText(groups[position].groupName);
        myViewHolder.lastCommitTextview.setText("2015-05-12 12:12");
        myViewHolder.groupFaceImageView.setImageUrl(groups[position].groupFace.toString(),
                GetVolley.getmInstance(context).getImageLoader());
    }

    @Override
    public int getItemCount() {
        return groups == null ? 0 : groups.length;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView groupFaceImageView;
        public TextView groupNameTextview, lastCommitTextview, unreadTextview, lastTimeTextview;

        public MyViewHolder(View itemView) {
            super(itemView);
            groupFaceImageView = (NetworkImageView) itemView.findViewById(R.id.group_face_image_view);
            groupNameTextview = (TextView) itemView.findViewById(R.id.group_name_text_view);
            lastCommitTextview = (TextView) itemView.findViewById(R.id.last_commit_text_view);
            unreadTextview = (TextView) itemView.findViewById(R.id.unread_text_view);
            lastTimeTextview = (TextView) itemView.findViewById(R.id.last_commit_text_view);
        }
    }
}
