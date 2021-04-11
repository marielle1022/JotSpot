package edu.neu.madcourse.jotspot.past_entries;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

// An object in the list
public class PastEntriesRecyclerHolder extends RecyclerView.ViewHolder {
    // TODO: declare views here (public)

    public PastEntriesRecyclerHolder(View itemCardView, final PastEntriesRecyclerListener listener) {
        // Note: MUST call super first
        super(itemCardView);
        // Since findViewById is costly, only want to do it once per item
        // TODO: set views (need all views for all possible item cards?)


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
