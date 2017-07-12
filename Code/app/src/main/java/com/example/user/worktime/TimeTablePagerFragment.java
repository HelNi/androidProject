package com.example.user.worktime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.worktime.Backend.BackendClient;
import com.example.user.worktime.Classes.DateHelpers;
import com.example.user.worktime.Classes.TimeTable.Activity;

import net.danlew.android.joda.DateUtils;

import org.joda.time.LocalDate;

import java.util.List;

import retrofit2.Call;

/**
 * Created by User on 05.07.2017.
 */

public class TimeTablePagerFragment extends Fragment implements View.OnClickListener {

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
        mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TimeTablePageFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                // FIXME: This is a workaround. It should be virtually infinite,
                // But when it's set the the max integer, the UI freezes for 10 seconds
                // On back-navigation. This is a bad compromise.
                // We need to find a real solution.
                return 50000; // TODO
            }
        };
        mPager.setOffscreenPageLimit(1);
        mPager.setAdapter(mPagerAdapter);
        changeSelectedDate(new LocalDate());
    }

    public void changeSelectedDate(LocalDate date) {
        int dayNum = DateHelpers.getDayPage(date);
        mPager.setCurrentItem(dayNum);
    }

    @Override
    public void onClick(View v) {
        int currentDay = mPager.getCurrentItem();
        LocalDate currentDate = new LocalDate(DateHelpers.dayNumToDate(currentDay));

        String dateString = DateUtils.formatDateTime(getActivity(), currentDate, DateUtils.FORMAT_SHOW_DATE);
        Snackbar.make(getView(), dateString, Snackbar.LENGTH_SHORT).show();
    }

    public Call<List<Activity>> getActivitiesAsync() {
        // Asynchronously fetch activities. Sub-Views can register callbacks.
        return BackendClient.getInstance().getActivityService().getActivities();
    }
}
