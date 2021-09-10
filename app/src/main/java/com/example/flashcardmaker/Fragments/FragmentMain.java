package com.example.flashcardmaker.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.flashcardmaker.Data.Database.SetDatabase;
import com.example.flashcardmaker.Data.Database.SetItemDao;
import com.example.flashcardmaker.R;

import java.lang.ref.WeakReference;

public class FragmentMain extends Fragment {
    private RelativeLayout createFlashcardsRelLayout, myFlashcardsRelLayout, recentlyStudiedRelLayout, favouriteSetsRelLayout, settingsRelLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(view);
        new ReceiveAndUpdateSetNumbers(getContext(), view).execute();

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
                FragmentSets fragmentSets = new FragmentSets();
                Bundle bundle = new Bundle();
                bundle.putString(FragmentSets.TYPE_SET, FragmentSets.ALL_SETS);
                fragmentSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentSets);
                transaction.commit();
            }
        });

        favouriteSetsRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSets fragmentSets = new FragmentSets();
                Bundle bundle = new Bundle();
                bundle.putString(FragmentSets.TYPE_SET, FragmentSets.FAVOURITE_SETS);
                fragmentSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentSets);
                transaction.commit();
            }
        });

        recentlyStudiedRelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSets fragmentSets = new FragmentSets();
                Bundle bundle = new Bundle();
                bundle.putString(FragmentSets.TYPE_SET, FragmentSets.RECENTLY_STUDIED_SETS);
                fragmentSets.setArguments(bundle);
                transaction.replace(R.id.fragmentContainer, fragmentSets);
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

    private static void updateSets(View view, Integer[] integers) {
        TextView txtNumSetsAll = view.findViewById(R.id.txtNumSetsAll);
        TextView txtNumSetsRecent = view.findViewById(R.id.txtNumSetsRecent);
        TextView txtNumSetsFavourite = view.findViewById(R.id.txtNumSetsFavourite);
        txtNumSetsAll.setText(String.valueOf(integers[0]));
        txtNumSetsRecent.setText(String.valueOf(integers[1]));
        txtNumSetsFavourite.setText(String.valueOf(integers[2]));
    }

    private static class ReceiveAndUpdateSetNumbers extends AsyncTask<Void, Void, Integer[]> {
        private final WeakReference<View> view_reference;
        private final SetItemDao dao;

        public ReceiveAndUpdateSetNumbers(Context context, View view) {
            view_reference = new WeakReference<>(view);
            dao = SetDatabase.getInstance(context).setItemDao();
        }

        @Override
        protected Integer[] doInBackground(Void... voids) {
            Integer[] intList = new Integer[3];
            intList[0] = dao.receiveAllSetsNumber();
            intList[1] = dao.receiveRecentlyStudiedSetsNumber();
            intList[2] = dao.receiveFavouriteSetsNumber();
            return intList;
        }

        @Override
        protected void onPostExecute(Integer[] integers) {
            super.onPostExecute(integers);
            updateSets(view_reference.get(), integers);
        }
    }
}
