package com.trackme.trackmeapplication.home.userHome;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.trackme.trackmeapplication.request.individualRequest.IndividualMessageFragment;

/**
 * User page adapter handles the viewPager widget. It creates a specific new fragment when the
 * user skip to other views. It also handles the top TabMenu.
 *
 * @author Mattia Tibaldi
 */
public class UserPageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    /**
     * Constructor.
     *
     * @param fm fragment manager
     * @param numOfTabs number of item in the menu
     */
    UserPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    /**
     * Getter method for get the correct fragment.
     *
     * @param position the item chosen.
     * @return a new fragment if possible, otherwise null
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserHomeFragment();
            case 1:
                return new UserHistoryFragment();
            case 2:
                return new IndividualMessageFragment();
            default:
                return null;
        }
    }

    /**
     * Getter method, return the number of tabs.
     *
     * @return number of tabs.
     */
    @Override
    public int getCount() {
        return numOfTabs;
    }
}
