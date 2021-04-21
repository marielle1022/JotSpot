package edu.neu.madcourse.jotspot.moods;

// Object that will be used in the Adapter
public class MoodHistRecyclerItemCard {

    // Remember: "final" means that once a variable has been assigned, its value can't change
    // Can't be "final with empty constructor

    // Timestamp of entry with this mood
    private String timestamp;
    // String describing mood
    private String mood;

    // Empty constructor and public getters required for getValue -- see Firebase documentation
    private MoodHistRecyclerItemCard() { }

    // Constructor -- takes in arguments and assigns values listed above
    public MoodHistRecyclerItemCard(String timestamp, String mood) {
        this.timestamp = timestamp;
        this.mood = mood;
    }

    // TODO: add option to delete? Otherwise, don't really need onItemClick
    // If item is clicked
//    public void onItemClick(int listPosition) {
//        // Switch value of isItemChecked
//        // isLinkChecked = !isLinkChecked;
//    }

    // Getters for Item Card information -- all are public
    public String getTimestamp() {
        return timestamp;
    }
    // TODO: emoji should display
    public String getMood() {
        return mood;
    }
}
// Note: this was adapted from a portion of a previous assignment written by Marielle, which
// referenced the sample code provided in class.
