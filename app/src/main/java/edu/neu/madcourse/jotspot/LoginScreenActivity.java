package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import edu.neu.madcourse.jotspot.firebase_helpers.User;

public class LoginScreenActivity extends AppCompatActivity {

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private SharedPreferences sharedPreferences;
    private final String defaultString = "default";

    private String storedUsername;
    private String storedPassword;

    private TextInputEditText usernameView;
    private TextInputEditText passwordView;

    private Button loginButton;
    private Button createButton;

    private boolean credentialsValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Firebase database objects
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // Used Android documentation on how to implement shared preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        storedUsername = sharedPreferences.getString(getString(R.string.username_preferences_key), defaultString);
        storedPassword = sharedPreferences.getString(getString(R.string.password_preferences_key), defaultString);

        getViews();

        final boolean[] checkCredentials = {false};

        if (!storedUsername.equals(defaultString) && !storedPassword.equals(defaultString)) {
            usernameView.setText(storedUsername);
            passwordView.setText(storedPassword);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameView != null) {
                    if (usernameView.getText() != null) {
                        checkUsernameExists(usernameView.getText().toString());
                    }
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void getViews() {
        usernameView = findViewById(R.id.username_login);
        passwordView = findViewById(R.id.password_login);
        loginButton = findViewById(R.id.login_button);
        createButton = findViewById(R.id.create_account_button);
    }

    // Check whether username exists in the database
    private void checkUsernameExists(final String usernameVal) {
        databaseRef.child(getString(R.string.users_path,
                usernameVal)).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to check if username exists
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            // TODO: move to within other function
                            // Referenced Android documentation to write username locally
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.username_preferences_key), usernameVal);
                            editor.apply();

                            checkPassword(user.getHashedPassword());
                        }
                    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "This username does not exist.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Please note: all password hashing taken from
    // https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private void checkPassword(String storedPasswordStr) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (passwordView.getText() != null && !passwordView.getText().toString().equals("")) {
            if (!validatePassword(passwordView.getText().toString(), storedPasswordStr)) {
                Toast.makeText(getApplicationContext(), "Oops! This password is incorrect.", Toast.LENGTH_LONG).show();
            } else {
                logIn();
            }
        }
    }

    // Please note: all password hashing taken from
    // https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    // Please note: all password hashing taken from
    // https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i<bytes.length ;i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public void logIn() {
        Intent loginIntent = new Intent(LoginScreenActivity.this, HomeScreenActivity.class);
        LoginScreenActivity.this.startActivity(loginIntent);
    }

    public void createAccount() {
        Intent createIntent = new Intent(LoginScreenActivity.this, CreateAccountActivity.class);
        LoginScreenActivity.this.startActivity(createIntent);
    }

}