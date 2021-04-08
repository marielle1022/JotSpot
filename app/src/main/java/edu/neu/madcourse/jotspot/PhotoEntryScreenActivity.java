package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PhotoEntryScreenActivity extends AppCompatActivity {

    private Button saveButton;
    private Button discardButton;
    private ImageButton cameraButton;
    private ImageButton uploadButton;
    static final int MY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_entry_screen);

        saveButton = (Button) findViewById(R.id.save_button_photo);
        discardButton = (Button) findViewById(R.id.discard_button_photo);

        cameraButton = (ImageButton) findViewById(R.id.camera_device_button);
        uploadButton = (ImageButton) findViewById(R.id.upload_device_button);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCamera();
            }
        });

    }

    private void goToCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
        } else {
            try {
                Intent cameraIntent = new Intent();
                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(cameraIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}