package com.example.basicapplicationfunction;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.basicapplicationfunction.Gallery.Gallery;

public class FragmentAdapter extends FragmentPagerAdapter {
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Fragment1.newInstance();
            case 1:
                return Gallery.newInstance();
            case 2:
                return Fragment3.newInstance();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }
}
