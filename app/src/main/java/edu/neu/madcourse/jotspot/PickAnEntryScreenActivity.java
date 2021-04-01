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
    static final int MY_REQUEST_CODE = 1;

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
                goToCamera();
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

    private void goToCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
        }
        try {
            Intent cameraIntent = new Intent();
            cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(cameraIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createVoiceRecording() {
        Intent micIntent = new Intent(PickAnEntryScreenActivity.this, VoiceRecordingScreenActivity.class);
        PickAnEntryScreenActivity.this.startActivity(micIntent);
    }

}