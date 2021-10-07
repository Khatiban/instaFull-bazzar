package com.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fragments.TabMyImagesStory;
import com.fragments.TabMyVideosStory;

public class ViewPagerStory extends FragmentStatePagerAdapter {
    int NumbOfTabs;
    CharSequence[] Titles;

    public ViewPagerStory(FragmentManager fm, CharSequence[] mTitles, int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return new TabMyImagesStory();
        }
        if (position == 1) {
            return new TabMyVideosStory();
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return this.Titles[position];
    }

    public int getCount() {
        return this.NumbOfTabs;
    }
}
