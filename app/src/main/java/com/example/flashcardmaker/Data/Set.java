package com.example.flashcardmaker.Data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.flashcardmaker.Data.Database.CardsTypeConverter;

import java.util.ArrayList;

@Entity(tableName = "set_items")
public class Set {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean favourite;
    private boolean recentlyStudied;
    private String title = "Set Name";
    private String desc = "Set Description";
    @TypeConverters(CardsTypeConverter.class)
    private ArrayList<Card> setCards;

    @Ignore
    public Set() {
    }

    public Set(String title, String desc, ArrayList<Card> setCards) {
        this.title = title;
        this.desc = desc;
        this.setCards = setCards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isRecentlyStudied() {
        return recentlyStudied;
    }

    public void setRecentlyStudied(boolean recentlyStudied) {
        this.recentlyStudied = recentlyStudied;
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
