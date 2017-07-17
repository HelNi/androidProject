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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.user.worktime.Backend.BackendClient;
import com.example.user.worktime.Classes.DateHelpers;
import com.example.user.worktime.Classes.TimeTable.Activity;
import com.example.user.worktime.Classes.TimeTable.TimeTableEntry;
import com.example.user.worktime.Classes.User.User;
import com.example.user.worktime.Factory.IntentFactory;
import com.example.user.worktime.Helpers.TimeTableEntryCollectionHelper;

import net.danlew.android.joda.DateUtils;

import org.joda.time.Duration;
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
    /**
     * List of all time table entries. Might be empty before it is first loaded.
     */
    List<TimeTableEntry> mTimeTableEntries = new ArrayList<>();
    /**
     * Adapter for the recycleview.
     */
    TimeTableEntryAdapter mAdapter;

    // Start time that is pre-filled for new entries.
    LocalTime mSuggestedStartTime;

    public TimeTablePageFragment() {
        this.position = 0;
        date = DateHelpers.dayNumToDate(0);
        recalculateSuggestedStartTime();
    }

    // TODO: Is this how you use arguments?
    @Override
    public void setArguments(Bundle args) {
        position = args.getInt("num", 0);
        date = DateHelpers.dayNumToDate(position);
        super.setArguments(args);
    }

    /**
     * Call this when either the list of activities or the list of entries changes
     * To re-draw the data.
     */
    public void updateList() {
        // This causes the adapter to re-determine count and to re-bind everything.
        mAdapter.notifyDataSetChanged();
        recalculateSuggestedStartTime();
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
        fetchTimeTableListFromServer();
    }

    private Call<List<TimeTableEntry>> getEntriesAsync() {
        LocalDateTime start = date.toLocalDateTime(LocalTime.MIDNIGHT);
        LocalDateTime end = date.toLocalDateTime(LocalTime.MIDNIGHT).plusDays(1).minusSeconds(1);

        return BackendClient.getInstance().getTimeTableEntryService().getEntriesBetweenDates(start, end);
    }

    public void fetchTimeTableListFromServer() {
        final ProgressBar progress = (ProgressBar) getView().findViewById(R.id.time_table_progress);
        progress.setVisibility(View.VISIBLE);

        this.getEntriesAsync().enqueue(new Callback<List<TimeTableEntry>>() {
            @Override
            public void onResponse(Call<List<TimeTableEntry>> call, Response<List<TimeTableEntry>> response) {
                List<TimeTableEntry> body = response.body();

                mTimeTableEntries.clear();
                mTimeTableEntries.addAll(body);
                progress.setVisibility(View.GONE);

                updateList();
            }

            @Override
            public void onFailure(Call<List<TimeTableEntry>> call, Throwable t) {
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE).show();
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void recalculateSuggestedStartTime() {
        mSuggestedStartTime = TimeTableEntryCollectionHelper.suggestNextStartTime(mTimeTableEntries);
    }
    ///////////////////////////////////////////////////////////////////////////
    //  Time Table Entry Adpter
    ///////////////////////////////////////////////////////////////////////////
    protected class TimeTableEntryAdapter extends RecyclerView.Adapter<TimeTableEntryAdapter.EntryViewHolder> {
        List<TimeTableEntry> mEntries;
        SparseArray<Duration> mGapDurations;

        public List<TimeTableEntry> getmEntries() {
             return mEntries;
        }

        public void setmEntries(List<TimeTableEntry> mEntries) {
            this.mEntries = mEntries;
            mGapDurations = TimeTableEntryCollectionHelper.gapLengths(mEntries);
        }

        public TimeTableEntryAdapter(List<TimeTableEntry> mEntries) {
            setmEntries(mEntries);
        }

        @Override
        public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_table_entry_card, parent, false);
            return new EntryViewHolder(v);
        }

        @Override
        public void onBindViewHolder(EntryViewHolder holder, int position) {
            Duration gapToPrevious = mGapDurations.get(position);
            // Render either the gap to the previous entry or the entry itself.
            if (gapToPrevious != null) {
                holder.durationBefore.setText(String.format(getString(R.string.date_duration),
                        gapToPrevious.getStandardHours(), gapToPrevious.getStandardMinutes() % 60));
            }
            else {
                holder.entry_duration_before_layout.setVisibility(View.GONE);
            }
            // Otherwise, render the entry.
            final TimeTableEntry entry = mEntries.get(position);

            holder.description.setText(entry.getDescription());
            holder.interval.setText(DateUtils.formatDateRange(getContext(), entry.getStart(), entry.getEnd(), DateUtils.FORMAT_SHOW_TIME));
            Minutes minutes = Minutes.minutesBetween(entry.getStart(), entry.getEnd());
            holder.duration.setText(String.format(getString(R.string.date_duration),
                    Hours.hoursBetween(entry.getStart(),
                            entry.getEnd()).getHours(), minutes.getMinutes() % 60));

            Activity a = entry.getActivity();
            // When would that happen? But let's handle it anyway.
            if (a == null) {
                holder.activity_category.setText("Unbek.");
                holder.activity_name.setText("Unbek.");
            }
            else {
                holder.activity_category.setText(a.getCategoryName());
                holder.activity_name.setText(a.getName());
            }

            // BUTTONS TODO (setOnClickListener)

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = entry.getUser();
                    Intent i = IntentFactory.createNewEntryCreationIntent(getContext(), entry, true);
                    getParentFragment().startActivityForResult(i, TimeTablePagerFragment.REQUEST_CODE);
                }
            });

            // ask for confirmation if the user really wants the entry to be deleted.
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Eintrag löschen")
                            .setCancelable(true)
                            .setNegativeButton("behalten", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Löschen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Call<Void> deleteAction = BackendClient.getInstance().getTimeTableEntryService().deleteEntry(entry.getId());
                                    final ProgressBar progress = (ProgressBar) getView().findViewById(R.id.time_table_progress);
                                    progress.setVisibility(View.VISIBLE);

                                    deleteAction.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, Response<Void> response) {
                                            // TODO: Snackbar on failure
                                            fetchTimeTableListFromServer();
                                            progress.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {
                                            // TODO: Snackbar
                                            progress.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            })
                            .setIcon(R.drawable.ic_delete_forever_black_24dp)
                            .setMessage(String.format(getString(R.string.entry_deletion_confirmation),
                                    DateUtils.formatDateTime(getContext(), entry.getStart(), DateUtils.FORMAT_SHOW_TIME),
                                    DateUtils.formatDateTime(getContext(), entry.getStart(), DateUtils.FORMAT_SHOW_TIME)));

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            });
        }



        @Override
        public int getItemCount() {
            // Recompute mGapDurations here. The array may have changed.
            mGapDurations = TimeTableEntryCollectionHelper.gapLengths(mEntries);
            return mEntries.size();
        }

        ///////////////////////////////////////////////////////////////////////////
        // View Holder
        ///////////////////////////////////////////////////////////////////////////
        protected class EntryViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView interval;
            TextView duration;
            TextView description;
            TextView activity_category;
            TextView activity_name;

            TextView durationBefore;
            View entry_duration_before_layout;

            Button editButton;
            Button deleteButton;

            public EntryViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                interval = (TextView) itemView.findViewById(R.id.entry_interval);
                duration = (TextView) itemView.findViewById(R.id.entry_duration);
                activity_category = (TextView) itemView.findViewById(R.id.project_activity_category);
                activity_name = (TextView) itemView.findViewById(R.id.project_activity_name);
                description = (TextView) itemView.findViewById(R.id.entry_description);
                durationBefore = (TextView) itemView.findViewById(R.id.entry_duration_before);
                entry_duration_before_layout = itemView.findViewById(R.id.entry_duration_before_layout);

                editButton = (Button) itemView.findViewById(R.id.entry_edit_button);
                deleteButton = (Button) itemView.findViewById(R.id.entry_delete_button);
            }
        }
    }
}
