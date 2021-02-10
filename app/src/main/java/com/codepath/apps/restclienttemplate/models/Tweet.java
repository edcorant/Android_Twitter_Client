package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Tweet {

    private String content, created_at;
    private User user;

    public static Tweet from_json(JSONObject jsonObject) throws JSONException {

        Tweet my_tweet = new Tweet();
        my_tweet.content = jsonObject.getString("text");
        my_tweet.created_at = jsonObject.getString("created_at");
        my_tweet.user = User.from_json(jsonObject.getJSONObject("user"));

        return my_tweet;
    }

    public static List<Tweet> from_json_array(JSONArray array) throws JSONException {

        List<Tweet> my_list = new ArrayList<>();
        int len = array.length();

        for (int i = 0; i < len; ++ i)
            my_list.add(from_json(array.getJSONObject(i)));

        return my_list;
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
}
