package ru.sberbank.homework10.cp;

import android.database.Cursor;

import java.util.List;

import ru.sberbank.homework10.model.Note;

public interface DAO {

    long addNote(Note note);
    int deleteNoteByid(int id);
    int updateNote(Note note);
    List<Note> getNotes();
    Cursor selectAll();
}
