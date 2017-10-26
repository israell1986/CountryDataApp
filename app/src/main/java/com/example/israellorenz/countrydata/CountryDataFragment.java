package com.example.israellorenz.countrydata;

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

import java.util.ArrayList;

/**
 * Created by israel.lorenz on 26/10/2017.
 */

public class CountryDataFragment extends Fragment implements View.OnClickListener {

    private static final String ARGS_BORDERS = "argsBorders";
    private static final String SELECTED_COUNTRY = "selectedCountry";
    private static final String CAPITAL = "capital";
    private static final String CURRENCIES = "currencies";

    private RecyclerView bordersList;

    private String capitalText;
    private ArrayList<CountryData.CurrenciesEntity> currencies;
    private ArrayList<String> borders;
    private String selectedCountry;

    public static CountryDataFragment newInstance(ArrayList<String> borders, String selectedCountry, String capital, ArrayList<CountryData.CurrenciesEntity> currencies){
        CountryDataFragment fragment = new CountryDataFragment();
        Bundle args = new Bundle();
        args.putString(CAPITAL, capital);
        args.putParcelableArrayList(CURRENCIES, currencies);
        args.putString(SELECTED_COUNTRY, selectedCountry);
        args.putStringArrayList(ARGS_BORDERS, borders);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null){
            capitalText = args.getString(CAPITAL);
            currencies = args.getParcelableArrayList(CURRENCIES);
            borders = args.getStringArrayList(ARGS_BORDERS);
            selectedCountry = args.getString(SELECTED_COUNTRY);
        } else {
            borders = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.country_data_layout, container, false);
        TextView title = (TextView) view.findViewById(R.id.country_data_title);
        TextView capital = (TextView) view.findViewById(R.id.country_data_capital);
        TextView currencyTitle = (TextView) view.findViewById(R.id.country_data_currencies_title);
        TextView currency = (TextView) view.findViewById(R.id.country_data_currencies);
        TextView bordersEmptyMsg = (TextView) view.findViewById(R.id.borders_list_empty_msg);
        TextView bordersListTitle = (TextView) view.findViewById(R.id.borders_list_title);
        TextView closetButton = (TextView) view.findViewById(R.id.borders_list_close_btn);
        LinearLayout hasBordersLayout = (LinearLayout) view.findViewById(R.id.borders_list_layout);
        LinearLayout noBordersLayout = (LinearLayout) view.findViewById(R.id.borders_empty_list_layout);
        bordersList = (RecyclerView) view.findViewById(R.id.borders_list_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        bordersList.setLayoutManager(mLayoutManager);
        bordersList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        title.setText(getString(R.string.country_data_title, selectedCountry));
        capital.setText(capitalText);
        currencyTitle.setText(getString(currencies.size() == 1 ? R.string.capital_currencies_single : R.string.capital_currencies_multiple));
        StringBuilder allCurrencies = new StringBuilder();
        for (CountryData.CurrenciesEntity currentCurrency : currencies){
            if (currentCurrency != null && currentCurrency.getName() != null) {
                if (allCurrencies.length() != 0) {
                    allCurrencies.append(", ");
                }
                allCurrencies.append(currentCurrency.getName());
            }
        }


        currency.setText(allCurrencies);
        closetButton.setOnClickListener(this);
        if (borders.size() == 0){
            bordersEmptyMsg.setText(getString(R.string.no_borders_msg, selectedCountry));
            hasBordersLayout.setVisibility(View.GONE);
        } else {
            bordersListTitle.setText(getString(R.string.borders_list_title, selectedCountry));
            ListAdapter bordersListAdapter = new ListAdapter(borders, null);
            bordersList.setAdapter(bordersListAdapter);
            noBordersLayout.setVisibility(View.GONE);
        }
        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.borders_list_close_btn:
                getActivity().onBackPressed();
                break;
        }
    }
}
