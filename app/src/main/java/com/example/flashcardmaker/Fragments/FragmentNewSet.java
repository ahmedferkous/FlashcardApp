package com.example.flashcardmaker.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.Adapters.CardsAdapter;
import com.example.flashcardmaker.DataObjects.Card;
import com.example.flashcardmaker.R;

import java.util.ArrayList;

public class FragmentNewSet extends Fragment implements CardsAdapter.onRemovedCard {
    @Override
    public void onRemovedCardResult() {
        btnAddNewCard.setText("Add a new card (" + adapter.getItemCount() + ")");
    }

    private EditText edtTxtTitle, edtTxtDesc;
    private Button btnAddNewCard;
    private RecyclerView recView;
    private CardsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_set, container, false);
        initViews(view);
        btnAddNewCard = view.findViewById(R.id.btnAddNewCard);

        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addCard(new Card());
                btnAddNewCard.setText("Add a new card (" + adapter.getItemCount() + ")");
            }
        });

        return view;
    }

    private void initViews(View view) {
        edtTxtTitle = view.findViewById(R.id.edtTxtTitle);
        edtTxtDesc = view.findViewById(R.id.edtTxtDesc);
        btnAddNewCard = view.findViewById(R.id.btnAddNewCard);
        recView = view.findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CardsAdapter(this, getContext());
        recView.setAdapter(adapter);
        adapter.setCards(new ArrayList<>());
    }
}
