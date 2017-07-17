package com.example.user.worktime;

import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.example.user.worktime.Backend.BackendClient;
import com.example.user.worktime.Backend.Services.TimeTableEntryService;
import com.example.user.worktime.Classes.TimeTable.Activity;
import com.example.user.worktime.Classes.TimeTable.Category;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;
import com.example.user.worktime.Helpers.ActivityFetcher;

import net.danlew.android.joda.DateUtils;

import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeTableEntryCreationActivity extends AppCompatActivity {
    TimeTableEntry mEntry; // The entry we edit or create.
    boolean mIsEdit; // Whether we edit or create a new entry

    boolean endDurationFlag = true; // Helper to prevent an infinite loop

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mEntry = (TimeTableEntry) getIntent().getSerializableExtra("entry");

        mIsEdit = getIntent().getBooleanExtra("isEdit", false);

        setContentView(R.layout.activity_time_table_entry_creation);

        addItemsOnSpinner();
        handleInputs();

        //Set the focus to the parent-LinearLayout to not focus the EditText at startup.
        LinearLayout focusableParent = (LinearLayout) findViewById(R.id.entry_creation_form);
        focusableParent.requestFocus();

        //Add back button to the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateTextViews();

        if (mIsEdit) {
            setTitle(String.format(getString(R.string.entry_edit_title),
                    DateUtils.formatDateTime(this, mEntry.getStart(),
                            DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY)));
        }
    }

    private boolean enableSendButtonIfPossible() {
        // Validate input fields, if any ix invalid or blank we can't send the entity.

        final FloatingActionButton sendButton = (FloatingActionButton) findViewById(R.id.entry_form_create_button);
        final EditText dateEditText = (EditText) findViewById(R.id.date_text_edit);
        final EditText startTimeEdit = (EditText) findViewById(R.id.start_time_text_edit);
        final EditText endTimeEdit = (EditText) findViewById(R.id.end_time_text_edit);
        EditText descriptionEditor = (EditText) findViewById(R.id.description);

        if ((descriptionEditor.length() == 0 || descriptionEditor.getError() != null)
                || (dateEditText.length() == 0 || dateEditText.getError() != null)
                || (startTimeEdit.length() == 0 || startTimeEdit.getError() != null)
                || (endTimeEdit.length() == 0 || endTimeEdit.getError() != null)) {
            sendButton.setEnabled(false);
            sendButton.hide();
            return false;
        }

        // Check if start date is after end date - if it is, that's invalid, too.
        if (mEntry.getStart().isAfter(mEntry.getEnd())) {
            endTimeEdit.setError(getString(R.string.entry_error_end_before_start));
            sendButton.setEnabled(false);
            sendButton.hide();
            return false;
        } else {
            endTimeEdit.setError(null);
        }

        sendButton.setEnabled(true);
        sendButton.show();
        return true;
    }

    /**
     * Handles user changes to input fields, parse their values and writes it to our entry.
     */
    private void handleInputs() {
        final EditText startTimeEdit = (EditText) findViewById(R.id.start_time_text_edit);
        final EditText endTimeEdit = (EditText) findViewById(R.id.end_time_text_edit);

        startTimeEdit.addTextChangedListener(new TimeTextWatcher(startTimeEdit));
        endTimeEdit.addTextChangedListener(new TimeTextWatcher(endTimeEdit));

        final EditText dateEditText = (EditText) findViewById(R.id.date_text_edit);

        // Change entry date
        dateEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();
                // specify some fallback formats, too.

                DateTimeParser[] parsers = {
                        DateTimeFormat.forPattern("dd.MM.yyyy").getParser(),
                        DateTimeFormat.forPattern("yyyy-MM-dd").getParser()};

                try {
                    LocalDate parsedDate = LocalDate.parse(s.toString(), dateTimeFormatterBuilder.append(null, parsers).toFormatter());

                    mEntry.setStart(mEntry.getStart().withDate(parsedDate.getYear(), parsedDate.getMonthOfYear(), parsedDate.getDayOfMonth()));
                    mEntry.setEnd(mEntry.getEnd().withDate(parsedDate.getYear(), parsedDate.getMonthOfYear(), parsedDate.getDayOfMonth()));
                    dateEditText.setError(null);
                } catch (IllegalArgumentException e) {
                    dateEditText.setError("Bitte ein Datum im Format Tag.Monat.Jahr eingeben. Alternativ linken Button klicken.");
                } finally {
                    enableSendButtonIfPossible();
                }
            }
        });

        // change entry time (start and end)
        final EditText descriptionEditor = (EditText) findViewById(R.id.description);
        descriptionEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 3) {
                    descriptionEditor.setError("Bitte Mindestens 3 Buchstaben eingeben");
                } else {
                    descriptionEditor.setError(null);
                }
                mEntry.setDescription(s.toString());
                enableSendButtonIfPossible();
            }
        });

        // change to duration (sets end to start + input duration

        final EditText durationEdit = (EditText) findViewById(R.id.entry_duration_text_edit);
        durationEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Parse duration as a time for now.

                try {
                    LocalTime parsedTime = LocalTime.parse(s.toString(), DateTimeFormat.forPattern("HH:mm"));
                    // Need to check if it already is that value to prevent an infinite loop.
                    String endTimeText = DateUtils.formatDateTime(TimeTableEntryCreationActivity.this, mEntry.getStart().plus(Duration.millis(parsedTime.getMillisOfDay())), DateUtils.FORMAT_SHOW_TIME);
                    if (endDurationFlag && !endTimeEdit.getText().toString().equals(endTimeText)) {
                        endDurationFlag = false;
                        endTimeEdit.setText(endTimeText);
                    }
                    durationEdit.setError(null);
                } catch (IllegalArgumentException e) {
                    durationEdit.setError("Bitte eine Dauer im Format Stunden:Minuten eingeben");
                }
            }
        });

        // Oh boy, I sure wonder what the "add 15 minutes button" does...
        findViewById(R.id.entry_add_15_minutes_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEntry.setEnd(mEntry.getEnd().plusMinutes(15));
                updateTextViews();
            }
        });
    }

    /**
     * Updates the values in the text views according to what is in our current entry.
     */
    public void updateTextViews() {
        // Set the inputs according to the initial input from the entry.
        //TextView dateTextView = (TextView) findViewById(R.id.date_textview);
        //dateTextView.setText(DateUtils.formatDateTime(getApplicationContext(), mEntry.getStart(), DateUtils.FORMAT_SHOW_DATE));

        EditText dateEdit = (EditText) findViewById(R.id.date_text_edit);
        dateEdit.setText(DateUtils.formatDateTime(this, mEntry.getStart(), DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_ALL));

        EditText startTimeEdit = (EditText) findViewById(R.id.start_time_text_edit);
        startTimeEdit.setText(DateUtils.formatDateTime(this, mEntry.getStart(), DateUtils.FORMAT_SHOW_TIME));

        EditText endTimeEdit = (EditText) findViewById(R.id.end_time_text_edit);
        endTimeEdit.setText(DateUtils.formatDateTime(this, mEntry.getEnd(), DateUtils.FORMAT_SHOW_TIME));


        TextView descriptionView = (TextView) findViewById(R.id.description);
        descriptionView.setText(mEntry.getDescription());


        enableSendButtonIfPossible();
    }

    private void updateDuration() {
        EditText durationInput = (EditText) findViewById(R.id.entry_duration_text_edit);
        Duration duration = mEntry.getDuration();

        String durationText = String.format(getString(R.string.duration_format), duration.getStandardHours(), duration.getStandardMinutes() % 60);
        if (endDurationFlag && !durationInput.getText().toString().equals(durationText)) {
            endDurationFlag = false;
            durationInput.setText(durationText);
        }
        endDurationFlag = true;
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
                isStart ? mEntry.getStart().getMinuteOfHour() : mEntry.getEnd().getMinuteOfHour(), false);

        timePickerDialog.show();
    }

    // Add all values of the Activity enum into the spinner
    private void addItemsOnSpinner() {
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final List<Activity> activities = new ArrayList<>();

        final ArrayAdapter<Activity> activityAdapter = new ArrayAdapter<Activity>(this, android.R.layout.simple_spinner_item, activities);

        ActivityFetcher.getActivitiesAsync().enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                if (!response.isSuccessful()) {
                    // TODO: error message
                    return;
                }
                activities.addAll(response.body());
                activityAdapter.notifyDataSetChanged();

                spinner.setSelection(activityAdapter.getPosition(mEntry.getActivity()));
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                // TODO: Error message
            }
        });

        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(activityAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mEntry.setActivity((Activity) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mEntry.setActivity(null);
            }
        });
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

    /**
     * Depending on whether the title is being edited or newly created, send it to the backend-API.
     *
     * @param view
     */
    public void persistEntry(View view) {
        // TODO: Show progress

        TimeTableEntryService timeTableEntryService = BackendClient.getInstance().getTimeTableEntryService();
        Call<Void> call;
        if (mIsEdit) {
            call = timeTableEntryService.updateEntry(mEntry.getId(), mEntry);
        } else {
            call = timeTableEntryService.createEntry(mEntry);
        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    try {
                        String errorString = response.errorBody().string();
                        Snackbar.make(findViewById(R.id.activity_coordinator_layout), errorString, Snackbar.LENGTH_INDEFINITE).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // TODO:STOP SPINNER
                // Alsoproper handling
                Snackbar.make(findViewById(R.id.activity_coordinator_layout), t.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private class TimeTextWatcher implements TextWatcher {
        private final EditText textEdit;

        public TimeTextWatcher(EditText edit) {
            this.textEdit = edit;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputString = s.toString();
            try {
                LocalTime parsedTime = LocalTime.parse(inputString, DateTimeFormat.forPattern("HH:mm"));
                if (textEdit.getId() == R.id.start_time_text_edit) {
                    mEntry.setStart(mEntry.getStart().withTime(parsedTime.getHourOfDay(), parsedTime.getMinuteOfHour(), 0, 0));
                } else {
                    mEntry.setEnd(mEntry.getEnd().withTime(parsedTime.getHourOfDay(), parsedTime.getMinuteOfHour(), 0, 0));
                }
                textEdit.setError(null);
                updateDuration();
            } catch (IllegalArgumentException e) {
                textEdit.setError("Bitte eine Uhrzeit im Format HH:mm Eingeben. Alternativ linken Button klicken.");
            } finally {
                enableSendButtonIfPossible();
            }
        }
    }
}

