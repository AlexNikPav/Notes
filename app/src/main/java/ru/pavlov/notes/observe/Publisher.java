package ru.pavlov.notes.observe;

import java.util.ArrayList;
import java.util.List;

import ru.pavlov.notes.data.NoteData;

public class Publisher {
    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }

    public void notifySingle(NoteData noteData) {
        for (Observer observer : observers) {
            observer.updateCardData(noteData);
        }
    }

    public void clear() {
        observers.clear();
    }
}
