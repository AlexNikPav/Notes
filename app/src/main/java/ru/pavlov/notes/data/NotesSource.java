package ru.pavlov.notes.data;

public interface NotesSource {
    NoteData getNoteData(int position);

    int size();

    void delete(int position);

    void update(int position, NoteData cardData);

    void add(NoteData cardData);

    void clearAll();

    NoteData getNewNoteData();
}