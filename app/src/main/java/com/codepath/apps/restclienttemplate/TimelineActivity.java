package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

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
    private EndlessRecyclerViewScrollListener endless_scroller;

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

        LinearLayoutManager my_lin_manager = new LinearLayoutManager(this);

        recycler_view_tweets.setLayoutManager(my_lin_manager);
        recycler_view_tweets.setAdapter(adapter);

        endless_scroller = new EndlessRecyclerViewScrollListener(my_lin_manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "On Load More: " + page);
                loadMoreData();
            }
        };

        // Adds the scroll listener to RecyclerView
        recycler_view_tweets.addOnScrollListener(endless_scroller);

        populateHomeTimeline();
    }

    private void loadMoreData() {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "loadMoreData(): Success -> " + json.toString());

                //  --> Deserialize and construct new model objects from the API response
                JSONArray my_array = json.jsonArray;

                try {
                    List<Tweet> older_tweets = Tweet.from_json_array(my_array);
                    //  --> Append the new data objects to the existing set of items inside the array of items
                    //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
                    adapter.addAll(older_tweets);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "loadMoreData(): Failure ->", throwable);
            }
        }, tweet_feed.get(tweet_feed.size() - 1).getId());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.compose_button) {
            Intent intent = new Intent(this, ComposeActivity.class);
            startActivityForResult(intent, getResources().getInteger(R.integer.REQUEST_CODE));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        if (requestCode == getResources().getInteger(R.integer.REQUEST_CODE) && resultCode == RESULT_OK) {
            // get data from intent (tweet content)
            Tweet submitted_tweet = Parcels.unwrap(data.getParcelableExtra(getString(R.string.TWEET_PARCEL_ID)));
            // put tweet on recycler view
            tweet_feed.add(0, submitted_tweet);
            adapter.notifyItemInserted(0);
            recycler_view_tweets.smoothScrollToPosition(0);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}