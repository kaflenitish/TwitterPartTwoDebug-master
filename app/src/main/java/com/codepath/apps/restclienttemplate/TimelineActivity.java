package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.adaptors.TweetAdaptor;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.supportLibs.EndlessRecyclerViewScrollListener;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    private SwipeRefreshLayout swipeRefreshLayout;
    public ChirpClient client;
    public RecyclerView rvTweets;
    public ArrayList<Tweet> tweets;
    public TweetAdaptor adaptor;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        rvTweets = findViewById(R.id.rvTweet);
        swipeRefreshLayout = findViewById(R.id.svTimeline);
        tweets = new ArrayList<>();
        adaptor = new TweetAdaptor(this, tweets);
        linearLayoutManager = new LinearLayoutManager(this);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }
        };

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateHomeTimeline(0);
            }
        });

        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(adaptor);
        rvTweets.addOnScrollListener(endlessRecyclerViewScrollListener);

        client = ChirpApplication.getRestClient(this);
        populateHomeTimeline(0);
        adaptor.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.compose) {
            Toast.makeText(TimelineActivity.this, "Compose", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
            startActivityForResult(intent ,REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("Tweet"));
            tweets.add(tweet);
            adaptor.notifyItemInserted(0);
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void loadMoreData() {
        client.getNextPageOfTweets(tweets.get(tweets.size() - 1).id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONArray jsonArray = json.jsonArray;
                tweets.addAll(Tweet.fromJson(jsonArray));
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("TimelineActivity", "LoadMoreData Failed with response " + response);
            }
        });
    }


    private void populateHomeTimeline(int page) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json)
            {
                Log.i("JsonCheck", json.toString());

                JSONArray jsonArray = json.jsonArray;
                adaptor.clear();
                tweets.addAll(Tweet.fromJson(jsonArray));
                adaptor.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable)
            {
                Log.e("populateHomeTimeline", "unable to populate home timeline exit with response : " + response);
            }
        });

    }
}
