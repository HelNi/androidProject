package com.example.user.worktime;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.example.user.worktime.R;

import static android.Manifest.permission.READ_CONTACTS;
import static android.R.attr.value;

public class TimeTableEntryCreationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_entry_creation);

        addItemsOnSpinner();
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
        EnumSet<com.example.user.worktime.Classes.TimeTable.Activity> activities = EnumSet.allOf(com.example.user.worktime.Classes.TimeTable.Activity.class);

        for (com.example.user.worktime.Classes.TimeTable.Activity act : activities) {
            list.add(act.toString());
        }
        
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

}

