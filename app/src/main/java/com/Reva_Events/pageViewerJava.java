package com.Reva_Events;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class pageViewerJava extends FragmentPagerAdapter {

    public pageViewerJava(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0)
        {

            return new ApproveFrag();
        } else if (position==1) {
            return new PendingFrag();
        } else {
            return new RejectFrag();

        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0)
        {
            return "Approve";
        } else if (position==1) {
            return "Pending";
        }
        else {
            return "Rejected";
        }
    }

}
