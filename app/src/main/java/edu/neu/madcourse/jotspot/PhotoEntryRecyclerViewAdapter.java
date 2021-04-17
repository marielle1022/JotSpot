package edu.neu.madcourse.jotspot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class PhotoEntryRecyclerViewAdapter extends RecyclerView.Adapter<PhotoEntryRecyclerViewHolder> {

    private final ArrayList<PhotoEntryItemCard> itemlist;
    private PhotoEntryItemClickListener listener;

    public PhotoEntryRecyclerViewAdapter(ArrayList<PhotoEntryItemCard> itemList) {this.itemlist = itemList; }

    public void setOnItemClickListener(PhotoEntryItemClickListener listener) {this.listener = listener;}

    @Override
    public PhotoEntryRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_photo_entry, parent, false);
        return new PhotoEntryRecyclerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(PhotoEntryRecyclerViewHolder holder, int position) {
        PhotoEntryItemCard currentItem = itemlist.get(position);

//        holder.photoPreview.set;
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}
