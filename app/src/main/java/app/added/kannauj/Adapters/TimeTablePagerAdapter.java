package app.added.kannauj.Adapters;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import app.added.kannauj.Fragments.TimeTableFragment;
import app.added.kannauj.Models.TimeTableModel;

public class TimeTablePagerAdapter extends FragmentStatePagerAdapter {

    List<List<TimeTableModel>> list;
    private static final String TAG = "TimeTablePagerAdapter";

    public TimeTablePagerAdapter(FragmentManager fm, List<List<TimeTableModel>> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        Bundle args = new Bundle();
        List<TimeTableModel> tempList = list.get(i);
        args.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) tempList);
        TimeTableFragment fragment = new TimeTableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "MONDAY";
            case 1:
                return "TUESDAY";
            case 2:
                return "WEDNESDAY";
            case 3:
                return "THURSDAY";
            case 4:
                return "FRIDAY";
            case 5:
                return "SATURDAY";
        }
        return "MONDAY";
    }
}
