package edu.neu.madcourse.jotspot.past_entries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.jotspot.HomeScreenActivity;
import edu.neu.madcourse.jotspot.PastPhotoEntryActivity;
import edu.neu.madcourse.jotspot.PastSentenceActivity;
import edu.neu.madcourse.jotspot.PastTextEntryActivity;
import edu.neu.madcourse.jotspot.PastVoiceEntryActivity;
import edu.neu.madcourse.jotspot.R;
import edu.neu.madcourse.jotspot.firebase_helpers.Entry;
import edu.neu.madcourse.jotspot.firebase_helpers.PhotoEntryParcel;
import edu.neu.madcourse.jotspot.firebase_helpers.SentenceEntryParcel;
import edu.neu.madcourse.jotspot.firebase_helpers.TextEntryParcel;
import edu.neu.madcourse.jotspot.firebase_helpers.VoiceEntryParcel;

public class PastEntriesActivity extends AppCompatActivity {

    // RecyclerView components
    private RecyclerView pastEntriesRecyclerView;
    private PastEntriesRecyclerAdapter pastEntriesAdapter;
    private RecyclerView.LayoutManager pastEntriesLayoutManager;

    // List of past entry items
    private ArrayList<PastEntriesItemCard> entriesList = new ArrayList<>();

    // Current username
    private String username;

    private static final String TEXT_TAG = "edu.neu.madcourse.jotspot.firebase_helpers.TextEntryParcel";
    private static final String VOICE_TAG = "edu.neu.madcourse.jotspot.firebase_helpers.VoiceEntryParcel";
    private static final String PHOTO_TAG = "edu.neu.madcourse.jotspot.firebase_helpers.PhotoEntryParcel";
    private static final String SENTENCE_TAG = "edu.neu.madcourse.jotspot.firebase_helpers.SentenceEntryParcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_entries);

        username = "testUser";

        createRecyclerView();
    }

    // Create RecyclerView
    // Note: adapted from the StickItToEm assignment RecyclerView, which Marielle wrote
    private void createRecyclerView() {
        // Create layout for RecyclerView
        pastEntriesLayoutManager = new LinearLayoutManager(this);
        pastEntriesRecyclerView = findViewById(R.id.past_entries_recycler_view);
        // Setting fixed size means items won't change size
        // Don't need to do recalculations
        pastEntriesRecyclerView.setHasFixedSize(true);
        // Retrieve data from database and add it to the list
        // Note: references Unique Andro Code in how to retrieve data from firebase
        // Set up database reference pointing to Received messages under the current user
        final DatabaseReference db =
                FirebaseDatabase.getInstance().getReference(getString(R.string.users_path,
                        username));
        // Note: new ValueEventListener auto populates onDataChange and onCancelled
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to populate fields for item card
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Get snapshot of each child
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        // Put values from each child into an entry object
                        Entry newEntry = childSnapshot.getValue(Entry.class);
                        // Create an item card from the entry object
                        if (newEntry != null) {
                            PastEntriesItemCard itemCard = new PastEntriesItemCard(newEntry);
                            // Add the item card to the list
                            entriesList.add(itemCard);
                        }
                    }

                    // Create adapter from list of items (created inside if statement)
                    pastEntriesAdapter = new PastEntriesRecyclerAdapter(entriesList);

                    // Set listener for on item clicked
                    PastEntriesRecyclerListener itemClickListener = new PastEntriesRecyclerListener() {
                        @Override
                        public void onItemClick(int position) {
                            String entryType = entriesList.get(position).getPastEntryType();
                            // Note: instruction on making things Parcelable taken from
                            // https://coderwall.com/p/vfbing/passing-objects-between-activities-in-android
                            switch (entryType) {
                                case "TEXT":
                                    TextEntryParcel textEntryParcel = new
                                            TextEntryParcel(entriesList.get(position).getPastEntryType(),
                                            entriesList.get(position).getMood(),
                                            entriesList.get(position).getTimestamp(),
                                            entriesList.get(position).getFormattedDateTime(),
                                            entriesList.get(position).getPastTextEntry());
                                    Intent pastTextEntryIntent = new Intent(PastEntriesActivity.this,
                                            PastTextEntryActivity.class);
                                    pastTextEntryIntent.putExtra(TEXT_TAG, textEntryParcel);
                                    PastEntriesActivity.this.startActivity(pastTextEntryIntent);
                                    break;
                                case "VOICE":
                                    VoiceEntryParcel voiceEntryParcel = new
                                            VoiceEntryParcel(entriesList.get(position).getPastEntryType(),
                                            entriesList.get(position).getMood(),
                                            entriesList.get(position).getTimestamp(),
                                            entriesList.get(position).getFormattedDateTime(),
                                            entriesList.get(position).getVoiceEntryPath());
                                    Intent pastVoiceEntryIntent = new Intent(PastEntriesActivity.this,
                                            PastVoiceEntryActivity.class);
                                    pastVoiceEntryIntent.putExtra(VOICE_TAG, voiceEntryParcel);
                                    PastEntriesActivity.this.startActivity(pastVoiceEntryIntent);
                                    break;
                                case "PHOTO":
                                    PhotoEntryParcel photoEntryParcel = new
                                            PhotoEntryParcel(entriesList.get(position).getPastEntryType(),
                                            entriesList.get(position).getMood(),
                                            entriesList.get(position).getTimestamp(),
                                            entriesList.get(position).getFormattedDateTime(),
                                            entriesList.get(position).getPhotoEntry());
                                    Intent pastPhotoEntryIntent = new Intent(PastEntriesActivity.this,
                                            PastPhotoEntryActivity.class);
                                    pastPhotoEntryIntent.putExtra(PHOTO_TAG, photoEntryParcel);
                                    PastEntriesActivity.this.startActivity(pastPhotoEntryIntent);
                                    break;
                                case "SENTENCE":
                                    SentenceEntryParcel sentenceEntryParcel = new
                                            SentenceEntryParcel(entriesList.get(position).getPastEntryType(),
                                            entriesList.get(position).getMood(),
                                            entriesList.get(position).getTimestamp(),
                                            entriesList.get(position).getFormattedDateTime(),
                                            entriesList.get(position).getPrompt(),
                                            entriesList.get(position).getPastSentenceEntry());
                                    Intent pastSentenceEntryIntent = new Intent(PastEntriesActivity.this,
                                            PastSentenceActivity.class);
                                    pastSentenceEntryIntent.putExtra(SENTENCE_TAG, sentenceEntryParcel);
                                    PastEntriesActivity.this.startActivity(pastSentenceEntryIntent);
                                    break;
                            }
                        }
                    };
                    pastEntriesAdapter.setOnItemClickListener(itemClickListener);
                    // Set recycler view using adapter
                    pastEntriesRecyclerView.setAdapter(pastEntriesAdapter);
                    // Set layout manager
                    pastEntriesRecyclerView.setLayoutManager(pastEntriesLayoutManager);
                } else {
                    Log.w("recycler", "in createRecyclerView, snapshot doesn't exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("recycler", "in createRecyclerView, onCancelled");
            }
        });
    }
}

// Note: this was adapted from Marielle's previous assignment, which referenced the sample code
// provided in class.