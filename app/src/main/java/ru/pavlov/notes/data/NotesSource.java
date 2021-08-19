package ru.pavlov.notes.data;

public interface NotesSource {
    NoteData getNoteData(int position);

    int size();

    void deleteNoteData(int position);

    void updateNoteData(int position, NoteData cardData);

    void addNoteData(NoteData cardData);

    void clearNoteData();
}