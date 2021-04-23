package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.neu.madcourse.jotspot.moods.MoodHistoryActivity;
import edu.neu.madcourse.jotspot.past_entries.PastEntriesActivity;

public class HomeScreenActivity extends AppCompatActivity {


    // Button variables
    private Button createButton;
    private Button pastEntriesButton;
    private Button oneSentencePromptButton;
    private Button resourcesButton;
    private FloatingActionButton settingsButton;
    private ImageButton moodButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        createButton = (Button) findViewById(R.id.create_button);
        pastEntriesButton = (Button) findViewById(R.id.pastentries_button);
        oneSentencePromptButton = (Button) findViewById(R.id.one_sentence_button);
        resourcesButton = (Button) findViewById(R.id.resources_button);
        settingsButton = (FloatingActionButton) findViewById(R.id.settings_button);
        moodButton = (ImageButton) findViewById(R.id.mood_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEntry();
            }
        });

        resourcesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWellnessResources();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSettings();
            }
        });

        pastEntriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPastEntries();
            }
        });

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMoodHistory();
            }
        });

        oneSentencePromptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOneSentencePrompt();
            }
        });
    }

    private void createEntry() {
        Intent createIntent = new Intent(HomeScreenActivity.this, PickAnEntryScreenActivity.class);
        HomeScreenActivity.this.startActivity(createIntent);
    }

    private void getWellnessResources() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mental Health Resources");
        builder.setMessage("If you or someone you know is in a mental health crisis, you can contact:\n\n" +
                "National Suicide Prevention Lifeline: 1-800-273-8255\n\n" +
                "National Alliance on Mental Illness (NAMI): 800-950-NAMI\n\n" +
                "Substance Abuse and Mental Health Services Admin. (SAMHSA): 1-800-662-4357\n\n" +
                "In case of emergency, please dial 911");
        builder.create();
        builder.show();
    }

    private void getSettings() {
        Intent settingsIntent = new Intent(HomeScreenActivity.this, SettingsScreenActivity.class);
        HomeScreenActivity.this.startActivity(settingsIntent);
    }

    private void viewPastEntries() {
        Intent pastEntriesIntent = new Intent(HomeScreenActivity.this, PastEntriesActivity.class);
        HomeScreenActivity.this.startActivity(pastEntriesIntent);
    }


    private void viewMoodHistory() {
        Intent moodHistoryIntent = new Intent(HomeScreenActivity.this, MoodHistoryActivity.class);
        HomeScreenActivity.this.startActivity(moodHistoryIntent);
    }

    private void goToOneSentencePrompt() {
        Intent oneSentenceIntent = new Intent(HomeScreenActivity.this, OneSentencePromptActivity.class);
        HomeScreenActivity.this.startActivity(oneSentenceIntent);
    }
}