package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    private final String TAG = getClass().getCanonicalName();
    TwitterClient client;
    RecyclerView recycler_view_tweets;
    List<Tweet> tweet_feed;
    TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        recycler_view_tweets = findViewById(R.id.recycler_view_tweets);
        tweet_feed = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweet_feed);

        recycler_view_tweets.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_tweets.setAdapter(adapter);

        populateHomeTimeline();
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Success: " + json.toString());

                JSONArray my_array = json.jsonArray;

                try {
                    tweet_feed.addAll(Tweet.from_json_array(my_array));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "JSON exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "Failure: " + response, throwable);
            }
        });
    }
}