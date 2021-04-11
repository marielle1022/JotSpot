package edu.neu.madcourse.jotspot.past_entries;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.jotspot.R;

public class PastEntriesRecyclerAdapter extends RecyclerView.Adapter<PastEntriesRecyclerHolder> {

    // Create an array list of item cards
    private final ArrayList<PastEntriesItemCard> listItemCards;
    private PastEntriesRecyclerListener listener;

    // Constructor
    public PastEntriesRecyclerAdapter(ArrayList<PastEntriesItemCard> inListItemCards) {
        this.listItemCards = inListItemCards;
    }

    // Set listener
    public void setOnItemClickListener(PastEntriesRecyclerListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PastEntriesRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: use if statement to determine which item_card.xml to use
        // Create item card (this one is for text entry), inflate dif xml for others
        View icView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_past_text_entry,
                        parent, false);

        // Create new ViewHolder from item card view and LCItemClickListener
        return new PastEntriesRecyclerHolder(icView, listener);
    }

    // TODO: in onBindViewHolder, use switch statement to get type from item card object and assign
    //  views in item card xml based on this?
    @Override
    // Set all values
    public void onBindViewHolder(PastEntriesRecyclerHolder holder, int listPosition) {
        PastEntriesItemCard currentItem = listItemCards.get(listPosition);
        // Example: friendName is a public field in the holder
        // holder.friendName.setText("text");
    }

    @Override
    // Get number of items in the list of links
    public int getItemCount() {
        return listItemCards.size();
    }
}

// Note: this was adapted from Marielle's previous assignment, which referenced the sample code
// provided in class.