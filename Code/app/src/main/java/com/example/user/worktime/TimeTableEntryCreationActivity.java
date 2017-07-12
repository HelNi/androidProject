package com.example.user.worktime;

import android.app.Activity;
import android.app.DialogFragment;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import com.example.user.worktime.Classes.TimeTable.Category;

public class TimeTableEntryCreationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

}

