package edu.neu.madcourse.jotspot.past_entries;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.neu.madcourse.jotspot.R;

public class PastEntriesRecyclerAdapter extends RecyclerView.Adapter<PastEntriesRecyclerHolder> {

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private String username;

    String timestamp;

    // Create an array list of item cards
    private final ArrayList<PastEntriesItemCard> listItemCards;
    private PastEntriesRecyclerListener listener;

    // Constructor
    public PastEntriesRecyclerAdapter(ArrayList<PastEntriesItemCard> inListItemCards) {
        this.listItemCards = inListItemCards;
        // Firebase Storage instance
        storage = FirebaseStorage.getInstance();
        // Creating a storage reference
        storageRef = storage.getReference();
        // TODO: get actual username
        username = "testUser";
    }

    // Set listener
    public void setOnItemClickListener(PastEntriesRecyclerListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        PastEntriesItemCard ic = listItemCards.get(position);
        switch (ic.getPastEntryType().toUpperCase()) {
            case "VOICE":
                viewType = 0;
                break;
            case "PHOTO":
                viewType = 1;
                break;
            case "TEXT":
                viewType = 2;
                break;
            default:
            // Use SENTENCE as default
                viewType = 3;
                break;
        }
        return viewType;
    }

    @NonNull
    @Override
    public PastEntriesRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO: use if statement to determine which item_card.xml to use
        View icView;
        // Create item card based on viewType (0 is voice, 1 is photo, 2 is text, 3 is sentence);
        switch (viewType) {
            case 0:
                icView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_past_voice_entry,
                                parent, false);
                break;
            case 1:
                icView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_past_photo_entry,
                                parent, false);
                break;
            case 2:
                icView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_past_text_entry,
                                parent, false);
                break;
            default:
                icView =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_past_sentence_entry,
                                parent, false);
        }
        // Create new ViewHolder from item card view and LCItemClickListener
        return new PastEntriesRecyclerHolder(icView, listener);
    }

    // TODO: in onBindViewHolder, use switch statement to get type from item card object and assign
    //  views in item card xml based on this?
    @Override
    // Set all values
    public void onBindViewHolder(PastEntriesRecyclerHolder holder, int listPosition) {
        PastEntriesItemCard currentItem = listItemCards.get(listPosition);
        String viewType = currentItem.getPastEntryType();
        timestamp = currentItem.getTimestamp();
        if (viewType == null || currentItem.getFormattedDateTime() == null) {
            return;
        }
        switch (viewType) {
            case "VOICE":
                holder.voiceEntryDateTime.setText(currentItem.getFormattedDateTime());
                String audioFilePath = currentItem.getVoiceEntryPath();
                if (audioFilePath != null) {
//                    try {
//                        getAudioSetButtons(audioFilePath, holder);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
                break;
            case "PHOTO":
                holder.photoEntryDateTime.setText(currentItem.getFormattedDateTime());
                String photo1 = currentItem.getPastPhoto1();
                String photo2 = currentItem.getPastPhoto2();
                String photo3 = currentItem.getPastPhoto3();
                if (photo1 != null) {
                    // Get image from cloud storage
                    // Set image view
                    try {
                        getImageSetView(photo1, 1, holder);
                    } catch (IOException e) {
                        Log.w("PHOTO1", e);
                    }
                }
                if (photo2 != null) {
                    // Get image from cloud storage
                    // Set image view
                    try {
                        getImageSetView(photo2, 2, holder);
                    } catch (IOException e) {
                        Log.w("PHOTO2", e);
                    }
                }
                if (photo3 != null) {
                    // Get image from cloud storage
                    // Set image view
                    try {
                        getImageSetView(photo3, 3, holder);
                    } catch (IOException e) {
                        Log.w("PHOTO3", e);
                    }
                }
                break;
            case "TEXT":
                holder.textEntryDateTime.setText(currentItem.getFormattedDateTime());
                String savedText = currentItem.getPastTextEntry();
                if (savedText != null) {
                    holder.textEntryView.setText(savedText);
                }
                break;
            default:
                // Use "SENTENCE" as default
                holder.sentenceEntryDateTime.setText(currentItem.getFormattedDateTime());
                String savedPrompt = currentItem.getPrompt();
                if (savedPrompt != null) {
                    holder.sentencePromptView.setText(holder.itemView.getContext().getString(R.string.prompt_listing, savedPrompt));
                }
                String savedSentence = currentItem.getPastSentenceEntry();
                if (savedSentence != null) {
                    holder.sentenceEntryView.setText(savedSentence);
                }
        }
        // Example: friendName is a public field in the holder
        // holder.friendName.setText("text");
    }

    @Override
    // Get number of items in the list of links
    public int getItemCount() {
        return listItemCards.size();
    }

    // Get photo from Firebase Cloud Storage and save locally
    // Set image views if temp file created successfully
    // Taken from Firebase documentation on how to download files
    // TODO: check if overwrites file -- why is it a temp file?
    private void getImageSetView(String photoName, int photoNum, PastEntriesRecyclerHolder holder) throws IOException {
        StorageReference photoRef = storageRef.child(username).child(timestamp).child(photoName + ".jpg");
        File localFile = File.createTempFile(photoName, "jpg");
        photoRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Photo successfully downloaded
                // Display in corresponding imageview
                // Used TutorialsPoint to set image drawable
                if (photoNum == 1) {
                    holder.photoEntryImageView1.setImageDrawable(Drawable.createFromPath(localFile.toString()));
                } else if (photoNum == 2) {
                    holder.photoEntryImageView2.setImageDrawable(Drawable.createFromPath(localFile.toString()));
                } else {
                    holder.photoEntryImageView3.setImageDrawable(Drawable.createFromPath(localFile.toString()));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // TODO: figure out best way to handle errors
                Log.w("set_images", e);
            }
        });
    }

    // Get audio recording from Firebase Cloud Storage and save locally
    // Set media player if temp file created successfully
    // Taken from Firebase documentation on how to download files
//    private void getAudioSetButtons(String audioName, PastEntriesRecyclerHolder holder) throws IOException {
//        StorageReference voiceRef = storageRef.child(username).child(audioName);
//        File localFile = File.createTempFile(timestamp, "3gp");
//        voiceRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Audio successfully downloaded
//                // Set buttons for Media Player
//                MediaPlayer player = new MediaPlayer();
//                holder.voiceEntryPlayButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        try {
//                            player.setDataSource(String.valueOf(localFile));
//                            player.prepare();
//                            player.start();
//                        } catch (IOException e) {
//                            Log.w("Play Exception", e);
//                        }
//                    }
//                });
//                holder.voiceEntryStopButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (player != null) {
//                            if (player.isPlaying()) {
//                                player.pause();
//                                // TODO: figure out release
//                            }
//                        }
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // TODO: figure out best way to handle errors
//                Log.w("in play audio", e);
//            }
//        });
//    }
}

// Note: this was adapted from Marielle's previous assignment, which referenced the sample code
// provided in class.