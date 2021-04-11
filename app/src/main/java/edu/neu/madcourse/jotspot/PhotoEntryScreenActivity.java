package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhotoEntryScreenActivity extends AppCompatActivity {

    private Button saveButton;
    private Button discardButton;
    private ImageButton cameraButton;
    private ImageButton uploadButton;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String currentPhotoPath;
    private String imageFileName;
    private List<String> listImageFileNames;
    private List<Uri> listPhotoUris;
    private String username;
    private String entryTimestamp;
    static final int MY_REQUEST_CODE = 1;
    static final int MY_PHOTO_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_entry_screen);

        username = "test";

        // Firebase Storage instance
        storage = FirebaseStorage.getInstance();
        // Creating a storage reference
        storageRef = storage.getReference();
//        storageRef = storage.getReferenceFromUrl("gs://jotspot-fa2ac.appspot.com");

        entryTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        listImageFileNames = new ArrayList<>();
        listPhotoUris = new ArrayList<>();

        saveButton = (Button) findViewById(R.id.save_button_photo);
        discardButton = (Button) findViewById(R.id.discard_button_photo);

        cameraButton = (ImageButton) findViewById(R.id.camera_device_button);
        uploadButton = (ImageButton) findViewById(R.id.upload_device_button);

        // Take a photo
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCamera();
            }
        });

        // Save photo entry and upload to cloud storage
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if ((listImageFileNames.size() != 0) && (listPhotoUris.size() != 0)) {
                    // For each image in the entry, create a reference to where the image will be
                    // stored in cloud storage
                    for (int i = 0; i < listImageFileNames.size(); i++) {
                        StorageReference photoReference =
                                storageRef.child(username).child(entryTimestamp).child(listImageFileNames.get(i) + ".jpg");
                        uploadPhoto(photoReference, listPhotoUris.get(i));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "There are no photos to save.", Toast.LENGTH_LONG).show();
                }
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
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                // Create file for photo
                // Using Android Camera/Photo Basics documentation
                File photoFile = null;
                try {
                    photoFile = createFileForPhoto();
                } catch (IOException e) {
                    Log.w("PHOTO", "error with creating photo file");
                }
                // If the file was created successfully, take the photo
                if (photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(this, "edu.neu.madcourse.jotspot.fileprovider", photoFile);
                    Log.w("photo", photoUri.toString());
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(cameraIntent, MY_PHOTO_REQUEST_CODE);
                    // Add file name to list
                    listImageFileNames.add(imageFileName);
                    // Add photo uri to list
                    listPhotoUris.add(photoUri);
                    // TODO: check this, add child for username
                    // TODO: move this to "save", need to modify for multiple files
                    // TODO: figure out how to ask for and grant storage permissions
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Upload photo to cloud storage
    private void uploadPhoto(StorageReference photoRef, Uri photoUri) {
        // Reference Firebase documentation on how to upload files
        // Upload to cloud storage
        UploadTask uploadTask = photoRef.putFile(photoUri);
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.w("upload", "upload success");
                Toast.makeText(getApplicationContext(), "Photo entry saved successfully.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Taken from Android Camera/Photo Basics documentation
    private File createFileForPhoto() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }



}