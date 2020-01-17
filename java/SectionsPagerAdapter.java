package com.example.weatherapp;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private String JSONString;
    private String cityPhotos;
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = TodayFragment.newInstance(JSONString);
                break;
            case 1:
                fragment = WeeklyFragment.newInstance(JSONString);
                break;
            case 2:
                fragment = PhotosFragment.newInstance(cityPhotos, context);
                break;
        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "TODAY";
            case 1:
                return "WEEKLY";
            case 2:
                return "PHOTOS";
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
    public void setJSONString (String s){
        this.JSONString = s;
    }
    public void setcityPhotos (String s){
        this.cityPhotos = s;
    }

}