package com.example.user.worktime;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.worktime.Classes.User.User;
import com.example.user.worktime.Helpers.DateHelpers;

import org.joda.time.LocalDate;
import org.joda.time.Weeks;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER = "param1";

    // TODO: Rename and change types of parameters
    private User mUser;

    public OverviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OverviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(User user) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        ViewPager overviewPager = (ViewPager) view.findViewById(R.id.overview_pager);

        overviewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return OverviewPageFragent.newInstance(mUser, position);
            }

            @Override
            public int getCount() {
                // Workaround. INT_MAX lags the app.
                return 5000;
            }
        });

        overviewPager.setCurrentItem(DateHelpers.getWeekPage(LocalDate.now()));

        return view;
    }

    public void selectWeek(int weekOfWeekyear) {
        ViewPager overviewPager = (ViewPager) getView().findViewById(R.id.overview_pager);
        overviewPager.setCurrentItem(weekOfWeekyear);

    }
}
