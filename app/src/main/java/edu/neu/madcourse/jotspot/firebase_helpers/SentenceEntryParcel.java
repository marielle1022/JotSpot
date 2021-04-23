package edu.neu.madcourse.jotspot.firebase_helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class SentenceEntryParcel implements Parcelable {

    private String entryType;

    // String representing mood
    // Options: "NONE", "VERY HAPPY", "HAPPY", "NEUTRAL", "SLIGHTLY BUMMED", "SAD", "WEEPY"
    private String mood;

    // Auto-generated timestamp
    private String timestamp;

    // Formatted date and time string based on timestamp
    private String dateTimeStr;

    // Prompt
    private String prompt;

    // Text for sentence entry
    private String sentenceEntry;

    public SentenceEntryParcel(String inEntryType, String inMood, String inTimestamp,
                               String inDateTimeStr, String inPrompt, String inSentenceEntry) {
        this.entryType = inEntryType;
        this.mood = inMood;
        this.timestamp = inTimestamp;
        this.dateTimeStr = inDateTimeStr;
        this.prompt = inPrompt;
        this.sentenceEntry = inSentenceEntry;
    }

    // Used when reconstructing SentenceEntryParcel object from parcel
    protected SentenceEntryParcel(Parcel in) {
        this.entryType = in.readString();
        this.mood = in.readString();
        this.timestamp = in.readString();
        this.dateTimeStr = in.readString();
        this.prompt = in.readString();
        this.sentenceEntry = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entryType);
        dest.writeString(mood);
        dest.writeString(timestamp);
        dest.writeString(dateTimeStr);
        dest.writeString(prompt);
        dest.writeString(sentenceEntry);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SentenceEntryParcel> CREATOR = new Creator<SentenceEntryParcel>() {
        @Override
        public SentenceEntryParcel createFromParcel(Parcel in) {
            return new SentenceEntryParcel(in);
        }

        @Override
        public SentenceEntryParcel[] newArray(int size) {
            return new SentenceEntryParcel[size];
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
    public String getPrompt() {
        return prompt;
    }
    public String getSentenceEntry() {
        return sentenceEntry;
    }
}
