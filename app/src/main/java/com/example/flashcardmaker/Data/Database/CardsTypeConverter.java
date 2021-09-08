package com.example.flashcardmaker.Data.Database;

import com.example.flashcardmaker.Data.Card;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CardsTypeConverter {
    private Gson gson = new Gson();
    @androidx.room.TypeConverter
    public String setToJsonObject(ArrayList<Card> cards) {
        return gson.toJson(cards);
    }
    @androidx.room.TypeConverter
    public ArrayList<Card> jsonToSet(String json) {
        return gson.fromJson(json, new TypeToken<ArrayList<Card>>(){}.getType());
    }
}
