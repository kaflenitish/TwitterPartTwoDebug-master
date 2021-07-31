package com.codepath.apps.restclienttemplate.models;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TwitterDao {
    // Record finders
    @Query("SELECT * FROM Tweet WHERE id = :tweetId")
    Tweet byTweetId(Long tweetId);

    @Query("SELECT * FROM Tweet ORDER BY timestamp")
    List<Tweet> getRecentTweets();

    // Replace strategy is needed to ensure an update on the table row.  Otherwise the insertion will
    // fail.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTweet(Tweet... tweets);
}
