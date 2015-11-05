package com.tizi.quanzi.ui.new_group;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.TagSelectAdapter;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.network.GroupSetting;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.quanzi_zone.QuanziZoneActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupTagFragment extends BaseFragment {


    private static final String SELECT_TAGS = "selectTags";
    private static final String NEED_OK_BTN = "needOkBtn";

    private boolean needOkbtn;


    private TextView[] tagTextViews = new TextView[5];
    private android.widget.TextView changetags;
    private RecyclerView tagrecyclerview;
    private TextView parentTagView;
    private Button okBtn;

    private ArrayList<AllTags.TagsEntity> selectTags = new ArrayList<>();
    private Map<String, List<AllTags.TagsEntity>> tagsMap = new TreeMap<>();
    private int parentLastPosition = 0;

    public GroupTagFragment() {
        // Required empty public constructor
    }

    public static GroupTagFragment newInstance(ArrayList<AllTags.TagsEntity> selectTags, boolean needOkBtn) {
        GroupTagFragment fragment = new GroupTagFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SELECT_TAGS, selectTags);
        args.putBoolean(NEED_OK_BTN, needOkBtn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectTags = getArguments().getParcelableArrayList(SELECT_TAGS);
            needOkbtn = getArguments().getBoolean(NEED_OK_BTN);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_tag, container, false);

    }

    @Override
    protected void findViews(View view) {
        this.tagrecyclerview = (RecyclerView) view.findViewById(R.id.tag_recycler_view);
        this.changetags = (TextView) view.findViewById(R.id.change_tags);
        this.tagTextViews[4] = (TextView) view.findViewById(R.id.tag_5);
        this.tagTextViews[3] = (TextView) view.findViewById(R.id.tag_4);
        this.tagTextViews[2] = (TextView) view.findViewById(R.id.tag_3);
        this.tagTextViews[1] = (TextView) view.findViewById(R.id.tag_2);
        this.tagTextViews[0] = (TextView) view.findViewById(R.id.tag_1);
        parentTagView = (TextView) view.findViewById(R.id.parent_tag_text_view);
        okBtn = (Button) view.findViewById(R.id.ok_btn);
    }

    @Override
    protected void initViewsAndSetEvent() {
        tagrecyclerview.setLayoutManager(new GridLayoutManager(mContext, 4));
        tagrecyclerview.setHasFixedSize(false);

        changetags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTags();
            }
        });

        GroupSetting.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                AllTags allTags = (AllTags) ts;
                for (AllTags.TagsEntity tag : allTags.tags) {
                    if (tagsMap.get(tag.parentTagName) == null) {
                        tagsMap.put(tag.parentTagName, new ArrayList<AllTags.TagsEntity>());
                    }
                    tagsMap.get(tag.parentTagName).add(tag);
                    setTags();
                }
            }

            @Override
            public void onError(String Message) {

            }
        }).findAllTags();

        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            tagTextViews[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selectTags.remove(finalI);
                    setSelectTagsTextView();
                    return true;
                }
            });
        }
        setSelectTagsTextView();

        if (needOkbtn) {
            okBtn.setVisibility(View.VISIBLE);
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((QuanziZoneActivity) mActivity).OnTagsSelectOk(selectTags);
                }
            });
        }
    }

    private void setTags() {
        String[] parentnames = new String[tagsMap.size()];
        tagsMap.keySet().toArray(parentnames);
        if (tagsMap.size() == 0) {
            return;
        }
        String nowParent = parentnames[(parentLastPosition++) % tagsMap.size()];
        parentTagView.setText(nowParent);
        TagSelectAdapter tagSelectAdapter = new TagSelectAdapter(
                tagsMap.get(nowParent), new TagSelectAdapter.OnSelect() {
            @Override
            public void select(AllTags.TagsEntity tag) {
                if (selectTags.size() < 5) {
                    for (AllTags.TagsEntity tempTag : selectTags) {
                        if (tempTag.tagName.compareTo(tag.tagName) == 0) {
                            Snackbar.make(view, "此标签已被选择", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                    }
                    selectTags.add(tag);
                    setSelectTagsTextView();
                } else {
                    Snackbar.make(view, "只能选择5个标签", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        tagrecyclerview.setAdapter(tagSelectAdapter);

    }

    private void setSelectTagsTextView() {
        int size = selectTags.size();
        for (int i = 0; i < size; i++) {
            tagTextViews[i].setText(selectTags.get(i).tagName);
            tagTextViews[i].setVisibility(View.VISIBLE);
            tagTextViews[i].setEnabled(true);
        }

        for (int i = size; i < 5; i++) {
            tagTextViews[i].setVisibility(View.INVISIBLE);
            tagTextViews[i].setEnabled(false);
        }
    }

    public ArrayList<AllTags.TagsEntity> OnOK() {
        return selectTags;
    }

    public interface OnOK {
        void OK(ArrayList<AllTags.TagsEntity> tags);
    }

}
