package com.example.flashcardmaker.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.Adapters.SetAdapter;
import com.example.flashcardmaker.DataObjects.Set;
import com.example.flashcardmaker.R;

import java.util.ArrayList;

public class FragmentAllFlashcards extends Fragment {
    private RecyclerView recView;
    private SetAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_flashcards, container, false);
        recView = view.findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SetAdapter(getContext());
        recView.setAdapter(adapter);

        ArrayList<Set> sets = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Set set = new Set();
            set.setSetCards(new ArrayList<>());
            sets.add(set);
        }
        adapter.setSets(sets);
        return view;
    }
}
