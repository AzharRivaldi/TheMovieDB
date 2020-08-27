package com.azhar.moviedb.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.azhar.moviedb.fragment.FragmentFavoriteMovie;
import com.azhar.moviedb.fragment.FragmentFavoriteTv;

public class ViewPageAdapter extends FragmentPagerAdapter {

    private final Fragment[] tabFragments;

    public ViewPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        tabFragments = new Fragment[]{
                new FragmentFavoriteMovie(),
                new FragmentFavoriteTv()
        };
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return tabFragments[position];
    }

    @Override
    public int getCount() {
        return tabFragments.length;
    }
}
