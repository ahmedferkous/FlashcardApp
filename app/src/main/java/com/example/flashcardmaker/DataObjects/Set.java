package com.example.flashcardmaker.DataObjects;

import java.util.ArrayList;

public class Set {
    private String title = "Set Name";
    private String desc = "Set Description";
    private ArrayList<Card> setCards;

    public Set() {
    }

    public Set(String title, String desc, ArrayList<Card> setCards) {
        this.title = title;
        this.desc = desc;
        this.setCards = setCards;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<Card> getSetCards() {
        return setCards;
    }

    public void setSetCards(ArrayList<Card> setCards) {
        this.setCards = setCards;
    }
}
