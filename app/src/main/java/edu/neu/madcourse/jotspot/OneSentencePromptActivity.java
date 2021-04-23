package edu.neu.madcourse.jotspot;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.neu.madcourse.jotspot.firebase_helpers.Entry;
import edu.neu.madcourse.jotspot.firebase_helpers.User;

public class OneSentencePromptActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner promptSpinner;
    private String prompt;

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private String timestamp;

    private SharedPreferences sharedPreferences;
    private final String defaultString = "default";

    private String username;

    EditText sentenceEntryView;

    Button discardSentenceButton;
    Button saveSentenceButton;

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
        setContentView(R.layout.activity_one_sentence_prompt);

        // Referenced Android documentation to retrieve data from Shared Preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        promptSpinner = (Spinner) findViewById(R.id.prompt_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prompt_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        promptSpinner.setAdapter(adapter);
        promptSpinner.setOnItemSelectedListener(this);

        // Firebase database objects
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // TODO: need Firebase tokens?

        mood = strFeeling0;

        getButtonViews();

        setFeelingOnClicks();

        sentenceEntryView = (EditText) findViewById(R.id.one_sentence_entry);

        discardSentenceButton = (Button) findViewById(R.id.sentence_discard_button);

        discardSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discard();
            }
        });

        saveSentenceButton = (Button) findViewById(R.id.sentence_save_button);

        saveSentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSentenceEntry();
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
                sentenceEntryView.setText("");
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

    // Save the sentence entry
    private void saveSentenceEntry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm save");
        builder.setMessage("Are you sure you are finished with this entry?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                String sentenceForEntry = sentenceEntryView.getText().toString();
                if (prompt != null) {
                    SentenceDbTask task = new SentenceDbTask();
                    task.execute(sentenceForEntry);
                    sentenceEntryView.setText("");
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please choose a prompt.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
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

    // Create sentence entry and add to the database
    private void addSentenceEntryToDb(String inSentenceEntry) {
        timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            Entry sentenceEntryObj = new Entry("SENTENCE", timestamp, prompt, inSentenceEntry, mood);
            // Method to add to firebase taken from Firebase Realtime Database
            // documentation on saving data
            DatabaseReference usersRef = databaseRef.child(getString(R.string.entries_path, username));
            usersRef.child(timestamp).setValue(sentenceEntryObj);
            mood = strFeeling0;
        } catch (ParseException e) {
            Log.w("SentenceCatch", e);
        }
    }

    @Override
    // Note: basis taken from https://www.tutorialspoint.com/how-to-get-spinner-value-in-android
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        // If the text is not "Choose a prompt!", save it as the prompt
        if (!text.equals(getString(R.string.choose_a_prompt))) {
            prompt = text;
        } else {
            prompt = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Move sentence "upload to db" to a separate task
    // AsyncTask<params, progress, results>
    private class SentenceDbTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String text = strings[0];
            addSentenceEntryToDb(text);
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