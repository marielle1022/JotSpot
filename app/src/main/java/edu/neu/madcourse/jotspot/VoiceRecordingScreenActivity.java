package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;

import java.io.IOException;
import java.util.UUID;

public class VoiceRecordingScreenActivity extends AppCompatActivity {

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
    private static final String LOG_TAG = "AudioRecording";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_entry_screen);


        saveButton = (Button) findViewById(R.id.save_voice_button);
        discardButton = (Button) findViewById(R.id.discard_voice_button);

        startbtn = (ImageButton) findViewById(R.id.record_button);
        playbtn = (ImageButton) findViewById(R.id.play_button);
        stopbtn = (ImageButton) findViewById(R.id.stop_button);


        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + UUID.randomUUID().toString() + "_audio_record.3gp";



        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckPermissions()) {
                    stopbtn.setEnabled(true);
                    startbtn.setEnabled(false);
                    playbtn.setEnabled(false);
                    //stopplay.setEnabled(false);
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile("/dev/null");
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

}