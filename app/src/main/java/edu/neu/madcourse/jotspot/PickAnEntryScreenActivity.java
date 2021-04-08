package edu.neu.madcourse.jotspot;


import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;

public class PickAnEntryScreenActivity extends AppCompatActivity {

    private ImageButton textButton;
    private ImageButton cameraButton;
    private ImageButton micButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_an_entry_screen);

        textButton = (ImageButton) findViewById(R.id.text_button);
        cameraButton = (ImageButton) findViewById(R.id.camera_button);
        micButton = (ImageButton) findViewById(R.id.mic_button);

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTextEntry();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPhotoEntry();
            }
        });

        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createVoiceRecording();
            }
        });
    }

    private void createTextEntry() {
        Intent textIntent = new Intent(PickAnEntryScreenActivity.this, TextEntryScreenActivity.class);
        PickAnEntryScreenActivity.this.startActivity(textIntent);
    }

    private void createPhotoEntry() {
        Intent photoIntent = new Intent(PickAnEntryScreenActivity.this, PhotoEntryScreenActivity.class);
        PickAnEntryScreenActivity.this.startActivity(photoIntent);
    }

    private void createVoiceRecording() {
        Intent micIntent = new Intent(PickAnEntryScreenActivity.this, VoiceRecordingScreenActivity.class);
        PickAnEntryScreenActivity.this.startActivity(micIntent);
    }

}