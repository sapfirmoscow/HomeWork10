package ru.sberbank.homework10.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import java.util.List;

import ru.sberbank.homework10.model.Note;

@Dao
public interface NoteDAO {

    @Query("select * from note")
    List<Note> getNotes();

    @Insert(onConflict = OnConflictStrategy.FAIL)
    long insert(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Note note);

    @Delete
    int delete(Note note);

    @Query("delete from note where id=:id")
    int deleteById(int id);

    @Query("SELECT * FROM  note")
    Cursor selectAll();

}
