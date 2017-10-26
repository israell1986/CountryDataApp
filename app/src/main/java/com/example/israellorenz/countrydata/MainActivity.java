package com.example.israellorenz.countrydata;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.israellorenz.countrydata.Data.CountryData;
import com.example.israellorenz.countryData.R;

import java.util.ArrayList;
import java.util.List;


// this activity is App activity and take care for all fragment in app:
// main fragment - "RegionListFragment" which present user all regions
// "CountriesListFragment" - which present all countries for selected region
// "CountryDataFragment" - which preset specific data for selected country
// Source for app base on website: https://www.restcountries.eu

public class MainActivity extends AppCompatActivity implements RegionListFragment.RegionListFragmentListener, CountriesListFragment.CountriesListFragmentListener {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegionListFragment regionListFragment = RegionListFragment.newInstance();
        fragmentManager = getFragmentManager();
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(R.id.content_frame, regionListFragment, regionListFragment.getClass().getName());
        beginTransaction.commit();
    }

    @Override
    public void onRegionPressed(String selectedRegion) {
        CountriesListFragment countriesListFragment = CountriesListFragment.newInstance(selectedRegion);
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(R.id.content_frame, countriesListFragment, countriesListFragment.getClass().getName());
        beginTransaction.addToBackStack(countriesListFragment.getClass().getName());
        beginTransaction.commit();
    }

    @Override
    public void onCountryPressed(List<String> borders, String selectedCountry, String capital, List<CountryData.CurrenciesEntity> currencies) {
        CountryDataFragment countryDataFragment = CountryDataFragment.newInstance(new ArrayList<>(borders), selectedCountry, capital, new ArrayList<>(currencies));
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(R.id.content_frame, countryDataFragment, countryDataFragment.getClass().getName());
        beginTransaction.addToBackStack(countryDataFragment.getClass().getName());
        beginTransaction.commit();

    }
}
