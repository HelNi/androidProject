package com.example.user.worktime;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.worktime.Helpers.DateHelpers;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;
import com.example.user.worktime.Classes.User.User;
import com.example.user.worktime.Factory.IntentFactory;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by User on 05.07.2017.
 */

public class TimeTablePagerFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 5;
    ViewPager mPager = null;
    PagerAdapter mPagerAdapter = null;
    // Needed to fetch the currently active fragment from the pager.
    SparseArrayCompat<TimeTablePageFragment> mPagerFragments = new SparseArrayCompat<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_table_pager_fragment, container, false);
    }

    /**
     * @return The fragment the view pager is currently showing.
     */
    public TimeTablePageFragment getCurrentFragment() {
        return mPagerFragments.get(mPager.getCurrentItem());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPager = (ViewPager) view.findViewById(R.id.view_pager);
        mPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
                mPagerFragments.remove(position);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Object fragment = super.instantiateItem(container, position);
                mPagerFragments.put(position, (TimeTablePageFragment) fragment);
                return fragment;
            }

            @Override
            public Fragment getItem(int position) {
                return TimeTablePageFragment.newInstance(position, ((MainActivity) getActivity()).getUser());

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
        mPager.setOffscreenPageLimit(2);
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

        User user = ((MainActivity) getActivity()).getUser();
        TimeTablePageFragment currentFragment = getCurrentFragment();

        LocalTime suggestedStartTime = currentFragment.mSuggestedStartTime;
        LocalTime suggestedEndTime = suggestedStartTime.plusMinutes(15);

        TimeTableEntry entry = new TimeTableEntry(currentDate.toLocalDateTime(suggestedStartTime), currentDate.toLocalDateTime(suggestedEndTime), "", null, user);
        startActivityForResult(IntentFactory.createNewEntryCreationIntent(getContext(), entry, false), REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // This is called when an entry edit or entry creation finishes.
        // When the entry activity finishes, we must update our results.
        if (requestCode != REQUEST_CODE) {
            return;
        }

        // Re-populate the currently drawn fragments from the database.
        // not just the currently active one, As the others might have been affected.
        for(int i = 0; i < mPagerFragments.size(); i++) {
            TimeTablePageFragment timeTablePageFragment = mPagerFragments.valueAt(i);
            timeTablePageFragment.fetchTimeTableListFromServer();
        }

        //TimeTablePageFragment currentFragment = getCurrentFragment();
        //currentFragment.fetchTimeTableListFromServer(); // Re-populate.
    }
}
