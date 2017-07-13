package com.example.user.worktime;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.user.worktime.Backend.BackendClient;
import com.example.user.worktime.Classes.DateHelpers;
import com.example.user.worktime.Classes.TimeTable.Activity;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;
import com.example.user.worktime.Classes.User.User;
import com.example.user.worktime.Factory.IntentFactory;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTimeUtils;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Minutes;

import java.util.ArrayList;
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
                        ((TimeTablePagerFragment) getParentFragment()).changeSelectedDate(new LocalDate(year, month + 1, dayOfMonth));
                    }
                }, date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
                datePickerDialog.setCancelable(true);
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
        fetchTimeTableListFromServer();
    }

    private Call<List<TimeTableEntry>> getEntriesAsync() {
        LocalDateTime start = date.toLocalDateTime(LocalTime.MIDNIGHT);
        LocalDateTime end = date.toLocalDateTime(LocalTime.MIDNIGHT).plusDays(1);

        return BackendClient.getInstance().getTimeTableEntryService().getEntriesBetweenDates(start, end);
    }

    public void fetchTimeTableListFromServer() {
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
            final TimeTableEntry entry = entries.get(position);
            holder.description.setText(entry.getDescription());
            holder.interval.setText(DateUtils.formatDateRange(getContext(), entry.getStart(), entry.getEnd(), DateUtils.FORMAT_SHOW_TIME));
            //holder.duration.setText(DateUtils.formatDuration(getContext(), entry.getDuration()));
            Minutes minutes = Minutes.minutesBetween(entry.getStart(), entry.getEnd());
            holder.duration.setText(String.format(getString(R.string.date_duration),
                    Hours.hoursBetween(entry.getStart(),
                            entry.getEnd()).getHours(), minutes.getMinutes() % 60));
            // ACTIVITY TODO
            // BUTTONS TODO (setOnClickListener)

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = entry.getUser();
                    Intent i = IntentFactory.createNewEntryCreationIntent(getContext(), entry, true);
                    getActivity().startActivityFromFragment(TimeTablePageFragment.this, i, 0);
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Eintrag löschen")
                            .setCancelable(true)
                            .setNegativeButton("Nicht löschen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BackendClient.getInstance().getTimeTableEntryService().deleteEntry(entry.getId());
                                    fetchTimeTableListFromServer();
                                }
                            })
                            .setIcon(R.drawable.ic_delete_forever_black_24dp)
                            .setMessage(String.format("Eintrag von %1$s bis %2$s wirklich löschen?",
                                    DateUtils.formatDateTime(getContext(), entry.getStart(), DateUtils.FORMAT_SHOW_TIME),
                                    DateUtils.formatDateTime(getContext(), entry.getStart(), DateUtils.FORMAT_SHOW_TIME)));

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }

        @Override
        public int getItemCount() {
            return entries.size();
        }

        protected class EntryViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView interval;
            TextView duration;
            TextView description;
            TextView activity;

            Button editButton;
            Button deleteButton;

            public EntryViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                interval = (TextView) itemView.findViewById(R.id.entry_interval);
                duration = (TextView) itemView.findViewById(R.id.entry_duration);
                activity = (TextView) itemView.findViewById(R.id.project_name);
                description = (TextView) itemView.findViewById(R.id.entry_description);

                editButton = (Button) itemView.findViewById(R.id.entry_edit_button);
                deleteButton = (Button) itemView.findViewById(R.id.entry_delete_button);
            }
        }
    }
}
