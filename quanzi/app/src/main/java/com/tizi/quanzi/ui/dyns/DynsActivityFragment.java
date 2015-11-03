package com.tizi.quanzi.ui.dyns;

import android.os.Bundle;
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
import com.tizi.quanzi.ui.BaseFragment;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DynsActivityFragment extends BaseFragment {

    private DynsAdapter dynsAdapter;
    private RecyclerView recyclerView;

    private String themeID, GroupID;


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

        dynsAdapter = new DynsAdapter(dynsList, getContext());
        dynsAdapter.setOnclick(new DynsAdapter.Onclick() {
            @Override
            public void click(Dyns.DynsEntity dyn) {
                DynInfoFragment dynInfoFragment = new DynInfoFragment();
                dynInfoFragment.setDyn(dyn);
                getFragmentManager().beginTransaction().replace(R.id.fragment, dynInfoFragment)
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
    }

    private void quaryMore(String thmemID, String groupID) {
        Log.i(TAG, "查询群动态 lastIndex=" + dynsAdapter.getItemCount());

        RetrofitNetworkAbs.NetworkListener listener = new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Dyns dyns = (Dyns) ts;
                dynsAdapter.addItems(dyns.dyns);
            }

            @Override
            public void onError(String Message) {

            }
        };

        if (groupID == null) {
            DynamicAct.getNewInstance().setNetworkListener(listener).getGroupDynamic(false, thmemID, dynsAdapter.getItemCount());
        } else {
            DynamicAct.getNewInstance().setNetworkListener(listener).getGroupDynamic(true, groupID, dynsAdapter.getItemCount());
        }
    }

}
