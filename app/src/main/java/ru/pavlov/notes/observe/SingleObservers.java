package ru.pavlov.notes.observe;

import java.util.ArrayList;
import java.util.List;

import ru.pavlov.notes.data.NoteData;

public class SingleObservers {
    private List<Subscriber> subscribers;

    public SingleObservers() {
        subscribers = new ArrayList<>();
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.clear();
        subscribers.add(subscriber);
    }

    public void notify(NoteData noteData) {
        for (Subscriber subscriber : subscribers) {
            subscriber.handlerUpdateNoteData(noteData);
        }
    }
}
