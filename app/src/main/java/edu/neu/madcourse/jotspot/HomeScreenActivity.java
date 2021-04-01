package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeScreenActivity extends AppCompatActivity {

    private Button createButton;
    private Button pastEntriesButton;
    private Button resourcesButton;
    private FloatingActionButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        createButton = (Button) findViewById(R.id.create_button);
        pastEntriesButton = (Button) findViewById(R.id.pastentries_button);
        resourcesButton = (Button) findViewById(R.id.resources_button);
        settingsButton = (FloatingActionButton) findViewById(R.id.settings_button);

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
}