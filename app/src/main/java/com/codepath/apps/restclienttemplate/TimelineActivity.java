package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    private final String TAG = getClass().getCanonicalName();
    private TwitterClient client;
    private RecyclerView recycler_view_tweets;
    private List<Tweet> tweet_feed;
    private TweetsAdapter adapter;
    private SwipeRefreshLayout swiper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        swiper = findViewById(R.id.swipe_to_refresh);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "User pulled to refresh. Fetching new data...");
                populateHomeTimeline();
            }
        });
        // Configure the refreshing colors
        swiper.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

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
                    adapter.clear();
                    adapter.addAll(Tweet.from_json_array(my_array));
                    swiper.setRefreshing(false);
                }

                catch (JSONException e) {
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