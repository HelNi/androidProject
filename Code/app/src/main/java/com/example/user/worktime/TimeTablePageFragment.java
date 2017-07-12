package com.example.user.worktime;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.user.worktime.Backend.BackendClient;
import com.example.user.worktime.Classes.DateHelpers;
import com.example.user.worktime.Classes.TimeTable.Activity;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;

import net.danlew.android.joda.DateUtils;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 05.07.2017.
 */

public class TimeTablePageFragment extends Fragment {
    private static final String TAG = "TimeTablePagerFragment";
    int position;
    LocalDate date;

    public TimeTablePageFragment() {
        this.position = 0;
        date = DateHelpers.dayNumToDate(0);
    }

    // TODO: Is this how you use arguments?
    @Override
    public void setArguments(Bundle args) {
        position = args.getInt("num", 0);
        date = DateHelpers.dayNumToDate(position);
        super.setArguments(args);
    }

    public static TimeTablePageFragment newInstance(int position) {
        TimeTablePageFragment fragment = new TimeTablePageFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("num", position);
        fragment.setArguments(bundle);

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //new Throwable("A").printStackTrace();
        Log.wtf(TAG, "onCreateView: " + position);
        View view = inflater.inflate(R.layout.time_table_fragment, container, false);
        TextView dateShow = (TextView) view.findViewById(R.id.page_number);
        dateShow.setText(DateUtils.formatDateTime(getContext(), date, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY));

        TextView weekNumberView = (TextView) view.findViewById(R.id.week_number);
        weekNumberView.setText(String.format(getString(R.string.weekNo), String.valueOf(date.getWeekOfWeekyear())));

        registerActivitiesCallback();
        populateTimeTableList();

        dateShow.setClickable(true);
        dateShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ((TimeTablePagerFragment) getParentFragment()).changeSelectedDate(new LocalDate(year, month, dayOfMonth));
                    }
                }, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
                datePickerDialog.show();
            }
        });

        return view;
    }

    private Call<List<TimeTableEntry>> getEntriesAsync() {
        LocalDateTime start = date.toLocalDateTime(LocalTime.MIDNIGHT);
        LocalDateTime end = date.toLocalDateTime(LocalTime.MIDNIGHT).plusDays(1);

        return BackendClient.getInstance().getTimeTableEntryService().getEntriesBetweenDates(start, end);
    }

    public void populateTimeTableList() {
        this.getEntriesAsync().enqueue(new Callback<List<TimeTableEntry>>() {
            @Override
            public void onResponse(Call<List<TimeTableEntry>> call, Response<List<TimeTableEntry>> response) {
                //
            }

            @Override
            public void onFailure(Call<List<TimeTableEntry>> call, Throwable t) {

            }
        });
    }

    // When activities arrive from the nework,
    private void registerActivitiesCallback() {
        TimeTablePagerFragment parentFragment = (TimeTablePagerFragment) this.getParentFragment();

        parentFragment.getActivitiesAsync().enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                List<Activity> body = response.body();
                // TODO: Do something with that list.
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG);
            }
        });
    }
}
