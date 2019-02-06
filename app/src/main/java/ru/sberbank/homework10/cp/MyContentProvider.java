package ru.sberbank.homework10.cp;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URI;

import ru.sberbank.homework10.db.NoteDB;
import ru.sberbank.homework10.model.Note;

public class MyContentProvider extends ContentProvider {

    private static final String DATABASE_NAME = "notes";

    public static final String AUTHORITY = "ru.sberbank.homework10.cp.MyContentProvider";
    public static final String NOTE_TABLE = "Note";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTE_TABLE);

    public static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int NOTES = 1;
    public static final int NOTE_ID = 2;


    static {
        sURIMatcher.addURI(AUTHORITY,NOTE_TABLE,NOTES);
        sURIMatcher.addURI(AUTHORITY, "note" + "/#", NOTE_ID);
    }


    private DAO mDAO ;
    private NoteDB mDatabase;

    @Override
    public boolean onCreate() {
        mDatabase = Room.databaseBuilder(getContext(),NoteDB.class,DATABASE_NAME).build();
        mDAO = new DaoImpl(mDatabase);
        return mDatabase != null ? true : false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = sURIMatcher.match(uri);
        final Cursor cursor;
        switch (uriType) {
            case NOTES:
                cursor = mDAO.selectAll();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        long id;
        switch (uriType){
            case NOTES:
                if(values!= null) id = mDAO.addNote(ConvertUtils.convertContentValuesToNote(values));
                else throw new IllegalArgumentException("CV can't be null");
                break;
                default:
                    throw new IllegalArgumentException("Uknown URI");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return Uri.parse("note/" + id);


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsDeleted = 0;
        switch (uriType){
            case NOTES:
                String id = uri.getLastPathSegment();
                rowsDeleted =mDAO.deleteNoteByid(Integer.valueOf(id));
                break;
                default:
                    throw new IllegalArgumentException("Uknown URI");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsUpdated;
        switch (uriType){
            case NOTES:
                if (values != null){
                    rowsUpdated = mDAO.updateNote(ConvertUtils.convertContentValuesToNote(values));
                }else throw new IllegalArgumentException("CV can't be null");
                break;
                default:
                    throw new IllegalArgumentException("Uknown URI");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }
}
