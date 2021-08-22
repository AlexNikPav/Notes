package ru.pavlov.notes.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ru.pavlov.notes.MainActivity;
import ru.pavlov.notes.R;
import ru.pavlov.notes.data.NoteData;
import ru.pavlov.notes.observe.SingleObservers;

public class NoteDetailFragment extends FragmentBase {
    private SingleObservers publisher;
    private NoteData noteData;
    private TextInputEditText titleTextInput;
    private TextInputEditText descTextInput;
    private TextView dateTextView;
    private AppCompatButton buttonSetTimeNow;
    private AppCompatButton buttonSaveNote;

    public static NoteDetailFragment newInstance(NoteData noteData) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_KEY_NOTE, noteData);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(BUNDLE_KEY_NOTE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = (View) inflater.inflate(R.layout.fragment_note_detail, container, false);
        initView(layout);
        if (noteData != null) {
            setDataViews();
        }
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
        if (!isLandScape()) {
            publisher.notify(noteData);
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

        if (isLandScape()) {
            buttonSaveNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collectCardData();
                    publisher.notify(noteData);
                }
            });
        }
    }

    private void initView(View layout) {
        titleTextInput = layout.findViewById(R.id.inputTitle);
        descTextInput = layout.findViewById(R.id.inputDescription);
        dateTextView = layout.findViewById(R.id.date);
        buttonSetTimeNow = layout.findViewById(R.id.button_set_now);
        if (isLandScape()) {
            buttonSaveNote = layout.findViewById(R.id.button_save_note);
        }
    }

    private void setDataViews() {
        titleTextInput.setText(this.noteData.getTitle());
        descTextInput.setText(this.noteData.getDescription());
        setCurrentDateOnView(noteData.getDateTime());
    }

    private void setCurrentDateOnView(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        dateTextView.setText(formatter.format(calendar.getTime()));
        noteData.setDateTime(calendar);
    }
}