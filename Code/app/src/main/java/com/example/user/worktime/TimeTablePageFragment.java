package com.example.user.worktime;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
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
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.user.worktime.TimeTablePageFragment.TimeTableEntryAdapter.*;

/**
 * Created by User on 05.07.2017.
 */

public class TimeTablePageFragment extends Fragment {
    private static final String TAG = "TimeTablePagerFragment";
    int position;
    LocalDate date;

    List<Activity> mActivities = new ArrayList<>();
    List<TimeTableEntry> mTimeTableEntries = new ArrayList<>();
    TimeTableEntryAdapter mAdapter;

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

    public void updateList() {
        mAdapter.notifyDataSetChanged();
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

        prepareCards(view);

        TextView weekNumberView = (TextView) view.findViewById(R.id.week_number);
        weekNumberView.setText(String.format(getString(R.string.weekNo), String.valueOf(date.getWeekOfWeekyear())));

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

    private void prepareCards(View view) {
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.time_table_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TimeTableEntryAdapter(mTimeTableEntries);
        recycler.setAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        registerActivitiesCallback();
        populateTimeTableList();
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
                List<TimeTableEntry> body = response.body();

                mTimeTableEntries.clear();
                mTimeTableEntries.addAll(body);
                updateList();
            }

            @Override
            public void onFailure(Call<List<TimeTableEntry>> call, Throwable t) {
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).show();

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

    protected class TimeTableEntryAdapter extends RecyclerView.Adapter<TimeTableEntryAdapter.EntryViewHolder> {
        List<TimeTableEntry> entries;

        public List<TimeTableEntry> getEntries() {
            return entries;
        }

        public void setEntries(List<TimeTableEntry> entries) {
            this.entries = entries;
        }

        public TimeTableEntryAdapter(List<TimeTableEntry> entries) {
            this.entries = entries;
        }

        @Override
        public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_entry_card, parent, false);
            return new EntryViewHolder(v);
        }

        @Override
        public void onBindViewHolder(EntryViewHolder holder, int position) {
            TimeTableEntry entry = entries.get(position);
            holder.start.setText(DateUtils.formatDateTime(getContext(), entry.getStart(), DateUtils.FORMAT_SHOW_TIME));
            holder.end.setText(DateUtils.formatDateTime(getContext(), entry.getEnd(), DateUtils.FORMAT_SHOW_TIME));
            holder.description.setText(entry.getDescription());
            // ACTIVITY TODO
            // BUTTONS TODO (setOnClickListener)
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        protected class EntryViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView start;
            TextView end;
            TextView description;
            TextView activity;

            public EntryViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                start = (TextView) itemView.findViewById(R.id.entry_start_time);
                end = (TextView) itemView.findViewById(R.id.entry_end_time);
                activity = (TextView) itemView.findViewById(R.id.project_name);
                description = (TextView) itemView.findViewById(R.id.entry_description);
            }
        }
    }
}
