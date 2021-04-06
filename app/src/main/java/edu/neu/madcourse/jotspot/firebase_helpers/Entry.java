package edu.neu.madcourse.jotspot.firebase_helpers;

import android.net.Uri;

public class Entry {

    private EntryType type;
    private String timestamp;
    private String textEntry;
    private Uri voiceEntry;

    // Empty (default) constructor needed for calls to DataSnapshot.getValue
    public Entry() {}

    public Entry(String inType, String inTimestamp, String inTextEntry) {
        type = setEntryType(inType);
        timestamp = inTimestamp;
        textEntry = inTextEntry;
    }

    // TODO: getters needed for everything

    public EntryType getEntryType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getTextEntry() {
        return textEntry;
    }

    // Set the entry type based on the string passed in
    private EntryType setEntryType(String strType) {
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

}
