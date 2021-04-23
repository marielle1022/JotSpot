package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.neu.madcourse.jotspot.firebase_helpers.Entry;
import edu.neu.madcourse.jotspot.firebase_helpers.ThreadTaskHelper;

public class PhotoEntryScreenActivity extends AppCompatActivity {

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private Button saveButton;
    private Button discardButton;
    private ImageButton cameraButton;
    private ImageButton uploadButton;

    private String currentPhotoPath;
    private String imageFileName;
    private List<String> listImageFileNames;
    private List<Uri> listPhotoUris;

    private Uri photoUri;

    int numPhotosThisEntry;
    int numPhotosLeftThisEntry;

    private String username;
    private String entryTimestamp;

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

    // ImageViews for thumbnails
    private ImageView thumbnail1;
    private ImageView thumbnail2;
    private ImageView thumbnail3;

    static final int MY_REQUEST_CODE = 1;
    static final int MY_PHOTO_REQUEST_CODE = 2;
    private String TAG = "EntryTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_entry_screen);

        username = "testUser";

        // Firebase database objects
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // Firebase Storage instance
        storage = FirebaseStorage.getInstance();
        // Creating a storage reference
        storageRef = storage.getReference();

//        storageRef = storage.getReferenceFromUrl("gs://jotspot-fa2ac.appspot.com");

        entryTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        listImageFileNames = new ArrayList<>();
        listPhotoUris = new ArrayList<>();

        numPhotosThisEntry = 0;
        numPhotosLeftThisEntry = 0;

        mood = strFeeling0;

        thumbnail1 = this.findViewById(R.id.photo_preview_placeholder1);
        thumbnail2 = this.findViewById(R.id.photo_preview_placeholder2);
        thumbnail3 = this.findViewById(R.id.photo_preview_placeholder3);

        getButtonViews();

        setFeelingOnClicks();

        saveButton = (Button) findViewById(R.id.save_button_photo);
        discardButton = (Button) findViewById(R.id.discard_button_photo);

        cameraButton = (ImageButton) findViewById(R.id.camera_device_button);
        uploadButton = (ImageButton) findViewById(R.id.upload_device_button);

        // Take a photo
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: clean this up; should be able to delete photos
                if (listImageFileNames.size() >= 3) {
                    Toast.makeText(getApplicationContext(), "You can only upload 3 photos per entry.", Toast.LENGTH_LONG).show();
                } else {
                    goToCamera();
                }
            }
        });

        // Save photo entry and upload to cloud storage
        saveButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick (View view) {
                if ((listImageFileNames.size() != 0) && (listPhotoUris.size() != 0)) {
                    // For each image in the entry, create a reference to where the image will be
                    // stored in cloud storage
                    numPhotosLeftThisEntry = numPhotosThisEntry;
                    for (int i = 0; i < listImageFileNames.size(); i++) {
                        StorageReference photoReference =
                                storageRef.child(username).child(entryTimestamp).child(listImageFileNames.get(i) + ".jpg");
                        ThreadTaskHelper threadHelper = new ThreadTaskHelper(photoReference, listPhotoUris.get(i));
                        PhotoUploadTask task = new PhotoUploadTask();
                        task.execute(threadHelper);
                        thumbnail1.setImageDrawable(getDrawable(android.R.drawable.ic_menu_gallery));
                        thumbnail2.setImageDrawable(getDrawable(android.R.drawable.ic_menu_gallery));
                        thumbnail3.setImageDrawable(getDrawable(android.R.drawable.ic_menu_gallery));
                        mood = strFeeling0;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "There are no photos to save.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Discard entry
        // TODO: figure out how to delete locally stored files
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discardPhotoEntry();
            }
        });
    }

    // Taken from Android Camera/Photo Basics documentation
    // Create file where a photo can be saved locally
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

    // Take a photo with the camera and save it locally in the file created
    private void goToCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
        } else {
            try {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
//                    Uri photoUri = FileProvider.getUriForFile(this, "edu.neu.madcourse.jotspot.fileprovider", photoFile);
                    photoUri = FileProvider.getUriForFile(this, "edu.neu.madcourse.jotspot.fileprovider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(cameraIntent, MY_PHOTO_REQUEST_CODE);
//                    displayPhotoFromUri(photoUri);
                    // Add file name to list
                    listImageFileNames.add(imageFileName);
                    // Add photo uri to list
                    listPhotoUris.add(photoUri);
//                    // Add 1 to number of photos for this entry
//                    numPhotosThisEntry += 1;
                    // TODO: figure out how to ask for and grant storage permissions?
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayPhotoFromUri() throws IOException {
        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
//        Bitmap scaledImageBitmap = Bitmap.createScaledBitmap(imageBitmap,  600 ,600, true);
        if (numPhotosThisEntry == 0) {
            thumbnail1.setImageBitmap(imageBitmap);
            thumbnail1.setVisibility(View.VISIBLE);
        } else if (numPhotosThisEntry == 1) {
            thumbnail2.setImageBitmap(imageBitmap);
            thumbnail2.setVisibility(View.VISIBLE);
        } else if (numPhotosThisEntry == 2) {
            thumbnail3.setImageBitmap(imageBitmap);
            thumbnail3.setVisibility(View.VISIBLE);
        }
        // Add 1 to number of photos for this entry
        numPhotosThisEntry += 1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                displayPhotoFromUri();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    // Upload photo to cloud storage
//    private void uploadPhoto(StorageReference photoRef, Uri photoUri) {
//        // Reference Firebase documentation on how to upload files
//        // Upload to cloud storage
//        UploadTask uploadTask = photoRef.putFile(photoUri);
//        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//                // TODO: create photo entry object and upload to realtime db
//                if (numPhotosLeftThisEntry == numPhotosThisEntry) {
//                    addPhotoEntryToDb();
//                }
//                numPhotosLeftThisEntry -= 1;
//                if (numPhotosLeftThisEntry == 0) {
//                    // Reset timestamp, list of photo file names, and list of photo file uris
//                    entryTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                    listImageFileNames = new ArrayList<>();
//                    listPhotoUris = new ArrayList<>();
//                }
//                Log.w("upload", "upload success");
//                Toast.makeText(getApplicationContext(), "Photo entry saved successfully.", Toast.LENGTH_LONG).show();
//            }
//        });
//    }



    // TODO: figure out another method of threading (AsyncTask, IntentService both deprecated -- maybe RxJava?)
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
                // TODO: create photo entry object and upload to realtime db
                if (numPhotosLeftThisEntry == numPhotosThisEntry) {
                    addPhotoEntryToDb();
                }
                numPhotosLeftThisEntry -= 1;
                if (numPhotosLeftThisEntry == 0) {
                    // Reset timestamp, list of photo file names, and list of photo file uris
                    entryTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    listImageFileNames = new ArrayList<>();
                    listPhotoUris = new ArrayList<>();
                }
                Log.w("upload", "upload success");
                Toast.makeText(getApplicationContext(), "Photo entry saved successfully.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Create photo entry and add to the database
    private void addPhotoEntryToDb() {
        try {
            Entry photoEntryObj = new Entry("PHOTO", entryTimestamp, listImageFileNames, mood);
            // Method to add to firebase taken from Firebase Realtime Database
            // documentation on saving data
            DatabaseReference usersRef = databaseRef.child(getString(R.string.entries_path, username));
            usersRef.child(entryTimestamp).setValue(photoEntryObj);
        } catch (ParseException e) {
            Log.w(TAG, e);
        }
    }

    // Set up dialog box for "discard" button
    private void discardPhotoEntry() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.discard_photo_warning));
        builder.setCancelable(true);
        builder.setPositiveButton(getString(R.string.discard_photo_yes), new DialogInterface.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset timestamp, list of photo file names, and list of photo file uris
                entryTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                listImageFileNames = new ArrayList<>();
                listPhotoUris = new ArrayList<>();
                thumbnail1.setImageDrawable(getDrawable(android.R.drawable.ic_menu_gallery));
                thumbnail2.setImageDrawable(getDrawable(android.R.drawable.ic_menu_gallery));
                thumbnail3.setImageDrawable(getDrawable(android.R.drawable.ic_menu_gallery));
                mood = strFeeling0;
            }
        });
        builder.setNegativeButton(getString(R.string.discard_photo_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.RED);
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.BLUE);
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

    // Move photo "upload to storage and db" to a separate task
    // AsyncTask<params, prgoress, results>
    // TODO: add progress marker?
    private class PhotoUploadTask extends AsyncTask<ThreadTaskHelper, Void, Void> {
        @Override
        protected Void doInBackground(ThreadTaskHelper... threadTaskHelpers) {
            ThreadTaskHelper helper = threadTaskHelpers[0];
            uploadPhoto(helper.getStorageReference(), helper.getUri());
            return null;
        }
    }

}