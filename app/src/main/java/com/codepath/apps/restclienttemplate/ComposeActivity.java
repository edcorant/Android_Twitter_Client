package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ComposeActivity extends AppCompatActivity {

    private EditText text_box;
    private Button submit_tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        text_box = findViewById(R.id.compose_text_box);
        submit_tweet = findViewById(R.id.compose_button);

        submit_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here we want to make the API call to Twitter
                String content = text_box.getText().toString();

                // check if Tweet is too short or too long
                if (content.isEmpty())
                    Toast.makeText(ComposeActivity.this, R.string.empty_tweet_warning, Toast.LENGTH_SHORT).show();
                if (content.length() > 140)
                    Toast.makeText(ComposeActivity.this, getString(R.string.tweet_too_long_warning, R.integer.MAX_TWEET_LENGTH), Toast.LENGTH_SHORT).show();

                // if no warnings, make API call
                else {

                }
            }
        });


    }
}