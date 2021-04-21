package edu.neu.madcourse.jotspot.firebase_helpers;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class ThreadTaskHelper {

    StorageReference storageRef;
    Uri uri;

    // Constructor for photo thread task helper
    public ThreadTaskHelper(StorageReference inStorageRef, Uri inUri) {
        storageRef = inStorageRef;
        uri = inUri;
    }

    // Getter for storage reference
    public StorageReference getStorageReference() {
        return storageRef;
    }

    // Getter for uri
    public Uri getUri() {
        return uri;
    }
}
