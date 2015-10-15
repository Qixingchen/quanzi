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
import com.tizi.quanzi.tool.StaticField;
import com.tizi.quanzi.ui.BaseFragment;
import com.tizi.quanzi.ui.quanzi_zone.DynInfoFragment;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DynsActivityFragment extends BaseFragment {

    private DynsAdapter dynsAdapter;
    private RecyclerView recyclerView;

    private boolean hasMoreToGet = true;
    private int lastIndex = 0;


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
    }

    @Override
    protected void initViewsAndSetEvent() {
        ArrayList<Dyns.DynsEntity> dynsList = null;
        try {
            dynsList = getActivity().getIntent().getExtras().getParcelableArrayList("dyns");
        } catch (NullPointerException ignore) {
        }
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
                if (hasMoreToGet) {
                    quaryMore("", lastIndex);
                    lastIndex += StaticField.QueryLimit.DynamicLimit;
                }
            }
        });
        quaryMore("", lastIndex);
        recyclerView.setAdapter(dynsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void quaryMore(String groupID, int lastIndex) {
        Log.i(TAG, "查询群动态 lastIndex=" + lastIndex);
        DynamicAct.getNewInstance().setNetworkListener(new RetrofitNetworkAbs.NetworkListener() {
            @Override
            public void onOK(Object ts) {
                Dyns dyns = (Dyns) ts;
                dynsAdapter.addItems(dyns.dyns);
                if (dyns.dyns.size() != StaticField.QueryLimit.DynamicLimit) {
                    hasMoreToGet = false;
                }
            }

            @Override
            public void onError(String Message) {

            }
        }).getGroupDynamic("", groupID, lastIndex);
    }
}
