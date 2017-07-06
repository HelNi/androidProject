package com.example.user.worktime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.user.worktime.Classes.DateHelpers;

import net.danlew.android.joda.DateUtils;

import org.joda.time.LocalDate;

/**
 * Created by User on 05.07.2017.
 */

public class TimeTablePageFragment extends Fragment {
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

    private void getTimeTable() {
        // TODO: Get a proper one from the backend-API... when it exists.

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_table_fragment, container, false);
        TextView dateShow = (TextView) view.findViewById(R.id.page_number);
        dateShow.setText(DateUtils.formatDateTime(getContext(), date, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_ABBREV_WEEKDAY |  DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY));

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
}
