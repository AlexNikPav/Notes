package ru.pavlov.notes.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

abstract class FragmentBase extends Fragment {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final int MY_DEFAULT_DURATION = 500;
    public static final String BUNDLE_KEY_NOTE = "note";

    private boolean isLandScape;
    private boolean isPortrait;

    public void initOrientation() {
        isLandScape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        isPortrait = !isLandScape;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initOrientation();
    }

    public boolean isLandScape() {
        return isLandScape;
    }

    public void setLandScape(boolean landScape) {
        isLandScape = landScape;
    }

    public boolean isPortrait() {
        return isPortrait;
    }

    public void setPortrait(boolean portrait) {
        isPortrait = portrait;
    }
}
