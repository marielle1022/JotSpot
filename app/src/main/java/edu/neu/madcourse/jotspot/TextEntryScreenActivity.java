package edu.neu.madcourse.jotspot;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class TextEntryScreenActivity extends AppCompatActivity {

    Button discardTextEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_entry_screen);

        discardTextEntry = (Button) findViewById(R.id.discard_text_button);

        discardTextEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discard();
            }
        });


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