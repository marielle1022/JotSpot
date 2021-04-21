package edu.neu.madcourse.jotspot.firebase_helpers;

import android.net.Uri;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Entry {

    private EntryType etType;
    private String entryType;

    // String representing mood
    // Options: "NONE", "VERY HAPPY", "HAPPY", "NEUTRAL", "SLIGHTLY BUMMED", "SAD", "WEEPY"
    private String mood;

    // Auto-generated timestamp
    private String timestamp;
    // Timestamp with "_" removed
    private String timestampParsed;
    // Formatted date and time based on timestamp
    private Date dateTime;
    // Formatted date and time string based on timestamp
    private String dateTimeStr;
//    // Formatted date based on timestamp
//    private LocalDate dateObj;
//    // Formatted time based on timestamp
//    private LocalTime timeObj;

    // Text for text entry
    private String textEntry;
    // List of Cloud Storage filepaths for photos for an entry
    private List<String> photoEntry;
//    private Uri voiceEntry;
    // Cloud Storage filepath for voice recording for an entry
    private String voiceEntry;
    // Text for prompt
    private String prompt;
    // Text for one-sentence entry
    private String sentenceEntry;

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
    public Entry(String inType, String inTimestamp, String inEntry, String inMood) throws ParseException {
        entryType = inType;
        if (inType.equals("VOICE")) {
            voiceEntry = inEntry;
        } else {
            textEntry = inEntry;
        }
        timestamp = inTimestamp;
        dateTimeStr = convertTimestamp();
        mood = inMood;
    }

    // Create entry object for one-sentence prompt
    public Entry(String inType, String inTimestamp, String inPrompt, String inEntry, String inMood) throws ParseException {
        entryType = inType;
        prompt = inPrompt;
        sentenceEntry = inEntry;
        timestamp = inTimestamp;
        dateTimeStr = convertTimestamp();
    }

    // Create entry object for photo entry
    public Entry(String inType, String inTimestamp, List<String> inPhotoEntry, String inMood) throws ParseException {
        entryType = inType;
        timestamp = inTimestamp;
        photoEntry = inPhotoEntry;
        dateTimeStr = convertTimestamp();
        mood = inMood;
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

    public String getSentenceEntry() {
        return sentenceEntry;
    }

    public void setSentenceEntry(String sentenceEntry) {
        this.sentenceEntry = sentenceEntry;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
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

    public String getDateTimeStr() {
        return dateTimeStr;
    }

    public void setDateTimeStr(String dateTimeStr) {
        this.dateTimeStr = dateTimeStr;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
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

    // Convert timestamp to String of date and time
    public String convertTimestamp() throws ParseException {
        timestampParsed = timestamp.replace("_", "");
        dateTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(timestampParsed);
        if (dateTime != null) {
            dateTimeStr = dateTime.toString();
            return dateTimeStr;
        } else  {
            return null;
        }
    }

}
