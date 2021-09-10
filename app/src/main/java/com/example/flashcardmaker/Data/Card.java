package com.example.flashcardmaker.Data;

public class Card {
    private String front = "";
    private String back = "";
    private int gotCorrect = -1;
    private int setId;

    public Card(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public Card() {
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public int getGotCorrect() {
        return gotCorrect;
    }

    public void setGotCorrect(int gotCorrect) {
        this.gotCorrect = gotCorrect;
    }

    public int getSetId() {
        return setId;
    }

    public void setSetId(int setId) {
        this.setId = setId;
    }
}
