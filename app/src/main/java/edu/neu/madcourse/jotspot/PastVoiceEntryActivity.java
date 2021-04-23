package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import edu.neu.madcourse.jotspot.firebase_helpers.TextEntryParcel;
import edu.neu.madcourse.jotspot.firebase_helpers.VoiceEntryParcel;
import edu.neu.madcourse.jotspot.past_entries.PastEntriesRecyclerHolder;

public class PastVoiceEntryActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private SharedPreferences sharedPreferences;
    private final String defaultString = "default";

    private String username;

    private ImageButton pauseButton;
    private ImageButton playButton;

    private TextView dateTimeView;

    private VoiceEntryParcel voiceEntryParcel;

    private String timestamp;
    private String voiceEntryPath;

    private static final String VOICE_TAG = "edu.neu.madcourse.jotspot.firebase_helpers.VoiceEntryParcel";

    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_voice_entry);

        Bundle bundle = getIntent().getExtras();
        voiceEntryParcel = bundle.getParcelable(VOICE_TAG);

        timestamp = voiceEntryParcel.getTimestamp();
        Log.w("GET TIMESTAMP", timestamp);
        voiceEntryPath = voiceEntryParcel.getVoiceEntryPath();
        Log.w("GET PATH", voiceEntryPath);

        storage = FirebaseStorage.getInstance();
        // Creating a storage reference
        storageRef = storage.getReference();

        // Referenced Android documentation to retrieve data from Shared Preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        username = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);

        player = new MediaPlayer();

        getViews();
        if (voiceEntryPath != null && timestamp != null) {
            try {
                getAudioSetButtons();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getViews() {
        dateTimeView = this.findViewById(R.id.past_voice_entry_date_time);
        dateTimeView.setText(voiceEntryParcel.getDateTimeStr());
        playButton = this.findViewById(R.id.past_voice_entry_play);
        pauseButton = this.findViewById(R.id.past_voice_entry_pause);
    }

    // Get audio recording from Firebase Cloud Storage and save locally
    // Set media player if temp file created successfully
    // Taken from Firebase documentation on how to download files
    private void getAudioSetButtons() throws IOException {
        StorageReference voiceRef = storageRef.child(username).child(voiceEntryPath);
        File localFile = File.createTempFile(timestamp, "3gp");
        voiceRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                try {
                    player.setDataSource(String.valueOf(localFile));
                    player.prepare();

                    // Audio successfully downloaded
                    // Set buttons for Media Player
                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (player != null) {
                                player.start();
                            }
                        }
                    });
                    pauseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (player != null) {
                                if (player.isPlaying()) {
                                    player.pause();
                                }
                            }
                        }
                    });

                } catch (IOException e) {
                    Log.w("Play Exception", e);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO: figure out best way to handle errors
                Log.w("in play audio", e);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}