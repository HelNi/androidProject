package com.example.user.worktime;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.worktime.Backend.BackendClient;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;
import com.example.user.worktime.Classes.User.User;
import com.example.user.worktime.Helpers.DateHelpers;
import com.example.user.worktime.Helpers.TimeTableEntryCollectionHelper;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.List;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewPageFragent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewPageFragent extends Fragment {
    private static final String ARG_USER = "user";
    private static final String ARG_WEEK_NO = "weekNo";
    private static final String ARG_ADMIN_USER = "admin";

    // TODO: Rename and change types of parameters
    private User mUser;
    private LocalDate mWeek;

    public OverviewPageFragent() {
        // Required empty public constructor
    }

    public static OverviewPageFragent newInstance(User user, int weekNo) {
        return newInstance(user, weekNo, null);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user   The user for which to
     * @param weekNo Parameter 2.
     * @return A new instance of fragment OverviewPageFragent.
     */
    // TODO: Rename and change types and number of parameters
    public static OverviewPageFragent newInstance(User user, int weekNo, @Nullable User adminUser) {
        OverviewPageFragent fragment = new OverviewPageFragent();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putInt(ARG_WEEK_NO, weekNo);
        if (adminUser != null) {
            args.putSerializable(ARG_ADMIN_USER, adminUser);
        }

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = (User) getArguments().getSerializable(ARG_USER);
            mWeek = DateHelpers.weekToDate(getArguments().getInt(ARG_WEEK_NO));
        }
    }

    public void renderTable(List<TimeTableEntry> entries) {
        View view = getView();

        ((TextView) view.findViewById(R.id.overview_total_hours)).setText(DateUtils.formatDuration(getContext(), TimeTableEntryCollectionHelper.sumDuration(entries)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview_page_fragent, container, false);

        TextView pageNumber = (TextView) view.findViewById(R.id.overview_page_number);
        pageNumber.setText(String.format(getString(R.string.weekNo), String.valueOf(mWeek.getWeekOfWeekyear())));
        pageNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // There's no "week picker" or anything...
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        LocalDate date = new LocalDate(i, i1 + 1, i2).withDayOfWeek(DateTimeConstants.MONDAY);
                        Toast.makeText(getContext(), DateUtils.formatDateTime(getContext(), date, DateUtils.FORMAT_ABBREV_ALL), Toast.LENGTH_LONG).show();
                        OverviewFragment parentFragment = (OverviewFragment) getParentFragment();
                        parentFragment.selectWeek(DateHelpers.getWeekPage(date));
                    }
                }, mWeek.getYear(), mWeek.getMonthOfYear() - 1, mWeek.getDayOfMonth());

                datePickerDialog.show();
            }
        });

        ((TextView) view.findViewById(R.id.overview_year)).setText(DateUtils.formatDateRange(getContext(), mWeek, mWeek.plusDays(6), DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL));


        BackendClient.getInstance().getTimeTableEntryService().getEntriesBetweenDates(mWeek.toLocalDateTime(LocalTime.MIDNIGHT), mWeek.plusWeeks(1).toLocalDateTime(LocalTime.MIDNIGHT).minusSeconds(1)).enqueue(new Callback<List<TimeTableEntry>>() {
            @Override
            public void onResponse(Call<List<TimeTableEntry>> call, Response<List<TimeTableEntry>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "FUGG", Toast.LENGTH_SHORT).show();
                }

                HttpUrl url = call.request().url();

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
