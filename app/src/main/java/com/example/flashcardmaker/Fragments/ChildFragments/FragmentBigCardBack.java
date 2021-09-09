package com.example.flashcardmaker.Fragments.ChildFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flashcardmaker.R;
import com.google.android.material.card.MaterialCardView;

public class FragmentBigCardBack extends Fragment {
    private TextView txtBackCardText, txtCorrectStatus;
    private MaterialCardView parentCard;
    private String text;
    private boolean status;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_big_card_back, container, false);
        txtBackCardText = view.findViewById(R.id.txtBackCardText);
        txtCorrectStatus = view.findViewById(R.id.txtCorrectStatus);
        parentCard = view.findViewById(R.id.parentCard);

        txtBackCardText.setText(text);

        txtCorrectStatus.setVisibility(View.VISIBLE);
        if (status) {
            txtCorrectStatus.setText("Correct!");
            parentCard.setBackgroundColor(getResources().getColor(R.color.lightGreen));
        } else {
            txtCorrectStatus.setText("Incorrect!");
            parentCard.setBackgroundColor(getResources().getColor(R.color.lightRed));
        }

        return view;
    }

    public void setBack(String text) {
        this.text = text;
    }

    public void setCorrectStatus(boolean status) {
        this.status = status;
    }
}
