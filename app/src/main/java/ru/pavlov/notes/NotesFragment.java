package ru.pavlov.notes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotesFragment extends Fragment {

    private static final String KEY_NOTE = "note";
    private Note currentNote;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentNote = getArguments().getParcelable(KEY_NOTE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_notes, container, false);

        String[] notes = getResources().getStringArray(R.array.notes_array);

        for (int i = 0; i < notes.length; i++) {
            String name = notes[i];
            TextView textView = new TextView(getContext());
            textView.setText(name);
            textView.setTextSize(30);
            layout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        return layout;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(KEY_NOTE, currentNote);
    }
}