package edu.neu.madcourse.jotspot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class LoginScreenActivity extends AppCompatActivity {

    private TextInputEditText username;

    private TextInputEditText password;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);


        username = findViewById(R.id.username_login);

        password = findViewById(R.id.password_login);

        loginButton = findViewById(R.id.login_button);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

    }

    public void logIn() {
        Intent loginIntent = new Intent(LoginScreenActivity.this, HomeScreenActivity.class);
        LoginScreenActivity.this.startActivity(loginIntent);
    }


}