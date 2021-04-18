package edu.neu.madcourse.jotspot.past_entries;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import edu.neu.madcourse.jotspot.HomeScreenActivity;
import edu.neu.madcourse.jotspot.LoginActivity;
import edu.neu.madcourse.jotspot.R;

public class PhotoPreviewActivity extends AppCompatActivity {

    private Button finishPreview;
    private ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);

        finishPreview = findViewById(R.id.finish_photo_entry_preview);

        finishPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToEntries();
            }
        });
    }


    public void returnToEntries() {
        Intent returnIntent = new Intent(PhotoPreviewActivity.this, PastEntriesActivity.class);
        PhotoPreviewActivity.this.startActivity(returnIntent);
    }
}