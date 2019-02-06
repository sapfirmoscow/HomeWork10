package ru.sberbank.homework10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    public static final String PREFERENCE_NOTE_TEXT_SIZE = "note_text_size";
    public static final int DEFAULT_NOTE_TEXT_SIZE = 20;
    private SeekBar seekBar;
    private TextView textView;

    //значения для ползунка
    private int step = 1;
    private int max = 30;
    private int min = 14;
    private int currentValue = 0;

    private SharedPreferences mSharedPreferences;

    public static final Intent newIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListeners();
        initValues();
        initSeekbar();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initValues() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingActivity.this);
    }

    private void initListeners() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentValue = min + (progress * step);
                textView.setText(String.valueOf(currentValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                saveTextSize(currentValue);
            }
        });
    }


    private void initView() {
        seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView);
    }


    private void initSeekbar() {
        seekBar.setMax((max - min) / step);
        seekBar.setProgress(getTextSize() - min);
        textView.setText(String.valueOf(getTextSize()));
    }


    private int getTextSize() {
        return mSharedPreferences.getInt(PREFERENCE_NOTE_TEXT_SIZE, DEFAULT_NOTE_TEXT_SIZE);
    }

    private void saveTextSize(int textSize) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(PREFERENCE_NOTE_TEXT_SIZE, textSize);
        editor.apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(3);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(3);
        finish();
    }
}
