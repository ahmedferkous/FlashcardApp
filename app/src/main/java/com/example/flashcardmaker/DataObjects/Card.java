package com.example.flashcardmaker.DataObjects;

public class Card {
    private String front = "";
    private String back = "";
    private boolean favourite, studied;

    public Card(String front, String back, boolean favourite, boolean studied) {
        this.front = front;
        this.back = back;
        this.favourite = favourite;
        this.studied = studied;
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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isStudied() {
        return studied;
    }

    public void setStudied(boolean studied) {
        this.studied = studied;
    }
}
