package com.nova_smartlock.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Pager extends FragmentStatePagerAdapter {
    int tabcount;
    public Pager(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount=tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TimedFragment timed_fragment=new TimedFragment();
                return timed_fragment;
            case 1:
             PermanentFragment permanentFragment=new PermanentFragment();
              return permanentFragment;
            case 2:
             One_timeFragment one_timeFragment=new One_timeFragment();
             return one_timeFragment;
            case 3:
               CyclicFragment cyclicFragment=new CyclicFragment();
               return cyclicFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return tabcount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);
        switch (position) {
            case 0:
                return "Timed";
            case 1:
                return "Permanent";
            case 2:
                return "One-Time";
            case 3:
                return "Cyclic";
            default:
                return null;
        }
    }
}
