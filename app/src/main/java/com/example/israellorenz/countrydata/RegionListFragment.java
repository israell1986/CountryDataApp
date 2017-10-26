package com.example.israellorenz.countrydata;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.israellorenz.countrydata.Data.CountryData;
import com.example.israellorenz.countryData.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by israel.lorenz on 26/10/2017.
 */

public class RegionListFragment extends Fragment implements GetJsonTask.GetJsonTaskListener, ListAdapter.CountriesListAdapterListener, View.OnClickListener {

    private RecyclerView regionList;
    private LinearLayoutManager mLayoutManager;
    private RegionListFragmentListener listener;
    private List<CountryData> countryData;
    private LinearLayout listLayout;
    private LinearLayout errorLayout;
    private TextView closetButton;
    private ArrayList<String> ordersRigion;

    interface RegionListFragmentListener {
        void onRegionPressed(String selectedRegion);
    }

    public static RegionListFragment newInstance(){
        RegionListFragment fragment = new RegionListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.region_list_layout, null);
        closetButton = (TextView) view.findViewById(R.id.region_list_close_btn);
        listLayout = (LinearLayout)view.findViewById(R.id.region_list_layout);
        errorLayout = (LinearLayout)view.findViewById(R.id.region_list_errorLayout);
        regionList = (RecyclerView) view.findViewById(R.id.region_list_recycler_view);
        regionList.setHasFixedSize(true);
        regionList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        closetButton.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        regionList.setLayoutManager(mLayoutManager);

        getData();
        return view;
    }

    private void getData() {
        GetJsonTask getCountriesTask = new GetJsonTask(getActivity(), this);
        getCountriesTask.execute(getString(R.string.countries_list_url));
    }


    @Override
    public void gotJsonSuccess(String json) {
        Gson gson = new Gson();
        TypeToken<List<CountryData>> token = new TypeToken<List<CountryData>>() {};
        countryData = gson.fromJson(json, token.getType());
        HashSet<String> regionNames = new HashSet<String>();
        ordersRigion = new ArrayList<>();
        for (CountryData countryData : this.countryData){
            for (CountryData.RegionalBlocsEntity region : countryData.getRegionalBlocs()){
                regionNames.add(region.getAcronym() + " (" + region.getName() +")");
            }
        }
        ordersRigion = new ArrayList<>(regionNames);
        Collections.sort(ordersRigion);
        ListAdapter listAdapter = new ListAdapter(ordersRigion, this);
        regionList.setAdapter(listAdapter);
        errorLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void gotJsonFailed() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onItemClicked(int position) {
        listener.onRegionPressed(ordersRigion.get(position).split(" ")[0]);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.region_list_close_btn:
                getActivity().finish();
                break;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (RegionListFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CountriesListFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
