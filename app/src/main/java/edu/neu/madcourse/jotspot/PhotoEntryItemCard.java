package edu.neu.madcourse.jotspot;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class PhotoEntryItemCard implements PhotoEntryItemClickListener {

    private final ImageView photoImage;
    private boolean isPressed;

    public PhotoEntryItemCard(ImageView photoImage, boolean isChecked) {
        this.photoImage = photoImage;
        this.isPressed = false;
    }

    public ImageView getImageSource() {return photoImage; }

    @Override
    public void onItemClick(int position) { isPressed = !isPressed;}

}
