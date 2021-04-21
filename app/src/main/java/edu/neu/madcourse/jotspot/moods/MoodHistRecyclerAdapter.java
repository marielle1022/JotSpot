package edu.neu.madcourse.jotspot.moods;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.neu.madcourse.jotspot.R;

import edu.neu.madcourse.jotspot.moods.MoodHistRecyclerHolder;

public class MoodHistRecyclerAdapter extends RecyclerView.Adapter<MoodHistRecyclerHolder> {
    // Create an array list of item cards
    private final ArrayList<MoodHistRecyclerItemCard> listMoods;
    private MoodHistRecyclerListener listener;

    // Constructor
    public MoodHistRecyclerAdapter(ArrayList<MoodHistRecyclerItemCard> inListMoods) {
        this.listMoods = inListMoods;
    }

    // Set listener
    public void setOnItemClickListener(MoodHistRecyclerListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoodHistRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create item card using item_card.xml
        View icView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_mood_history,
                        parent, false);

        // Create new ViewHolder from item card view and LCItemClickListener
        return new MoodHistRecyclerHolder(icView, listener);
    }

    @Override
    // Set all values
    public void onBindViewHolder(MoodHistRecyclerHolder holder, int listPosition) {
        MoodHistRecyclerItemCard currentItem = listMoods.get(listPosition);
        holder.timestampView.setText(currentItem.getTimestamp());
        int moodDrawable;
        // Options: "NONE", "VERY HAPPY", "HAPPY", "NEUTRAL", "SLIGHTLY BUMMED", "SAD", "WEEPY"
        switch (currentItem.getMood().toUpperCase()) {
            case "VERY HAPPY":
                moodDrawable = R.drawable.grinning_face_1f600;
                break;
            case "HAPPY":
                moodDrawable = R.drawable.slightly_smiling;
                break;
            case "NEUTRAL":
                moodDrawable = R.drawable.neutral_face_1f610;
                break;
            case "SLIGHTLY BUMMED":
                moodDrawable = R.drawable.confused_face_1f615;
                break;
            case "SAD":
                moodDrawable = R.drawable.frowning_face_2639_fe0f;
                break;
            case "WEEPY":
                moodDrawable = R.drawable.downcast_face_with_sweat_1f613;
                break;
            default:
                // Set default as "loving" to avoid "may not be instantiated" error
                // TODO: figure out how to not create item card for entries with "NONE"
                moodDrawable = R.drawable.neutral_face_1f610;
                break;
        }
        holder.moodView.setImageResource(moodDrawable);
    }

    @Override
    // Get number of items in the list of links
    public int getItemCount() {
        return listMoods.size();
    }
}
// Note: this was adapted from a portion of a previous assignment written by Marielle, which
// referenced the sample code provided in class.
