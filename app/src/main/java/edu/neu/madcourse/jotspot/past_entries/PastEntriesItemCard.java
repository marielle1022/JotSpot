package edu.neu.madcourse.jotspot.past_entries;

import java.util.List;

import edu.neu.madcourse.jotspot.firebase_helpers.Entry;

// Object that will be used in the Adapter
public class PastEntriesItemCard {
    // TODO: declare variables for entry type, timestamp, entry, etc

    // Type of entry
    private String pastEntryType;
    // Date and time to be listed for each entry
    private String formattedDateTime;
    // Unformatted timestamp
    private String timestamp;
    // Text entry
    private String pastTextEntry;
    // Sentence entry
    private String pastSentenceEntry;
    // Prompt for sentence entry
    private String prompt;
    // List of photo file paths for past entry
    private List<String> listPhotoEntryStorage;
    // Photo file path for past entry
    private String pastPhoto1;
    private String pastPhoto2;
    private String pastPhoto3;
    // Audio file path for past entry
    private String voiceEntryPath;

    // String for mood
    private String mood;

    // TODO: set multiple different constructors?

    // Note: empty constructor needed for database reference
//    public PastEntriesItemCard() { }

    public PastEntriesItemCard(Entry entry) {
        this.pastEntryType = entry.getEntryType().toUpperCase();
        this.formattedDateTime = entry.getDateTimeStr();
        this.timestamp = entry.getTimestamp();
        this.mood = entry.getMood();
        switch (this.pastEntryType) {
            case "TEXT":
                this.pastTextEntry = entry.getTextEntry();
                break;
            case "PHOTO":
                listPhotoEntryStorage = entry.getPhotoEntry();
                for (int i = 0; i < listPhotoEntryStorage.size(); i++) {
                    if (i == 0) {
                        this.pastPhoto1 = listPhotoEntryStorage.get(i);
                    } else if (i == 1) {
                        this.pastPhoto2 = listPhotoEntryStorage.get(i);
                    } else {
                        this.pastPhoto3 = listPhotoEntryStorage.get(i);
                    }
                }
                break;
            case "VOICE":
                voiceEntryPath = entry.getVoiceEntry();
                break;
            case "SENTENCE":
                this.pastSentenceEntry = entry.getSentenceEntry();
                this.prompt = entry.getPrompt();
                break;
            default:
                break;
        }
    }

    // TODO: set getters for item card information
    // Getters for Item Card information -- all are public
    public String getPastEntryType() {
        return this.pastEntryType;
    }
    public String getFormattedDateTime() {
        return this.formattedDateTime;
    }
    public String getPastTextEntry() {
        return this.pastTextEntry;
    }
    public String getPastSentenceEntry() {
        return this.pastSentenceEntry;
    }
    public String getPrompt() {
        return this.prompt;
    }
    public List<String> getPhotoEntry() {
        return this.listPhotoEntryStorage;
    }
    public String getPastPhoto1() {
        return this.pastPhoto1;
    }
    public String getPastPhoto2() {
        return this.pastPhoto2;
    }
    public String getPastPhoto3() {
        return this.pastPhoto3;
    }
    public String getVoiceEntryPath() {
        return this.voiceEntryPath;
    }
    public String getTimestamp() {
        return this.timestamp;
    }
    public String getMood() {
        return this.mood;
    }
}

// Note: this was adapted from Marielle's previous assignment, which referenced the sample code
// provided in class.