package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import edu.neu.madcourse.jotspot.firebase_helpers.Entry;

public class VoiceRecordingScreenActivity extends AppCompatActivity {

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private String username;

    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    final int MY_REQUEST_CODE = 1000;
    ImageButton startbtn;
    ImageButton playbtn;
    ImageButton stopbtn;
//    Button stopPlay;
    Button saveButton;
    Button discardButton;
    private static String outputFile = "";
    private String recordingFileName;
    private static final String LOG_TAG = "AudioRecording";
    String timeStamp;
    String recordingFilePath;
    File outputRecordingFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_entry_screen);

        // TODO: store username locally
        username = "testUser";

        // Firebase database objects
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // Firebase Storage instance
        storage = FirebaseStorage.getInstance();
        // Creating a storage reference
        storageRef = storage.getReference();


        saveButton = (Button) findViewById(R.id.save_voice_button);
        discardButton = (Button) findViewById(R.id.discard_voice_button);

        startbtn = (ImageButton) findViewById(R.id.record_button);
        playbtn = (ImageButton) findViewById(R.id.play_button);
        stopbtn = (ImageButton) findViewById(R.id.stop_button);


        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
//                + UUID.randomUUID().toString() + "_audio_record.3gp";
//        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
//                + timeStamp + "_audio_record.3gp";
//        Log.w("outputfile", outputFile);




        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckPermissions()) {
                    try {
                        outputRecordingFile = createFileForRecording();
                        stopbtn.setEnabled(true);
                        startbtn.setEnabled(false);
                        playbtn.setEnabled(false);
                        //stopplay.setEnabled(false);
                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//                        mRecorder.setOutputFile(outputFile);
                        mRecorder.setOutputFile(outputRecordingFile);
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        //                    mRecorder.setOutputFile("/dev/null");
                        Log.e("OutputFile", outputFile);
                        try {
                            mRecorder.prepare();
                            mRecorder.start();
                            Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "prepare() failed - io");
                        } catch (IllegalStateException e) {
                            Log.e(LOG_TAG, "prepare() failed - illegal state");
                        }
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "file create failed");
                    }
                }
                else
                {
                    RequestPermissions();
                }
            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopbtn.setEnabled(false);
                startbtn.setEnabled(true);
                playbtn.setEnabled(true);
                //stopplay.setEnabled(true);
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
                Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
            }

        });
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopbtn.setEnabled(false);
                startbtn.setEnabled(true);
                playbtn.setEnabled(false);
                //stopplay.setEnabled(true);
                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(outputFile);
                    mPlayer.prepare();
                    mPlayer.start();
                    Toast.makeText(getApplicationContext(), "Recording Started Playing", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if (outputRecordingFile == null) {
                    Uri voiceUri = FileProvider.getUriForFile(getApplicationContext(), "edu.neu.madcourse.jotspot.fileprovider", outputRecordingFile);
                    StorageReference voiceReference = storageRef.child(username).child(recordingFileName);
                    uploadRecording(voiceReference, voiceUri);
                } else {
                    Toast.makeText(getApplicationContext(), "There is no recording to save.", Toast.LENGTH_LONG).show();
                }
            }
        });

//        stopPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopButton.setEnabled(true);
//                recordButton.setEnabled(true);
//                //stopPlay.setEnabled(false);
//                playButton.setEnabled(true);
//
//                if (player != null) {
//                    player.stop();
//                    player.release();
//                    setUpMediaRecorder();
//                }
//            }
//        });

    }

//    public void setUpMediaRecorder() {
//        recorder = new MediaRecorder();
//        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//        recorder.setOutputFile(outputFile);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(this, RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }


    private void RequestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
    }

    // Upload recording to cloud storage
    private void uploadRecording(StorageReference voiceRef, Uri voiceUri) {
        // Reference Firebase documentation on how to upload files
        // Upload to cloud storage
        UploadTask uploadTask = voiceRef.putFile(voiceUri);
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
                addVoiceEntryToDb();
                Log.w("upload", "upload success");
                Toast.makeText(getApplicationContext(), "Photo entry saved successfully.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Create voice entry and add to the database
    private void addVoiceEntryToDb() {
        try {
            Entry voiceEntryObj = new Entry("VOICE", timeStamp, outputFile);
            // Method to add to firebase taken from Firebase Realtime Database
            // documentation on saving data
            DatabaseReference usersRef = databaseRef.child("users");
            usersRef.child(username).child(timeStamp).setValue(voiceEntryObj);
        } catch (ParseException e) {
            Log.w("in addVoiceEntryToDb", e);
        }
    }

    // Taken from Android Camera/Photo Basics documentation
    // Create file where a recording can be saved locally
    private File createFileForRecording() throws IOException {
        // Create a file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        recordingFileName = "THREE_GPP" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File recording = File.createTempFile(
                recordingFileName,  /* prefix */
                ".3gp",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file path for use with ACTION_VIEW intents
        recordingFilePath = recording.getAbsolutePath();
        return recording;
    }

}