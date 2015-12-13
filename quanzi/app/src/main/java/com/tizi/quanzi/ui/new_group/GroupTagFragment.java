package com.tizi.quanzi.ui.new_group;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tizi.quanzi.R;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.network.GroupSetting;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupTagFragment extends BaseFragment {


    private static final String SELECT_TAGS = "selectTags";
    private static final String IS_GROUP = "isGroup";

    private static final int MAX_TAG_NUM = 5;
    private static final int TAG_PER_GIVEN_NUM = 5;

    private boolean isGroup;


    private TagView[] selectedTagTextViews = new TagView[MAX_TAG_NUM];
    private TextView[] givenTagTextVuews = new TextView[TAG_PER_GIVEN_NUM];
    private android.widget.TextView changetags;
    private android.widget.TextView addTag;
    private TextView parentTagView;
    private OnOK onOK;

    private AllTags.TagsEntity[] showTags = new AllTags.TagsEntity[MAX_TAG_NUM];
    private Map<String, List<AllTags.TagsEntity>> tagsMap = new TreeMap<>();
    private int parentLastPosition = 0;

    public GroupTagFragment() {
        // Required empty public constructor
    }

    public static GroupTagFragment newInstance(ArrayList<AllTags.TagsEntity> selectTags, boolean isGroup) {
        GroupTagFragment fragment = new GroupTagFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SELECT_TAGS, selectTags);
        args.putBoolean(IS_GROUP, isGroup);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ArrayList<AllTags.TagsEntity> selectTags = getArguments().getParcelableArrayList(SELECT_TAGS);
            if (selectTags != null) {
                for (int i = 0; i < selectTags.size(); i++) {
                    showTags[i] = selectTags.get(i);
                }
            }
            isGroup = getArguments().getBoolean(IS_GROUP);
        }
        //noinspection ConstantConditions
        ((AppCompatActivity) mActivity).getSupportActionBar().setTitle("设置标签");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_tag, container, false);
    }

    @Override
    protected void findViews(View view) {
        this.givenTagTextVuews[0] = (TextView) view.findViewById(R.id.tag_need_select_1);
        this.givenTagTextVuews[1] = (TextView) view.findViewById(R.id.tag_need_select_2);
        this.givenTagTextVuews[2] = (TextView) view.findViewById(R.id.tag_need_select_3);
        this.givenTagTextVuews[3] = (TextView) view.findViewById(R.id.tag_need_select_4);
        this.givenTagTextVuews[4] = (TextView) view.findViewById(R.id.tag_need_select_5);
        this.changetags = (TextView) view.findViewById(R.id.change_tags);
        this.selectedTagTextViews[4] = new TagView(view.findViewById(R.id.tag_selected_5));
        this.selectedTagTextViews[3] = new TagView(view.findViewById(R.id.tag_selected_4));
        this.selectedTagTextViews[2] = new TagView(view.findViewById(R.id.tag_selected_3));
        this.selectedTagTextViews[1] = new TagView(view.findViewById(R.id.tag_selected_2));
        this.selectedTagTextViews[0] = new TagView(view.findViewById(R.id.tag_selected_1));
        parentTagView = (TextView) view.findViewById(R.id.parent_tag_text_view);
        addTag = (TextView) view.findViewById(R.id.add_tag);
    }

    @Override
    protected void initViewsAndSetEvent() {

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
                }
                setTags();
            }

            @Override
            public void onError(String Message) {

            }
        }).findAllTags(isGroup);

        for (int i = 0; i < MAX_TAG_NUM; i++) {
            final int finalI = i;
            selectedTagTextViews[i].view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteTagFromShowTags(finalI);
                    setSelectTagsTextView();
                }
            });
        }
        setSelectTagsTextView();

        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTagsList().size() >= MAX_TAG_NUM) {
                    Snackbar.make(view, String.format("只能选择%d个标签", MAX_TAG_NUM), Snackbar.LENGTH_LONG).show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    final LayoutInflater inflater = mActivity.getLayoutInflater();
                    final View layout = inflater.inflate(R.layout.dialog_one_line,
                            (ViewGroup) mActivity.findViewById(R.id.dialog_one_line));
                    final EditText input = (EditText) layout.findViewById(R.id.dialog_edit_text);
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(7);
                    input.setFilters(filterArray);
                    builder.setView(layout).setTitle("输入新的标签")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (input.getText().toString().equals("")) {
                                Snackbar.make(view, "标签不能为空", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            if (getTagsList().size() >= MAX_TAG_NUM) {
                                Snackbar.make(view, String.format("只能选择%d个标签", MAX_TAG_NUM),
                                        Snackbar.LENGTH_LONG).show();
                                return;
                            }
                            addTagToShowTags(AllTags.TagsEntity.getNewTag(
                                    input.getText().toString().replace(",", " ")));
                            dialog.dismiss();
                        }
                    });
                }
            }
        });

        for (int i = 0; i < TAG_PER_GIVEN_NUM; i++) {
            givenTagTextVuews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

        final List<AllTags.TagsEntity> thisTag = tagsMap.get(nowParent);

        for (int i = 0; i < TAG_PER_GIVEN_NUM; i++) {
            final AllTags.TagsEntity tag = thisTag.get(i);
            givenTagTextVuews[i].setText(tag.tagName);
            givenTagTextVuews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectTag(tag);
                }
            });
        }

    }

    private void onSelectTag(AllTags.TagsEntity tag) {
        if (getTagsList().size() < MAX_TAG_NUM) {
            for (AllTags.TagsEntity tempTag : getTagsList()) {
                if (tempTag.tagName.compareTo(tag.tagName) == 0) {
                    Snackbar.make(view, "此标签已被选择", Snackbar.LENGTH_LONG).show();
                    return;
                }
            }
            addTagToShowTags(tag);
        } else {
            Snackbar.make(view, String.format("只能选择%d个标签", MAX_TAG_NUM), Snackbar.LENGTH_LONG).show();
        }
    }

    private void setSelectTagsTextView() {

        for (int i = 0; i < MAX_TAG_NUM; i++) {
            if (showTags[i] == null) {
                selectedTagTextViews[i].view.setVisibility(View.INVISIBLE);
                selectedTagTextViews[i].view.setEnabled(false);
            } else {
                selectedTagTextViews[i].tagText.setText(showTags[i].tagName);
                selectedTagTextViews[i].view.setVisibility(View.VISIBLE);
                selectedTagTextViews[i].view.setEnabled(true);
            }
        }

    }

    public ArrayList<AllTags.TagsEntity> OnOK() {
        return getTagsList();
    }

    /**
     * 将tag加入 showTags ,用于保持一个tag显示位置的固定
     */
    private void addTagToShowTags(AllTags.TagsEntity tag) {
        for (int i = 0; i < MAX_TAG_NUM; i++) {
            if (showTags[i] == null) {
                showTags[i] = tag;
                break;
            }
        }
        setSelectTagsTextView();
    }

    /**
     * 在 showTags 中删除指定的tag
     */
    private void deleteTagFromShowTags(int postion) {
        showTags[postion] = null;
        setSelectTagsTextView();
    }

    private ArrayList<AllTags.TagsEntity> getTagsList() {
        ArrayList<AllTags.TagsEntity> ans = new ArrayList<>();
        for (AllTags.TagsEntity tag : showTags) {
            if (tag != null) {
                ans.add(tag);
            }
        }
        return ans;
    }

    public void setOnOK(OnOK onOK) {
        this.onOK = onOK;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_tag_complete) {
            if (onOK != null) {
                onOK.OK(getTagsList());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_tag_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public interface OnOK {
        void OK(ArrayList<AllTags.TagsEntity> tags);
    }

    private class TagView {

        private TextView tagText;
        private ImageButton deleteButton;
        private View view;

        public TagView(View view) {
            this.view = view;
            tagText = (TextView) view.findViewById(R.id.tag);
            deleteButton = (ImageButton) view.findViewById(R.id.delete_tag_button);
        }
    }
}
