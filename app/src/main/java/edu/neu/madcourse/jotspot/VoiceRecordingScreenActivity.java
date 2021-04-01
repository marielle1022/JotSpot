package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;

public class VoiceRecordingScreenActivity extends AppCompatActivity {

    private static MediaRecorder recorder;
    private static MediaPlayer player;
    static final int MY_REQUEST_CODE = 1;
    ImageButton recordButton;
    ImageButton playButton;
    ImageButton stopButton;
    Button saveButton;
    Button discardButton;
    private String outputFile;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_entry_screen);

        saveButton = (Button) findViewById(R.id.save_voice_button);
        discardButton = (Button) findViewById(R.id.discard_voice_button);

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discard();
            }
        });

        recordButton = (ImageButton) findViewById(R.id.record_button);
        playButton = (ImageButton) findViewById(R.id.play_button);
        stopButton = (ImageButton) findViewById(R.id.stop_button);

        if (!hasMicrophone())
        {
            stopButton.setEnabled(false);
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
        } else {
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
        }

        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";

        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_REQUEST_CODE);
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE);
        }

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    recordAudio(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAudio(v);
            }
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    playAudio(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }


    private void recordAudio(View view) throws IOException, IllegalStateException {
        isRecording = true;
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        recordButton.setEnabled(false);

        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setOutputFile(outputFile);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void stopAudio(View view) {
        stopButton.setEnabled(false);
        playButton.setEnabled(true);

        if (isRecording)
        {
            recordButton.setEnabled(false);
            recorder.stop();
            recorder.release();
            recorder = null;
            isRecording = false;
        } else {
            player.release();
            player = null;
            recordButton.setEnabled(true);
        }

    }

    private void playAudio(View v) throws IOException{
        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);

        player = new MediaPlayer();
        player.setDataSource(outputFile);
        player.prepare();
        player.start();

    }

    private void discard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm discard");
        builder.setMessage("Are you sure you want to discard this entry?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

}