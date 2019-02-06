package ru.sberbank.homework10_second;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import ru.sberbank.homework10.model.Note;
import ru.sberbank.homework10_second.util.ConvertUtils;

public class MainActivity extends AppCompatActivity {

    public static final String AUTHORITY = "ru.sberbank.homework10.cp.MyContentProvider";
    public static final String NOTE_TABLE = "Note";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + NOTE_TABLE);


    private static final String DATABASE_NAME = "notes";
    private FloatingActionButton mFloatingActionButton;
    private RecyclerView mRecyclerView;
    private MyAdapter mMyAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContentObserver mContentObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListeners();
        initRecyclerView();
        initContentObserver();


        downloadNotes();
    }

    private void initContentObserver() {
        mContentObserver = new NoteContentObserver(new Handler(Looper.getMainLooper()),this::downloadNotes);
    }


    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mMyAdapter = new MyAdapter(MainActivity.this, this::openNote, this::deleteNote);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mMyAdapter);
    }



    //метод используется когда мы хотим отредачить записку
    private void openNote(Note note) {
        Intent intent = CreateNoteActivity.newIntent(MainActivity.this);
        intent.putExtra("note", note);
        startActivityForResult(intent, 2);
    }

    private void initListeners() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CreateNoteActivity.newIntent(MainActivity.this);
                startActivityForResult(intent, 1);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.floatingActionButton), "", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


        switch (resultCode) {
            case 1:
                adddNote((Note) data.getExtras().get("note"));
                downloadNotes();
                snackbar.setText("Note added");
                snackbar.show();
                break;
            case 2:
                updateNote((Note) data.getExtras().get("note"));
                downloadNotes();
                snackbar.setText("Note updated");
                snackbar.show();
                break;
            case 3:
                snackbar.setText("Text size updated");
                snackbar.show();
                mMyAdapter.invalidateItems();
                break;
        }
    }


    private void initView() {
        mFloatingActionButton = findViewById(R.id.floatingActionButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }


    private void adddNote(Note note) {
        new Thread(() -> {
           // mNoteDB.getNoteDAO().insert(note);
           getContentResolver().insert(CONTENT_URI, ConvertUtils.getNoteContentValues(note));
            downloadNotes();
        }).start();
    }

    private void updateNote(Note note) {

        new Thread(() -> {
          //  mNoteDB.getNoteDAO().update(note);
           getContentResolver().update(CONTENT_URI,ConvertUtils.getNoteContentValues(note),null,null);
            downloadNotes();
        }).start();
    }

    @SuppressLint("StaticFieldLeak")
    private void downloadNotes() {
        new AsyncTask<Void, Void, List<Note>>() {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                Cursor cursor = getContentResolver().query(CONTENT_URI,new String[]{
                        "id",
                        "title",
                        "date",
                        "color"},null,null,null);
                return ConvertUtils.pareseCursorToNotes(cursor);
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                mMyAdapter.setNotes(notes);
            }
        }.execute();
    }

    private void deleteNote(Note note) {
        new Thread(() -> {
         //   mNoteDB.getNoteDAO().deleteById(note.getId());
            getContentResolver().delete(CONTENT_URI, String.valueOf(note.getId()), null);
            downloadNotes();
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting_menu) {
            Intent intent = SettingActivity.newIntent(MainActivity.this);
            startActivityForResult(intent, 3);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getContentResolver().registerContentObserver(CONTENT_URI,true,mContentObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // getContentResolver().unregisterContentObserver(mContentObserver); Если мы хотим чтобы изменения прилетали даже когда открыто первое приложение
    }
}
