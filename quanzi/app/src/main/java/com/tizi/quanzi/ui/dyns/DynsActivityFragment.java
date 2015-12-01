package com.tizi.quanzi.ui.dyns;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tizi.quanzi.R;
import com.tizi.quanzi.adapter.DynsAdapter;
import com.tizi.quanzi.gson.Dyns;
import com.tizi.quanzi.log.Log;
import com.tizi.quanzi.network.DynamicAct;
import com.tizi.quanzi.network.RetrofitNetworkAbs;
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DynsActivityFragment extends BaseFragment {

    private DynsAdapter dynsAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String themeID, GroupID, themeAD, userID;
    private boolean isAllLoaded = false;
    private int page = 0;
    private boolean isUser;


    public DynsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dyns, container, false);
    }

    @Override
    protected void findViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.dyns_item_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeToRefresh);
        view.findViewById(R.id.send_dyn_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DynsActivity) mActivity).toSendDyn();
            }
        });
    }

    @Override
    protected void initViewsAndSetEvent() {
        ArrayList<Dyns.DynsEntity> dynsList = null;
        try {
            dynsList = getActivity().getIntent().getExtras().getParcelableArrayList("dyns");
        } catch (NullPointerException ignore) {
        }
        themeID = getActivity().getIntent().getStringExtra("themeID");
        GroupID = getActivity().getIntent().getStringExtra("groupID");
        themeAD = getActivity().getIntent().getStringExtra("themeAD");
        userID = getActivity().getIntent().getStringExtra("userID");
        isUser = getActivity().getIntent().getBooleanExtra("isUser", false);

        dynsAdapter = new DynsAdapter(dynsList, themeAD, getContext(), isUser);
        dynsAdapter.setShowUser(false);
        dynsAdapter.setOnclick(new DynsAdapter.Onclick() {
            @Override
            public void click(Dyns.DynsEntity dyn) {
                DynInfoFragment dynInfoFragment = new DynInfoFragment();
                dynInfoFragment.setDyn(dyn);
                dynInfoFragment.setIsUser(isUser);
                dynInfoFragment.setShowUser(false);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.disapear,
                                R.anim.no_change, R.anim.slide_out_to_bottom)
                        .hide(mFragment).add(R.id.fragment, dynInfoFragment)
                        .addToBackStack("DynInfoFragment").commit();
            }
        });
        dynsAdapter.setNeedMore(new DynsAdapter.NeedMore() {
            @Override
            public void needMore() {
                quaryMore(themeID, GroupID);
            }
        });
        quaryMore(themeID, GroupID);
        recyclerView.setAdapter(dynsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                quaryMore(themeID, GroupID, 0);
            }
        });
    }

    public void quaryMore(final String thmemID, final String groupID, final int nowPageCount) {
        swipeRefreshLayout.setRefreshing(true);
        Log.i(TAG, "查询群动态 PageIndex=" + nowPageCount);

        RetrofitNetworkAbs.NetworkListener listener = new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Dyns dyns = (Dyns) ts;
                int oldCount = dynsAdapter.getItemCount();
                isAllLoaded = dyns.dyns.size() < StaticField.Limit.DynamicLimit;
                dynsAdapter.addItems(dyns.dyns);
                if (!isAllLoaded && nowPageCount != 0 && dynsAdapter.getItemCount() - oldCount < StaticField.Limit.DynamicLimit) {
                    quaryMore(thmemID, groupID);
                }
                if (nowPageCount == 0) {
                    recyclerView.scrollToPosition(0);
                }
                if (nowPageCount < page - 2 && dynsAdapter.getItemCount() - oldCount == StaticField.Limit.DynamicLimit) {
                    quaryMore(thmemID, groupID, nowPageCount + 1);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String Message) {
                Snackbar.make(view, Message, Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        int count = nowPageCount * StaticField.Limit.DynamicLimit;
        if (isUser) {
            DynamicAct.getNewInstance(true).setNetworkListener(listener).getDynamic(false, userID, count);
        } else {
            if (groupID == null) {
                DynamicAct.getNewInstance(false).setNetworkListener(listener).getDynamic(false, thmemID, count);
            } else {
                DynamicAct.getNewInstance(false).setNetworkListener(listener).getDynamic(true, groupID, count);
            }
        }

    }

    /**
     * 查询动态 按照上次的page数查询
     * 查询后,page会+1
     */
    private void quaryMore(final String thmemID, final String groupID) {
        quaryMore(thmemID, groupID, page);
        page++;
    }
}
