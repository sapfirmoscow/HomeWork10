package ru.sberbank.homework10.cp;

import android.database.Cursor;

import java.util.List;

import ru.sberbank.homework10.db.NoteDB;
import ru.sberbank.homework10.model.Note;

public  class DaoImpl implements DAO {

    private final NoteDB mDatabase;

    public DaoImpl(NoteDB database) {
        mDatabase = database;
    }

    @Override
    public long addNote(Note note) {
       return mDatabase.getNoteDAO().insert(note);
    }
    @Override
    public int updateNote(Note note) {
        return mDatabase.getNoteDAO().update(note);
    }

    @Override
    public List<Note> getNotes() {
        return mDatabase.getNoteDAO().getNotes();
    }

    @Override
    public Cursor selectAll() {
        return mDatabase.getNoteDAO().selectAll();
    }

    @Override
    public int deleteNoteByid(int id) {
        return mDatabase.getNoteDAO().deleteById(id);
    }
}
