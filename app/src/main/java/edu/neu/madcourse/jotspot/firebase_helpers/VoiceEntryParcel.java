package edu.neu.madcourse.jotspot.firebase_helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class VoiceEntryParcel implements Parcelable {

    private String entryType;

    // String representing mood
    // Options: "NONE", "VERY HAPPY", "HAPPY", "NEUTRAL", "SLIGHTLY BUMMED", "SAD", "WEEPY"
    private String mood;

    // Auto-generated timestamp
    private String timestamp;

    // Formatted date and time string based on timestamp
    private String dateTimeStr;

    // Text for text entry
    private String voiceEntryPath;

    public VoiceEntryParcel(String inEntryType, String inMood, String inTimestamp, String inDateTimeStr, String inVoiceEntryPath) {
        this.entryType = inEntryType;
        this.mood = inMood;
        this.timestamp = inTimestamp;
        this.dateTimeStr = inDateTimeStr;
        this.voiceEntryPath = inVoiceEntryPath;
    }

    // Used when reconstructing VoiceEntryParcel object from Parcel
    protected VoiceEntryParcel(Parcel in) {
        this.entryType = in.readString();
        this.mood = in.readString();
        this.timestamp = in.readString();
        this.dateTimeStr = in.readString();
        this.voiceEntryPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entryType);
        dest.writeString(mood);
        dest.writeString(timestamp);
        dest.writeString(dateTimeStr);
        dest.writeString(voiceEntryPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VoiceEntryParcel> CREATOR = new Creator<VoiceEntryParcel>() {
        @Override
        public VoiceEntryParcel createFromParcel(Parcel in) {
            return new VoiceEntryParcel(in);
        }

        @Override
        public VoiceEntryParcel[] newArray(int size) {
            return new VoiceEntryParcel[size];
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
    public String getVoiceEntryPath() {
        return voiceEntryPath;
    }
}
