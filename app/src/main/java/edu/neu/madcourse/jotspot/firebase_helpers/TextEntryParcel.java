package edu.neu.madcourse.jotspot.firebase_helpers;


import android.os.Parcel;
import android.os.Parcelable;

public class TextEntryParcel implements Parcelable {

    private String entryType;

    // String representing mood
    // Options: "NONE", "VERY HAPPY", "HAPPY", "NEUTRAL", "SLIGHTLY BUMMED", "SAD", "WEEPY"
    private String mood;

    // Auto-generated timestamp
    private String timestamp;

    // Formatted date and time string based on timestamp
    private String dateTimeStr;

    // Text for text entry
    private String textEntry;

    public TextEntryParcel(String inEntryType, String inMood, String inTimestamp, String inDateTimeStr, String inTextEntryText) {
        this.entryType = inEntryType;
        this.mood = inMood;
        this.timestamp = inTimestamp;
        this.dateTimeStr = inDateTimeStr;
        this.textEntry = inTextEntryText;
    }

    // Used when reconstructing TextEntryParcel object from Parcel
    protected TextEntryParcel(Parcel in) {
        this.entryType = in.readString();
        this.mood = in.readString();
        this.timestamp = in.readString();
        this.dateTimeStr = in.readString();
        this.textEntry = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entryType);
        dest.writeString(mood);
        dest.writeString(timestamp);
        dest.writeString(dateTimeStr);
        dest.writeString(textEntry);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TextEntryParcel> CREATOR = new Creator<TextEntryParcel>() {
        @Override
        public TextEntryParcel createFromParcel(Parcel in) {
            return new TextEntryParcel(in);
        }

        @Override
        public TextEntryParcel[] newArray(int size) {
            return new TextEntryParcel[size];
        }
    };

    public String getEntryType() {
        return entryType;
    }
    public String getMood() {
        return mood;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getDateTimeStr() {
        return dateTimeStr;
    }
    public String getTextEntry() {
        return textEntry;
    }
}
