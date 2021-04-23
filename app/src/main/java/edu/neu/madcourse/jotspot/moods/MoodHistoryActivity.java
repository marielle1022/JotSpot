package edu.neu.madcourse.jotspot.moods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.madcourse.jotspot.R;
import edu.neu.madcourse.jotspot.firebase_helpers.Entry;

public class MoodHistoryActivity extends AppCompatActivity {

    // RecyclerView components
    private RecyclerView moodHistRecyclerView;
    private MoodHistRecyclerAdapter moodHistAdapter;
    private RecyclerView.LayoutManager moodHistLayoutManager;

    // List of link items
    private ArrayList<MoodHistRecyclerItemCard> moodList = new ArrayList<>();

    // Current user
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_history);

        username = "testUser";

        createRecyclerView();
    }

    // Create RecyclerView
    // Note: adapted from the StickItToEm assignment RecyclerView, which Marielle wrote
    private void createRecyclerView() {
        // Create layout for RecyclerView
        moodHistLayoutManager = new LinearLayoutManager(this);
        moodHistRecyclerView = findViewById(R.id.mood_hist_recyclerview);
        // Setting fixed size means items won't change size
        // Don't need to do recalculations
        moodHistRecyclerView.setHasFixedSize(true);
        // Retrieve data from database and add it to the list
        // Note: references Unique Andro Code in how to retrieve data from firebase
        // Set up database reference pointing to Received messages under the current user
        final DatabaseReference db =
                FirebaseDatabase.getInstance().getReference(getString(R.string.entries_path,
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
                            if (newEntry.getMood() != null && !newEntry.getMood().toUpperCase().equals("NONE")) {
                                MoodHistRecyclerItemCard itemCard = new
                                        MoodHistRecyclerItemCard(newEntry.getDateTimeStr(),
                                        newEntry.getMood());
                                // Add the item card to the list
                                moodList.add(itemCard);
                            }
                        }
                    }
                    // Create adapter from list of items (created inside if statement)
                    moodHistAdapter = new MoodHistRecyclerAdapter(moodList);
                    // Set recycler view using adapter
                    moodHistRecyclerView.setAdapter(moodHistAdapter);
                    // Set layout manager
                    moodHistRecyclerView.setLayoutManager(moodHistLayoutManager);
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


//        // Create adapter from list of items
//        msgHistAdapter = new MsgHistoryRecyclerAdapter(msgList);

        // Create item click listener
//        MsgHistoryRecyclerListener itemClickListener = new MsgHistoryRecyclerListener() {
//            @Override
//            public void onItemClick(int listPosition) {
//                linkList.get(listPosition).onItemClick(listPosition);
//                msgHistAdapter.notifyItemChanged(listPosition);
//            }
//        };

        // Set item click listener for adapter
//        msgHistAdapter.setOnItemClickListener(itemClickListener);

//        // Set recycler view using adapter
//        msgHistRecyclerView.setAdapter(msgHistAdapter);
//        // Set layout manager
//        msgHistRecyclerView.setLayoutManager(msgHistLayoutManager);
}

// Note: this was adapted from a portion of a previous assignment written by Marielle, which
// referenced the sample code provided in class.