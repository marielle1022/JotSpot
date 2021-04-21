package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

    private String username = "testUser";
    private User user;

    EditText sentenceEntryView;

    Button discardSentenceButton;
    Button saveSentenceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_sentence_prompt);

        promptSpinner = (Spinner) findViewById(R.id.prompt_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.prompt_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        promptSpinner.setAdapter(adapter);
        promptSpinner.setOnItemSelectedListener(this);

        // Firebase database objects
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // TODO: set up actual user
        user = new User(username);

        // TODO: need Firebase tokens?

        sentenceEntryView = (EditText) findViewById(R.id.text_entry_box);

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

    // Save the sentence entry
    private void saveSentenceEntry() {
        String sentenceForEntry = sentenceEntryView.getText().toString();
        if (prompt != null) {
            addSentenceEntryToDb(sentenceForEntry);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Please choose a prompt.", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    // Create sentence entry and add to the database
    private void addSentenceEntryToDb(String inSentenceEntry) {
        timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        try {
            Entry sentenceEntryObj = new Entry("SENTENCE", timestamp, prompt, inSentenceEntry);
            // Method to add to firebase taken from Firebase Realtime Database
            // documentation on saving data
            DatabaseReference usersRef = databaseRef.child("users");
            usersRef.child(username).child(timestamp).setValue(sentenceEntryObj);
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
}