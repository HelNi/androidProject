package com.example.user.worktime;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.Fade;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import com.example.user.worktime.Classes.TimeTable.Category;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class TimeTableEntryCreationActivity extends Activity {
    TimeTableEntry mEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mEntry = new TimeTableEntry(new LocalDateTime(), new LocalDateTime(), "", null, null);

        setContentView(R.layout.activity_time_table_entry_creation);

        addItemsOnSpinner();

        LinearLayout focusableParent = (LinearLayout) findViewById(R.id.entry_creation_form);
        focusableParent.requestFocus();

        TextView dateTextView = (TextView) findViewById(R.id.date_textview);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        dateTextView.setText(sdf.format(new Date()).toString());
    }

    public void startTimePicker(View view) {
        DialogFragment fragment = new TimePickerFragment();

        Bundle args = new Bundle();
        args.putInt("viewId", view.getId());

        fragment.setArguments(args);

        fragment.setEnterTransition(new Fade());
        fragment.setExitTransition(new Fade());
        fragment.show(getFragmentManager(),"TimePicker");
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
            }
        }, 1992, 12,14); /*Dummy- Werte, mit Datum ersetzen*/
        datePickerDialog.show();
    }
}

