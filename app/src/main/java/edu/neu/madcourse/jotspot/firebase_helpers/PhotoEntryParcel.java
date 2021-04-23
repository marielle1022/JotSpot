package edu.neu.madcourse.jotspot.firebase_helpers;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PhotoEntryParcel implements Parcelable {

    private String entryType;

    // String representing mood
    // Options: "NONE", "VERY HAPPY", "HAPPY", "NEUTRAL", "SLIGHTLY BUMMED", "SAD", "WEEPY"
    private String mood;

    // Auto-generated timestamp
    private String timestamp;

    // Formatted date and time string based on timestamp
    private String dateTimeStr;

    // List of Cloud Storage filepaths for photos for an entry
    private List<String> photoEntry;

    public PhotoEntryParcel(String inEntryType, String inMood, String inTimestamp, String inDateTimeStr, List<String> inPhotoEntry) {
        this.entryType = inEntryType;
        this.mood = inMood;
        this.timestamp = inTimestamp;
        this.dateTimeStr = inDateTimeStr;
        this.photoEntry = inPhotoEntry;
    }

    // Used when reconstructing a PhotoEntryParcel object from a Parcel
    protected PhotoEntryParcel(Parcel in) {
        this.entryType = in.readString();
        this.mood = in.readString();
        this.timestamp = in.readString();
        this.dateTimeStr = in.readString();
        this.photoEntry = new ArrayList<String>();
        in.readStringList(photoEntry);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entryType);
        dest.writeString(mood);
        dest.writeString(timestamp);
        dest.writeString(dateTimeStr);
        dest.writeStringList(photoEntry);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoEntryParcel> CREATOR = new Creator<PhotoEntryParcel>() {
        @Override
        public PhotoEntryParcel createFromParcel(Parcel in) {
            return new PhotoEntryParcel(in);
        }

        @Override
        public PhotoEntryParcel[] newArray(int size) {
            return new PhotoEntryParcel[size];
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
    public List<String> getPhotoEntry() {
        return photoEntry;
    }
}
