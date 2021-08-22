package ru.pavlov.notes;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {
    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private void addFragment(int container, Fragment fragment, boolean useBackStack) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        if (useBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void addFragmentToMainArea(Fragment fragment, boolean useBackStack) {
        addFragment(R.id.notes_container, fragment, useBackStack);
    }

    public void addFragmentToRightArea(Fragment fragment, boolean useBackStack) {
        addFragment(R.id.note_detail_container, fragment, useBackStack);
    }
}
