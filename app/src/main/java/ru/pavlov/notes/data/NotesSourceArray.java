package ru.pavlov.notes.data;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.pavlov.notes.R;

public class NotesSourceArray implements NotesSource {
    private List<NoteData> dataSource;
    private Resources resources;

    public NotesSourceArray(Resources resources) {
        dataSource = new ArrayList<>(7);
        this.resources = resources;
    }

    public NotesSourceArray init() {
        String[] titles = resources.getStringArray(R.array.notes_array);
        String[] descriptions = resources.getStringArray(R.array.description_array);
        for (int i = 0; i < descriptions.length; i++) {
            Calendar calendar = Calendar.getInstance();
            dataSource.add(new NoteData(titles[i], descriptions[i], calendar));
        }
        return this;
    }

    public NoteData getNoteData(int position) {
        return dataSource.get(position);
    }

    public int size() {
        return dataSource.size();
    }

    @Override
    public void delete(int position) {
        dataSource.remove(position);
    }

    @Override
    public void update(int position, NoteData cardData) {
        dataSource.set(position, cardData);
    }

    @Override
    public void add(NoteData cardData) {
        dataSource.add(cardData);
    }

    @Override
    public void clearAll() {
        dataSource.clear();
    }

    public NoteData getNewNoteData() {
        Calendar calendar = Calendar.getInstance();
        return new NoteData("", "", calendar);
    }
}