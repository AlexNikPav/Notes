package ru.pavlov.notes.data;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CardDataMapping {

    public static class Fields {
        public final static String DATE = "date";
        public final static String TITLE = "title";
        public final static String DESCRIPTION = "description";
    }

    public static NoteData toCardData(String id, Map<String, Object> doc) {
        Timestamp timeStamp = (Timestamp) doc.get(Fields.DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp.getTime());
        NoteData noteData = new NoteData((String) doc.get(Fields.TITLE), (String) doc.get(Fields.DESCRIPTION), calendar);
        noteData.setId(id);

        return noteData;
    }

    public static Map<String, Object> toDocument(NoteData cardData) {
        Map<String, Object> noteData = new HashMap<>();
        noteData.put(Fields.TITLE, cardData.getTitle());
        noteData.put(Fields.DESCRIPTION, cardData.getDescription());
        noteData.put(Fields.DATE, cardData.getDateTime());
        return noteData;
    }

}
