package edu.neu.madcourse.jotspot;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;


public class PhotoEntryRecyclerViewHolder extends RecyclerView.ViewHolder{
    public ImageView photoPreview;


    public PhotoEntryRecyclerViewHolder(View itemView, final PhotoEntryItemClickListener listener) {
        super(itemView);
        photoPreview = itemView.findViewById(R.id.photo_preview);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}
