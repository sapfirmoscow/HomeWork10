package ru.sberbank.homework10_second.util;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.sberbank.homework10.model.Note;

public class ConvertUtils {

    public static final String TABLE_NAME = "notes";
    public static final String DB_NAME = "db_notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_CREATED = "created";
    private static final int VERSION_DB = 2;


    public static List<Note> pareseCursorToNotes(Cursor cursor) {
        List<Note> temp = new ArrayList<>();
        while (cursor.moveToNext()) {
            temp.add(parseCursorToNote(cursor));
        }
        return temp;
    }

    public static Note parseCursorToNote(Cursor cursor) {
        return new Note(
                cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TEXT)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CREATED)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_COLOR)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
        );
    }

    public static ContentValues getNoteContentValues(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, note.getTitle());
        contentValues.put(COLUMN_TEXT, note.getText());
        contentValues.put(COLUMN_CREATED, note.getCreationDate());
        contentValues.put(COLUMN_ID, note.getId());
        contentValues.put(COLUMN_COLOR, note.getColor());
        return contentValues;
    }

    public static Note convertContentValuesToNote(ContentValues contentValues) {
        Note note = new Note();
        note.setId(contentValues.getAsInteger(COLUMN_ID));
        note.setTitle(contentValues.getAsString(COLUMN_TITLE));
        note.setText(contentValues.getAsString(COLUMN_TEXT));
        note.setCreationDate(contentValues.getAsString(COLUMN_CREATED));
        note.setColor(contentValues.getAsInteger(COLUMN_COLOR));
        return note;
    }


}