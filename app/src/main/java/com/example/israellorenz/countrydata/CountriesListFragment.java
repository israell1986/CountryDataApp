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
import java.util.Comparator;
import java.util.List;

/**
 * Created by israel.lorenz on 26/10/2017.
 */

public class CountriesListFragment extends Fragment implements GetJsonTask.GetJsonTaskListener, ListAdapter.CountriesListAdapterListener, View.OnClickListener {

    private static final String SELECTED_REGION = "selectedRegion";
    private RecyclerView countriesList;
    private LinearLayoutManager mLayoutManager;
    private CountriesListFragmentListener listener;
    private List<CountryData> countryData;
    private LinearLayout listLayout;
    private LinearLayout errorLayout;
    private TextView closetButton;

    interface CountriesListFragmentListener {
        void onCountryPressed(List<String> borders, String selectedCountry, String capital, List<CountryData.CurrenciesEntity> currencies);
    }

    public static CountriesListFragment newInstance(String selectedRegion){
        CountriesListFragment fragment = new CountriesListFragment();
        Bundle args = new Bundle();
        args.putString(SELECTED_REGION, selectedRegion);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.countries_list_layout, null);
        closetButton = (TextView) view.findViewById(R.id.countries_list_close_btn);
        listLayout = (LinearLayout)view.findViewById(R.id.countries_list_layout);
        errorLayout = (LinearLayout)view.findViewById(R.id.countries_list_errorLayout);
        countriesList = (RecyclerView) view.findViewById(R.id.countries_list_recycler_view);
        countriesList.setHasFixedSize(true);
        countriesList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        closetButton.setOnClickListener(this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        countriesList.setLayoutManager(mLayoutManager);

        getData();
        return view;
    }

    private void getData() {
        GetJsonTask getCountriesTask = new GetJsonTask(getActivity(), this);
        getCountriesTask.execute(getString(R.string.region_list_url) + getArguments().getString(SELECTED_REGION));
    }


    @Override
    public void gotJsonSuccess(String json) {
        Gson gson = new Gson();
        TypeToken<List<CountryData>> token = new TypeToken<List<CountryData>>() {};
        countryData = gson.fromJson(json, token.getType());
        Collections.sort(countryData, new Comparator<CountryData>() {
            @Override public int compare(CountryData countryData1, CountryData countryData2) {
                return (int) (countryData2.getArea() - countryData1.getArea());
            }
        });
        ArrayList<String> ordersCountries = new ArrayList<>();
        for (CountryData countryData : this.countryData){
            ordersCountries.add(countryData.getName());
        }
        ListAdapter listAdapter = new ListAdapter(ordersCountries, this);
        countriesList.setAdapter(listAdapter);
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
        listener.onCountryPressed(countryData.get(position).getBorders(), countryData.get(position).getName(), countryData.get(position).getCapital(), countryData.get(position).getCurrencies());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.countries_list_close_btn:
                getActivity().onBackPressed();
                break;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (CountriesListFragmentListener) activity;
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
