package ipayuv1.source.cfts.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ipayuv1.source.cfts.ipayu_v1.Fragment_chat;
import ipayuv1.source.cfts.ipayu_v1.Fragment_emarket;
import ipayuv1.source.cfts.ipayu_v1.Fragment_eshop;
import ipayuv1.source.cfts.ipayu_v1.Fragment_itaxi;
import ipayuv1.source.cfts.ipayu_v1.Fragment_wallet;

/**
 * Created by michael on 5/23/2015.
 */
public class HomeFragmentAdapter extends FragmentStatePagerAdapter{

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
        int NumbOfTabs;

    public HomeFragmentAdapter(FragmentManager fm, CharSequence Titles[], int mNumbOfTabsumb) {
            super(fm);
            this.Titles = Titles;
            this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int index) {
            // TODO Auto-generated method stub
            if(index == 0)
                return new Fragment_wallet();
            if(index == 1)
                return new Fragment_chat();
            if(index == 2)
                return new Fragment_eshop();
            if(index == 3)
                return new Fragment_emarket();
            if(index == 4)
                return new Fragment_itaxi();
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