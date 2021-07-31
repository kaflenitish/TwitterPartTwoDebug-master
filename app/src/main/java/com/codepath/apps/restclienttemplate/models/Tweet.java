// models/Tweet.java
package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Parcel
@Entity
public class Tweet {
    // Define database columns and associated fields
    @PrimaryKey
    @ColumnInfo
    public Long id;
    @ColumnInfo
    public String timestamp;
    @ColumnInfo
    public String body;

    @ColumnInfo
    public String Media = "";

    // Use @Embedded to keep the column entries as part of the same table while still
    // keeping the logical separation between the two objects.
    @Embedded
    public User user;

    // for Parceler
    public Tweet() {}

    // Add a constructor that creates an object from the JSON response
    public Tweet(JSONObject object){
        try {
            this.id = object.getLong("id");
            this.user = User.parseJSON(object.getJSONObject("user"));
            this.timestamp = object.getString("created_at");
            this.body = object.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = new Tweet(tweetJson);
            tweets.add(tweet);
        }

        return tweets;
    }
}
