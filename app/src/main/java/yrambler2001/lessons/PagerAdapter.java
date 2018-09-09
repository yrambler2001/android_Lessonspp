package yrambler2001.lessons;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by Юра on 31.03.2017.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    PagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return 9;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        if (position == 0) return Teachers.newInstance();
        if (position == 8) return Settings.newInstance();
        return DayFragment.newInstance(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return MainActivity.intToDay(position + 1);
    }

}