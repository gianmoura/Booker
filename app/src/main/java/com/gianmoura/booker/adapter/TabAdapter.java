package com.gianmoura.booker.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gianmoura.booker.R;
import com.gianmoura.booker.fragment.CollectionFragment;
import com.gianmoura.booker.fragment.FilterFragment;
import com.gianmoura.booker.fragment.NegocationsFragment;
import com.gianmoura.booker.fragment.PreferencesFragment;
import com.gianmoura.booker.fragment.ProfileFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private int[] icons = {
            R.drawable.ic_action_book,
            R.drawable.ic_library_books,
            R.drawable.ic_message,
            R.drawable.ic_action_settings,
            R.drawable.ic_action_user};

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new FilterFragment();
                break;
            case 1:
                fragment = new CollectionFragment();
                break;
            case 2:
                fragment = new NegocationsFragment();
                break;
            case 3:
                fragment = new PreferencesFragment();
                break;
            case 4:
                fragment = new ProfileFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return icons.length;
    }

    public int getDrawableId(int position) {
        return icons[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
