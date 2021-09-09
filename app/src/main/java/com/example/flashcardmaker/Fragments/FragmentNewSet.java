package com.example.flashcardmaker.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardmaker.Adapters.CardsAdapter;
import com.example.flashcardmaker.Data.Card;
import com.example.flashcardmaker.Data.Set;
import com.example.flashcardmaker.Data.Database.SetDatabase;
import com.example.flashcardmaker.Data.Database.SetItemDao;
import com.example.flashcardmaker.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FragmentNewSet extends Fragment implements CardsAdapter.onRemovedCard {
    public interface OnFinishedSet {
        void onFinishedSetResult();
    }
    private static final String TAG = "FragmentNewSet";
    public static final String ID_SET_KEY = "id_set_key";
    public static final String TYPE_SET = "type_set";
    public static final String SET_KEY = "set_key";
    public static final String EDIT_SET = "Edit Set";
    public static final String NEW_SET = "New Set";
    public static final String SAVE_KEY = "save";
    public static final String UPDATE_KEY = "update";

    @Override
    public void onRemovedCardResult() {
        btnAddNewCard.setText("Add a new card (" + adapter.getItemCount() + ")");
    }

    private TextView txtSave;
    private EditText edtTxtTitle, edtTxtDesc;
    private Button btnAddNewCard;
    private RecyclerView recView;
    private CardsAdapter adapter;
    private String typeOfSet;
    private int idOfSet;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_set, container, false);
        initViews(view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String type = bundle.getString(TYPE_SET);
            if (type.equals(NEW_SET)) {
                typeOfSet = NEW_SET;
                getActivity().setTitle(NEW_SET);
            } else if (type.equals(EDIT_SET)) {
                idOfSet = bundle.getInt(ID_SET_KEY);
                if (idOfSet > 0) {
                    typeOfSet = EDIT_SET;
                    getActivity().setTitle(EDIT_SET);
                    Gson gson = new Gson();
                    String json = bundle.getString(SET_KEY);
                    Set set = gson.fromJson(json, new TypeToken<Set>() {
                    }.getType());
                    edtTxtTitle.setText(set.getTitle());
                    edtTxtDesc.setText(set.getDesc());
                    adapter.setCards(set.getSetCards());
                    btnAddNewCard.setText("Add a new card (" + adapter.getItemCount() + ")");
                }
            }
            setupListeners();
        }
        return view;
    }

    private void setupListeners() {
        btnAddNewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addCard(new Card());
                btnAddNewCard.setText("Add a new card (" + adapter.getItemCount() + ")");
            }
        });

        // TODO: 7/09/2021 validation testing (already exists, text fields filled in)
        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTxtTitle.getText().toString();
                String desc = edtTxtDesc.getText().toString();
                if (title.equals("")) {
                    title = "No Title";
                }
                if (desc.equals("")) {
                    desc = "No Description";
                }

                if (typeOfSet.equals(NEW_SET)) {
                    new SaveUpdateSetTask(getContext()).execute(SAVE_KEY, title, desc);
                } else {
                    new SaveUpdateSetTask(getContext()).execute(UPDATE_KEY, String.valueOf(idOfSet), title, desc);
                }
            }
        });


    }

    private void initViews(View view) {
        btnAddNewCard = view.findViewById(R.id.btnAddNewCard);
        txtSave = view.findViewById(R.id.btnSave);
        edtTxtTitle = view.findViewById(R.id.edtTxtTitle);
        edtTxtDesc = view.findViewById(R.id.edtTxtDesc);
        btnAddNewCard = view.findViewById(R.id.btnAddNewCard);
        recView = view.findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CardsAdapter(this, getContext());
        recView.setAdapter(adapter);
        adapter.setCards(new ArrayList<>());
    }

    private class SaveUpdateSetTask extends AsyncTask<String, Void, Void> {
        private final WeakReference<Context> context_reference;
        private final SetItemDao dao;

        public SaveUpdateSetTask(Context context) {
            context_reference = new WeakReference<>(context);
            dao = SetDatabase.getInstance(context).setItemDao();
        }


        @Override
        protected Void doInBackground(String... strings) {
            adapter.nullCheck();

            if (strings[0].equals(SAVE_KEY)) {
                dao.insert(new Set(strings[1], strings[2], adapter.getCards()));
            } else {
                dao.updateSet(Integer.parseInt(strings[1]), strings[2], strings[3], new Gson().toJson(adapter.getCards()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(context_reference.get(), "Successfully Saved/Edited Set!", Toast.LENGTH_SHORT).show();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            FragmentAllSets fragmentAllSets = new FragmentAllSets();
            Bundle bundle = new Bundle();
            bundle.putString(FragmentAllSets.TYPE_SET, FragmentAllSets.ALL_SETS);
            fragmentAllSets.setArguments(bundle);
            transaction.replace(R.id.fragmentContainer, fragmentAllSets);
            transaction.commit();
        }
    }
}
