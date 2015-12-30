package com.tizi.quanzi.ui.chat_list;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.service.chooser.ChooserTarget;
import android.service.chooser.ChooserTargetService;
import android.support.v7.util.SortedList;

import com.squareup.picasso.Picasso;
import com.tizi.quanzi.dataStatic.ConvGroupAbs;
import com.tizi.quanzi.database.DBAct;
import com.tizi.quanzi.model.BoomGroupClass;
import com.tizi.quanzi.tool.StaticField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qixingchen on 15/12/22.
 * 直接分享
 */
@TargetApi(Build.VERSION_CODES.M)
public class MyChooserTargetService extends ChooserTargetService {

    SortedList<ConvGroupAbs> convGroups;

    /**
     * Called by the system to retrieve a set of deep-link {@link ChooserTarget targets} that
     * can handle an intent.
     * <p/>
     * <p>The returned list should be sorted such that the most relevant targets appear first.
     * The score for each ChooserTarget will be combined with the system's score for the original
     * target Activity to sort and filter targets presented to the user.</p>
     * <p/>
     * <p><em>Important:</em> Calls to this method from other applications will occur on
     * a binder thread, not on your app's main thread. Make sure that access to relevant data
     * within your app is thread-safe.</p>
     *
     * @param targetActivityName the ComponentName of the matched activity that referred the system
     *                           to this ChooserTargetService
     * @param matchedFilter      the specific IntentFilter on the component that was matched
     *
     * @return a list of deep-link targets to fulfill the intent match, sorted by relevance
     */
    @Override
    public List<ChooserTarget> onGetChooserTargets(ComponentName targetActivityName, IntentFilter matchedFilter) {
        ComponentName componentName = new ComponentName(getPackageName(),
                ChatListActivity.class.getCanonicalName());
        // The list of Direct Share items. The system will show the items the way they are sorted
        // in this list.
        ArrayList<ChooserTarget> targets = new ArrayList<>();

        convGroups = new SortedList<>(ConvGroupAbs.class,
                new SortedList.Callback<ConvGroupAbs>() {
                    @Override
                    public int compare(ConvGroupAbs o1, ConvGroupAbs o2) {
                        return (int) (o2.getLastMessTime() / 1000L - o1.getLastMessTime() / 1000L);
                    }

                    @Override
                    public void onInserted(int position, int count) {

                    }

                    @Override
                    public void onRemoved(int position, int count) {

                    }

                    @Override
                    public void onMoved(int fromPosition, int toPosition) {

                    }

                    @Override
                    public void onChanged(int position, int count) {

                    }

                    @Override
                    public boolean areContentsTheSame(ConvGroupAbs oldItem, ConvGroupAbs newItem) {
                        return oldItem == newItem;
                    }

                    @Override
                    public boolean areItemsTheSame(ConvGroupAbs item1, ConvGroupAbs item2) {
                        if (item1.getID() == null) {
                            return false;
                        }
                        return item1.getID().equals(item2.getID());
                    }
                });
        convGroups.addAll(DBAct.getInstance().quaryAllChatGroup());

        for (int i = 0; i < convGroups.size(); ++i) {
            ConvGroupAbs group = convGroups.get(i);
            Bundle extras = new Bundle();
            extras.putSerializable("group", group);
            Bitmap bitmap = null;
            String name = group.getName();
            if (group.getType() != StaticField.ConvType.BOOM_GROUP) {
                try {
                    bitmap = Picasso.with(getApplicationContext()).load(group.getFace()).resize(96, 96).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                name = String.format("%s 与 %s",
                        ((BoomGroupClass) group).groupName1, ((BoomGroupClass) group).groupName2);
                if (isMyGroup(((BoomGroupClass) group).groupId1)) {
                    try {
                        bitmap = Picasso.with(getApplicationContext()).load(((BoomGroupClass) group).icon2)
                                .resize(96, 96).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bitmap = Picasso.with(getApplicationContext()).load(((BoomGroupClass) group).icon1)
                                .resize(96, 96).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            targets.add(new ChooserTarget(
                    // The name of this target.
                    name,
                    // The icon to represent this target.
                    Icon.createWithBitmap(bitmap),
                    // The ranking score for this target (0.0-1.0); the system will omit items with
                    // low scores when there are too many Direct Share items.
                    1.0f,
                    // The name of the component to be launched if this target is chosen.
                    componentName,
                    // The extra values here will be merged into the Intent when this target is
                    // chosen.
                    extras));
        }
        return targets;
    }

    /**
     * 判断是不是自己的群
     *
     * @param GroupID 群号
     *
     * @return true：是自己的圈子
     */
    private boolean isMyGroup(String GroupID) {
        return DBAct.getInstance().quaryChatGroup(GroupID) != null;
    }

}
