package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView textViewId, textViewEmail, textViewNama , textView, textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }


        textView = (TextView) findViewById(R.id.textView);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewNama = (TextView) findViewById(R.id.textViewNama);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);

        //getting the current user
        User user = SharedPrefManager.getInstance(this).getUser();

        //setting the values to the textviews
        textViewEmail.setText(user.getEmail());
        textViewNama.setText(user.getNamapelapor());
        textView.setText(user.getTextView());
        textViewStatus.setText(user.getStatus_laporan());

        //when the user presses logout button
        //calling the logout method
        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });
    }

}