package com.example.user.worktime;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by User on 05.07.2017.
 */

public class TimeTablePageFragment extends Fragment {
    int position;

    public TimeTablePageFragment() {
        this.position = 0;
    }

    // TODO: Is this how you use arguments?
    @Override
    public void setArguments(Bundle args) {
        position = args.getInt("num", 0);
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
        View view = inflater.inflate(R.layout.time_table_fragment, container, false);
        ((TextView) view.findViewById(R.id.page_number)).setText("Woche Nummer " + position);

        return view;
    }
}
