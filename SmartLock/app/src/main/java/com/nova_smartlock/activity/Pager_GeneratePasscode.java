package com.nova_smartlock.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Pager_GeneratePasscode extends FragmentStatePagerAdapter {
    int tabcount;
    public Pager_GeneratePasscode(FragmentManager fm, int tabcount) {
        super(fm);
        this.tabcount=tabcount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
               Permanent_GeneratePasscodeFragment permanent_generatePasscodeFragment=new Permanent_GeneratePasscodeFragment();
               return  permanent_generatePasscodeFragment;
            case 1:
                Timed_GeneratedPasscodeFragment timed_generatedPasscodeFragment=new Timed_GeneratedPasscodeFragment();
                return timed_generatedPasscodeFragment;
            case 2:
                Onetime_GeneratePasscodeFragment onetime_generatePasscodeFragment=new Onetime_GeneratePasscodeFragment();
                return onetime_generatePasscodeFragment;

          /*  case 3:
                Erase_GeneatePasscodeFragment eraseFragment=new Erase_GeneatePasscodeFragment();
                return eraseFragment;*/

            case 3:
                Custom_GeneratePasscodeFragment custom_generatePasscodeFragment=new Custom_GeneratePasscodeFragment();
                return custom_generatePasscodeFragment;
           /* case 5:
                Cyclic_GeneatePasscodeFragment cyclicFragment=new Cyclic_GeneatePasscodeFragment();
                return cyclicFragment;*/
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
                return "Permanent";
            case 1:
                return "Timed";
            case 2:
                return "One-Time";
           /* case 3:
                return "Erase";*/
            case 3:
                return "Custom";
           /* case 5:
                return "Cyclic";*/
            default:
                return null;
        }
    }
}
