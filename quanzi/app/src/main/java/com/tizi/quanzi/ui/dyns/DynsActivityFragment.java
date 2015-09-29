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
import com.tizi.quanzi.ui.BaseFragment;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DynsActivityFragment extends BaseFragment {

    private DynsAdapter dynsAdapter;
    private RecyclerView recyclerView;


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

        ArrayList<Dyns.DynsEntity> dynsList = getActivity().getIntent().getExtras().getParcelableArrayList("dyns");

        dynsAdapter = new DynsAdapter(dynsList, getContext());
        recyclerView.setAdapter(dynsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
