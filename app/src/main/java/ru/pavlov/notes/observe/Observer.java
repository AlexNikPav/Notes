package ru.pavlov.notes.observe;

import ru.pavlov.notes.data.NoteData;

public interface Observer {
    void updateNoteData(NoteData cardData);
}
