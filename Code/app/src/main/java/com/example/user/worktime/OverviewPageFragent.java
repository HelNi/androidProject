package com.example.user.worktime;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.worktime.Backend.BackendClient;
import com.example.user.worktime.Classes.TimeTable.Activity;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;
import com.example.user.worktime.Classes.User.User;
import com.example.user.worktime.Helpers.DateHelpers;
import com.example.user.worktime.Helpers.TimeTableEntryCollectionHelper;

import net.danlew.android.joda.DateUtils;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewPageFragent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewPageFragent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER = "user";
    private static final String ARG_WEEK_NO = "weekNo";

    // TODO: Rename and change types of parameters
    private User mUser;
    private LocalDate mWeek;

    public OverviewPageFragent() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user   Parameter 1.
     * @param weekNo Parameter 2.
     * @return A new instance of fragment OverviewPageFragent.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewPageFragent newInstance(User user, int weekNo) {
        OverviewPageFragent fragment = new OverviewPageFragent();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putInt(ARG_WEEK_NO, weekNo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_USER);
            mWeek = DateHelpers.wekkNumToDate(getArguments().getInt(ARG_WEEK_NO));
        }
    }

    public void renderTable(List<TimeTableEntry> entries) {
        View view = getView();

        //((TextView) view.findViewById(R.id.overview_total_hours)).setText(DateUtils.formatDuration(TimeTableEntryCollectionHelper.sumDuration(entries)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview_page_fragent, container, false);

        ((TextView) view.findViewById(R.id.overview_page_number)).setText(String.format(getString(R.string.weekNo), String.valueOf(mWeek.getWeekOfWeekyear())));
        ((TextView) view.findViewById(R.id.overview_year)).setText(DateUtils.formatDateTime(getContext(), mWeek, DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_NO_MONTH_DAY));

        BackendClient.getInstance().getTimeTableEntryService().getEntriesBetweenDates(mWeek.toLocalDateTime(LocalTime.MIDNIGHT), mWeek.plusWeeks(1).toLocalDateTime(LocalTime.MIDNIGHT).minusSeconds(1)).enqueue(new Callback<List<TimeTableEntry>>() {
            @Override
            public void onResponse(Call<List<TimeTableEntry>> call, Response<List<TimeTableEntry>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "FUGG", Toast.LENGTH_SHORT).show();
                }

                renderTable(response.body());
            }

            @Override
            public void onFailure(Call<List<TimeTableEntry>> call, Throwable t) {
                Toast.makeText(getContext(), "FUCK", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
