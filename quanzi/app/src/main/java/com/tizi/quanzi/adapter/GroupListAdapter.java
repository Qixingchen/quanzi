package com.tizi.quanzi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.tizi.quanzi.R;
import com.tizi.quanzi.model.GroupClass;
import com.tizi.quanzi.network.GetVolley;

import java.util.List;

/**
 * Created by qixingchen on 15/7/16.
 * 群组列表Adapter
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder> {

    private List<GroupClass> groupClasses;
    private Context context;
    private Onclick onclick;

    public GroupListAdapter(List<GroupClass> groupClasses, Context context, Onclick onclick) {
        this.groupClasses = groupClasses;
        this.context = context;
        this.onclick = onclick;
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
    public void onBindViewHolder(MyViewHolder myViewHolder, final int position) {
        myViewHolder.unreadTextview.setText("unread commit");
        myViewHolder.groupNameTextview.setText(groupClasses.get(position).groupName);
        myViewHolder.lastCommitTextview.setText("2015-05-12 12:12");
        myViewHolder.groupFaceImageView.setImageUrl(
                groupClasses.get(position).groupFace.toString(),
                GetVolley.getmInstance(context).getImageLoader());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupClasses == null ? 0 : groupClasses.size();
    }

    public interface Onclick {
        void itemClick(int position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView groupFaceImageView;
        public TextView groupNameTextview, lastCommitTextview, unreadTextview, lastTimeTextview;
        public View itemView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            groupFaceImageView = (NetworkImageView) itemView.findViewById(R.id.group_face_image_view);
            groupNameTextview = (TextView) itemView.findViewById(R.id.group_name_text_view);
            lastCommitTextview = (TextView) itemView.findViewById(R.id.last_commit_text_view);
            unreadTextview = (TextView) itemView.findViewById(R.id.unread_text_view);
            lastTimeTextview = (TextView) itemView.findViewById(R.id.last_commit_text_view);
        }
    }

    public void addAGroup(GroupClass group) {
        groupClasses.add(group);
        notifyDataSetChanged();
    }
}
