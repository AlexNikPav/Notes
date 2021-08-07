package ru.pavlov.notes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Note implements Parcelable {
    private int id;
    private String title;
    private String description;
    private Long dateTime = null;

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

    public Note(int id, String title, String description, Calendar dateTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateTime = dateTime.getTime().getTime();
    }

    protected Note(Parcel in) {
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
    }
}
