package com.example.flashcardmaker.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.Activities.MainActivity;
import com.example.flashcardmaker.Adapters.SetAdapter;
import com.example.flashcardmaker.Data.Set;
import com.example.flashcardmaker.Data.Database.SetDatabase;
import com.example.flashcardmaker.Data.Database.SetItemDao;
import com.example.flashcardmaker.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FragmentAllSets extends Fragment {
    private static final String TAG = "FragmentAllSets";
    public static final String ALL_SETS = "all_sets";
    public static final String TYPE_SET = "type_set";
    public static final String FAVOURITE_SETS = "favourite_sets";
    public static final String RECENTLY_STUDIED_SETS = "recently_studied_sets";

    private RecyclerView recView;
    private SetAdapter adapter;
    private Button btnTestMode;
    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Log.d(TAG, "onClick: " + which);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allsets, container, false);
        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String typeSet = bundle.getString(TYPE_SET, "");
            RetrieveSetsTask task = new RetrieveSetsTask(getContext(), view, getParentFragmentManager());
            if (typeSet.equals(ALL_SETS)) {
                getActivity().setTitle("All Sets");
                task.execute(ALL_SETS);
            } else if (typeSet.equals(FAVOURITE_SETS)) {
                getActivity().setTitle("Favourite Sets");
                task.execute(FAVOURITE_SETS);
            } else if (typeSet.equals(RECENTLY_STUDIED_SETS)) {
                getActivity().setTitle("Recently Studied Sets");
                task.execute(RECENTLY_STUDIED_SETS);
            }
        }
        
        btnTestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("Select your test mode (" + getSelected() + " Sets currently selected)")
                        .setItems(new CharSequence[] {"Feedback", "Timed"}, listener);
                builder.create().show();
            }
        });

        return view;
    }

    private int getSelected() {
        return 0;
    }

    private void initViews(View view) {
        btnTestMode = view.findViewById(R.id.btnTestMode);
    }

    private static class RetrieveSetsTask extends AsyncTask<String, Void, ArrayList<Set>> {
        private final WeakReference<View> view_reference;
        private final WeakReference<Context> context_reference;
        private final WeakReference<FragmentManager> managerReference;
        private final SetItemDao dao;

        public RetrieveSetsTask(Context context, View view, FragmentManager manager) {
            view_reference = new WeakReference<>(view);
            context_reference = new WeakReference<>(context);
            managerReference = new WeakReference<>(manager);
            dao = SetDatabase.getInstance(context).setItemDao();
        }


        @Override
        protected ArrayList<Set> doInBackground(String... strings) {
            ArrayList<Set> setArrayList = new ArrayList<>();
            switch (strings[0]) {
                case ALL_SETS:
                    setArrayList.addAll(dao.retrieveAllSets());
                    break;
                case FAVOURITE_SETS:
                    setArrayList.addAll(dao.retrieveAllFavourites());
                    break;
                case RECENTLY_STUDIED_SETS:
                    setArrayList.addAll(dao.retrieveAllRecentlyStudied());
                    break;
                default:
                    break;
            }
            return setArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Set> sets) {
            super.onPostExecute(sets);
            RecyclerView recView = view_reference.get().findViewById(R.id.recView);
            recView.setLayoutManager(new LinearLayoutManager(context_reference.get()));
            SetAdapter adapter = new SetAdapter(context_reference.get(), managerReference.get());
            recView.setAdapter(adapter);
            adapter.setSets(sets);
        }
    }
}
