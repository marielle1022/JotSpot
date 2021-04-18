package edu.neu.madcourse.jotspot.past_entries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.jotspot.R;
import edu.neu.madcourse.jotspot.firebase_helpers.Entry;

public class PastEntriesActivity extends AppCompatActivity {

    // RecyclerView components
    private RecyclerView pastEntriesRecyclerView;
    private PastEntriesRecyclerAdapter pastEntriesAdapter;
    private RecyclerView.LayoutManager pastEntriesLayoutManager;

    private ImageButton photo1;
    private ImageButton photo2;
    private ImageButton photo3;

    // List of past entry items
    private ArrayList<PastEntriesItemCard> entriesList = new ArrayList<>();

    // Current username
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_entries);

        username = "testUser";

        createRecyclerView();

        photo1 = (ImageButton) findViewById(R.id.photo_entry1_view);
        photo2 = (ImageButton) findViewById(R.id.photo_entry2_view);
        photo3 = (ImageButton) findViewById(R.id.photo_entry3_view);

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
        // TODO: need to get path right -- Users/"username"/Received messages ?
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
                        Log.w("snapshot", String.valueOf(childSnapshot));
                        // Put values from each child into an entry object
                        Entry newEntry = childSnapshot.getValue(Entry.class);
                        // Create an item card from the entry object
                        assert newEntry != null;
                        PastEntriesItemCard itemCard = new PastEntriesItemCard(newEntry);
                        // Add the item card to the list
                        entriesList.add(itemCard);
                    }
                    // Create adapter from list of items (created inside if statement)
                    pastEntriesAdapter = new PastEntriesRecyclerAdapter(entriesList);
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