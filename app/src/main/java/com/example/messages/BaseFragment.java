package com.example.messages;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseFragment extends Fragment {
    View view;

    protected void triggerFragment(Fragment fragment, boolean add) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainframelay, fragment);
        if (add) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}