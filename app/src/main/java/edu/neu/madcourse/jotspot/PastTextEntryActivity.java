package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import edu.neu.madcourse.jotspot.firebase_helpers.TextEntryParcel;

public class PastTextEntryActivity extends AppCompatActivity {

    private TextView dateTimeView;
    private TextView textEntryView;
    private TextEntryParcel textEntryParcel;
    private static final String TEXT_TAG = "edu.neu.madcourse.jotspot.firebase_helpers.TextEntryParcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_text_entry);

        Bundle bundle = getIntent().getExtras();
        textEntryParcel = bundle.getParcelable(TEXT_TAG);

        getViews();
        setViews();
    }

    private void getViews() {
        dateTimeView = this.findViewById(R.id.past_text_date_time);
        textEntryView = this.findViewById(R.id.past_text_entry_view);
    }

    private void setViews() {
        String dateTime = textEntryParcel.getDateTimeStr();
        String text = textEntryParcel.getTextEntry();
        if (dateTimeView != null) {
            if (dateTime != null) {
                dateTimeView.setText(dateTime);
            }
        }
        if (textEntryView != null) {
            if (text != null) {
                textEntryView.setText(text);
            }
        }
    }
}