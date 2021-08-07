package ru.pavlov.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NoteDetailFragment extends Fragment {

    private static final String KEY_NOTE = "note";

    private Note note;

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

        TextView titleTextView = layout.findViewById(R.id.title);
        titleTextView.setTextSize(30);
        titleTextView.setText(this.note.getTitle());

        TextView descTextView = layout.findViewById(R.id.description);
        descTextView.setTextSize(30);
        descTextView.setText(this.note.getDescription());

        return layout;
    }
}