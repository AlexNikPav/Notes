package ru.pavlov.notes.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.pavlov.notes.MainActivity;
import ru.pavlov.notes.Navigation;
import ru.pavlov.notes.R;
import ru.pavlov.notes.data.NotesSource;
import ru.pavlov.notes.data.NotesSourceArray;
import ru.pavlov.notes.data.NoteData;
import ru.pavlov.notes.observe.Subscriber;
import ru.pavlov.notes.observe.SingleObservers;

public class NotesFragment extends FragmentBase {
    private Navigation navigation;
    private SingleObservers publisher;
    private NotesSource notesSource;
    private NoteItemsAdapter noteItemsAdapter;
    private RecyclerView recyclerView;
    private boolean moveToLastPosition;
    private int positionShowNoteDetail;

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesSource = new NotesSourceArray(getResources()).init();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = layout.findViewById(R.id.recycler_view_notes);
        initRecyclerView(recyclerView, notesSource);
        setHasOptionsMenu(true);

        return layout;
    }

    private void initRecyclerView(RecyclerView recyclerView, NotesSource notesSource) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        noteItemsAdapter = new NoteItemsAdapter(notesSource, this);
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

        if (moveToLastPosition) {
            recyclerView.smoothScrollToPosition(notesSource.size() - 1);
            moveToLastPosition = false;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }


    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    private void showNoteDetailByIndex(int position) {
        showNoteDetail(position);
    }

    private void showNoteDetail(int position) {
        if (position < 0) {
            return;
        }
        if (isLandScape()) {
            showNoteDetailLand(position);
        } else {
            showNoteDetailPort(position);
        }
        publisher.subscribe(new Subscriber() {
            @Override
            public void handlerUpdateNoteData(NoteData noteData) {
                notesSource.update(position, noteData);
                noteItemsAdapter.notifyItemChanged(position);
            }
        });
        setPositionShowNoteDetail(position);
    }

    private void setPositionShowNoteDetail(int position) {
        this.positionShowNoteDetail = position;
    }

    private int getPositionShowNoteDetail() {
        return this.positionShowNoteDetail;
    }

    private void showNoteDetailPort(int position) {
        navigation.addFragmentToMainArea(NoteDetailFragment.newInstance(notesSource.getNoteData(position)), true);
    }

    private void showNoteDetailLand(int position) {
        navigation.addFragmentToRightArea(NoteDetailFragment.newInstance(notesSource.getNoteData(position)), false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (isLandScape()) {
                    navigation.addFragmentToRightArea(NoteDetailFragment.newInstance(notesSource.getNewNoteData()), false);
                } else {
                    navigation.addFragmentToMainArea(NoteDetailFragment.newInstance(notesSource.getNewNoteData()), true);
                }

                publisher.subscribe(new Subscriber() {
                    @Override
                    public void handlerUpdateNoteData(NoteData noteData) {
                        notesSource.add(noteData);
                        int lastPosition = notesSource.size() - 1;
                        noteItemsAdapter.notifyItemInserted(lastPosition);
                        NotesFragment.this.moveToLastPosition = true;
                        recyclerView.smoothScrollToPosition(lastPosition);
                        if (isLandScape()) {
                            publisher.subscribe(new Subscriber() {
                                @Override
                                public void handlerUpdateNoteData(NoteData noteData) {
                                    notesSource.update(lastPosition, noteData);
                                    noteItemsAdapter.notifyItemChanged(lastPosition);
                                }
                            });
                        }
                    }
                });
                return true;
            case R.id.action_clear:
                notesSource.clearAll();
                noteItemsAdapter.notifyDataSetChanged();
                navigation.clearFragmentToRightArea();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.card_menu, menu);
//    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final int position = noteItemsAdapter.getMenuPosition();
        switch (item.getItemId()) {
            case R.id.action_delete:
                if (getPositionShowNoteDetail() == position) {
                    navigation.clearFragmentToRightArea();
                }
                notesSource.delete(position);
                noteItemsAdapter.notifyItemRemoved(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

}