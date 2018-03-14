package hisns.com.test.hisns;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by jeong on 2017-03-14.
 */


public class TabPagerAdapter extends FragmentStatePagerAdapter{


    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount=tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        // Returning the current tabs
        switch (position) {
            case 0:
                pp1 p1 = new pp1();
                return p1;
            case 1:
                pp2 p2 = new pp2();
                return p2;
            case 2:
                pp3 p3 = new pp3();
                return p3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
