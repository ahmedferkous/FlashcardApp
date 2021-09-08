package com.example.flashcardmaker.Data;

public class Card {
    private String front = "";
    private String back = "";

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
}
