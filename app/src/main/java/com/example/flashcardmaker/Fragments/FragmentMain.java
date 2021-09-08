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

public class FragmentMain extends Fragment {
    private RelativeLayout createFlashcardsRelLayout, myFlashcardsRelLayout, recentlyStudiedRelLayout, favouriteSetsRelLayout, settingsRelLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);

        createFlashcardsRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentNewSet fragmentNewSet = new FragmentNewSet();
                Bundle bundle = new Bundle();
                bundle.putString(FragmentNewSet.TYPE_SET, FragmentNewSet.NEW_SET);
                fragmentNewSet.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentNewSet);
                transaction.commit();
            }
        });

        myFlashcardsRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentAllSets fragmentAllSets = new FragmentAllSets();
                Bundle bundle = new Bundle();
                bundle.putString(FragmentAllSets.TYPE_SET, FragmentAllSets.ALL_SETS);
                fragmentAllSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentAllSets);
                transaction.commit();
            }
        });

        favouriteSetsRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentAllSets fragmentAllSets = new FragmentAllSets();
                Bundle bundle = new Bundle();
                bundle.putString(FragmentAllSets.TYPE_SET, FragmentAllSets.FAVOURITE_SETS);
                fragmentAllSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentAllSets);
                transaction.commit();
            }
        });

        recentlyStudiedRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentAllSets fragmentAllSets = new FragmentAllSets();
                Bundle bundle = new Bundle();
                bundle.putString(FragmentAllSets.TYPE_SET, FragmentAllSets.RECENTLY_STUDIED_SETS);
                fragmentAllSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentAllSets);
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
