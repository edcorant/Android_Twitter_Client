package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.parceler.Parcels;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    private final String TAG = getClass().getCanonicalName();
    private EditText text_box;
    private Button submit_tweet;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        text_box = findViewById(R.id.compose_text_box);
        submit_tweet = findViewById(R.id.submit_tweet_button);
        client = TwitterApp.getRestClient(this);

        submit_tweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here we want to make the API call to Twitter
                String content = text_box.getText().toString();

                // check if Tweet is too short or too long
                if (content.isEmpty())
                    Toast.makeText(ComposeActivity.this, R.string.empty_tweet_warning, Toast.LENGTH_SHORT).show();
                if (content.length() > getResources().getInteger(R.integer.MAX_TWEET_LENGTH))
                    Toast.makeText(ComposeActivity.this,
                            getString(R.string.tweet_too_long_warning, getResources().getInteger(R.integer.MAX_TWEET_LENGTH)),
                            Toast.LENGTH_SHORT).show();

                // if no warnings, make API call
                else {
                    client.publishTweet(new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            Log.i(TAG, "Successfully published tweet");

                            try {
                                Tweet this_tweet = Tweet.from_json(json.jsonObject);

                                Log.i(TAG, "Published the tweet: " + this_tweet.getContent());

                                Intent intent = new Intent();
                                intent.putExtra(getString(R.string.TWEET_PARCEL_ID), Parcels.wrap(this_tweet));
                                setResult(RESULT_OK, intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            Log.e(TAG, "Failed to publish tweet ->", throwable);
                        }
                    }, content);
                }
            }
        });
    }
}