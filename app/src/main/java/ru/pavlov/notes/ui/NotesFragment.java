package ru.pavlov.notes.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.pavlov.notes.MainActivity;
import ru.pavlov.notes.Navigation;
import ru.pavlov.notes.R;
import ru.pavlov.notes.data.NotesSource;
import ru.pavlov.notes.data.NotesSourceArray;
import ru.pavlov.notes.data.NoteData;
import ru.pavlov.notes.observe.Observer;
import ru.pavlov.notes.observe.Publisher;

public class NotesFragment extends Fragment {

    private static final int MY_DEFAULT_DURATION = 1000;
    private static final String KEY_NOTE = "note";
    boolean isLandScape;
    private Navigation navigation;
    private Publisher publisher;
    private NotesSource notesSource;
    private NoteItemsAdapter noteItemsAdapter;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        notesSource = new NotesSourceArray(getResources()).init();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_notes, container, false);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view_notes);
        initRecyclerView(recyclerView, notesSource);

        return layout;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    private void initRecyclerView(RecyclerView recyclerView, NotesSource data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        noteItemsAdapter = new NoteItemsAdapter(data);
        noteItemsAdapter.setOnItemClickHandler(new OnItemClickHandler() {
            @Override
            public void onItemClick(View view, int position) {
                showNoteDetailByIndex(position);
            }
        });
        recyclerView.setAdapter(noteItemsAdapter);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    private void showNoteDetailByIndex(int position) {
        showNoteDetail(position);
    }

    private void showNoteDetail(int position) {
        if (isLandScape) {
            showNoteDetailLand(position);
        } else {
            showNoteDetailPort(position);
        }
        publisher.clear();
        publisher.subscribe(new Observer() {
            @Override
            public void updateCardData(NoteData noteData) {
                notesSource.updateNoteData(position, noteData);
                noteItemsAdapter.notifyItemChanged(position);
            }
        });
    }

    private void showNoteDetailPort(int position) {
        navigation.addFragmentToMainArea(NoteDetailFragment.newInstance(notesSource.getNoteData(position)), true);
    }

    private void showNoteDetailLand(int position) {
        navigation.addFragmentToRightArea(NoteDetailFragment.newInstance(notesSource.getNoteData(position)), false);
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

}