package edu.neu.madcourse.jotspot.moods;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.jotspot.R;

// An object in the list
public class MoodHistRecyclerHolder extends RecyclerView.ViewHolder {
    // View for the timestamp of when the emoji was sent
    public TextView timestampView;
    // View for the emoji representing the mood
    public ImageView moodView;

    public MoodHistRecyclerHolder(View histItemCardView, final MoodHistRecyclerListener listener) {
        // NB: MUST call super first
        super(histItemCardView);
        // Since findViewById is costly, only want to do it once per item
        timestampView = histItemCardView.findViewById(R.id.timestamp_textview);
        moodView = histItemCardView.findViewById(R.id.mood_hist_imageview);

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
// Note: this was adapted from a portion of a previous assignment written by Marielle, which
// referenced the sample code provided in class.
