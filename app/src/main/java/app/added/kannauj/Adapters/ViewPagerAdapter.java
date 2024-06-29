package app.added.kannauj.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import app.added.kannauj.Fragments.FeedFragment;
import app.added.kannauj.Fragments.MenuFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        if (i==0) {
            fragment = new FeedFragment();
        } else {
            fragment = new MenuFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
