package com.tizi.quanzi.ui.main;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tizi.quanzi.R;
import com.tizi.quanzi.app.AppStaticValue;
import com.tizi.quanzi.dataStatic.MyUserInfo;
import com.tizi.quanzi.databinding.FragmentUserInfoSetBinding;
import com.tizi.quanzi.gson.AllTags;
import com.tizi.quanzi.gson.UserTags;
import com.tizi.quanzi.network.GroupSetting;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.network.UserInfoSetting;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.new_group.GroupTagFragment;
import com.tizi.quanzi.view_model.UserInfoSetFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import me.next.tagview.TagCloudView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoSetFragment extends BaseFragment implements View.OnClickListener {


    private ProgressBar progressBar;
    private TagCloudView userTagView;
    private ArrayList<AllTags.TagsEntity> myTag;

    private FragmentUserInfoSetBinding binding;
    private UserInfoSetFragmentViewModel vm;

    public UserInfoSetFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info_set, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void findViews(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        userTagView = (TagCloudView) view.findViewById(R.id.user_tag_view);
        view.findViewById(R.id.user_tag).setOnClickListener(this);
    }

    public void setProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    protected void initViewsAndSetEvent() {
        binding.setUser(MyUserInfo.getInstance().getUserInfo());
        vm = new UserInfoSetFragmentViewModel(this, binding.getUser());
        binding.setVm(vm);

        GroupSetting.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                UserTags tags = (UserTags) ts;
                List<String> tagStrings = new ArrayList<>();
                myTag = new ArrayList<>(tags.usertags);
                for (AllTags.TagsEntity tag : tags.usertags) {
                    tagStrings.add(tag.tagName);
                }
                userTagView.setTags(tagStrings);
            }

            @Override
            public void onError(String Message) {

            }
        }).findUserTags(AppStaticValue.getUserID());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.user_tag:
                GroupTagFragment groupTagFragment = GroupTagFragment.newInstance(myTag, false);
                groupTagFragment.setOnOK(new GroupTagFragment.OnOK() {
                    @Override
                    public void OK(ArrayList<AllTags.TagsEntity> tags) {
                        List<String> tagStrings = new ArrayList<>();
                        for (AllTags.TagsEntity tag : tags) {
                            tagStrings.add(tag.tagName);
                        }
                        userTagView.setTags(tagStrings);
                        myTag = tags;
                        UserInfoSetting.getNewInstance().changeTag(tags);
                        getFragmentManager().popBackStack();
                    }
                });
                getFragmentManager().beginTransaction().hide(this)
                        .add(R.id.fragment, groupTagFragment)
                        .addToBackStack("GroupTagFragment").commit();
                break;
        }
    }



}

