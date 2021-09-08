package com.example.flashcardmaker.Data.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.flashcardmaker.Data.Card;
import com.example.flashcardmaker.Data.Set;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface SetItemDao {
    @Insert
    void insert(Set set);

    @Query("SELECT * FROM set_items WHERE favourite=1")
    List<Set> retrieveAllFavourites();

    @Query("SELECT * FROM set_items WHERE recentlyStudied=1")
    List<Set> retrieveAllRecentlyStudied();

    @Query("SELECT * FROM set_items")
    List<Set> retrieveAllSets();

    @Query("DELETE FROM set_items WHERE id=:id")
    void deleteSetById(int id);

    @Query("UPDATE set_items SET `desc`=:description, title=:title, setcards=:cards WHERE id=:id")
    void updateSet(int id, String title, String description, String cards);

    @Query("UPDATE set_items SET favourite=:integer WHERE id=:id")
    void updateFavouritesById(int id, int integer);

    @Query("SELECT COUNT(*) FROM set_items")
    int receiveAllSetsNumber();

    @Query("SELECT COUNT(*) FROM set_items WHERE favourite=1")
    int receiveFavouriteSetsNumber();

    @Query("SELECT COUNT(*) FROM set_items WHERE recentlyStudied=1")
    int receiveRecentlyStudiedSetsNumber();
}
