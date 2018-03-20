package ipayuv1.source.cfts.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ipayuv1.source.cfts.ipayu_v1.Fragment_graph;
import ipayuv1.source.cfts.ipayu_v1.Fragment_map;
import ipayuv1.source.cfts.ipayu_v1.Fragment_report;

/**
 * Created by michael on 5/23/2015.
 */
public class TabsFragmentPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs;

    public TabsFragmentPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        if(index == 0)
            return new Fragment_map();
        if(index == 2)
            return new Fragment_graph();
        if(index == 1)
            return new Fragment_report();
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
