package com.example.user.worktime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import com.example.user.worktime.Classes.TimeTable.Category;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;

import net.danlew.android.joda.DateUtils;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class TimeTableEntryCreationActivity extends AppCompatActivity {
    TimeTableEntry mEntry; // The entry we edit or create.
    boolean mIsEdit; // Whether we edit or create a new entry

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mEntry = (TimeTableEntry) getIntent().getSerializableExtra("entry");
        mIsEdit = getIntent().getBooleanExtra("isEdit", false);

        setContentView(R.layout.activity_time_table_entry_creation);

        addItemsOnSpinner();

        //Set the focus to the parent-LinearLayout to not focus the EditText at startup.
        LinearLayout focusableParent = (LinearLayout) findViewById(R.id.entry_creation_form);
        focusableParent.requestFocus();

        //Set the currently chosen date.


        //Add back button to the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateTextViews();
    }

    /**
     * Updates the values in the text views according to what is in our current entry.
     */
    public void updateTextViews() {
        // Set the inputs according to the initial input from the entry.
        TextView dateTextView = (TextView) findViewById(R.id.date_textview);
        dateTextView.setText(DateUtils.formatDateTime(getApplicationContext(), mEntry.getStart(), DateUtils.FORMAT_SHOW_DATE));

        TextView startView = (TextView) findViewById(R.id.start_time_text_view);
        TextView endView = (TextView) findViewById(R.id.end_time_text_view);

        startView.setText(DateUtils.formatDateTime(getApplicationContext(), mEntry.getStart(), DateUtils.FORMAT_SHOW_TIME));
        endView.setText(DateUtils.formatDateTime(getApplicationContext(), mEntry.getEnd(), DateUtils.FORMAT_SHOW_TIME));

        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(mEntry.getDescription());
    }

    //Needed to add the 'back'-button to the ActionBar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void startTimePicker(View view) {
        final boolean isStart = view.getId() == R.id.start_time;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (isStart) {
                    mEntry.setStart(mEntry.getStart().withTime(hourOfDay, minute, 0, 0));
                } else {
                    mEntry.setEnd(mEntry.getEnd().withTime(hourOfDay, minute, 0, 0));
                }

                updateTextViews();
            }
        },
                isStart ? mEntry.getStart().getHourOfDay() : mEntry.getEnd().getHourOfDay(),
                isStart ? mEntry.getStart().getMinuteOfHour() : mEntry.getEnd().getMinuteOfHour(), true);// TODO: Aus entry

        timePickerDialog.show();
    }

    // Add all values of the Activity enum into the spinner
    private void addItemsOnSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        EnumSet<Category> categories = EnumSet.allOf(Category.class);

        for (Category category : categories) {
            list.add(category.toString());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void startDateTimePicker(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                LocalDateTime start = mEntry.getStart();
                LocalDateTime end = mEntry.getEnd();

                mEntry.setStart(start.withDate(year, month + 1, dayOfMonth));
                mEntry.setEnd(end.withDate(year, month + 1, dayOfMonth));
                updateTextViews();


            }
        }, mEntry.getStart().getYear(), mEntry.getStart().getMonthOfYear(), mEntry.getStart().getDayOfMonth());
        datePickerDialog.show();

    }
}

