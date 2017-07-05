package com.example.user.worktime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by User on 05.07.2017.
 */

public class TimeTablePagerFragment extends Fragment {

    ViewPager mPager = null;
    PagerAdapter mPagerAdapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_table_pager_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPager = (ViewPager) view.findViewById(R.id.view_pager);
        mPager.setOffscreenPageLimit(3);
        mPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TimeTablePageFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return Integer.MAX_VALUE; // TODO
            }
        };
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(1500); // TODO: Calculate item for current week
    }
}
