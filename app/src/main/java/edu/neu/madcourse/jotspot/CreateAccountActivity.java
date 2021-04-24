package edu.neu.madcourse.jotspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CreateAccountActivity extends AppCompatActivity {

    // Firebase-related variables
    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    private SharedPreferences sharedPreferences;

    private TextInputEditText usernameView;
    private TextInputEditText passwordView;
    private Button createAccountButton;

    /*
    Checks username
    Allows for uppercase and lowercase letters (without accents) and certain special characters
    Does not allow spaces
    */
    private static final String USERNAME_PATTERN = "([a-zA-Z0-9\\-_!*@]+)";

    /*
    Checks username
    Allows for uppercase and lowercase letters (without accents) and certain special characters
    Does not allow spaces
    Requires at least 8 characters and at least one letter
    */
    private static final String PASSWORD_PATTERN = "([(?=.*[a-zA-Z])-9\\-_!*@]+).{7,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Firebase database objects
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // Used Android documentation on how to implement shared preferences
        sharedPreferences = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        getViews();

        createAccountButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        createUser();
                    }
                }
        );
    }

    private void getViews() {
        usernameView = this.findViewById(R.id.username_create);
        passwordView = this.findViewById(R.id.password_create);
        createAccountButton = this.findViewById(R.id.create_button);
    }

    private void createUser() {
        if (usernameView.getText() != null && !usernameView.getText().toString().equals("")) {
            String username = usernameView.getText().toString();
            checkUsernameExists(username);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter a username.", Toast.LENGTH_LONG).show();
        }
    }

    // Check that username does not already exist
    private void checkUsernameExists(final String usernameVal) {
        databaseRef.child(getString(R.string.users_path,
                usernameVal)).addListenerForSingleValueEvent(new ValueEventListener() {
            // Use snapshot to check if username exists
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    if (passwordView.getText() != null) {
                        // If the username does not exist, check if it's valid
                        if (!usernameVal.matches(USERNAME_PATTERN) || usernameVal.equals("")) {
                            Toast.makeText(getApplicationContext(), "Please enter a username with no spaces, only letters, numbers, and/or the following: - . _ ! * @", Toast.LENGTH_LONG).show();
                        } else if (!passwordView.getText().toString().matches(PASSWORD_PATTERN)) {
                            Toast.makeText(getApplicationContext(), "Please enter a password with at least 8 characters and no spaces. Allowed characters are letters, numbers, and/or the following: - . _ ! * @", Toast.LENGTH_LONG).show();
                        } else {
                            // If the username is valid, pass the username and hashed password to the databases
                            writePersonalInfo(usernameVal);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a password with no spaces, only letters, numbers, and/or the following: - . _ ! * @", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "This username already exists. Please choose another one.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Write personal information to database and store username locally
    private void writePersonalInfo(String username) {
        try {
            if (passwordView.getText() != null && !passwordView.getText().toString().equals("")) {
//                byte[] hashedPassword = hashPassword(passwordView.getText().toString());
                String hashedPassword = hashPassword(passwordView.getText().toString());
                databaseRef.child("users").child(username).child("username").setValue(username);
//                String hashedPasswordStr = Base64.getEncoder().encodeToString(hashedPassword);
//                databaseRef.child("users").child(username).child("hashedPassword").setValue(hashedPasswordStr);
                databaseRef.child("users").child(username).child("hashedPassword").setValue(hashedPassword);

                // Referenced Android documentation to write username locally
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.username_preferences_key), username);
                // Note: storing password locally to android device as plain text is not good
                // practice, but we think it is preferable to storing the password hash locally as
                // plain text.
                // Future iterations will implement Account Manager, but we were unable to get that
                // working for this iteration of the project.
                editor.putString(getString(R.string.password_preferences_key), passwordView.getText().toString());
                editor.apply();

                // Go to home activity
                Intent intent = new Intent(CreateAccountActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter a non-empty password.", Toast.LENGTH_LONG).show();
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    // Please note: all password hashing taken from
    // https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    // Please note: all password hashing taken from
    // https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    // Please note: all password hashing taken from
    // https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

}