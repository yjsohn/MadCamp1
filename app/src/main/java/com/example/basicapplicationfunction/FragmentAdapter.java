package com.example.basicapplicationfunction;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.basicapplicationfunction.Gallery.Gallery;
import com.example.basicapplicationfunction.Temp.AlarmsListFragment;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }
    Fragment1 fragment1 = Fragment1.newInstance();
    Gallery gallery = Gallery.newInstance();
    Fragment3 fragment3 = Fragment3.newInstance();

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return fragment1;
            case 1:
                return gallery;
            case 2:
                return fragment3;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }

    public Fragment1 getFragment1(){
        return fragment1;
    }
}