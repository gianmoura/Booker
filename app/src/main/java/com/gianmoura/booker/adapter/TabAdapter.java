package com.gianmoura.booker.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<Integer> mFragmentIconsList = new ArrayList<>();

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, int iconId) {
        mFragmentList.add(fragment);
        mFragmentIconsList.add(iconId);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public int getDrawableId(int position) {
        return mFragmentIconsList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}