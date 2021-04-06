package edu.neu.madcourse.jotspot;


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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.neu.madcourse.jotspot.firebase_helpers.User;


public class TextEntryScreenActivity extends AppCompatActivity {

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private String username = "testUser";
    private User user;

    EditText textEntryView;


    Button discardTextEntry;
    Button saveTextEntry;

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

        // TODO: set up actual user (do in home, then pass?)
        user = new User(username);

        // TODO: figure out Firebase tokens!
        // code from Firebase docs on getting tokens
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(NewStickerActivity.this, msg, Toast.LENGTH_SHORT).show();
//                        currentUser.setToken(token);
//                        mDatabaseRef.child("Users").child(currentUser.getUsername()).child("token").setValue(token);
//                        CLIENT_REGISTRATION_TOKEN = token;
//                    }
//
//                });

        textEntryView = (EditText) findViewById(R.id.text_entry_box);

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

    private void addTextEntryToDb(String inTextEntry) {
        // Method to add to firebase taken from Firebase Realtime Database
        // documentation on saving data
        DatabaseReference usersRef = databaseRef.child("users");
        // TODO: figure out why setValueAsync doesn't work
        // https://firebase.google.com/docs/database/admin/save-data
        // Use push() to set unique key
        usersRef.child(username).push().setValue(inTextEntry);
    }


}