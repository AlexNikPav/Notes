package ru.pavlov.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG_LOG = "tag_log";
    private static final String KEY_CURRENT_NOTE = "key_current_note";
    private static int currentIndexNote = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            setCurrentIndexNote(savedInstanceState.getInt(KEY_CURRENT_NOTE));
        }

        Log.d(TAG_LOG, "onCreate: " + currentIndexNote);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.notes_container, NotesFragment.newInstance())
                .commit();

    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(KEY_CURRENT_NOTE, getCurrentIndexNote());
    }

    public static int getCurrentIndexNote() {
        return currentIndexNote;
    }

    public static void setCurrentIndexNote(int currentIndexNote) {
        MainActivity.currentIndexNote = currentIndexNote;
    }

}