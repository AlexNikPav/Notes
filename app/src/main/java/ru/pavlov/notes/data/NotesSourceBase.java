package ru.pavlov.notes.data;

import java.util.Calendar;

public abstract class NotesSourceBase implements NotesSource {
    public NoteData getNewNoteData() {
        Calendar calendar = Calendar.getInstance();
        return new NoteData("", "", calendar);
    }
}
