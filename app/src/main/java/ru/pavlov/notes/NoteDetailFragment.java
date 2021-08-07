package ru.pavlov.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NoteDetailFragment extends Fragment {

    private static final String KEY_NOTE = "note";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private Note note;
    private TextView titleTextView;
    private TextView descTextView;
    private DatePicker mDatePicker;
    private TextView dateTextView;
    private Button buttonSetTimeNow;

    public static NoteDetailFragment newInstance(Note note) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(KEY_NOTE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_note_detail, container, false);
        initView(layout);

        initCalendarWidget();
        initListeners();

        return layout;
    }

    private void initListeners() {
        buttonSetTimeNow.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            setCurrentDateOnView(calendar);
        });
    }

    private void initCalendarWidget() {
        Calendar dateOfNote = note.getDateTime();
        mDatePicker.init(dateOfNote.get(Calendar.YEAR), dateOfNote.get(Calendar.MONTH),
                dateOfNote.get(Calendar.DAY_OF_MONTH), (view, year, monthOfYear, dayOfMonth) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    setCurrentDateOnView(calendar);
                });
    }

    private void initView(ViewGroup layout) {
        titleTextView = layout.findViewById(R.id.title);
        titleTextView.setTextSize(30);
        titleTextView.setText(this.note.getTitle());

        descTextView = layout.findViewById(R.id.description);
        descTextView.setTextSize(30);
        descTextView.setText(this.note.getDescription());

        dateTextView = layout.findViewById(R.id.date);
        mDatePicker = layout.findViewById(R.id.datePicker);

        buttonSetTimeNow = (Button) layout.findViewById(R.id.button_set_now);
        setCurrentDateOnView(note.getDateTime());
    }

    private void setCurrentDateOnView(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        dateTextView.setText(formatter.format(calendar.getTime()));
        note.setDateTime(calendar);
        initCalendarWidget();
    }
}