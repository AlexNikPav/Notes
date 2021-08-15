package ru.pavlov.notes.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import ru.pavlov.notes.MainActivity;
import ru.pavlov.notes.R;
import ru.pavlov.notes.data.CardsSource;
import ru.pavlov.notes.data.CardsSourceImpl;
import ru.pavlov.notes.data.NoteData;

public class NotesFragment extends Fragment {

    private static final String KEY_NOTE = "note";
    boolean isLandScape;
    private NoteData currentNote;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (getArguments() != null) {
            currentNote = getArguments().getParcelable(KEY_NOTE);
            if (currentNote != null) {
                showNoteDetail();
            }
        } else if (isLandScape && getCurrentIndexNoteInActivity() != 0) {
            showNoteDetailByIndex(getCurrentIndexNoteInActivity());
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_notes, container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view_notes);
        CardsSource data = new CardsSourceImpl(getResources()).init();
        initRecyclerView(recyclerView, data);

        return layout;
    }

    private void initRecyclerView(RecyclerView recyclerView, CardsSource data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        final NoteItemsAdapter adapter = new NoteItemsAdapter(data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickHandler(new OnItemClickHandler() {
            @Override
            public void onItemClick(View view, int position) {
                showNoteDetailByIndex(position);
            }
        });
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(KEY_NOTE, currentNote);
    }

    private void showNoteDetailByIndex(int index) {
        Calendar calendar = Calendar.getInstance();
        currentNote = new NoteData(index,
                getResources().getStringArray(R.array.notes_array)[index],
                getResources().getStringArray(R.array.description_array)[index],
                calendar);
        showNoteDetail();
        setCurrentIndexNoteInActivity();
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

    private void setCurrentIndexNoteInActivity() {
        if (currentNote != null && currentNote instanceof NoteData) {
            ((MainActivity) requireActivity()).setCurrentIndexNote(currentNote.getId());
        }
    }

    private int getCurrentIndexNoteInActivity() {
        return ((MainActivity) requireActivity()).getCurrentIndexNote();
    }
}