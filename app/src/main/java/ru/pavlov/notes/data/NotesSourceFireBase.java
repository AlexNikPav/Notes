package ru.pavlov.notes.data;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotesSourceFireBase implements NotesSource {
    private static final String CARDS_COLLECTION = "cards";
    private static final String TAG = "[CardsSourceFirebaseImpl]";
    private List<NoteData> dataSource;
    // База данных Firestore
    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    // Коллекция документов
    private CollectionReference collection = store.collection(CARDS_COLLECTION);

    public NotesSource init(NotesSourceResponse cardsSourceResponse) {
        // Получить всю коллекцию, отсортированную по полю «Дата»
        collection.orderBy(CardDataMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    // При удачном считывании данных загрузим список карточек
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataSource = new ArrayList<NoteData>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                NoteData cardData = CardDataMapping.toCardData(id, doc);
                                dataSource.add(cardData);
                            }
                            Log.d(TAG, "success " + dataSource.size() + " qnt");
                            cardsSourceResponse.initialized(NotesSourceFireBase.this);
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
        return this;
    }

    @Override
    public NoteData getNoteData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        if (dataSource == null) {
            return 0;
        }
        return dataSource.size();
    }

    @Override
    public void delete(int position) {
        // Удалить документ с определённым идентификатором
        collection.document(dataSource.get(position).getId()).delete();
        dataSource.remove(position);
    }

    @Override
    public void update(int position, NoteData cardData) {
        String id = cardData.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(CardDataMapping.toDocument(cardData));
    }

    @Override
    public void add(final NoteData cardData) {
        // Добавить документ
        collection.add(CardDataMapping.toDocument(cardData))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        cardData.setId(documentReference.getId());
                    }
                });
    }

    @Override
    public void clearAll() {
        for (NoteData cardData : dataSource) {
            collection.document(cardData.getId()).delete();
        }
        dataSource = new ArrayList<NoteData>();
    }

    @Override
    public NoteData getNewNoteData() {
        return null;
    }

}
