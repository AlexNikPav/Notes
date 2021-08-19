package ru.pavlov.notes.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.pavlov.notes.MainActivity;
import ru.pavlov.notes.R;
import ru.pavlov.notes.data.NoteData;
import ru.pavlov.notes.observe.Publisher;

public class NoteDetailFragment extends Fragment {

    private static final String KEY_NOTE = "note";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private Publisher publisher;
    private NoteData noteData;
    private TextInputEditText titleTextInput;
    private TextInputEditText descTextInput;
    private TextView dateTextView;
    private AppCompatButton buttonSetTimeNow;
    private AppCompatButton buttonSaveNote;
    private boolean isLandScape;

    // Для добавления новых данных
    public static NoteDetailFragment newInstance() {
        NoteDetailFragment fragment = new NoteDetailFragment();
        return fragment;
    }

    public static NoteDetailFragment newInstance(NoteData noteData) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_NOTE, noteData);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(KEY_NOTE);
        }
        isLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_note_detail, container, false);
        initView(layout);
        initListeners();

        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onStop() {
        super.onStop();
        collectCardData();
    }

    private void collectCardData() {
        noteData.setTitle(this.titleTextInput.getText().toString());
        noteData.setDescription(this.descTextInput.getText().toString());
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isLandScape) {
            publisher.notifySingle(noteData);
        }
    }

    private void initListeners() {
        buttonSetTimeNow.setOnClickListener(view -> {
            Calendar dateOfNote = noteData.getDateTime();

            DatePickerDialog.OnDateSetListener dateSetListener = (view1, year, monthOfYear, dayOfMonth) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setCurrentDateOnView(calendar);
            };

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
                    dateSetListener, dateOfNote.get(Calendar.YEAR), dateOfNote.get(Calendar.MONTH),
                    dateOfNote.get(Calendar.DAY_OF_MONTH));


            datePickerDialog.show();
        });

        if (isLandScape) {
            buttonSaveNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collectCardData();
                    publisher.notifySingle(noteData);
                }
            });
        }
    }

    private void initView(ViewGroup layout) {
        titleTextInput = layout.findViewById(R.id.inputTitle);
        titleTextInput.setText(this.noteData.getTitle());

        descTextInput = layout.findViewById(R.id.inputDescription);
        descTextInput.setText(this.noteData.getDescription());

        dateTextView = layout.findViewById(R.id.date);

        buttonSetTimeNow = layout.findViewById(R.id.button_set_now);
        setCurrentDateOnView(noteData.getDateTime());

        if (isLandScape) {
            buttonSaveNote = layout.findViewById(R.id.button_save_note);
        }
    }

    private void setCurrentDateOnView(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        dateTextView.setText(formatter.format(calendar.getTime()));
        noteData.setDateTime(calendar);
    }
}