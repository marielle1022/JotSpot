package edu.neu.madcourse.jotspot.firebase_helpers;

import android.net.Uri;

import java.util.List;

public class Entry {

    private EntryType etType;
    private String entryType;
    private String timestamp;
    private String textEntry;
    // List of Cloud Storage filepaths for photos for an entry
    private List<String> photoEntry;
//    private Uri voiceEntry;
    // Cloud Storage filepath for voice recording for an entry
    private String voiceEntry;

    // Empty (default) constructor needed for calls to DataSnapshot.getValue
    public Entry() {}

//    // Create entry object for text or voice entry
//    public Entry(EntryType inType, String inTimestamp, String inEntry) {
//        etType = inType;
//        if (inType == EntryType.VOICE) {
//            entryType = "VOICE";
//            voiceEntry = inEntry;
//        } else {
//            entryType = "TEXT";
//            textEntry = inEntry;
//        }
//        timestamp = inTimestamp;
//    }

    // Create entry object for text or voice entry
    public Entry(String inType, String inTimestamp, String inEntry) {
        entryType = inType;
        if (inType == "VOICE") {
            voiceEntry = inEntry;
        } else {
            textEntry = inEntry;
        }
        timestamp = inTimestamp;
    }

//    // Create entry object for photo entry
//    public Entry(String inType, String inTimestamp, List<String> inPhotoEntry) {
//        entryType = inType;
//        timestamp = inTimestamp;
//        photoEntry = inPhotoEntry;
//    }

    // TODO: use toString for EntryType to string?
//    // Create entry object for photo entry
//    public Entry(EntryType inType, String inTimestamp, List<String> inPhotoEntry) {
//        etType = inType;
//        entryType = "PHOTO";
//        timestamp = inTimestamp;
//        photoEntry = inPhotoEntry;
//    }

    // Create entry object for photo entry
    public Entry(String inType, String inTimestamp, List<String> inPhotoEntry) {
        entryType = inType;
        timestamp = inTimestamp;
        photoEntry = inPhotoEntry;
    }

    // TODO: getters/setters needed for everything

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTextEntry() {
        return textEntry;
    }

    public void setTextEntry(String textEntry) {
        this.textEntry = textEntry;
    }

    public List<String> getPhotoEntry() {
        return photoEntry;
    }

    public void setPhotoEntry(List<String> entry) {
        this.photoEntry = entry;
    }

    public String getVoiceEntry() {
        return voiceEntry;
    }

    public void setVoiceEntry(String entry) {
        this.voiceEntry = entry;
    }

//    public Uri getVoiceEntry() {
//        return voiceEntry;
//    }
//
//    public void setVoiceEntry(Uri entry) {
//        this.voiceEntry = entry;
//    }

    // Set the entry type based on the string passed in
    private EntryType setEtType(String strType) {
        EntryType tempType;
        switch(strType.toUpperCase()) {
            case "VOICE":
                tempType = EntryType.VOICE;
                break;
            case "IMAGE":
                tempType = EntryType.IMAGE;
                break;
            default:
                tempType = EntryType.TEXT;
                break;
        }
        return tempType;
    }

    public EntryType getEtType() {
        return etType;
    }

}
