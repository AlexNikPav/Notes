package ru.pavlov.notes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class NoteData implements Parcelable {
    private String id;
    private String title;
    private String description;
    private Long dateTime = null;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        if (dateTime == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(dateTime);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NoteData> CREATOR = new Creator<NoteData>() {
        @Override
        public NoteData createFromParcel(Parcel in) {
            return new NoteData(in);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };

    public Calendar getDateTime() {
        Calendar calendar = Calendar.getInstance();
        if (dateTime != null) {
            calendar.setTimeInMillis(dateTime);
        }
        return calendar;
    }

    public void setDateTime(Calendar calendar) {
        this.dateTime = calendar.getTime().getTime();
    }

    public NoteData(String title, String description, Calendar dateTime) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime.getTime().getTime();
    }

    protected NoteData(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
