package ru.pavlov.notes.data;

public interface CardsSource {
    NoteData getCardData(int position);
    int size();
}