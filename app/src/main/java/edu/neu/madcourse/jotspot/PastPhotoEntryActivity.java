package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.neu.madcourse.jotspot.firebase_helpers.PhotoEntryParcel;
import edu.neu.madcourse.jotspot.firebase_helpers.TextEntryParcel;
import edu.neu.madcourse.jotspot.past_entries.PastEntriesRecyclerHolder;

public class PastPhotoEntryActivity extends AppCompatActivity {

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private String username;

    private List<String> listPhotoPaths;

    private TextView dateTimeView;
    private ImageView photoView1;
    private ImageView photoView2;
    private ImageView photoView3;

    private String timestamp;

    private PhotoEntryParcel photoEntryParcel;
    private static final String PHOTO_TAG = "edu.neu.madcourse.jotspot.firebase_helpers.PhotoEntryParcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_photo_entry);

        Bundle bundle = getIntent().getExtras();
        photoEntryParcel = bundle.getParcelable(PHOTO_TAG);

        timestamp = photoEntryParcel.getTimestamp();

        storage = FirebaseStorage.getInstance();
        // Creating a storage reference
        storageRef = storage.getReference();

        // TODO: get actual username
        username = "testUser";

        getViews();

        try {
            setImageViews();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getViews() {
        dateTimeView = this.findViewById(R.id.past_photo_entry_date_time);
        photoView1 = this.findViewById(R.id.past_photo1_view);
        photoView2 = this.findViewById(R.id.past_photo2_view);
        photoView3 = this.findViewById(R.id.past_photo3_view);
    }

    private void setImageViews() throws IOException {
        listPhotoPaths = photoEntryParcel.getPhotoEntry();

        int i = 1;

        for (String photo : listPhotoPaths) {
            getImageSetView(photo, i);
            i ++;
        }

        dateTimeView.setText(photoEntryParcel.getDateTimeStr());

    }

    // Get photo from Firebase Cloud Storage and save locally
    // Set image views if temp file created successfully
    // Taken from Firebase documentation on how to download files
    // TODO: check if overwrites file -- why is it a temp file?
    private void getImageSetView(String photoName, int photoNum) throws IOException {
        StorageReference photoRef = storageRef.child(username).child(timestamp).child(photoName + ".jpg");
        File localFile = File.createTempFile(photoName, "jpg");
        photoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Photo successfully downloaded
                // Display in corresponding imageview
                // Used TutorialsPoint to set image drawable
                if (photoNum == 1) {
                    photoView1.setImageDrawable(Drawable.createFromPath(localFile.toString()));
                    photoView1.setVisibility(View.VISIBLE);
                } else if (photoNum == 2) {
                    photoView2.setImageDrawable(Drawable.createFromPath(localFile.toString()));
                    photoView2.setVisibility(View.VISIBLE);
                } else if (photoNum == 3) {
                    photoView3.setImageDrawable(Drawable.createFromPath(localFile.toString()));
                    photoView3.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO: figure out best way to handle errors
                Log.w("set_images", e);
            }
        });
    }
}