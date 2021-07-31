package com.codepath.apps.restclienttemplate.models;

// models/User.java

import androidx.room.ColumnInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {

    @ColumnInfo
    public String name;

    // normally this field would be annotated @PrimaryKey because this is an embedded object
    // it is not needed
    @ColumnInfo
    public Long twitter_id;

    public String profilePicture;

    // for parceler
    public User() {}

    public static User parseJSON(JSONObject tweetJson) throws JSONException {

        User user = new User();
        user.twitter_id = tweetJson.getLong("id");
        user.name = tweetJson.getString("name");
        user.profilePicture = tweetJson.getString("profile_image_url_https");
        return user;
    }
}