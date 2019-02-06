package ru.sberbank.homework10_second;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.sberbank.homework10.model.Note;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final OnClickListener mOpenListener;
    private final OnLongClickListener mDeleteListener;
    private List<Note> mListData;
    private int mNoteFontSize;
    private Context mContext;


    public MyAdapter(Context context, OnClickListener openListener, OnLongClickListener deleteListener) {
        mOpenListener = openListener;
        mDeleteListener = deleteListener;
        mListData = new ArrayList<>();
        mContext = context;
        invalidateItems();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.setNote(mListData.get(i), mNoteFontSize);

    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public void setNotes(List<Note> notes) {
        mListData.clear();
        mListData.addAll(notes);
        notifyDataSetChanged();
    }


    void invalidateItems() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mNoteFontSize = preferences.getInt(SettingActivity.PREFERENCE_NOTE_TEXT_SIZE, SettingActivity.DEFAULT_NOTE_TEXT_SIZE);
        notifyDataSetChanged();
    }


    public interface OnClickListener {

        void onClick(Note note);
    }

    public interface OnLongClickListener {
        void onLongClick(Note note);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private Note note;
        private CardView cardView;
        private TextView title;
        private TextView text;
        private TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
            initListeners();
        }

        private void initListeners() {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOpenListener.onClick(note);
                }
            });


            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mDeleteListener.onLongClick(note);
                    return true;
                }
            });
        }

        private void initViews() {
            cardView = itemView.findViewById(R.id.cardView);
            text = itemView.findViewById(R.id.text);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
        }

        public void setNote(Note note, int fontSize) {
            this.note = note;
            title.setText(note.getTitle());
            text.setText(note.getText());
            cardView.setBackgroundColor(note.getColor());
            date.setText(note.getCreationDate());
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, mNoteFontSize);
        }
    }


}