package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.storage.FileDownloadTask;
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

    // Variables storing buttons
    private ImageButton startRecordingButton;
    private ImageButton stopRecordingButton;
    private ImageButton playRecordingButton;
    private Button saveEntryButton;
    private Button discardEntryButton;

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

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    // Firebase Storage-related variables
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private String entryTimestamp;
    private String fileName;
    private File audioFile;
    private String username;

    private MediaRecorder recorder;
    private MediaPlayer player;

    // Note: if output is default, can change extension to what you want?
    private String extension = ".3gp";


    // TODO: on "discard", reset entryTimestamp and fileName(and try to delete existing file)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_entry_screen);

        // Firebase database objects
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // Firebase Storage instance
        storage = FirebaseStorage.getInstance();
        // Creating a storage reference
        storageRef = storage.getReference();

        username = "testUser";

        mood = strFeeling0;

        // TODO: check for permissions

        getButtonViews();

        setFeelingOnClicks();

        startRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordAudio();
            }
        });

        stopRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        playRecordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecording();
            }
        });

        // TODO: save to cloud storage
        // TODO: dialog
        saveEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recorder != null) {
                    recorder.release();
                    recorder = null;
                }
                if (player != null) {
                    player.release();
                    player = null;
                }
                uploadRecording();
            }
        });

        // TODO: figure out how to discard file
        // TODO: dialog
        discardEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recorder != null) {
                    recorder.release();
                    recorder = null;
                }
                if (player != null) {
                    player.release();
                    player = null;
                }
                // Reset file path and timestamp
                // TODO: check if this necessary
                createFilePath();
            }
        });
    }

    // Set the views for all buttons
    private void getButtonViews() {
        startRecordingButton = findViewById(R.id.record_button);
        stopRecordingButton = findViewById(R.id.stop_button);
        playRecordingButton = findViewById(R.id.play_button);
        saveEntryButton = findViewById(R.id.save_voice_button);
        discardEntryButton = findViewById(R.id.discard_voice_button);

        feeling1 = findViewById(R.id.feelingButton1);
        feeling2 = findViewById(R.id.feelingButton2);
        feeling3 = findViewById(R.id.feelingButton3);
        feeling4 = findViewById(R.id.feelingButton4);
        feeling5 = findViewById(R.id.feelingButton5);
        feeling6 = findViewById(R.id.feelingButton6);

    }

    // Create the filepath for local storage of the file
    private void createFilePath() {
        entryTimestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fileName = getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/" + entryTimestamp + extension;
    }

    // Start recording the audio. Call this on pressing the "record" button.
    // Note: this uses examples from the Android documentation for the MediaRecorder
    private void recordAudio() {
        // TODO: stop player to release
        if (player != null) {
            player.release();
            player = null;
        }
        // TODO: dialog b/c file will be overwritten
        createFilePath();
        // Create MediaRecorder
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("AUDIO", "prepare() MediaRecorder failed in recordAudio()");
        }
        recorder.start();
        Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
    }

    // Stop recording the audio. Call this on pressing the "stop" button.
    // Note: this uses examples from the Android documentation for the MediaRecorder
    private void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.reset();
            Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
            recorder.release();
            // TODO: figure out if "stopping" recording is better, or if should "pause" and wait to stop until "discard"
            // TODO: get rid of this "null" reset for dialog?
            recorder = null;
        }
    }

    // Play the audio recording. Call this on pressing the "play" button.
    // Note: this uses examples from the Android documentation for the MediaRecorder
    private void playRecording() {
        player = new MediaPlayer();
        if (fileName != null && !fileName.equals("")) {
            try {
                player.setDataSource(fileName);
                player.prepare();
                player.start();
                Toast.makeText(getApplicationContext(), "Recording Playing", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Log.e("AUDIO", "prepare() for MediaPlayer failed in playRecording()");
            }
            // TODO: need to release player
        }
    }

//    private void getAudioRecording() throws IOException {
//        StorageReference voiceRef = storageRef.child(username).child("20210417_193914.3gp");
//        File localFile = File.createTempFile("test_audio", "3gp");
//        voiceRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Photo successfully downloaded
//                // Display in corresponding imageview
//                // Used TutorialsPoint to set image drawable
//                player = new MediaPlayer();
//                try {
//                    player.setDataSource(String.valueOf(localFile));
//                    player.prepare();
//                    player.start();
//                    Toast.makeText(getApplicationContext(), "Recording Playing", Toast.LENGTH_LONG).show();
//                } catch (IOException e) {
//                    Log.e("AUDIO", "prepare() for MediaPlayer failed in playRecording()");
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // TODO: figure out best way to handle errors
//                Log.w("in play audio", e);
//            }
//        });
//    }

    // Upload audio to cloud storage
    private void uploadRecording() {
        StorageReference voiceReference =
                storageRef.child(username).child(entryTimestamp + extension);
        Uri file = Uri.fromFile(new File(fileName));
        UploadTask uploadTask = voiceReference.putFile(file);
        // Reference Firebase documentation on how to upload files
        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Voice entry not saved.", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                // TODO: create voice entry object and upload to realtime db
                addVoiceEntryToDb();
//                Log.w("upload", "upload success");
                Toast.makeText(getApplicationContext(), "Voice entry saved successfully.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Create voice entry and add to the database
    private void addVoiceEntryToDb() {
        try {
            Entry voiceEntryObj = new Entry("VOICE", entryTimestamp, entryTimestamp + extension, mood);
            // Method to add to firebase taken from Firebase Realtime Database
            // documentation on saving data
            DatabaseReference usersRef = databaseRef.child("users");
            usersRef.child(username).child(entryTimestamp).setValue(voiceEntryObj);
            // Create new filepath and entry timestamp
            createFilePath();
        } catch (ParseException e) {
            Log.w("VOICE", e);
        }
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

}

//    // Firebase-related variables
//    private FirebaseDatabase database;
//    private DatabaseReference databaseRef;
//
//    private FirebaseStorage storage;
//    private StorageReference storageRef;
//
//    private String username;
//
//    private MediaRecorder mRecorder;
//    private MediaPlayer mPlayer;
//    final int MY_REQUEST_CODE = 1000;
//    ImageButton startbtn;
//    ImageButton playbtn;
//    ImageButton stopbtn;
////    Button stopPlay;
//    Button saveButton;
//    Button discardButton;
//    private static String outputFile = "";
//    private String recordingFileName;
//    private static final String LOG_TAG = "AudioRecording";
//    String timeStamp;
//    String recordingFilePath;
//    File outputRecordingFile;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_voice_entry_screen);
//
//        // TODO: store username locally
//        username = "testUser";
//
//        // Firebase database objects
//        database = FirebaseDatabase.getInstance();
//        databaseRef = database.getReference();
//
//        // Firebase Storage instance
//        storage = FirebaseStorage.getInstance();
//        // Creating a storage reference
//        storageRef = storage.getReference();
//
//
//        saveButton = (Button) findViewById(R.id.save_voice_button);
//        discardButton = (Button) findViewById(R.id.discard_voice_button);
//
//        startbtn = (ImageButton) findViewById(R.id.record_button);
//        playbtn = (ImageButton) findViewById(R.id.play_button);
//        stopbtn = (ImageButton) findViewById(R.id.stop_button);
//
//
//        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
////        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
////                + UUID.randomUUID().toString() + "_audio_record.3gp";
////        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
////                + timeStamp + "_audio_record.3gp";
////        Log.w("outputfile", outputFile);
//
//
//
//
//        startbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(CheckPermissions()) {
//                    try {
//                        outputRecordingFile = createFileForRecording();
//                        stopbtn.setEnabled(true);
//                        startbtn.setEnabled(false);
//                        playbtn.setEnabled(false);
//                        //stopplay.setEnabled(false);
//                        mRecorder = new MediaRecorder();
//                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
////                        mRecorder.setOutputFile(outputFile);
//                        mRecorder.setOutputFile(outputRecordingFile);
//                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//                        //                    mRecorder.setOutputFile("/dev/null");
//                        Log.e("OutputFile", outputFile);
//                        try {
//                            mRecorder.prepare();
//                            mRecorder.start();
//                            Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
//                        } catch (IOException e) {
//                            Log.e(LOG_TAG, "prepare() failed - io");
//                        } catch (IllegalStateException e) {
//                            Log.e(LOG_TAG, "prepare() failed - illegal state");
//                        }
//                    } catch (IOException e) {
//                        Log.e(LOG_TAG, "file create failed");
//                    }
//                }
//                else
//                {
//                    RequestPermissions();
//                }
//            }
//        });
//
//        stopbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopbtn.setEnabled(false);
//                startbtn.setEnabled(true);
//                playbtn.setEnabled(true);
//                //stopplay.setEnabled(true);
//                mRecorder.stop();
//                mRecorder.release();
//                mRecorder = null;
//                Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
//            }
//
//        });
//        playbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stopbtn.setEnabled(false);
//                startbtn.setEnabled(true);
//                playbtn.setEnabled(false);
//                //stopplay.setEnabled(true);
//                mPlayer = new MediaPlayer();
//                try {
//                    mPlayer.setDataSource(outputFile);
//                    mPlayer.prepare();
//                    mPlayer.start();
//                    Toast.makeText(getApplicationContext(), "Recording Started Playing", Toast.LENGTH_LONG).show();
//                } catch (IOException e) {
//                    Log.e(LOG_TAG, "prepare() failed");
//                }
//            }
//        });
//
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick (View view) {
//                if (outputRecordingFile == null) {
//                    Uri voiceUri = FileProvider.getUriForFile(getApplicationContext(), "edu.neu.madcourse.jotspot.fileprovider", outputRecordingFile);
//                    StorageReference voiceReference = storageRef.child(username).child(recordingFileName);
//                    uploadRecording(voiceReference, voiceUri);
//                } else {
//                    Toast.makeText(getApplicationContext(), "There is no recording to save.", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
////        stopPlay.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                stopButton.setEnabled(true);
////                recordButton.setEnabled(true);
////                //stopPlay.setEnabled(false);
////                playButton.setEnabled(true);
////
////                if (player != null) {
////                    player.stop();
////                    player.release();
////                    setUpMediaRecorder();
////                }
////            }
////        });
//
//    }
//
////    public void setUpMediaRecorder() {
////        recorder = new MediaRecorder();
////        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
////        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
////        recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
////        recorder.setOutputFile(outputFile);
////    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case MY_REQUEST_CODE:
//                if (grantResults.length> 0) {
//                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
//                    if (permissionToRecord && permissionToStore) {
//                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
//                    }
//                }
//                break;
//        }
//    }
//
//
//    public boolean CheckPermissions() {
//        int result = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
//        int result1 = ContextCompat.checkSelfPermission(this, RECORD_AUDIO);
//        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
//    }
//
//
//    private void RequestPermissions() {
//        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
//    }
//
//    // Upload recording to cloud storage
//    private void uploadRecording(StorageReference voiceRef, Uri voiceUri) {
//        // Reference Firebase documentation on how to upload files
//        // Upload to cloud storage
//        UploadTask uploadTask = voiceRef.putFile(voiceUri);
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
//                addVoiceEntryToDb();
//                Log.w("upload", "upload success");
//                Toast.makeText(getApplicationContext(), "Photo entry saved successfully.", Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    // Create voice entry and add to the database
//    private void addVoiceEntryToDb() {
//        try {
//            Entry voiceEntryObj = new Entry("VOICE", timeStamp, outputFile);
//            // Method to add to firebase taken from Firebase Realtime Database
//            // documentation on saving data
//            DatabaseReference usersRef = databaseRef.child("users");
//            usersRef.child(username).child(timeStamp).setValue(voiceEntryObj);
//        } catch (ParseException e) {
//            Log.w("in addVoiceEntryToDb", e);
//        }
//    }
//
//    // Taken from Android Camera/Photo Basics documentation
//    // Create file where a recording can be saved locally
//    private File createFileForRecording() throws IOException {
//        // Create a file name
////        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        recordingFileName = "THREE_GPP" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        File recording = File.createTempFile(
//                recordingFileName,  /* prefix */
//                ".3gp",         /* suffix */
//                storageDir      /* directory */
//        );
//        // Save a file path for use with ACTION_VIEW intents
//        recordingFilePath = recording.getAbsolutePath();
//        return recording;
//    }
//
//}