package com.teamup.teamsgenerator.MainScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felipe on 30/05/2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabTitles;

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);

        fragments = new ArrayList<Fragment>();
        tabTitles = new ArrayList<String>();
    }

    public void addFragment(Fragment fragment, String title){
        this.fragments.add(fragment);
        this.tabTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return this.tabTitles.get(position);
    }
}
