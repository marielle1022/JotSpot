package edu.neu.madcourse.jotspot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.neu.madcourse.jotspot.firebase_helpers.Entry;
import edu.neu.madcourse.jotspot.firebase_helpers.EntryType;
import edu.neu.madcourse.jotspot.firebase_helpers.User;


public class TextEntryScreenActivity extends AppCompatActivity {

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private String timestamp;

    private String username = "testUser";
    private User user;

    EditText textEntryView;


    Button discardTextEntry;
    Button saveTextEntry;

    private String TAG = "EntryTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry_screen);

        // TODO: figure out a better way to save username
//        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//        String username = sharedPreferences.getString("USERNAME_PREFERENCES", "user_not_found");

        // Firebase database objects
        // TODO: figure out if this is the best place to do this
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // TODO: set up actual user
        user = new User(username);

        // TODO: need Firebase tokens?

        textEntryView = (EditText) findViewById(R.id.text_entry_box);

        discardTextEntry = (Button) findViewById(R.id.discard_text_button);

        discardTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                discard();
                test();
            }
        });

        saveTextEntry = (Button) findViewById(R.id.save_text_button);

        saveTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTextEntry();
            }
        });

    }

    // Discard text entry
    private void discard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm discard");
        builder.setMessage("Are you sure you want to discard this entry?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    // TODO: figure out dialogs?
    // Save the text entry
    private void saveTextEntry() {
        String textForEntry = textEntryView.getText().toString();
        addTextEntryToDb(textForEntry);
        Log.v("TEST", "did this get added to db?");
    }

    // Create text entry and add to the database
    private void addTextEntryToDb(String inTextEntry) {
        timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            Entry textEntryObj = new Entry("TEXT", timestamp, inTextEntry);
            // Method to add to firebase taken from Firebase Realtime Database
            // documentation on saving data
            DatabaseReference usersRef = databaseRef.child("users");
            usersRef.child(username).child(timestamp).setValue(textEntryObj);
        } catch (ParseException e) {
            Log.w(TAG, e);
        }
        // Method to add to firebase taken from Firebase Realtime Database
        // documentation on saving data
//        DatabaseReference usersRef = databaseRef.child("users");
        // TODO: figure out why setValueAsync doesn't work
        // https://firebase.google.com/docs/database/admin/save-data
        // Use push() to set unique key
//        usersRef.child(username).push().setValue(inTextEntry);
//        usersRef.child(username).child(timestamp).setValue(textEntryObj);
    }

    // TODO: Use this to get text entry from db
    private void test() {
        databaseRef.child("users").child(username).child(timestamp).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to create Entry object
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Entry textEntry = snapshot.getValue(Entry.class);
                    if (textEntry != null) {
                        try {
                            Log.w(TAG, textEntry.convertTimestamp());
                        } catch (ParseException e) {
                            Log.w(TAG, e);
                        }
                    }
                } else {
                    Log.w(TAG, "doesn't exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}