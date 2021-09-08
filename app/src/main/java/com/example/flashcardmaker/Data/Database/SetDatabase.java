package com.example.flashcardmaker.Data.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.flashcardmaker.Data.Set;

@Database(entities = {Set.class}, version = 1)
public abstract class SetDatabase extends RoomDatabase {
    public abstract SetItemDao setItemDao();
    private static SetDatabase instance;

    public static synchronized SetDatabase getInstance(Context context) {
        if (null == instance) {
            instance = Room.databaseBuilder(context, SetDatabase.class, "flashcard_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
