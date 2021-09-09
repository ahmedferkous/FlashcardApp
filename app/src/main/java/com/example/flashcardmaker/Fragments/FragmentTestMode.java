package com.example.flashcardmaker.Fragments;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.flashcardmaker.Activities.SecondsViewModel;
import com.example.flashcardmaker.Data.Card;
import com.example.flashcardmaker.Data.Database.SetDatabase;
import com.example.flashcardmaker.Data.Database.SetItemDao;
import com.example.flashcardmaker.Data.Set;
import com.example.flashcardmaker.Fragments.ChildFragments.FragmentBigCardBack;
import com.example.flashcardmaker.Fragments.ChildFragments.FragmentBigCardFront;
import com.example.flashcardmaker.R;
import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Locale;

import static com.example.flashcardmaker.Fragments.FragmentAllSets.ALL_SETS;
import static com.example.flashcardmaker.Fragments.FragmentAllSets.FAVOURITE_SETS;
import static com.example.flashcardmaker.Fragments.FragmentAllSets.RECENTLY_STUDIED_SETS;

public class FragmentTestMode extends Fragment {
    private static final String TAG = "FragmentTestMode";
    public static final String TYPE_OF_TEST = "type_of_test";
    public static final String TIMED_TEST_MODE = "Timed Test Mode";
    public static final String FEEDBACK_TEST_MODE = "Feedback Mode";
    public static final String FEEDBACK_INFO = "You have unlimited time. Once you submit an answer, the real answer will be revealed";
    public static final String TIMED_TEST_INFO = "You have limited time. You cannot view the answers until the test is fully completed";

    private TextView txtTime, txtTestTitle, txtTitleInfo, txtTestInfo, txtTimeTicking;
    private EditText edtTxtAnswer;
    private Button btnSubmitAnswer, btnNextQuestion, btnStart, btnComplete;
    private RelativeLayout mainRelLayoutTest, initialRelLayout, finalInitialRelLayout, completedRelLayout;
    private FragmentBigCardFront fragmentBigCardFront;
    private FragmentBigCardBack fragmentBigCardBack;

    private SecondsViewModel secondsViewModel;
    private ArrayList<Card> cardsPool = new ArrayList<>();
    private ArrayList<Set> selectedSets = new ArrayList<>();
    private int incrementingIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_mode, container, false);
        initViews(view);
        secondsViewModel = new ViewModelProvider(this).get(SecondsViewModel.class);

        fragmentBigCardFront = new FragmentBigCardFront();
        fragmentBigCardBack = new FragmentBigCardBack();

        Bundle bundle = getArguments();
        if (bundle != null) {
            String typeOfTest = bundle.getString(TYPE_OF_TEST);
            String type = bundle.getString(FragmentAllSets.TYPE_SET, null);
            if (typeOfTest != null && type != null) {
                if (typeOfTest.equals(TIMED_TEST_MODE)) {
                    setupTimedTestMode();
                } else if (typeOfTest.equals(FEEDBACK_TEST_MODE)) {
                    setupFeedbackTestMode();
                }
                btnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new RetrieveSelectedSetsTask(getContext(), type, typeOfTest).execute();
                    }
                });
            }
        }

        return view;
    }

    private void initViews(View view) {
        txtTime = view.findViewById(R.id.txtTime);
        txtTestTitle = view.findViewById(R.id.txtTestTitle);
        edtTxtAnswer = view.findViewById(R.id.edtTxtAnswer);
        btnSubmitAnswer = view.findViewById(R.id.btnSubmitAnswer);
        btnNextQuestion = view.findViewById(R.id.btnNextQuestion);
        btnComplete = view.findViewById(R.id.btnComplete);
        mainRelLayoutTest = view.findViewById(R.id.mainRelLayoutTest);
        initialRelLayout = view.findViewById(R.id.initialRelLayout);
        txtTitleInfo = view.findViewById(R.id.txtTitleInfo);
        txtTestInfo = view.findViewById(R.id.txtTestInfo);
        txtTimeTicking = view.findViewById(R.id.txtTimeTicking);
        btnStart = view.findViewById(R.id.btnStart);
        finalInitialRelLayout = view.findViewById(R.id.finalInitialRelLayout);
        completedRelLayout = view.findViewById(R.id.completedRelLayout);
    }

    private void setupTimedTestMode() {
        getActivity().setTitle(TIMED_TEST_MODE);
        txtTitleInfo.setText(TIMED_TEST_MODE);
        txtTestInfo.setText(TIMED_TEST_INFO);
    }

    private void completeSetupTimedTestMode() {

    }

    private void setupFeedbackTestMode() {
        getActivity().setTitle(FEEDBACK_TEST_MODE);
        txtTitleInfo.setText(FEEDBACK_TEST_MODE);
        txtTestInfo.setText(FEEDBACK_INFO);
    }

    @SuppressLint("SetTextI18n")
    private void completeSetupFeedbackTestMode() {
        if (incrementingIndex != cardsPool.size() ) {
            Card nextAvailableCard = cardsPool.get(incrementingIndex);

            txtTestTitle.setText("Card " + String.valueOf(incrementingIndex+1) + "/" + cardsPool.size());

            fragmentBigCardFront.setFront(nextAvailableCard.getFront());
            fragmentBigCardBack.setBack(nextAvailableCard.getBack());

            getChildFragmentManager().beginTransaction().replace(R.id.childFragmentContainer, fragmentBigCardFront).commit();

            btnSubmitAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String response = edtTxtAnswer.getText().toString();
                    if (answerEquals(response, nextAvailableCard.getBack())) {
                        fragmentBigCardBack.setCorrectStatus(true);
                        nextAvailableCard.setGotCorrect(true);
                    } else {
                        fragmentBigCardBack.setCorrectStatus(false);
                        nextAvailableCard.setGotCorrect(false);
                    }
                    cardsPool.set(incrementingIndex, nextAvailableCard);

                    getChildFragmentManager().beginTransaction().setCustomAnimations(R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                            R.animator.card_flip_left_in, R.animator.card_flip_left_out).replace(R.id.childFragmentContainer, fragmentBigCardBack).commit();

                    btnSubmitAnswer.setVisibility(View.INVISIBLE);
                    btnNextQuestion.setVisibility(View.VISIBLE);

                    btnNextQuestion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnSubmitAnswer.setVisibility(View.VISIBLE);
                            btnNextQuestion.setVisibility(View.INVISIBLE);
                            incrementingIndex++;
                            completeSetupFeedbackTestMode();
                        }
                    });
                }
            });
        } else {
            mainRelLayoutTest.setVisibility(View.GONE);
            completedRelLayout.setVisibility(View.VISIBLE);
            btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UpdateCardsPoolWithSets(getContext()).execute();
                }
            });
        }
    }

    private static boolean answerEquals(String textToCompare, String originalText) {
        return originalText.equalsIgnoreCase(textToCompare);
    }


    private ArrayList<Card> getCardsPool() {
        ArrayList<Card> cardsPool = new ArrayList<>();
        for (int i = 0; i < selectedSets.size(); i++) {
            cardsPool.addAll(selectedSets.get(i).getSetCards());
        }
        return cardsPool;
    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = secondsViewModel.getMainSeconds();
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d", hours,
                        minutes, secs);

                txtTime.setText("Time Elapsed - " + time);
                secondsViewModel.setMainSeconds(seconds + 1);

                handler.postDelayed(this, 1000);
            }
        });
    }

    private void runCountdownTimer(String typeOfTest) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int seconds = secondsViewModel.getCountdownSeconds();
                if (!(seconds < 1)) {
                    txtTimeTicking.setText(String.valueOf(seconds));
                    secondsViewModel.setCountdownSeconds(seconds - 1);
                    handler.postDelayed(this, 1000);
                } else if (seconds == 0) {
                    txtTimeTicking.setText("GO!");
                    secondsViewModel.setCountdownSeconds(-1);
                    handler.postDelayed(this, 1000);
                } else {
                    finalInitialRelLayout.setVisibility(View.GONE);
                    mainRelLayoutTest.setVisibility(View.VISIBLE);
                    cardsPool = getCardsPool();

                    if (typeOfTest.equals(TIMED_TEST_MODE)) {
                        completeSetupTimedTestMode();
                    } else if (typeOfTest.equals(FEEDBACK_TEST_MODE)) {
                        completeSetupFeedbackTestMode();
                    }
                    runTimer();
                }
            }
        });
    }

    private class RetrieveSelectedSetsTask extends AsyncTask<Void, Void, ArrayList<Set>> {
        private SetItemDao dao;
        private String type, typeOfTest;

        public RetrieveSelectedSetsTask(Context context, String type, String typeOfTest) {
            dao = SetDatabase.getInstance(context).setItemDao();
            this.type = type;
            this.typeOfTest = typeOfTest;
            Log.d(TAG, "RetrieveSelectedSetsTask: " + type);
        }

        @Override
        protected ArrayList<Set> doInBackground(Void... voids) {
            ArrayList<Set> setArrayList = new ArrayList<>();
            switch (type) {
                case ALL_SETS:
                    setArrayList.addAll(dao.retrieveSelectedAllSets());
                    break;
                case FAVOURITE_SETS:
                    setArrayList.addAll(dao.retrieveSelectedFavouriteSets());
                    break;
                case RECENTLY_STUDIED_SETS:
                    setArrayList.addAll(dao.retrieveSelectedRecentlyStudiedSets());
                    break;
                default:
                    break;
            }
            return setArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Set> sets) {
            super.onPostExecute(sets);
            initialRelLayout.setVisibility(View.GONE);
            finalInitialRelLayout.setVisibility(View.VISIBLE);
            selectedSets = sets;
            runCountdownTimer(typeOfTest);
        }
    }

    private class UpdateCardsPoolWithSets extends AsyncTask<Void, Void, Void> {
        private SetItemDao dao;

        public UpdateCardsPoolWithSets(Context context) {
            dao = SetDatabase.getInstance(context).setItemDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<Integer> setIds = new ArrayList<>();

            for (Card c : cardsPool) {
                if (!(setIds.contains(c.getSetId()))) {
                    setIds.add(c.getSetId());
                }
            }

            for (Integer setId : setIds) {
                ArrayList<Card> newCards = new ArrayList<>();
                for (Card c : cardsPool) {
                    if (setId.equals(c.getSetId())) {
                        newCards.add(c);
                    }
                }
                dao.updateCards(setId, new Gson().toJson(newCards));
                dao.updateRecentById(setId, 1);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            FragmentAllSets fragmentAllSets = new FragmentAllSets();
            bundle.putString(FragmentAllSets.TYPE_SET, FragmentAllSets.RECENTLY_STUDIED_SETS);
            fragmentAllSets.setArguments(bundle);
            transaction.replace(R.id.fragmentContainer, fragmentAllSets);
            transaction.commit();
            Toast.makeText(getContext(), "Test Completed!", Toast.LENGTH_SHORT).show();
        }
    }
}
