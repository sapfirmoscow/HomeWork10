package ru.sberbank.homework10.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.sberbank.homework10_second.util.ColorGenerator;
import ru.sberbank.homework10_second.util.UUIDGenerator;


public class Note implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private String mTitle;


    private String mText;


    private String mCreationDate;


    private int mColor;


    private int mId;


    public Note() {
        mId = UUIDGenerator.generateUniqueId();
        mColor = ColorGenerator.generateRandomColor();
        mCreationDate = new SimpleDateFormat("d MMM 'at' HH:mm").format(new Date());
    }

    public Note(String mTitle, String mText, String mCreationDate, int mColor, int mId) {
        this.mTitle = mTitle;
        this.mText = mText;
        this.mCreationDate = mCreationDate;
        this.mColor = mColor;
        this.mId = mId;
    }

    protected Note(Parcel in) {
        mTitle = in.readString();
        mText = in.readString();
        mCreationDate = in.readString();
        mColor = in.readInt();
        mId = in.readInt();
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    public String getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(String mCreationDate) {
        this.mCreationDate = mCreationDate;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mText);
        dest.writeString(mCreationDate);
        dest.writeInt(mColor);
        dest.writeInt(mId);
    }

}
