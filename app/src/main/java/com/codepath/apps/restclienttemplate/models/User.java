package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    private String name, handle, profile_photo_url;

    // empty constructor required by the Parceler library
    public User () { }

    public static User from_json(JSONObject jsonObject) throws JSONException {
        User my_user = new User();

        my_user.name = jsonObject.getString("name");
        my_user.handle = jsonObject.getString("screen_name");
        my_user.profile_photo_url = jsonObject.getString("profile_image_url_https");

        return my_user;
    }

    public String getName() {
        return name;
    }

    public String getHandle() {
        return handle;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }
}
