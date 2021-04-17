package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final int FIREBASE_LOGIN = 123;

    private FirebaseAuth auth;

    private TextInputEditText email;

    private TextInputEditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth object
        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_login);

        password = findViewById(R.id.password_login);
    }

    // Note: this was taken from the Firebase documentation on email/password authentication
    @Override
    public void onStart() {
        super.onStart();
        // If user is logged in, auth will be non-null
        // reload -- figure out what to do (update UI)
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
//            reload();
        }
    }

//    // Note: this was taken from the Firebase documentation on authentication
//    public void createSignInIntent() {
//        // Authenticate with email
//        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build());
//        // Create and launch sign-in intent
//        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), FIREBASE_LOGIN);
//    }
//
//    // Note: this was taken from the Firebase documentation on authentication
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // If the request code matches the code we created for login requests, get the response (success/failure of login)
//        if (requestCode == FIREBASE_LOGIN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            if (resultCode == RESULT_OK) {
//                // Sign in was successful
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//            } else {
//                // Sign in failed
//                // If response is null the user canceled the sign-in flow using the back button.
//                // TODO: handle other errors
//                Log.w("LOGIN", "login failed");
//            }
//        }
//    }
//
//    // Note: this was taken from the Firebase documentation on authentication
//    public void signOut() {
//        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
//            public void onComplete(@NonNull Task<Void> task) {
//                // TODO: do something
//            }
//        });
//    }

}