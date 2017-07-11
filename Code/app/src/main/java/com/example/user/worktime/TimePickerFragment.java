package com.example.user.worktime;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by User on 11.07.2017.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), R.style.DialogTheme, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        return tpd;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Integer value = getArguments().getInt("viewId");
        TextView tv = null;
        String hour = String.valueOf(hourOfDay);
        String min = String.valueOf(minute);
        if (minute < 10) {
            min = "0".concat(String.valueOf(minute));
        }
        if (hourOfDay < 10) {
            hour = "0".concat(String.valueOf(hourOfDay));
        }

        if (value.equals(R.id.start_time)) {
            tv = (TextView) getActivity().findViewById(R.id.start_time_text_view);
            tv.setText(hour + ":" + min + " Uhr");
        } else if (value.equals(R.id.end_time)) {
            tv = (TextView) getActivity().findViewById(R.id.end_time_text_view);
            tv.setText(hour + ":" + min + " Uhr");
        } else {
            // TODO Fehlerbehandlung. Sollte allerdings nie auftreten.
        }
    }
}
