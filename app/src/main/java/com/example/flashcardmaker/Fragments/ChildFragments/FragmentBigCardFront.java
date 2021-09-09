package com.example.flashcardmaker.Fragments.ChildFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flashcardmaker.R;

public class FragmentBigCardFront extends Fragment {
    private static final String TAG = "FragmentBigCardFront";
    private TextView txtFrontCardText;
    private String text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_big_card_front, container, false);
        txtFrontCardText = view.findViewById(R.id.txtFrontCardText);
        txtFrontCardText.setText(text);

        return view;
    }

    public void setFront(String text) {
        this.text = text;
    }
}
