package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    private String content, created_at;
    private User user;
    private long id;

    // empty constructor required by the Parceler library
    public Tweet () { }

    public static Tweet from_json(JSONObject jsonObject) throws JSONException {

        Tweet my_tweet = new Tweet();
        my_tweet.content = jsonObject.getString("text");
        my_tweet.created_at = jsonObject.getString("created_at");
        my_tweet.user = User.from_json(jsonObject.getJSONObject("user"));
        my_tweet.id = jsonObject.getLong("id");

        return my_tweet;
    }

    public static List<Tweet> from_json_array(JSONArray array) throws JSONException {

        List<Tweet> my_list = new ArrayList<>();
        int len = array.length();

        for (int i = 0; i < len; ++ i)
            my_list.add(from_json(array.getJSONObject(i)));

        return my_list;
    }

    public String getFormattedTimestamp() {
        return TimeFormatter.getTimeDifference(created_at);
    }

    public String getContent() {
        return content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public User getUser() {
        return user;
    }

    public long getId() {
        return id;
    }
}
