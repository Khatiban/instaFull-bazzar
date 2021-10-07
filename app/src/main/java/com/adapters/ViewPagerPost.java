package com.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fragments.TabMyImagesPost;
import com.fragments.TabMyVideosPost;

public class ViewPagerPost extends FragmentStatePagerAdapter {
    int NumbOfTabs;
    CharSequence[] Titles;

    public ViewPagerPost(FragmentManager fm, CharSequence[] mTitles, int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    public Fragment getItem(int position) {
        if (position == 0) {
            return new TabMyImagesPost();
        }
        if (position == 1) {
            return new TabMyVideosPost();
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
