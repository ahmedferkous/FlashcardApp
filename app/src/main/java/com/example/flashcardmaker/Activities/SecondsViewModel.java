package com.example.flashcardmaker.Activities;

import androidx.lifecycle.ViewModel;

public class SecondsViewModel extends ViewModel {
    private int mainSeconds = 0;
    private int countdownSeconds = 3;
    private int millisecondsPerQuestion = 15000;

    public int getMillisecondsPerQuestion() {
        return millisecondsPerQuestion;
    }

    public void setMillisecondsPerQuestion(int millisecondsPerQuestion) {
        this.millisecondsPerQuestion = millisecondsPerQuestion;
    }

    public int getMainSeconds() {
        return mainSeconds;
    }

    public void setMainSeconds(int mainSeconds) {
        this.mainSeconds = mainSeconds;
    }

    public int getCountdownSeconds() {
        return countdownSeconds;
    }

    public void setCountdownSeconds(int countdownSeconds) {
        this.countdownSeconds = countdownSeconds;
    }
}
