package ru.pavlov.notes.observe;

import ru.pavlov.notes.data.NoteData;

public interface Subscriber {
    void handlerUpdateNoteData(NoteData cardData);
}
