package edu.neu.madcourse.jotspot.past_entries;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.jotspot.R;

// An object in the list
public class PastEntriesRecyclerHolder extends RecyclerView.ViewHolder {
    // TODO: declare views here (public)
    // Date/Time text views
    public TextView textEntryDateTime;
    public TextView photoEntryDateTime;
    public TextView voiceEntryDateTime;
    // Text Entry text view
    public TextView textEntryView;
    // Photo Entry photo views
    public ImageView photoEntryImageView1;
    public ImageView photoEntryImageView2;
    public ImageView photoEntryImageView3;
    // Voice Entry view
    public ImageButton voiceEntryPlayButton;
    public ImageButton voiceEntryStopButton;

    public PastEntriesRecyclerHolder(View itemCardView, final PastEntriesRecyclerListener listener) {
        // Note: MUST call super first
        super(itemCardView);
        // Since findViewById is costly, only want to do it once per item
        // TODO: set views (need all views for all possible item cards?)
        textEntryDateTime = itemCardView.findViewById(R.id.text_date_time_view);
        photoEntryDateTime = itemCardView.findViewById(R.id.photo_date_time_view);
        voiceEntryDateTime = itemCardView.findViewById(R.id.voice_date_time_view);

        textEntryView = itemCardView.findViewById(R.id.text_entry_view);

        photoEntryImageView1 = itemCardView.findViewById(R.id.photo_entry1_view);
        photoEntryImageView2 = itemCardView.findViewById(R.id.photo_entry2_view);
        photoEntryImageView3 = itemCardView.findViewById(R.id.photo_entry3_view);

        voiceEntryPlayButton = itemCardView.findViewById(R.id.ic_play_button);
        voiceEntryStopButton = itemCardView.findViewById(R.id.ic_stop_button);


        // Set up listeners

        // Click on item
//        histCardItemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    // Check to see what item this is in the list (based on location, size)
//                    int list_position = getLayoutPosition();
//                    // If you get NO_POSITION, it means calculations haven't happened yet
//                    // Very rare, just ignore this
//                    if (list_position != RecyclerView.NO_POSITION) {
//                        listener.onItemClick(list_position);
//                    }
//                }
//            }
//        });
    }
}

// Note: this was adapted from Marielle's previous assignment, which referenced the sample code
// provided in class.
