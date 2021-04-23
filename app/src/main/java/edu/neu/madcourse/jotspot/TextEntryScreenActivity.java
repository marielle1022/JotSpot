package edu.neu.madcourse.jotspot;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import edu.neu.madcourse.jotspot.firebase_helpers.ThreadTaskHelper;

import edu.neu.madcourse.jotspot.firebase_helpers.User;


public class TextEntryScreenActivity extends AppCompatActivity {

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private String timestamp;

    private SharedPreferences sharedPreferences;
    private final String defaultString = "default";

    private String username;

    EditText textEntryView;

    Button discardTextEntry;
    Button saveTextEntry;

    private String TAG = "EntryTag";

    // Variables storing feelings buttons
    // Options: "NONE", "VERY HAPPY", "HAPPY", "NEUTRAL", "SLIGHTLY BUMMED", "SAD", "WEEPY"
    private ImageButton feeling1; // VERY HAPPY
    private ImageButton feeling2; // HAPPY
    private ImageButton feeling3; // NEUTRAL
    private ImageButton feeling4; // SLIGHTLY BUMMED
    private ImageButton feeling5; // SAD
    private ImageButton feeling6; // WEEPY

    // Strings for feelings
    private static final String strFeeling1 = "VERY HAPPY";
    private static final String strFeeling2 = "HAPPY";
    private static final String strFeeling3 = "NEUTRAL";
    private static final String strFeeling4 = "SLIGHTLY BUMMED";
    private static final String strFeeling5 = "SAD";
    private static final String strFeeling6 = "WEEPY";
    private static final String strFeeling0 = "NONE";

    // Variables storing mood
    private String mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry_screen);

        // Referenced Android documentation to retrieve data from Shared Preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        // Firebase database objects
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        mood = strFeeling0;

        getButtonViews();

        setFeelingOnClicks();

        // TODO: need Firebase tokens?

        textEntryView = (EditText) findViewById(R.id.one_sentence_entry);

        discardTextEntry = (Button) findViewById(R.id.discard_text_button);

        discardTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discard();
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
                textEntryView.setText("");
                mood = strFeeling0;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm save");
        builder.setMessage("Are you sure you are finished with this entry?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String textForEntry = textEntryView.getText().toString();
                TextDbTask task = new TextDbTask();
                task.execute(textForEntry);
                Toast.makeText(getApplicationContext(), "Text entry saved successfully.", Toast.LENGTH_LONG).show();
//                Toast toast = Toast.makeText(getApplicationContext(), "Text entry saved successfully.", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
//                toast.show();
                textEntryView.setText("");
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

    // Create text entry and add to the database
    private void addTextEntryToDb(String inTextEntry) {
        timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            Entry textEntryObj = new Entry("TEXT", timestamp, inTextEntry, mood);
            // Method to add to firebase taken from Firebase Realtime Database
            // documentation on saving data
            DatabaseReference usersRef = databaseRef.child(getString(R.string.entries_path, username));
            usersRef.child(timestamp).setValue(textEntryObj);
            mood = strFeeling0;
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

    // Move text "upload to db" to a separate task
    // AsyncTask<params, progress, results>
    private class TextDbTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String text = strings[0];
            addTextEntryToDb(text);
            return null;
        }
    }

    // Set the views for all buttons
    private void getButtonViews() {
        feeling1 = findViewById(R.id.feelingButton1);
        feeling2 = findViewById(R.id.feelingButton2);
        feeling3 = findViewById(R.id.feelingButton3);
        feeling4 = findViewById(R.id.feelingButton4);
        feeling5 = findViewById(R.id.feelingButton5);
        feeling6 = findViewById(R.id.feelingButton6);
    }

    // Set onClicks for all mood buttons
    private void setFeelingOnClicks() {
        feeling1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = strFeeling1;
            }
        });

        feeling2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = strFeeling2;
            }
        });
        feeling3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = strFeeling3;
            }
        });
        feeling4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = strFeeling4;
            }
        });
        feeling5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = strFeeling5;
            }
        });
        feeling6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = strFeeling6;
            }
        });
    }

}