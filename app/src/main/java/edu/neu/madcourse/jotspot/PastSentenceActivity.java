package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import edu.neu.madcourse.jotspot.firebase_helpers.SentenceEntryParcel;
import edu.neu.madcourse.jotspot.firebase_helpers.TextEntryParcel;

public class PastSentenceActivity extends AppCompatActivity {

    private TextView dateTimeView;
    private TextView promptView;
    private TextView sentenceEntryView;

    private SentenceEntryParcel sentenceEntryParcel;

    private static final String SENTENCE_TAG = "edu.neu.madcourse.jotspot.firebase_helpers.SentenceEntryParcel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_sentence);

        Bundle bundle = getIntent().getExtras();
        sentenceEntryParcel = bundle.getParcelable(SENTENCE_TAG);

        getViews();
        setViews();
    }

    private void getViews() {
        dateTimeView = this.findViewById(R.id.past_sentence_date_time);
        promptView = this.findViewById(R.id.past_sentence_prompt_view);
        sentenceEntryView = this.findViewById(R.id.past_sentence_entry_view);
    }

    private void setViews() {
        String dateTime = sentenceEntryParcel.getDateTimeStr();
        String prompt = sentenceEntryParcel.getPrompt();
        String sentence = sentenceEntryParcel.getSentenceEntry();
        if (dateTimeView != null) {
            if (dateTime != null) {
                dateTimeView.setText(dateTime);
            }
        }
        if (promptView != null) {
            if (prompt != null) {
                promptView.setText(prompt);
            }
        }
        if (sentenceEntryView != null) {
            if (sentence != null) {
                sentenceEntryView.setText(sentence);
            }
        }
    }
}