package ru.pavlov.notes;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NotesFragment extends Fragment {

    private static final String KEY_NOTE = "note";
    boolean isLandScape;
    private Note currentNote;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (getArguments() != null) {
            currentNote = getArguments().getParcelable(KEY_NOTE);
        }
        if (currentNote != null) {
            showNoteDetail();
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

            int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showNoteDetailByIndex(finalI);
                }
            });
        }

        return layout;
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(KEY_NOTE, currentNote);
    }

    private void showNoteDetailByIndex(int index) {
        currentNote = new Note(getResources().getStringArray(R.array.notes_array)[index],
                getResources().getStringArray(R.array.description_array)[index]);

        showNoteDetail();
    }

    private void showNoteDetail() {
        if (isLandScape) {
            showNoteDetailLand();
        } else {
            showNoteDetailPort();
        }
    }

    private void showNoteDetailPort() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.notes_container, NoteDetailFragment.newInstance(currentNote))
                .addToBackStack("")
                .commit();
    }

    private void showNoteDetailLand() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.note_detail_container, NoteDetailFragment.newInstance(currentNote))
                .commit();
    }
}