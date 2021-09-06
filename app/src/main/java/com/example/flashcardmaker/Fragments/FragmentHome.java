package com.example.flashcardmaker.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.flashcardmaker.R;

public class FragmentHome extends Fragment {
    private RelativeLayout createFlashcardsRelLayout, myFlashcardsRelLayout, recentlyStudiedRelLayout, favouriteSetsRelLayout, settingsRelLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);

        createFlashcardsRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, new FragmentNewSet());
                transaction.commit();
            }
        });

        myFlashcardsRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, new FragmentAllFlashcards());
                transaction.commit();
            }
        });

        return view;
    }

    private void initViews(View view) {
        createFlashcardsRelLayout = view.findViewById(R.id.createFlashcardsRelLayout);
        myFlashcardsRelLayout = view.findViewById(R.id.myFlashcardsRelLayout);
        recentlyStudiedRelLayout = view.findViewById(R.id.recentlyStudiedRelLayout);
        favouriteSetsRelLayout = view.findViewById(R.id.favouriteSetsRelLayout);
        settingsRelLayout = view.findViewById(R.id.settingsRelLayout);
    }
}
